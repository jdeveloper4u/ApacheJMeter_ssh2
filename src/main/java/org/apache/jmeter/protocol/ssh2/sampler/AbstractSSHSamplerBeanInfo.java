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

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.FileEditor;
import org.apache.jmeter.testbeans.gui.TypeEditor;

/**
 * The Class AbstractSSHSamplerBeanInfo.
 */
public abstract class AbstractSSHSamplerBeanInfo extends BeanInfoSupport {

    /**
     * Instantiates a new abstract ssh sampler bean info.
     *
     * @param clazz the clazz
     */
    public AbstractSSHSamplerBeanInfo(Class<? extends AbstractSSHSampler> clazz) {
        super(clazz);

        createPropertyGroup("server",
                new String[]{
                    "hostname",
                    "port",
                    "connectionTimeout",
                    "maxWaitForCommandOutput",
                    "username"
                });

        createPropertyGroup("authWithPassword",
                new String[]{
                    "password"
                });

        createPropertyGroup("authWithKeyFile",
                new String[]{
                    "sshkeyfile",
                    "passphrase"
                });

        PropertyDescriptor p;
        // server
        p = property("hostname");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("port");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, new Integer(22));

        p = property("connectionTimeout");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, new Integer(30000));

        p = property("maxWaitForCommandOutput");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, new Integer(10000));

        // user
        p = property("username");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("password", TypeEditor.PasswordEditor);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");


        // keyfile
        p = property("sshkeyfile");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setPropertyEditorClass(FileEditor.class);

        p = property("passphrase", TypeEditor.PasswordEditor);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

    }
}
