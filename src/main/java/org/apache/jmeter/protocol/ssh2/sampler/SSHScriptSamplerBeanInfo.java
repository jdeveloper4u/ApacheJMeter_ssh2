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

import org.apache.jmeter.testbeans.gui.TypeEditor;

public class SSHScriptSamplerBeanInfo extends AbstractSSHSamplerBeanInfo {

    public SSHScriptSamplerBeanInfo() {

        super(SSHScriptSampler.class);

        createPropertyGroup("executeScript", new String[] { "enableWaitCmd"
                , "delayAfterEachCmd"
                , "terminalType"
                , "terminalWidth"
                , "terminalHeight"
                , "scriptContent" });

        PropertyDescriptor p = property("enableWaitCmd");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);

        p = property("delayAfterEachCmd");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 0);

        p = property("terminalType");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "vt220");

        p = property("terminalWidth");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 200);

        p = property("terminalHeight");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 50);

        p = property("scriptContent", TypeEditor.TextAreaEditor);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(TEXT_LANGUAGE, "bash");

        // override the default value set by parent
        p = property("maxWaitForCommandOutput");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 30000);

    }

}
