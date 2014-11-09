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

import org.apache.jmeter.protocol.ssh2.util.SSHClientConfig;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.testbeans.TestBean;

/**
 * The Class AbstractSSHSampler is parent class for all SSH2 type samplers.
 */
public abstract class AbstractSSHSampler extends AbstractSampler implements TestBean {

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID       = 1L;

    /** The response message's template. */
    protected static final String RESPONSE_MSG_TEMPLATE  = "ExitSignal:[{0}], StdErr:[{1}]";

    /** The response data encoding. */
    protected static final String RESPONSE_DATA_ENCODING = "UTF-8";

    /** The response data type. */
    protected static final String RESPONSE_DATA_TYPE     = "text";

    /** The response content type. */
    protected static final String RESPONSE_CONTENT_TYPE  = "text/plain";

    /** The host-name or IP address of remote SSH server. */
    private String              hostname               = null;

    /** The port of remote SSH server. */
    private int                 port                   = 22;

    /** The user-name. */
    private String              username               = null;

    /** The password of remote SSH server. */
    private String              password               = null;

    /** The ssh-key-file (private key file id_rsa/id_dsa type). */
    private String              sshkeyfile             = null;

    /**
     * The passphrase of ssh-key-file (which is provided at time of ssh-key-file
     * generation).
     */
    private String              passphrase             = null;

    /** The connection timeout while creating connection to remote SSH server. */
    private int                 connectionTimeout      = 30000;

    /** The max wait for command output. */
    private int               maxWaitForCommandOutput;

    /**
     * Instantiates a new abstract SSH sampler.
     *
     * @param name
     *            the name of this sampler.
     */
    public AbstractSSHSampler(String name) {
        super();
        setName(name);
    }

    /**
     * Gets the SSHClientConfig initialized with common properties.
     *
     * @return the SSHClientConfig initialized with common properties
     */
    protected SSHClientConfig getSSHClientConfig() {
        SSHClientConfig clientConfig = new SSHClientConfig();
        clientConfig.setHostname(hostname);
        clientConfig.setPort(port);
        clientConfig.setPassword(password);
        clientConfig.setUserName(username);
        clientConfig.setSshkeyfile(sshkeyfile);
        clientConfig.setPassphrase(passphrase);
        clientConfig.setConnectionTimeout(connectionTimeout);
        clientConfig.setMaxWaitTimeForOutput(maxWaitForCommandOutput);
        return clientConfig;
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
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Gets the max wait for command output.
     *
     * @return the max wait for command output
     */
    public int getMaxWaitForCommandOutput() {
        return maxWaitForCommandOutput;
    }

    /**
     * Sets the max wait for command output.
     *
     * @param maxWaitForCommandOutput
     *            the new max wait for command output
     */
    public void setMaxWaitForCommandOutput(int maxWaitForCommandOutput) {
        this.maxWaitForCommandOutput = maxWaitForCommandOutput;
    }


}
