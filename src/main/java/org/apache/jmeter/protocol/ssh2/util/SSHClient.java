/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.ssh2.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * The Class SSHClient executes commands on remote SSH server. Server
 * information is provided as SSHClientConfig.
 */
public class SSHClient {

    /** The SSH script's line end character pattern. */
    private static final String SCRIPT_LINE_END_PATTERN   = "[\r\n]+";

    /** The SSH script comment character. */
    private static final String SCRIPT_COMMENT_IDENTIFIER = "#";

    /** The wait command. */
    private static final String SCRIPT_WAIT_COMMAND       = "wait ";

    /** The end line character send to server to tell end of command. */
    private static final String SCRIPT_END_LINE           = "\n";

    /** The patter to split the string of wait command. */
    private static final String WAIT_CMD_SPLIT_PATTERN    = "[ \t]+";

    /** The Constant logger. */
    private static final Logger logger                    = LoggingManager.getLoggerForClass();

    /** The ssh client config. */
    private SSHClientConfig     sshClientConfig;

    /**
     * Instantiates a new SSH client.
     *
     * @param sshClientConfig
     *            the ssh client config
     */
    public SSHClient(SSHClientConfig sshClientConfig) {
        super();
        this.sshClientConfig = sshClientConfig;
    }

    /**
     * Gets the authenticated SSH connection. Connection is authenticated with
     * password provided as SSHClientConfig.<br/>
     * If password not provided then connection authenticated by ssh-key-file
     * and passphrase<br/>
     * If ssh-key-file not provided then connection with authenticated with none
     * (i.e. only username)
     *
     * @return the authenticated SSH connection
     * @throws SSHException
     *             if user is not authenticated or on IO exception.
     */
    protected Connection getSSHConnection() throws SSHException {

        Connection conn;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating SSH connection.");
            }
            conn = new Connection(sshClientConfig.getHostname(), sshClientConfig.getPort());
            conn.connect();
            boolean isAuthenticated = false;
            String authType = null;
            if (StringUtils.isNotBlank(sshClientConfig.getPassword())) {
                authType = "authentication with password";
                isAuthenticated = conn.authenticateWithPassword(sshClientConfig.getUserName(),
                                                                sshClientConfig.getPassword());
            } else if (StringUtils.isNotBlank(sshClientConfig.getSshkeyfile())) {
                authType = "authentication with public key";
                isAuthenticated = conn.authenticateWithPublicKey(sshClientConfig.getUserName(),
                                                                 new File(sshClientConfig
                                                                         .getSshkeyfile()),
                                                                 sshClientConfig.getPassphrase());
            } else {
                authType = "authentication with none";
                isAuthenticated = conn.authenticateWithNone(sshClientConfig.getUserName());
            }

            if (isAuthenticated == false) {
                throw new SSHException("Failed to create SSH connection :" + authType);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("SSH connection created with " + authType);
            }
        } catch (IOException ioe) {
            throw new SSHException("Failed to create SSH connection ", ioe);
        }
        return conn;
    }

    /**
     * Gets the simple SSH session for given SSH connection.
     *
     * @param conn
     *            the SSH connection
     * @return the simple SSH session
     * @throws SSHException
     *             if failed to open SSH session.
     */
    protected Session getSSHSession(Connection conn) throws SSHException {
        try {
            return conn.openSession();
        } catch (IOException e) {
            throw new SSHException("Failed to open SSH session", e);
        }
    }

    /**
     * Gets the SSH session with PTY requested.
     *
     * @param conn
     *            the SSH connection
     * @return the SSH session with PTY requested.
     * @throws SSHException
     *             if failed to open session.
     */
    protected Session getSSHSessionWithPTY(Connection conn) throws SSHException {

        Session session = null;
        try {
            /* Create a session */
            session = conn.openSession();
            session.requestPTY(sshClientConfig.getTerminalType(),
                               sshClientConfig.getTerminalWidth(),
                               sshClientConfig.getTerminalHeight(),
                               0,
                               0,
                               null);
            session.startShell();
        } catch (IOException ioe) {
            throw new SSHException("Failed to create SSH session with PTY", ioe);
        }
        return session;
    }

    /**
     * Execute the command.
     *
     * @param cmd
     *            the input command to be executed.
     * @param waitForResponseInSec
     *            the wait for response (in sec.) after executing this command
     * @return the result of SSH command
     * @throws SSHException
     *             if failed to execute this command.
     */
    public SSHResult executeCmd(String cmd,
                                int waitForResponseInSec) throws SSHException
    {

        Connection conn = null;
        Session session = null;
        try {

            conn = this.getSSHConnection();
            session = conn.openSession();

            ShellConsoleReader consoleThread = new ShellConsoleReader(session);
            consoleThread.setName("ShellConsoleReader");
            consoleThread.setDaemon(true);
            consoleThread.start();
            logger.info(sshClientConfig.describe());
            logger.info(">>command: " + cmd);
            session.execCommand(cmd + " && sleep " + waitForResponseInSec);

            // wait till command properly executed or timed-out
            consoleThread.join(sshClientConfig.getMaxWaitTimeForOutput());

            SSHResult result = new SSHResult();
            result.setExitSignal(session.getExitSignal());
            result.setExitStatus(session.getExitStatus());
            result.setStdErr(consoleThread.getStdErr().toString());
            result.setStdOut(consoleThread.getStdOut().toString());
            return result;

        } catch (InterruptedException e) {
            throw new SSHException("Failed to execute command:[" + cmd + "]", e);
        } catch (IOException e) {
            throw new SSHException("Failed to execute command:[" + cmd + "]", e);
        } finally {
            closeSSHResource(session, conn);
        }

    }

    /**
     * Execute the script. Script will be executed line by line.
     *
     * @param script
     *            the input script content
     * @param waitCmdEnabled
     *            the 'wait <time-in-sec>' command enabled or not. if false then
     *            wait command will be ignored.
     * @param delayAfterEachCmd
     *            the delay after each command.
     * @return the SSH command result
     * @throws SSHException
     *             if failed to execute the SSH script.
     */
    @SuppressWarnings("resource")
    public SSHResult executeScript(String script,
                                   boolean waitCmdEnabled,
                                   long delayAfterEachCmd) throws SSHException
    {
        Connection conn = null;
        Session session = null;
        OutputStream cmdOutStream = null;
        try {

            conn = this.getSSHConnection();
            session = this.getSSHSessionWithPTY(conn);
            cmdOutStream = session.getStdin();

            ShellConsoleReader consoleThread = new ShellConsoleReader(session);
            consoleThread.setName("ShellConsoleReader");
            consoleThread.start();

            logger.info(sshClientConfig.describe());

            String commands[] = script.split(SCRIPT_LINE_END_PATTERN);
            for (String command : commands) {
                // ignore empty line
                if (StringUtils.isNotBlank(command)) {

                    // skip # (comment) line.
                    if (command.startsWith(SCRIPT_COMMENT_IDENTIFIER)) {
                        continue;
                    }

                    // check for wait command.
                    if (command.startsWith(SCRIPT_WAIT_COMMAND)) {
                        // sleep the thread on wait command call.
                        if (waitCmdEnabled) {
                            // assuming 'wait' command format is like: wait 5
                            String sleepArgs[] = command.split(WAIT_CMD_SPLIT_PATTERN);
                            logger.info("Thread is waiting for(in sec):" + sleepArgs[1]);
                            try {
                                Thread.sleep(Long.parseLong(sleepArgs[1]) * 1000);
                            } catch (NumberFormatException e) {
                                throw new SSHException("Failed to parse time in wait command", e);
                            } catch (InterruptedException ignore) {
                                logger.debug("Command thread intruptted while execute wait command",
                                             ignore);
                            }
                        }
                        continue;
                    }

                    cmdOutStream.write((command + SCRIPT_END_LINE).getBytes());
                    cmdOutStream.flush();

                    logger.info(">>command: " + command);

                    // add delay after each command
                    if (delayAfterEachCmd > 0) {
                        try {
                            Thread.sleep(delayAfterEachCmd);
                        } catch (InterruptedException ignore) {
                            logger.debug("Command thread intruptted while delay after command",
                                         ignore);
                        }
                    }
                }

            }

            logger.info("Script execution completed.");
            try {
                consoleThread.join(sshClientConfig.getMaxWaitTimeForOutput());
            } catch (InterruptedException ignore) {
                logger.debug("ShellConsoleReader thread intruptted", ignore);
            }

            // collection the result
            SSHResult result = new SSHResult();
            result.setExitSignal(session.getExitSignal());
            result.setExitStatus(session.getExitStatus());
            String output = consoleThread.getStdOut();
            result.setStdOut(output);

            return result;

        } catch (IOException e) {
            throw new SSHException("Failed to execute command:[" + script + "]", e);
        } finally {
            closeOutputStream(cmdOutStream);
            closeSSHResource(session, conn);
        }

    }

    /**
     * The Class ShellConsoleReader reads the standard out and standard error
     * for SSH session.
     */
    class ShellConsoleReader extends Thread {

        /** The output stream buffer. */
        ByteArrayOutputStream stdOut = null;

        /** The error stream buffer. */
        ByteArrayOutputStream stdErr = null;

        /** The session. */
        private Session       session;

        /**
         * Instantiates a new shell console reader.
         *
         * @param sess
         *            the SSH session to be read.
         */
        public ShellConsoleReader(Session sess) {
            super();
            this.session = sess;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Thread#run()
         */
        public void run() {
            byte buffer[] = new byte[10240];
            if (stdOut == null) {
                stdOut = new ByteArrayOutputStream(10240);
            }
            if (stdErr == null) {
                stdErr = new ByteArrayOutputStream(10240);
            }
            BufferedInputStream shellInputStream = null;
            InputStream cmdErrStream = null;

            try {
                shellInputStream = new BufferedInputStream(session.getStdout());
                cmdErrStream = session.getStderr();

                while (true) {
                    try {
                        if (!waitForCondition(session)) {
                            break;
                        }
                        int len = -1;
                        while ((len = shellInputStream.read(buffer)) != -1) {
                            stdOut.write(buffer, 0, len);
                        }

                        while ((len = cmdErrStream.read(buffer)) != -1) {
                            stdErr.write(buffer, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                closeInputStream(cmdErrStream);
                closeInputStream(shellInputStream);
            }

        }

        /**
         * Gets the standard output of SSH session.
         *
         * @return the standard output of SSH session or blank string if nothing
         *         read.
         */
        public String getStdOut() {
            if (stdOut == null) {
                return "";
            }
            return stdOut.toString();
        }

        /**
         * Gets the standard error of SSH session.
         *
         * @return the standard error of SSH session or blank string if nothing
         *         read.
         */
        public String getStdErr() {
            if (stdErr == null) {
                return "";
            }
            return stdErr.toString();
        }
    }

    /**
     * The Class SSHResult hold the result of SSH Command.
     */
    public class SSHResult {

        /** The std out. */
        String  stdOut;

        /** The std err. */
        String  stdErr;

        /** The exit status. */
        Integer exitStatus;

        /** The exit signal. */
        String  exitSignal;

        /**
         * Gets the standard output of SSH command.
         *
         * @return the stdOut
         */
        public String getStdOut() {
            return this.stdOut;
        }

        /**
         * Sets the standard output of SSH command.
         *
         * @param stdOut
         *            the stdOut to set
         */
        public void setStdOut(String stdOut) {
            this.stdOut = stdOut;
        }

        /**
         * Gets the standard error of SSH command.
         *
         * @return the stdErr
         */
        public String getStdErr() {
            return this.stdErr;
        }

        /**
         * Sets the standard error of SSH command.
         *
         * @param stdErr
         *            the stdErr to set
         */
        public void setStdErr(String stdErr) {
            this.stdErr = stdErr;
        }

        /**
         * Gets the exit status of SSH command.
         *
         * @return the exitStatus
         */
        public Integer getExitStatus() {
            return this.exitStatus;
        }

        /**
         * Sets the exit status of SSH command.
         *
         * @param exitStatus
         *            the exitStatus to set
         */
        public void setExitStatus(Integer exitStatus) {
            this.exitStatus = exitStatus;
        }

        /**
         * Gets the exit signal of SSH command.
         *
         * @return the exitSignal
         */
        public String getExitSignal() {
            return this.exitSignal;
        }

        /**
         * Sets the exit signal of SSH command.
         *
         * @param exitSignal
         *            the exitSignal to set
         */
        public void setExitSignal(String exitSignal) {
            this.exitSignal = exitSignal;
        }
    }

    /**
     * Wait for condition(Whether data available on channel of timeout
     * happened).
     *
     * @param session
     *            the ssh session
     * @return true, if data available on channel. false, if timeout happened
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected boolean waitForCondition(Session session) throws IOException {
        int conditions = session.waitForCondition(ChannelCondition.STDOUT_DATA
                                                          | ChannelCondition.STDERR_DATA
                                                          | ChannelCondition.EOF,
                                                  sshClientConfig.getMaxWaitTimeForOutput());

        if ((conditions & ChannelCondition.TIMEOUT) != 0) {
            // A timeout occurred.
            if (logger.isDebugEnabled()) {
                logger.debug("WaitForCondition: waiting timeout");
            }
            return false;
        }

        /*
         * Here we are not checking separately for CLOSED, since CLOSED implies
         * EOF
         */
        if ((conditions & ChannelCondition.EOF) != 0) {
            // The remote side will not send us further data
            if (logger.isDebugEnabled()) {
                logger.debug("WaitForCondition: EOF reached");
            }
            return false;
        }
        if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0) {
            // we have consumed all data in the local arrival window.
            if (logger.isDebugEnabled()) {
                logger.debug("WaitForCondition(): Unexpected condtion occured");
            }
            return false;
        }
        return true;

    }

    /**
     * Close input stream.
     *
     * @param resource
     *            the resource
     */
    public static void closeInputStream(InputStream resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * Close output stream.
     *
     * @param resource
     *            the resource
     */
    public static void closeOutputStream(OutputStream resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * Close ssh resource.
     *
     * @param sess
     *            the sess
     * @param conn
     *            the conn
     */
    public static void closeSSHResource(Session sess,
                                        Connection conn)
    {
        try {
            if (sess != null) {
                sess.close();
            }
        } catch (Exception ignore) {
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ignore) {
        }
    }

}
