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
package org.apache.tuscany.sca.node.equinox.launcher;

/**
 * Represents an SCA contribution uri + location.
 *
 * @version $Rev: 689677 $ $Date: 2008-08-28 01:17:02 +0100 (Thu, 28 Aug 2008) $
 */
public final class Contribution {
    private String uri;
    private String location;
    
    /**
     * Constructs a new SCA contribution.
     * 
     * @param uri
     * @param location
     */
    public Contribution(String uri, String location) {
        this.uri = uri;
        this.location = location;
    }
    
    public String getURI() {
        return uri;
    }
    
    public String getLocation() {
        return location;
    }
}