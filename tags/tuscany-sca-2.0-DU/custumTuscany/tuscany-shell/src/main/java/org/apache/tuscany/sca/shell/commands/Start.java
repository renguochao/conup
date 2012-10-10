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

package org.apache.tuscany.sca.shell.commands;

import java.io.File;
import java.util.List;

import jline.Completor;
import jline.NullCompletor;

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.assembly.Composite;
import org.apache.tuscany.sca.contribution.Contribution;
import org.apache.tuscany.sca.shell.Command;
import org.apache.tuscany.sca.shell.Shell;
import org.apache.tuscany.sca.shell.jline.CompositeURICompletor;
import org.apache.tuscany.sca.shell.jline.ICURICompletor;
import org.apache.tuscany.sca.shell.jline.RemoteNodeCompletor;

import cn.edu.nju.moon.conup.container.VcContainer;
import cn.edu.nju.moon.conup.container.VcContainerImpl;
import cn.edu.nju.moon.conup.def.NodeHolder;

public class Start implements Command {

    private Shell shell;
    
    public Start(Shell shell) {
        this.shell = shell;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getShortHelp() {
        return "start <curi> <compositeUri> [<remoteNode>]";
    }

    @Override
    public String getHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("   Start a composite.\n");
        helpText.append("\n");
        helpText.append("   The composite is added to the domain composite with semantics that correspond to the domain-level\n");
        helpText.append("   composite having an <include> statement that references the supplied composite. All of the composite's\n");
        helpText.append("   components become top-level components and the component services become externally visible\n");
        helpText.append("   services (eg. they would be present in a WSDL description of the Domain).\n");
        helpText.append("\n");
        helpText.append("   Arguments:\n");
        helpText.append("      curi         - (required) the URI of an installed contribution\n");
        helpText.append("      compositeUri - (required) the URI of a composite within the contribution to start\n");
        helpText.append("      remoteNode   - (optional) the name of a remote node which should run the composite\n");
        return helpText.toString();
    }

    @Override
    public Completor[] getCompletors() {
        return new Completor[]{new ICURICompletor(shell), new CompositeURICompletor(shell), new RemoteNodeCompletor(shell), new NullCompletor()};
    }

    @Override
    public boolean invoke(String[] args) throws Exception {
        if (args.length == 2) {
            shell.getNode().startComposite(args[0], args[1]);
        } else if (args.length == 3) {
            shell.getNode().startComposite(args[0], args[1], args[2]);
        } else {
            System.err.println("Wrong number of args");
            System.err.println(getShortHelp());
            return true;
        }
        
        //added for conup
        String curi = args[0];
        File file = new File(curi);
        String absContributionPath = file.getAbsolutePath();
        Node node = shell.getNode();
        Contribution cont = node.getContribution(curi);
        List<Composite> composites = cont.getDeployables();
        Composite composite = composites.get(0);
        String componentName = null;
        String compositeName = null;
        String compositeFileName = null;
        //get component name
        componentName = composite.getComponents().get(0).getName();
        compositeName = composite.getURI();
        compositeFileName = args[1];
        System.out.println("absContributionPath: " + absContributionPath);
        System.out.println("compositeFileName: " + compositeFileName);
        //get VcContainer
        String compositeLocation = curi + File.separator + compositeName;	//TODO
        if(!absContributionPath.contains("conup-domain-manager")){
        	VcContainer container = VcContainerImpl.getInstance();
            container.setBusinessComponentName(componentName, compositeLocation, 
            		absContributionPath, compositeFileName, shell.getCurrentDomain());
//            container.analyseNodeComposite(compositeLocation);
            //add current business node to container
            container.setBusinessNode(node, componentName);
        }  else{
        	NodeHolder nodeHolder = null;
        	nodeHolder = NodeHolder.getInstance();
        	nodeHolder.setNode(node);
        }
        
        return true;
    }

}
