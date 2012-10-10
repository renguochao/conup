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

package org.apache.tuscany.sca.binding.rest.wireformat.xml;

import javax.xml.namespace.QName;

import org.apache.tuscany.sca.assembly.WireFormat;

/**
 * XML Wireformat model for REST Binding
 * 
 * @version $Rev: 939193 $ $Date: 2010-04-29 05:49:38 +0100 (Thu, 29 Apr 2010) $
 */
public interface XMLWireFormat extends WireFormat {

    /**
     *  QName representing the XML Wireformat for REST Binding 
     */
    public static final QName REST_WIREFORMAT_XML_QNAME = new QName(SCA11_TUSCANY_NS, "wireFormat.xml");
    
    /**
     * Return the QName identifying the wire format 
     * @return the QName identifying the wire format
     */
    QName getSchemaName();
}