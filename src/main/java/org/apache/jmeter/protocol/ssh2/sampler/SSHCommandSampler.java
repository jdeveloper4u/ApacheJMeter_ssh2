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
package org.apache.jmeter.protocol.ssh2.sampler;

import java.io.StringWriter;
import java.text.MessageFormat;

import org.apache.jmeter.protocol.ssh2.util.SSHClient;
import org.apache.jmeter.protocol.ssh2.util.SSHClient.SSHResult;
import org.apache.jmeter.protocol.ssh2.util.SSHClientConfig;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

/**
 * SSH Command Sampler that executes a single command on remote SSH server and
 * returns its output as response data.
 */
public class SSHCommandSampler extends AbstractSSHSampler {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The command. */
    private String            command;

    /** The wait time in sec. */
    private int               waitTimeInSec;

    /**
     * Instantiates a new SSH command sampler.
     */
    public SSHCommandSampler() {
        super("SSH2 Command Sampler");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.samplers.Sampler#sample(org.apache.jmeter.samplers.
     * Entry)
     */
    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(super.getName());
        result.setDataType(RESPONSE_DATA_TYPE);
        result.setContentType(RESPONSE_CONTENT_TYPE);
        result.sampleStart();

        try {

            result.setSamplerData(command);
            SSHClientConfig sshClientConfig = this.getSSHClientConfig();
            SSHClient sshClient = new SSHClient(sshClientConfig);

            SSHResult sshCmdResult = sshClient.executeCmd(command, waitTimeInSec);

            // fetch the result
            result.sampleEnd();
            result.setSuccessful(true);
            result.setResponseMessage(MessageFormat.format(RESPONSE_MSG_TEMPLATE,
                                                           sshCmdResult.getExitSignal(),
                                                           sshCmdResult.getStdErr()));
            result.setResponseCode(String.valueOf(sshCmdResult.getExitStatus()));
            result.setResponseData(sshCmdResult.getStdOut(), RESPONSE_DATA_ENCODING);
        } catch (Exception e) {
            result.sampleEnd();
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            // get stack trace as a String to return as document data
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new java.io.PrintWriter(stringWriter));
            result.setResponseData(stringWriter.toString(), RESPONSE_DATA_ENCODING);
            result.setResponseCode("-1");

        }
        return result;
    }

    /**
     * Gets the command.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command.
     *
     * @param command
     *            the new command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Gets the wait time in sec.
     *
     * @return the wait time in sec
     */
    public int getWaitTimeInSec() {
        return waitTimeInSec;
    }

    /**
     * Sets the wait time in sec.
     *
     * @param waitTimeInSec
     *            the new wait time in sec
     */
    public void setWaitTimeInSec(int waitTimeInSec) {
        this.waitTimeInSec = waitTimeInSec;
    }
}
