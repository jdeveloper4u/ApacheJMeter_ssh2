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
 * SSH Script Sampler that executes multiple command on remote server. Commands are executes line-by-line.
 *
 */
public class SSHScriptSampler extends AbstractSSHSampler {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The enable wait cmd. */
    private boolean           enableWaitCmd;

    /** The delay after each cmd. */
    private long              delayAfterEachCmd;

    /** The script content. */
    private String            scriptContent;

    /** The terminal type. */
    private String            terminalType;

    /** The terminal width. */
    private int               terminalWidth;

    /** The terminal height. */
    private int               terminalHeight;

    /**
     * Instantiates a new SSH script sampler.
     */
    public SSHScriptSampler() {
        super("SSH2 Script Sampler");
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.samplers.Sampler#sample(org.apache.jmeter.samplers.Entry)
     */
    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(super.getName());
        result.setDataType(RESPONSE_DATA_TYPE);
        result.setContentType(RESPONSE_CONTENT_TYPE);
        result.sampleStart();

        try {

            result.setSamplerData(scriptContent);
            SSHClientConfig sshClientConfig = this.getSSHClientConfig();
            sshClientConfig.setTerminalType(terminalType);
            sshClientConfig.setTerminalWidth(terminalWidth);
            sshClientConfig.setTerminalHeight(terminalHeight);

            SSHClient sshClient = new SSHClient(sshClientConfig);

            SSHResult sshCmdResult = sshClient.executeScript(scriptContent,
                                                             enableWaitCmd,
                                                             delayAfterEachCmd);

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
     * Gets the script content.
     *
     * @return the script content
     */
    public String getScriptContent() {
        return scriptContent;
    }

    /**
     * Sets the script content.
     *
     * @param scriptContent the new script content
     */
    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    /**
     * Checks if is enable wait cmd.
     *
     * @return true, if is enable wait cmd
     */
    public boolean isEnableWaitCmd() {
        return enableWaitCmd;
    }

    /**
     * Sets the enable wait cmd.
     *
     * @param enableWaitCmd the new enable wait cmd
     */
    public void setEnableWaitCmd(boolean enableWaitCmd) {
        this.enableWaitCmd = enableWaitCmd;
    }

    /**
     * Gets the delay after each cmd.
     *
     * @return the delay after each cmd
     */
    public long getDelayAfterEachCmd() {
        return delayAfterEachCmd;
    }

    /**
     * Sets the delay after each cmd.
     *
     * @param delayAfterEachCmd the new delay after each cmd
     */
    public void setDelayAfterEachCmd(long delayAfterEachCmd) {
        this.delayAfterEachCmd = delayAfterEachCmd;
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
     * @param terminalType the new terminal type
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
     * @param terminalWidth the new terminal width
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
     * @param terminalHeight the new terminal height
     */
    public void setTerminalHeight(int terminalHeight) {
        this.terminalHeight = terminalHeight;
    }

}
