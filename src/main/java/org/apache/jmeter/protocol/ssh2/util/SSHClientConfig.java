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

/**
 * The Class SSHClientConfig is a POJO that contains the SSH client's
 * configuration properties.
 *
 *
 */
public class SSHClientConfig {

    /** The hostname. */
    private String hostname;

    /** The port. */
    private int    port;

    /** The connection timeout. */
    private int    connectionTimeout;

    /** The max wait time for output. */
    private int    maxWaitTimeForOutput;

    /** The user name. */
    private String userName;

    /** The password. */
    private String password;

    /** The sshkeyfile. */
    private String sshkeyfile;

    /** The passphrase. */
    private String passphrase;

    /** The terminal type. */
    private String terminalType;

    /** The terminal width. */
    private int    terminalWidth;

    /** The terminal height. */
    private int    terminalHeight;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SSHClientConfig [");
        sb.append("hostname=").append(hostname);
        sb.append(", port=").append(port);
        sb.append(", connectionTimeout=").append(connectionTimeout);
        sb.append(", maxWaitTimeForOutput=").append(maxWaitTimeForOutput);
        sb.append(", userName=").append(userName);
        sb.append(", sshkeyfile=").append(sshkeyfile);
        sb.append(", terminalType=").append(terminalType);
        sb.append(", terminalWidth=").append(terminalWidth);
        sb.append(", terminalHeight=").append(terminalHeight);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Describe the SSHClientConfig object.
     *
     * @return the string
     */
    public String describe() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(userName).append("@").append(hostname);
        sb.append(":").append(port);
        sb.append("/?connectionTimeout=").append(connectionTimeout);
        sb.append(" &maxWaitTimeForOutput=").append(maxWaitTimeForOutput);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets the hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the hostname.
     *
     * @param hostname
     *            the new hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the connection timeout.
     *
     * @return the connection timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the connection timeout.
     *
     * @param connectionTimeout
     *            the new connection timeout
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Gets the max wait time for output.
     *
     * @return the max wait time for output
     */
    public int getMaxWaitTimeForOutput() {
        return maxWaitTimeForOutput;
    }

    /**
     * Sets the max wait time for output.
     *
     * @param maxWaitTimeForOutput
     *            the new max wait time for output
     */
    public void setMaxWaitTimeForOutput(int maxWaitTimeForOutput) {
        this.maxWaitTimeForOutput = maxWaitTimeForOutput;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *            the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the sshkeyfile.
     *
     * @return the sshkeyfile
     */
    public String getSshkeyfile() {
        return sshkeyfile;
    }

    /**
     * Sets the sshkeyfile.
     *
     * @param sshkeyfile
     *            the new sshkeyfile
     */
    public void setSshkeyfile(String sshkeyfile) {
        this.sshkeyfile = sshkeyfile;
    }

    /**
     * Gets the passphrase.
     *
     * @return the passphrase
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * Sets the passphrase.
     *
     * @param passphrase
     *            the new passphrase
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * Gets the terminal type.
     *
     * @return the terminal type
     */
    public String getTerminalType() {
        return terminalType;
    }

    /**
     * Sets the terminal type.
     *
     * @param terminalType
     *            the new terminal type
     */
    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    /**
     * Gets the terminal width.
     *
     * @return the terminal width
     */
    public int getTerminalWidth() {
        return terminalWidth;
    }

    /**
     * Sets the terminal width.
     *
     * @param terminalWidth
     *            the new terminal width
     */
    public void setTerminalWidth(int terminalWidth) {
        this.terminalWidth = terminalWidth;
    }

    /**
     * Gets the terminal height.
     *
     * @return the terminal height
     */
    public int getTerminalHeight() {
        return terminalHeight;
    }

    /**
     * Sets the terminal height.
     *
     * @param terminalHeight
     *            the new terminal height
     */
    public void setTerminalHeight(int terminalHeight) {
        this.terminalHeight = terminalHeight;
    }

}
