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
 * The Class SSHException.
 */
public class SSHException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7362396076622734049L;

    /**
     * Instantiates a new SSH exception.
     */
    public SSHException() {
    }

    /**
     * Instantiates a new SSH exception.
     *
     * @param message
     *            the message
     */
    public SSHException(String message) {
        super(message);
    }

    /**
     * Instantiates a new SSH exception.
     *
     * @param cause
     *            the cause
     */
    public SSHException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new SSH exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new SSH exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     * @param enableSuppression
     *            the enable suppression
     * @param writableStackTrace
     *            the writable stack trace
     */
    public SSHException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
