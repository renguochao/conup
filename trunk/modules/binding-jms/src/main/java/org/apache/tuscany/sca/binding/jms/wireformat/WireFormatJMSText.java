/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.tuscany.sca.binding.jms.wireformat;

import javax.xml.namespace.QName;

import org.apache.tuscany.sca.assembly.WireFormat;
import org.apache.tuscany.sca.assembly.xml.Constants;

/**
 * Implementation for policies that could be injected as parameter
 * into the axis2config.
 *
 * @version $Rev: 1292261 $ $Date: 2012-02-22 12:48:51 +0000 (Wed, 22 Feb 2012) $
 */
public class WireFormatJMSText implements WireFormat {
    public static final QName WIRE_FORMAT_JMS_BYTES_QNAME = new QName(Constants.SCA11_TUSCANY_NS, "wireFormat.jmsText");
    
    public QName getSchemaName() {
        return WIRE_FORMAT_JMS_BYTES_QNAME;
    }

    public boolean isUnresolved() {
        return false;
    }

    public void setUnresolved(boolean unresolved) {
    }
    
    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
