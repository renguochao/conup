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

package org.apache.tuscany.sca.shell;

import static java.lang.System.in;
import static java.lang.System.out;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.assembly.Component;
import org.apache.tuscany.sca.assembly.Composite;
import org.apache.tuscany.sca.assembly.Endpoint;
import org.apache.tuscany.sca.assembly.SCABinding;
import org.apache.tuscany.sca.assembly.xml.Utils;
import org.apache.tuscany.sca.common.java.io.IOHelper;
import org.apache.tuscany.sca.contribution.Artifact;
import org.apache.tuscany.sca.contribution.Contribution;
import org.apache.tuscany.sca.contribution.processor.ContributionReadException;
import org.apache.tuscany.sca.extensibility.ServiceDeclaration;
import org.apache.tuscany.sca.extensibility.ServiceDiscovery;
import org.apache.tuscany.sca.impl.DeployedComposite;
import org.apache.tuscany.sca.impl.NodeImpl;
import org.apache.tuscany.sca.monitor.ValidationException;
import org.apache.tuscany.sca.runtime.ActivationException;
import org.apache.tuscany.sca.runtime.ContributionDescription;
import org.apache.tuscany.sca.runtime.DomainRegistry;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.Version;
import org.apache.tuscany.sca.shell.jline.JLine;

import com.tuscanyscatours.coordination.CoordinationVisitorThread;
import com.tuscanyscatours.fake.root.FakeTxThread;

import cn.edu.nju.conup.comm.api.manager.CommServerManager;
import cn.edu.nju.moon.conup.apppre.TuscanyProgramAnalyzer;
import cn.edu.nju.moon.conup.comm.api.server.ServerIoHandler;
import cn.edu.nju.moon.conup.experiments.utils.ConupExpCommands;
import cn.edu.nju.moon.conup.experiments.utils.ExecutionRecorderAnalyzer;
import cn.edu.nju.moon.conup.experiments.utils.MyPoissonProcess;
import cn.edu.nju.moon.conup.experiments.utils.TravelCompUpdate;
import cn.edu.nju.moon.conup.experiments.utils.TravelExpResultQuery;
import cn.edu.nju.moon.conup.ext.comp.manager.CompLifecycleManagerImpl;
import cn.edu.nju.moon.conup.ext.tx.manager.TxDepMonitorImpl;
import cn.edu.nju.moon.conup.ext.tx.manager.TxLifecycleManagerImpl;
import cn.edu.nju.moon.conup.ext.utils.experiments.CallBack;
import cn.edu.nju.moon.conup.ext.utils.experiments.CorrectnessExp;
import cn.edu.nju.moon.conup.ext.utils.experiments.DeviationExp;
import cn.edu.nju.moon.conup.ext.utils.experiments.DisruptionExp;
import cn.edu.nju.moon.conup.ext.utils.experiments.OverheadExp;
import cn.edu.nju.moon.conup.ext.utils.experiments.model.ExpSetting;
import cn.edu.nju.moon.conup.ext.utils.experiments.model.ExperimentOperation;
import cn.edu.nju.moon.conup.ext.utils.experiments.model.ResponseTimeRecorder;
import cn.edu.nju.moon.conup.ext.utils.experiments.model.RqstInfo;
import cn.edu.nju.moon.conup.ext.utils.experiments.utils.ExpXMLUtil;
import cn.edu.nju.moon.conup.spi.datamodel.ComponentObject;
import cn.edu.nju.moon.conup.spi.datamodel.Scope;
import cn.edu.nju.moon.conup.spi.manager.DynamicDepManager;
import cn.edu.nju.moon.conup.spi.manager.NodeManager;
import cn.edu.nju.moon.conup.spi.tx.TxDepMonitor;
import cn.edu.nju.moon.conup.spi.tx.TxLifecycleManager;
import cn.edu.nju.moon.conup.spi.update.CompLifeCycleManager;
import cn.edu.nju.moon.conup.spi.update.UpdateManager;

/**
 * A little SCA command shell.
 */
public class Shell {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Shell.class.getName());
	private static boolean stopExp = false;
	
	private boolean useJline;
    final List<String> history = new ArrayList<String>();
    private TuscanyRuntime runtime;
    private String currentDomain = "";
    private Map<String, Node> standaloneNodes = new HashMap<String, Node>();
    private Map<String, Node> nodes = new HashMap<String, Node>();
    private Map<String, Command> commands = new HashMap<String, Command>();

    public static final String[] COMMANDS = new String[] {"bye", "domain", "domains", "domainComposite", "help", "install", "installed",
                                                          "load", "nodes", "remove", "run", "save", "services", "started"};

    public static void main(final String[] args) throws Exception {
        boolean useJline = true;
        boolean requirePreprocessor = true;
//        boolean requireContainer = true;
        String domainURI = "uri:default";
        
        @SuppressWarnings("unused")
		String compositeFileName = null;
        NodeManager nodeMgr = NodeManager.getInstance();
        
        boolean showHelp = false;
        String contribution = null;
        String nodeXML = null;
        for (String s : args) {
            if ("-nojline".equals(s)) {
                useJline = false;
            } else if ("-help".equals(s)) {
                showHelp = true;
            } else if (s.startsWith("-nodeXML:")) {
            	nodeXML = s.substring("-nodeXML:".length());
            } else if("-noPreprocessor".equals(s)){
            	requirePreprocessor = false;
            } else {
                if (s.startsWith("uri:") || s.startsWith("properties:")) {
                    domainURI = s;
                } else if(s.endsWith(".composite")){
                	compositeFileName = s;
                } else {
                    contribution = s;
                }
            }
        }
        Shell shell;
		if (nodeXML != null) {
			shell = new Shell(new File(nodeXML), useJline);
		} else {
			shell = new Shell(domainURI, useJline);
			if (showHelp || contribution == null) {
				shell.help(null);
			}	// END IF
			if (contribution != null) {
				// added for conup
				File file = new File(contribution);
				String absContributionPath = file.getAbsolutePath();

				System.out.println("Try to preprocess source code...");

				// preprocess
				if (requirePreprocessor) {	
					TuscanyProgramAnalyzer analyzer;
					analyzer = new TuscanyProgramAnalyzer();	
					analyzer.analyzeApplication(absContributionPath,	
							absContributionPath);
					System.out.println("Preprocessing is done...");	
				} else {
					System.out.println("Skipped preprocessing...");
				}

				System.out.println();
				System.out.println("install " + contribution + " -start");
				String curi = shell.getNode().installContribution(contribution);
				shell.getNode().startDeployables(curi);

				Thread.sleep(1500);
				
				Node node = shell.getNode();
				Map<String, DeployedComposite> startedComposites = ((NodeImpl)node).getStartedComposites();
				Iterator<Entry<String, DeployedComposite>> iterator = startedComposites.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, DeployedComposite> entry = iterator.next();
					String uri = entry.getKey();
					DeployedComposite deployComposite = entry.getValue();
					if(uri.contains(curi)){
						Composite composite = deployComposite.getBuiltComposite();
						List<Component> components = composite.getComponents();
						for(Component comp : components){
							RuntimeComponent runtimeComponent = (RuntimeComponent)comp;
							String componentName = runtimeComponent.getName();
							nodeMgr.loadConupConf(componentName, "Version");
							ComponentObject compObj = nodeMgr.getComponentObject(componentName);
							
							// create CompLifecycleManager, TxDepMonitor, TxLifecycleMgr
							CompLifeCycleManager compLifecycleMgr = new CompLifecycleManagerImpl(compObj);
							nodeMgr.setCompLifecycleManager(componentName, compLifecycleMgr);
							
							TxLifecycleManager txLifecycleMgr = new TxLifecycleManagerImpl(compObj);
							nodeMgr.setTxLifecycleManager(componentName, txLifecycleMgr);

							TxDepMonitor txDepMonitor = new TxDepMonitorImpl(compObj);
							nodeMgr.setTxDepMonitor(componentName, txDepMonitor);
							
							DynamicDepManager depMgr = NodeManager.getInstance().getDynamicDepManager(compObj.getIdentifier());
							depMgr.setTxLifecycleMgr(txLifecycleMgr);
							depMgr.setCompLifeCycleMgr(compLifecycleMgr);
							
							nodeMgr.getOndemandSetupHelper(compObj.getIdentifier());
							nodeMgr.setTuscanyNode(node);
							
							UpdateManager updateMgr = nodeMgr.getUpdateManageer(componentName);
							
						    CommServerManager.getInstance().start(componentName);
						    
							ServerIoHandler serverIoHandler = CommServerManager
									.getInstance().getCommServer(componentName)
									.getServerIOHandler();
							serverIoHandler.registerUpdateManager(updateMgr);
						    
						}
					}
				}
					
				if(absContributionPath.contains("coordination")){
					Thread.sleep(5000);
					accessServices(node, shell);
				}
			}	// END IF
		}	// END ELSE
        shell.run();
    }
    
    private static void accessServices(Node node, Shell shell) throws Exception{
		printHelp();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()){
			String [] input = scanner.nextLine().split(" ");
			ConupExpCommands command = null;
			try{
				command = Enum.valueOf(ConupExpCommands.class, input[0].trim());
			} catch(Exception e){
				System.out.println("Unsupported command. input 'help' for help.");
				continue;
			}
			
			switch (command) {
			case disruptionAndTimeliness:
				if(input.length == 1){
					doDisruptionAndTimelinessExp(node);
				} else{
					System.out.println("wrong number parameters for 'disruptionExp'");
					break;
				}
				break;
			case correctness:
				if(input.length == 1){
					doCorrectnessExp(node);
				} else{
					System.out.println("wrong number parameters for 'correctnessExp'");
					break;
				}
				break;
			case oneRoundResponse:
				if(input.length == 1){
					doOneRoundResponseExp(node);
				} else{
					System.out.println("wrong number parameters for 'oneRoundResponse'");
					break;
				}
			case txGranularity:
				doTxGranularity(node);
				break;
			case tuscany:
				shell.run();
				break;
			case invoke:
				int runningTimes = 0;
				if( input.length == 2){
					runningTimes = Integer.parseInt(input[1].trim());
					for(int i = 1; i<= runningTimes; i++){
						new CoordinationVisitorThread(node, i).start();
						Thread.sleep(200);
					}
				} else{
					System.out.println("Illegal parameters for 'invoke'");
					break;
				}
		
				break;
			case help:
				printHelp();
				break;
			case update:
				doUpdate();
				break;
			default:
				System.out.println("Unsupported command. input 'help' for help.");
				break;
			}
			
		}//WHILE
	}
    
    private static void doUpdate() {
    	ExpXMLUtil xmlUtil = new ExpXMLUtil();
    	ExpSetting expSetting = xmlUtil.getExpSetting();
    	
    	String targetComp = expSetting.getTargetComp();
    	String ipAddress = expSetting.getIpAddress();
    	Scope scope = expSetting.getScope();
    	
    	System.out.println("---------------------------------------------");
    	System.out.println("targetComp:" + targetComp);
    	System.out.println("ipAddress:" + ipAddress);
    	System.out.println("---------------------------------------------");
    	
    	TravelCompUpdate.update(targetComp, ipAddress, scope);
	}

	private static void doCorrectnessExp(Node node) throws InterruptedException, InvalidParamsException {
		
		CorrectnessExp correctnessExp = CorrectnessExp.getInstance();
		
		ExpXMLUtil xmlUtil = new ExpXMLUtil();
    	ExpSetting expSetting = xmlUtil.getExpSetting();
    	
    	// the interval between each request thread
    	int rqstInterval = expSetting.getRqstInterval();

    	int indepRun = expSetting.getIndepRun();
    	// launching the update threadId
    	final String targetComp = expSetting.getTargetComp();
    	final String ipAddress = expSetting.getIpAddress();
    	final Scope scope = expSetting.getScope();
		
		// make request arrival obey to poission process
    	Event event = null;
    	int seed = 123456789;
    	Properties params = new Properties();
    	float MeanArrival = rqstInterval;
    	params.setProperty("meanArrival", Float.toString(MeanArrival));
    	ArrayList<Event> refEvents = new ArrayList<Event>();
    	MyPoissonProcess mpp = new MyPoissonProcess("myPoissonProcess", params, null, refEvents);
    	Random random = new Random(seed);
    	mpp.setRandom(random);
		
		int warmUpTimes = 400;
		CountDownLatch warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new CoordinationVisitorThread(node, warmCountDown).start();
			if(i == 300){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			if(i > 200)
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			else	
				Thread.sleep(200);
		}
		warmCountDown.await();
		TravelExpResultQuery.queryExpResult(targetComp, ExperimentOperation.GET_EXECUTION_RECORDER, ipAddress);
		
		Thread.sleep(3000);
		
		DisruptionExp disExp = DisruptionExp.getInstance();
		final DisruptionExp anotherDisExp = disExp;
		disExp.setUpdateIsDoneCallBack(new CallBack(){
			@Override
			public void callback(){
				stopExp = true;
			}
		});

		for(int round = 0; round < indepRun; round++){
			ResponseTimeRecorder resTimeRec = new ResponseTimeRecorder();
			System.out.println("-------------round " + round + "--------------");
			
			
			TimerTask sendUpdateTask = new TimerTask(){
				@Override
				public void run(){
					System.out.println("start send update command " + new Date());
					anotherDisExp.setUpdateStartTime(System.nanoTime());
					TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
			};
			Timer sendUpdateTimer = new Timer();
			sendUpdateTimer.schedule(sendUpdateTask, 15000);
			
			TimerTask abortTask = new TimerTask(){

				@Override
				public void run() {
					if(!stopExp){
						System.out.println("excede 20s, stop to send request " + new Date());
						stopExp = true;
					}
				}
				
			};
			Timer abortTimer = new Timer();
			abortTimer.schedule(abortTask, 25000);
			
			int threadsNum = 0;
			while(!stopExp){
				new CoordinationVisitorThread(node, threadsNum + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
				threadsNum ++;
			}
			
			CountDownLatch updateCountDown = new CountDownLatch(50);
			for(int j = threadsNum; j < threadsNum + 50; j++){
				new CoordinationVisitorThread(node, updateCountDown, j + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			
			updateCountDown.await();
			
			int port = 0;
			if(targetComp.equals("CurrencyConverter")){
				port = 22300;
			} else if(targetComp.equals("TripPartner")){
				port = 22304;
			} else if(targetComp.equals("HotelPartner")){
				port = 22301;
			}
//			String updateEndTime = new RemoteConfServiceImpl().getUpdateEndTime(ipAddress, port, targetComp, "CONSISTENCY");
//			System.out.println("updateEndTime:" + updateEndTime + " algorithm: TRANQUILLITY");
//			DisruptionExp.getInstance().setUpdateEndTime(Long.parseLong(updateEndTime));
			System.out.println("updateEndTime:" + DisruptionExp.getInstance().getUpdateEndTime() + " updateStartTime:" + DisruptionExp.getInstance().getUpdateStartTime());
			
			Thread.sleep(3000);
			
			Map<Integer, Long> updateRes = resTimeRec.getUpdateRes();
			// write all normal, update response to file in devation folder
			
			System.out.println("updateRes.size() ==" + updateRes.size() + " threadsNum:" + threadsNum);
			
			Map<Integer, Double> disruptedTxsResTime = resTimeRec.getDisruptedTxResTime();
			int disruptedTxs = disruptedTxsResTime.size();
			String gerResult =TravelExpResultQuery.queryExpResult(targetComp, ExperimentOperation.GET_EXECUTION_RECORDER, ipAddress);
//			System.out.println(gerResult);
			ExecutionRecorderAnalyzer analyzer = new ExecutionRecorderAnalyzer(gerResult);
			int totalRecords = analyzer.getTotalRecords();
			String correctnessExpData = round + ", " + analyzer.getInconsistentRecords() + ", " + disruptedTxs + "\n";
			correctnessExp.writeToFile(correctnessExpData);
			System.out.println("inconsistent/total/disruptedTxs: " + analyzer.getInconsistentRecords() + "/" + totalRecords + "/" + disruptedTxs);
			
			Thread.sleep(3500);
			
			// after one round running, reset flag
			stopExp = false;
		}
	}

	@Deprecated
	private static void doTimelinessExp(Node node) throws InterruptedException, InvalidParamsException {
		
			ExpXMLUtil xmlUtil = new ExpXMLUtil();
	    	ExpSetting expSetting = xmlUtil.getExpSetting();
	    	
	    	// the interval between each request thread
	    	int rqstInterval = expSetting.getRqstInterval();
	    	
	    	int indepRun = expSetting.getIndepRun();
	    	int nThreads = expSetting.getnThreads();
	    	// launching the update threadId
	    	int threadId = expSetting.getThreadId();
	    	String targetComp = expSetting.getTargetComp();
	    	String ipAddress = expSetting.getIpAddress();
	    	Scope scope = expSetting.getScope();
	    	LOGGER.fine("nThreads:" + nThreads + "\nthreadId" + threadId);
	    	
	    	// make request arrival as poission process
	    	Event event = null;
	    	int seed = 123456789;
	    	Properties params = new Properties();
	    	float MeanArrival = rqstInterval;
	    	params.setProperty("meanArrival", Float.toString(MeanArrival));
	    	ArrayList<Event> refEvents = new ArrayList<Event>();
	    	MyPoissonProcess mpp = new MyPoissonProcess("myPoissonProcess", params, null, refEvents);
	    	Random random = new Random(seed);
	    	mpp.setRandom(random);

	    	// warm up code
	    	int warmUpTimes = 400;
			CountDownLatch warmCountDown = new CountDownLatch(warmUpTimes);
			for (int i = 0; i < warmUpTimes; i++) {
				new CoordinationVisitorThread(node, warmCountDown).start();
				if(i == 300){
					TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
				if(i > 200)
					Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
				else	
					Thread.sleep(200);
			}
			warmCountDown.await();
	
			Thread.sleep(3000);
	    	for(int i = 0; i < indepRun; i++){
	    		CountDownLatch updateCountDown = new CountDownLatch(nThreads);
				for (int j = 0; j < nThreads; j++) {
					Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
					new CoordinationVisitorThread(node).start();
					if(j == threadId)
						TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
				updateCountDown.await();
	    		Thread.sleep(3000);
	    	}
		}

	private static void doDisruptionAndTimelinessExp(Node node) throws InterruptedException, InvalidParamsException {
    	ExpXMLUtil xmlUtil = new ExpXMLUtil();
    	ExpSetting expSetting = xmlUtil.getExpSetting();
    	
    	// the interval between each request thread
    	int rqstInterval = expSetting.getRqstInterval();

    	int indepRun = expSetting.getIndepRun();
    	final String targetComp = expSetting.getTargetComp();
    	final String ipAddress = expSetting.getIpAddress();
    	final Scope scope = expSetting.getScope();
    	
    	System.out.println("---------------------------------------------");
    	System.out.println("targetComp:" + targetComp);
    	System.out.println("ipAddress:" + ipAddress);
    	System.out.println("scope:" + scope);
    	System.out.println("---------------------------------------------");
    	
    	// make request arrival as poission process
    	Event event = null;
    	int seed = 123456789;
    	Properties params = new Properties();
    	float MeanArrival = rqstInterval;
    	params.setProperty("meanArrival", Float.toString(MeanArrival));
    	ArrayList<Event> refEvents = new ArrayList<Event>();
    	MyPoissonProcess mpp = new MyPoissonProcess("myPoissonProcess", params, null, refEvents);
    	Random random = new Random(seed);
    	mpp.setRandom(random);
    	
		int warmUpTimes = 400;
		CountDownLatch warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new CoordinationVisitorThread(node, warmCountDown).start();
			if(i == 300){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			Thread.sleep(200);
		}
		warmCountDown.await();
		
		warmUpTimes = 200;
		warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new CoordinationVisitorThread(node, warmCountDown).start();
			if(i == 100){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
		}
		warmCountDown.await();
		
		Thread.sleep(3000);
		final DisruptionExp disExp = DisruptionExp.getInstance();
		disExp.setUpdateIsDoneCallBack(new CallBack(){
			@Override
			public void callback(){
				stopExp = true;
			}
		});
		DeviationExp devExp = DeviationExp.getInstance();

		for(int round = 0; round < indepRun; round++){
			ResponseTimeRecorder resTimeRec = new ResponseTimeRecorder();
			System.out.println("-------------round " + round + "--------------");
			
			TimerTask sendUpdateTask = new TimerTask(){
				@Override
				public void run(){
					System.out.println("start send update command " + new Date());
					disExp.setUpdateStartTime(System.nanoTime());
					TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
			};
			Timer sendUpdateTimer = new Timer();
			sendUpdateTimer.schedule(sendUpdateTask, 15000);
			
			TimerTask abortTask = new TimerTask(){

				@Override
				public void run() {
					if(!stopExp){
						System.out.println("excede 20s, stop to send request " + new Date());
						stopExp = true;
					}
				}
				
			};
			Timer abortTimer = new Timer();
			abortTimer.schedule(abortTask, 35000);
			
			int threadsNum = 0;
			while(!stopExp){
				new CoordinationVisitorThread(node, threadsNum + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
				threadsNum ++;
			}
			
			CountDownLatch updateCountDown = new CountDownLatch(100);
			for(int j = threadsNum; j < threadsNum + 100; j++){
				new CoordinationVisitorThread(node, updateCountDown, j + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			
			updateCountDown.await();
			
			Thread.sleep(3000);
			
			CountDownLatch normalCountDown = new CountDownLatch(threadsNum + 100);
			for (int i = 0; i < threadsNum + 100; i++) {
				new CoordinationVisitorThread(node, normalCountDown, i + 1, resTimeRec, "normal").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			normalCountDown.await();
			
			
			Thread.sleep(1000);
			
			Map<Integer, Long> normalRes = resTimeRec.getNormalRes();
			Map<Integer, Long> updateRes = resTimeRec.getUpdateRes();
			// write all normal, update response to file in devation folder
			devExp.writeToFile(round, normalRes, updateRes);
			
			System.out.println("normalRes.size() ==" + normalRes.size() + " threadsNum:" + threadsNum);
			System.out.println("updateRes.size() ==" + updateRes.size() + " threadsNum:" + threadsNum);
			
			Map<Integer, Double> disruptedTxsResTime = resTimeRec.getDisruptedTxResTime();
			Iterator<Entry<Integer, Double>> iter = disruptedTxsResTime.entrySet().iterator();
			int count = 0;
			String data = null;
			while(iter.hasNext()){
				Entry<Integer, Double> entry = iter.next();
				int curThreadId = entry.getKey();
				Double resTime = entry.getValue();
				if(count == 0)
					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "," + disExp.getTimelinessTime() + "\n";
				else
					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "\n";
				LOGGER.fine(data);
				disExp.writeToFile(data);
				count++;
			}
			if(round == 24)
				Thread.sleep(6000);
			else
				Thread.sleep(3500);
			
			// after one round running, reset flag
			stopExp = false;
		}
		
		Thread.sleep(1000);
		disExp.close();
    	
	}
	
	private static void doTxGranularity(Node node) throws InterruptedException, InvalidParamsException {
    	ExpXMLUtil xmlUtil = new ExpXMLUtil();
    	ExpSetting expSetting = xmlUtil.getExpSetting();
    	
    	// the interval between each request thread
    	int rqstInterval = expSetting.getRqstInterval();

    	int indepRun = expSetting.getIndepRun();
    	final String targetComp = expSetting.getTargetComp();
    	final String ipAddress = expSetting.getIpAddress();
    	final String baseDir = expSetting.getBaseDir();
    	final Scope scope = expSetting.getScope();
    	
    	System.out.println("---------------------------------------------");
    	System.out.println("targetComp:" + targetComp);
    	System.out.println("ipAddress:" + ipAddress);
    	System.out.println("baseDir:" + baseDir);
    	System.out.println("scope:" + scope);
    	System.out.println("---------------------------------------------");
    	
    	// make request arrival as poission process
    	Event event = null;
    	int seed = 123456789;
    	Properties params = new Properties();
    	float MeanArrival = rqstInterval;
    	params.setProperty("meanArrival", Float.toString(MeanArrival));
    	ArrayList<Event> refEvents = new ArrayList<Event>();
    	MyPoissonProcess mpp = new MyPoissonProcess("myPoissonProcess", params, null, refEvents);
    	Random random = new Random(seed);
    	mpp.setRandom(random);
    	
		int warmUpTimes = 400;
		CountDownLatch warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new FakeTxThread(node, warmCountDown).start();
			if(i == 300){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			Thread.sleep(200);
		}
		warmCountDown.await();
		
		warmUpTimes = 200;
		warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new FakeTxThread(node, warmCountDown).start();
			if(i == 100){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
		}
		warmCountDown.await();
		
		Thread.sleep(3000);
		final DisruptionExp disExp = DisruptionExp.getInstance();
		disExp.setUpdateIsDoneCallBack(new CallBack(){
			@Override
			public void callback(){
				stopExp = true;
			}
		});
		DeviationExp devExp = DeviationExp.getInstance();

		for(int round = 0; round < indepRun; round++){
			ResponseTimeRecorder resTimeRec = new ResponseTimeRecorder();
			System.out.println("-------------round " + round + "--------------");
			
			TimerTask sendUpdateTask = new TimerTask(){
				@Override
				public void run(){
					System.out.println("start send update command " + new Date());
					disExp.setUpdateStartTime(System.nanoTime());
					TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
			};
			Timer sendUpdateTimer = new Timer();
			sendUpdateTimer.schedule(sendUpdateTask, 15000);
			
			TimerTask abortTask = new TimerTask(){

				@Override
				public void run() {
					if(!stopExp){
						System.out.println("excede 20s, stop to send request " + new Date());
						stopExp = true;
					}
				}
				
			};
			Timer abortTimer = new Timer();
			abortTimer.schedule(abortTask, 35000);
			
			int threadsNum = 0;
			while(!stopExp){
				new FakeTxThread(node, threadsNum + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
				threadsNum ++;
			}
			
			CountDownLatch updateCountDown = new CountDownLatch(100);
			for(int j = threadsNum; j < threadsNum + 100; j++){
				new FakeTxThread(node, updateCountDown, j + 1, resTimeRec, "update").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			
			updateCountDown.await();
			
			Thread.sleep(3000);
			
			CountDownLatch normalCountDown = new CountDownLatch(threadsNum + 100);
			for (int i = 0; i < threadsNum + 100; i++) {
				new FakeTxThread(node, normalCountDown, i + 1, resTimeRec, "normal").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			normalCountDown.await();
			
			
			Thread.sleep(1000);
			
			Map<Integer, Long> normalRes = resTimeRec.getNormalRes();
			Map<Integer, Long> updateRes = resTimeRec.getUpdateRes();
			// write all normal, update response to file in devation folder
			devExp.writeToFile(round, normalRes, updateRes);
			
			System.out.println("normalRes.size() ==" + normalRes.size() + " threadsNum:" + threadsNum);
			System.out.println("updateRes.size() ==" + updateRes.size() + " threadsNum:" + threadsNum);
			
			Map<Integer, Double> disruptedTxsResTime = resTimeRec.getDisruptedTxResTime();
			Iterator<Entry<Integer, Double>> iter = disruptedTxsResTime.entrySet().iterator();
			int count = 0;
			String data = null;
			while(iter.hasNext()){
				Entry<Integer, Double> entry = iter.next();
				int curThreadId = entry.getKey();
				Double resTime = entry.getValue();
				if(count == 0)
					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "," + disExp.getTimelinessTime() + "\n";
				else
					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "\n";
				LOGGER.fine(data);
				disExp.writeToFile(data);
				count++;
			}
			if(round == 24)
				Thread.sleep(6000);
			else
				Thread.sleep(3500);
			
			// after one round running, reset flag
			stopExp = false;
		}
		
		Thread.sleep(1000);
		disExp.close();
    	
	}
	
	/**
	 * One round response experiment
	 * first run the application 1000 times to warm up, then start the experiment
	 * every experiment has the same request reach time
	 */
	private static void doOneRoundResponseExp(Node node) throws Exception {
		ExpXMLUtil xmlUtil = new ExpXMLUtil();
    	ExpSetting expSetting = xmlUtil.getExpSetting();
    	
    	// the interval between each request thread
    	int rqstInterval = expSetting.getRqstInterval();
    	int indepRun = expSetting.getIndepRun();
    	// launching the update threadId
    	final String targetComp = expSetting.getTargetComp();
    	final String ipAddress = expSetting.getIpAddress();
    	final Scope scope = expSetting.getScope();
    	
    	// make request arrival as poission process
    	Event event = null;
    	int seed = 123456789;
    	Properties params = new Properties();
    	float MeanArrival = rqstInterval;
    	params.setProperty("meanArrival", Float.toString(MeanArrival));
    	ArrayList<Event> refEvents = new ArrayList<Event>();
    	MyPoissonProcess mpp = new MyPoissonProcess("myPoissonProcess", params, null, refEvents);
    	Random random = new Random(seed);
    	mpp.setRandom(random);
    	
		int warmUpTimes = 400;
		CountDownLatch warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new CoordinationVisitorThread(node, warmCountDown).start();
			if(i == 300){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			Thread.sleep(200);
		}
		warmCountDown.await();
		
		warmUpTimes = 600;
		warmCountDown = new CountDownLatch(warmUpTimes);
		for (int i = 0; i < warmUpTimes; i++) {
			new CoordinationVisitorThread(node, warmCountDown).start();
			if(i % 100 == 0){
				TravelCompUpdate.update(targetComp, ipAddress, scope);
			}
			
			Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
		}
		warmCountDown.await();
		
		Thread.sleep(3000);
		
		final DisruptionExp disExp = DisruptionExp.getInstance();
		disExp.setUpdateIsDoneCallBack(new CallBack(){
			@Override
			public void callback(){
				stopExp = true;
			}
		});
		DeviationExp devExp = DeviationExp.getInstance();
		// reset random seed
		mpp.setRandom(random);
		for(int i = 0; i < 10000; i++)
			 mpp.getNextTriggeringTime(event, 0);
		
		for(int round = 0; round < indepRun; round++){
			ResponseTimeRecorder resTimeRec = new ResponseTimeRecorder();
			System.out.println("-------------round " + round + "--------------");
			
			// send update request at 15s
			TimerTask sendUpdateTask = new TimerTask(){
				@Override
				public void run(){
					System.out.println("start send update command " + new Date());
					disExp.setUpdateStartTime(System.nanoTime());
					TravelCompUpdate.update(targetComp, ipAddress, scope);
				}
			};
			Timer sendUpdateTimer = new Timer();
			sendUpdateTimer.schedule(sendUpdateTask, 15000);
			
			// when the update time exceed 20s, then we stop send any new requests.
			TimerTask abortTask = new TimerTask(){

				@Override
				public void run() {
					if(!stopExp){
						System.out.println("exceed 20s, stop to send request " + new Date());
						stopExp = true;
					}
				}
				
			};
			Timer abortTimer = new Timer();
			abortTimer.schedule(abortTask, 35000);
			
			int threadsNum = 0;
			long time = 0;
			while(!stopExp){
				new CoordinationVisitorThread(node, threadsNum + 1, resTimeRec, "update", time).start();
				long sleepTime = (long) mpp.getNextTriggeringTime(event, 0);
				Thread.sleep(sleepTime);
				time += sleepTime;
				threadsNum ++;
			}
			
			CountDownLatch updateCountDown = new CountDownLatch(100);
			for(int j = threadsNum; j < threadsNum + 100; j++){
				new CoordinationVisitorThread(node, updateCountDown, j + 1, resTimeRec, "update", time).start();
				long sleepTime = (long) mpp.getNextTriggeringTime(event, 0);
				Thread.sleep(sleepTime);
				time += sleepTime;
			}
			
			updateCountDown.await();
			
			Thread.sleep(3000);
			
			// reset random seed
			mpp.setRandom(random);
			for(int i = 0; i < 10000; i++)
				 mpp.getNextTriggeringTime(event, 0);
			
			CountDownLatch normalCountDown = new CountDownLatch(threadsNum + 100);
			for (int i = 0; i < threadsNum + 100; i++) {
				new CoordinationVisitorThread(node, normalCountDown, i + 1, resTimeRec, "normal").start();
				Thread.sleep((long) mpp.getNextTriggeringTime(event, 0));
			}
			normalCountDown.await();
			
			
			Thread.sleep(1000);
			
			Map<Integer, Long> normalRes = resTimeRec.getNormalRes();
			
			Set<RqstInfo> updateResInfos = resTimeRec.getUpdateResInfos();
			devExp.writeToFile(round, updateResInfos, normalRes, disExp.getTimelinessTime());
			
//			Map<Integer, Double> disruptedTxsResTime = resTimeRec.getDisruptedTxResTime();
//			Iterator<Entry<Integer, Double>> iter = disruptedTxsResTime.entrySet().iterator();
//			int count = 0;
//			String data = null;
//			while(iter.hasNext()){
//				Entry<Integer, Double> entry = iter.next();
//				int curThreadId = entry.getKey();
//				Double resTime = entry.getValue();
//				if(count == 0)
//					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "," + disExp.getTimelinessTime() + "\n";
//				else
//					data = round + "," + curThreadId + "," + normalRes.get(curThreadId) * 1e-6 + "," + resTime + "\n";
//				LOGGER.fine(data);
//				disExp.writeToFile(data);
//				count++;
//			}
			
			// after one round running, reset flag
			stopExp = false;
		}
	}

	private static void printHelp(){
		
		System.out.println("ConUp Shell");
		System.out.println("Commands:");
		System.out.println();
		
		System.out.println("\thelp");
		System.out.println("\tdisruptionAndTimeliness");
		System.out.println("\tcorrectness");
		System.out.println("\toneRoundResponse");
		System.out.println("\ttxGranularity");
		System.out.println("\ttuscany");
		System.out.print("\ndefault>");
		
//    	System.out.println();
//		System.out.println("Timeliness_Exp");
//		System.out.println("\t[usage] timelinessExp");
//		System.out.println("\t[behavior] do timelinessExp by following the configuration file:");
//		System.out.println("Disruption_Exp");
//		System.out.println("\t[usage] disruptionExp");
//		System.out.println("\t[behavior] do Disruption_Exp by following the configuration file:");
//		System.out.println("Overhead_Exp");
//		System.out.println("\t[usage] overheadExp");
//		System.out.println("\t[behavior] do Overhead_Exp by following the configuration file:");
//		System.out.println("Correctness_Exp");
//		System.out.println("\t[usage] correctnessExp");
//		System.out.println("\t[behavior] do Correctness_Exp by following the configuration file:");
		
//		System.out.println("'help' shows supported commands.");
//		System.out.println();
    }

    //added for conup
    public String getCurrentDomain() {
		return currentDomain;
	}

	public Shell(File nodeXML, boolean useJLine) throws ContributionReadException, MalformedURLException, ActivationException, ValidationException {
        this.runtime = TuscanyRuntime.newInstance();
        this.useJline = useJLine;
        
        try {
            initCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Node node = runtime.createNodeFromXML(nodeXML.toURI().toURL().toString());
        currentDomain = node.getDomainName();
        nodes.put(currentDomain, node);
    }
    
    public Shell(String domainURI, boolean useJLine) {
        this.runtime = TuscanyRuntime.newInstance();
        this.useJline = useJLine;
        
        try {
            initCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        if (domainURI != null) {
            domain(domainURI);
        }
    }

    void initCommands() throws IOException {
        for (ServiceDeclaration sd : ServiceDiscovery.getInstance().getServiceDeclarations(Command.class)) {
            try {
                Class<?> c = Class.forName(sd.getClassName());
                try {
                    Command command = (Command)c.getConstructor(Shell.class).newInstance(this);
                    commands.put(command.getName(), command);
                } catch (NoSuchMethodException e) {
                    Command command = (Command)c.newInstance();
                    commands.put(command.getName(), command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean domain(final String domainURI) {
        if (domainURI.length() < 1) {
            currentDomain = "";
        } else {
            for (Node node : nodes.values()) {
                if (domainURI.equals(node.getDomainName())) {
                    currentDomain = node.getDomainName();
                    return true;
                }
            }
            Node node = runtime.createNode(domainURI);
            currentDomain = node.getDomainName();
            nodes.put(currentDomain, node);
        }
        return true;
    }

    boolean domains() {
        for (Node node : nodes.values()) {
            System.out.println(node.getDomainName());
        }
        return true;
    }

    boolean domainComposite() {
        Composite domainComposite = getNode().getDomainComposite();
        System.out.println(Utils.modelToXML(domainComposite, true, runtime.getExtensionPointRegistry()));
        return true;
    }

    boolean install(final List<String> toks) throws ContributionReadException, ActivationException, ValidationException {
        if (getNode() == null) {
            out.println("not in domain, use domain command first");
            return true;
        }
        String metaDataURL = null;
        if (toks.contains("-metadata")) {
            metaDataURL = toks.get(toks.indexOf("-metadata") + 1);
        }
        List<String> duris = null;
        if (toks.contains("-duris")) {
            duris = Arrays.asList(toks.get(toks.indexOf("-duris") + 1).split(","));
        }

        String first = null;
        String second = null;
        for (int i = 1; i < toks.size(); i++) {
            if (toks.get(i).startsWith("-")) {
                if (!toks.get(i).equals("-start")) {
                    i++;
                }
            } else {
                if (first == null) {
                    first = toks.get(i);
                } else {
                    second = toks.get(i);
                    break;
                }
            }
        }

        String curi = null;
        String curl = null;
        if (second != null) {
            curi = first;
            curl = second;
        } else {
            curl = first;
        }
        
        curl = mavenProject(curl);

        String uri = getNode().installContribution(curi, curl, metaDataURL, duris);
        out.println("installed at: " + uri);
        return true;
    }

    /**
     * Try to simplify pointing at a Maven project contribution without needing target/classes suffix
     */
    private String mavenProject(String curl) {
        File f = new File(curl);
        if (!f.exists()) {
            return curl;
        }
        f = new File(f, "target");
        if (!f.exists()) {
            return curl;
        }
        f = new File(f, "classes");
        if (f.exists()) {
            return f.toURI().toString();
        }
        // TODO: look for .zip or .jar in target? 
        return curl;
    }

    boolean installed(final List<String> toks) throws ContributionReadException, ValidationException {
        if (getNode() == null) {
            return true;
        }
        if (toks.size() > 1) {
            String curi = toks.get(1);
            ContributionDescription cd = getNode().getInstalledContribution(toks.get(1));
            if (cd == null) {
                out.println("Contribution " + curi + " not installed");
            } else {
                out.println(curi);
                out.println("   URL: " + cd.getURL());

                List<String> ims = new ArrayList<String>();
                for (String im : cd.getJavaImports()) {
                        ims.add(im);
                }
                for (String im : cd.getNamespaceImports()) {
                    ims.add(im);
                }
                out.println("   Imports: " + ims);

                List<String> es = new ArrayList<String>();
                for (String e : cd.getJavaExports()) {
                    es.add(e);
                }
                for (String e : cd.getNamespaceExports()) {
                    es.add(e);
                }
                out.println("   Exports: " + es);

                List<String> ds = new ArrayList<String>();
                for (String cp : cd.getDeployables()) {
                    ds.add(cp);
                }
                for (String cp : cd.getAdditionalDeployables().keySet()) {
                    ds.add(cp);
                }
                out.println("   Deployables: " + ds);
            }
        } else {
            for (String curi : getNode().getInstalledContributionURIs()) {
                out.println("   " + curi);
            }
        }
        return true;
    }

    boolean listComposites(final String curi) throws ContributionReadException, ValidationException {
        if (getNode() == null) {
            return true;
        }
        Contribution c = getNode().getContribution(curi);
        for (Artifact a : c.getArtifacts()) {
            if (a.getModel() instanceof Composite) {
                out.println(((Composite)a.getModel()).getName());
            }
        }
        return true;
    }

    boolean load(final String configXmlUrl) throws ContributionReadException, ActivationException, ValidationException {
        Node node = runtime.createNodeFromXML(configXmlUrl);
        currentDomain = node.getDomainName();
        nodes.put(currentDomain, node);
        return true;
    }

    boolean run(final String commandsFileURL) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(IOHelper.getLocationAsURL(commandsFileURL).openStream()));
        String l;
        try {
            while ((l = r.readLine()) != null) {
                out.println(l);
                String[] toks = l != null ? l.trim().split(" ") : "".split(" ");
                List<String> toksList = getTokens(toks);
                apply(eval(toksList));
            }
        } finally {
            r.close();
        }
        return true;
    }

    boolean save(final String directory) throws IOException {
        out.println("TODO: not yet implemented");
        return true;
    }

    boolean services() throws IOException {
        if (getNode() == null) {
            return true;
        }
        DomainRegistry reg = ((NodeImpl)getNode()).getEndpointRegistry();
        for (Endpoint endpoint : reg.getEndpoints()) {
            out.println(endpoint.getComponent().getURI() + "/" + endpoint.getService().getName());
            for (Binding b : endpoint.getService().getBindings()) {
                if (!SCABinding.TYPE.equals(b.getType())) {
                    out.println("    " + b.getType().getLocalPart() + " " + b.getURI());
                }
            }
        }
        return true;
    }

    public boolean bye() {
        for (Node node : nodes.values()) {
            node.stop();
        }
        runtime.stop();
        for (Node node : standaloneNodes.values()) {
            node.stop();
        }
        return false;
    }

    boolean started(final List<String> toks) {
        if (standaloneNodes.size() > 0) {
            out.println("Standalone Nodes:");
            for (String nodeName : standaloneNodes.keySet()) {
                Node node = standaloneNodes.get(nodeName);
                Map<String, List<String>> scs = node.getStartedCompositeURIs();
                for (String curi : scs.keySet()) {
                    for (String dc : scs.get(curi)) {
                        out.println("   " + nodeName + " " + dc);
                    }
                }
            }
            out.println();
        }
        if (nodes.size() > 0) {
            for (Node node : nodes.values()) {
                out.println("Domain: " + node.getDomainName());
//                List<String> ics;
//                if (toks.size() > 1) {
//                    ics = new ArrayList<String>();
//                    ics.add(toks.get(1));
//                } else {
//                    ics = node.getInstalledContributionURIs();
//                }
//
//                for (String curi : ics) {
//                    List<String> cs = node.getStartedCompositeURIs().get(curi);
//                    if (cs != null) {
                for (String curi : node.getStartedCompositeURIs().keySet()) {
                    for (String compositeURI : node.getStartedCompositeURIs().get(curi)) {
                        
                        String runningNodeName = node.getRunningNodeName(curi, compositeURI);
                        if (node.getLocalNodeName().equals(runningNodeName)) {
                            runningNodeName = "this";
                        }
                        if ("LocalOnly".equals(runningNodeName)) {
                            runningNodeName = ""; 
                        } else {
                            runningNodeName = " (" + runningNodeName + ")"; 
                        }
                        
                        out.println("   " + curi + " " + compositeURI + runningNodeName);
                    }
                }
//                    }
//                }
            }
        }
        return true;
    }

    boolean nodes() {
        String localNode = getNode().getLocalNodeName();
        for (String node : getNode().getNodeNames()) {
            out.println(node + (localNode.equals(node) ? " (this)" : ""));
        }
        return true;
    }

    boolean status() {
        return true;
    }

    boolean history() {
        for (String l : history)
            out.println(l);
        return true;
    }

    public String[] getCommandNames() {
        List<String> cmds = new ArrayList<String>();
        cmds.addAll(commands.keySet());
        cmds.addAll(Arrays.asList(COMMANDS));
        return cmds.toArray(new String[]{});
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
    
    public Node getNode() {
        return nodes.get(currentDomain);
    }

    List<String> read(Object r) throws IOException {
        out.println();
        out.print(currentDomain + "> ");
        final String l;
        if (useJline) {
            l = JLine.readLine(r);
        } else {
            l = ((BufferedReader)r).readLine();
            history.add(l);
        }
        
        String[] toks = l != null ? l.trim().split(" ") : "bye".split(" ");
        return getTokens(toks);
    }

    /**
     * Parse the string into tokens, which may include quoted strings
     */
    List<String> getTokens(String[] toks) {
        List<String> toksList = new ArrayList<String>();
        for (int i=0; i<toks.length; i++) {
            if (toks[i] != null && toks[i].trim().length() > 0) {
                int j = quotedString(toks, i);
                if (j > -1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(toks[i]);
                    for (int k=i+1; k<=j; k++) {
                        sb.append(" ");
                        sb.append(toks[k]);
                    }
                    i = j;
                    String s = sb.toString();
                    toksList.add(s.substring(1, s.length()-1));
                } else {
                    toksList.add(toks[i]);
                }
            }
        }
        return toksList;
    }
    
    int quotedString(String[] toks, int i) {
        if (toks[i].startsWith("\"") || toks[i].startsWith("'")) {
            for (int j=i+1; j<toks.length; j++) {
                if (toks[j].endsWith(toks[i].substring(0,1))) {
                    return j;
                }
            }
        }
        return -1;
    }

    Callable<Boolean> eval(final List<String> toks) {
        final String op = toks.size() > 0 ? toks.get(0) : "";

        if (commands.keySet().contains(op)) {
            toks.remove(0);
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                   return commands.get(op).invoke(toks.toArray(new String[0]));
                }
            };
        }
        if (op.equalsIgnoreCase("domain"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return domain(toks.size() > 1 ? toks.get(1) : "");
                }
            };
        if (op.equalsIgnoreCase("domains"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return domains();
                }
            };
        if (op.equalsIgnoreCase("domainComposite"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return domainComposite();
                }
            };
        if (op.equalsIgnoreCase("install"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return install(toks);
                }
            };
        if (op.equalsIgnoreCase("installed"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return installed(toks);
                }
            };
        if (op.equalsIgnoreCase("load"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return load(toks.get(1));
                }
            };
        if (op.equalsIgnoreCase("nodes"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return nodes();
                }
            };
        if (op.equalsIgnoreCase("run"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return run(toks.get(1));
                }
            };
        if (op.equalsIgnoreCase("help"))
            return new Callable<Boolean>() {
                public Boolean call() {
                    return help(toks);
                }
            };
        if (op.equalsIgnoreCase("save"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return save(toks.get(1));
                }
            };
        if (op.equalsIgnoreCase("services"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return services();
                }
            };
        if (op.equalsIgnoreCase("bye"))
            return new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return bye();
                }
            };
        if (op.equalsIgnoreCase("started"))
            return new Callable<Boolean>() {
                public Boolean call() {
                    return started(toks);
                }
            };
        if (op.equalsIgnoreCase("status"))
            return new Callable<Boolean>() {
                public Boolean call() {
                     return status();
                }
            };
        if (op.equalsIgnoreCase("history"))
            return new Callable<Boolean>() {
                public Boolean call() {
                    return history();
                }
            };
        if (op.equalsIgnoreCase("") || op.startsWith("#"))
            return new Callable<Boolean>() {
                public Boolean call() {
                    return true;
                }
            };
        return new Callable<Boolean>() {
            public Boolean call() {
                out.println("unknown command");
                return true;
            }
        };
    }

    boolean apply(final Callable<Boolean> func) {
        try {
            return func.call();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void run() throws IOException {
        Object reader;
        if (useJline) {
            reader = JLine.createJLineReader(this);
        } else {
            reader = new BufferedReader(new InputStreamReader(in));
        }
        while (apply(eval(read(reader))))
            ;
    }

    boolean help(List<String> toks) {
        String command = (toks == null || toks.size() < 2) ? null : toks.get(1);
        if (command == null) {
            helpOverview();
        } else if (commands.keySet().contains(command)) {
            out.println(commands.get(command).getShortHelp());
            out.println();
            out.println(commands.get(command).getHelp());
        } else if ("help".equalsIgnoreCase(command)) {
            helpHelp();
        } else if ("install".equalsIgnoreCase(command)) {
            helpInstall();
        } else if ("installed".equalsIgnoreCase(command)) {
            helpInstalled();
        } else if ("load".equalsIgnoreCase(command)) {
            helpLoad();
        } else if ("run".equalsIgnoreCase(command)) {
            helpRun();
        } else if ("save".equalsIgnoreCase(command)) {
            helpSave();
        } else if ("started".equalsIgnoreCase(command)) {
            helpStarted();
        } else if ("startup".equalsIgnoreCase(command)) {
            helpStartUp();
        } else if ("status".equalsIgnoreCase(command)) {
            helpStatus();
        } else if ("services".equalsIgnoreCase(command)) {
            helpServices();
        } else if ("bye".equalsIgnoreCase(command)) {
            helpBye();
        }
        return true;
    }

    boolean helpOverview() {
        out.println("Apache Tuscany Shell (" + Version.getVersion() + ")");
        out.println("Commands:");
        out.println();
        out.println("   help");
        
        for (Command command : commands.values()) {
            out.println("   " + command.getShortHelp());
        }

        out.println("   domain <domainURI>");
        out.println("   domains");
        out.println("   install [<uri>] <contributionURL> [-metadata <url>] [-duris <uri,uri,...>]");
        out.println("   installed [<contributionURI>]");
        out.println("   nodes");
        out.println("   run <commandsFileURL>");
        out.println("   save <directoryPath>");
        out.println("   services");
        out.println("   started");
        out.println("   status");
        out.println("   bye");
        out.println();
        if (useJline)
            out.println("Use Tab key for command and argument completion");
        out.println("For detailed help on each command do 'help <command>', for help of startup options do 'help startup'");
        return true;
    }

    void helpHelp() {
        out.println("   help [<command>]");
        out.println();
        out.println("   Outputs help on the Tuscany Shell");
        out.println("   If the command argument is used it provides detailed help on that command otherwise");
        out.println("   it provides an overview of available Shell commands");
        out.println();
        out.println("   To get help on starting the Tuscany Shell use 'help startup'");
        out.println();
        out.println("   Arguments:");
        out.println("      <command> - (optional) the command to get detailed help on");
    }

    void helpDomain() {
        out.println("   domain [<domainURI>]");
        out.println();
        out.println("   Starts or connects to a domain for the given domain URI.");
        out.println("   If no domain URI is specified switch to standalone mode.");
        out.println();
        out.println("   Arguments:");
        out.println("      <domainURI> - (optional) the domain URI of the domain");
    }

    void helpDomains() {
        out.println("   domains");
        out.println();
        out.println("   Shows the currently defined domain URIs");
        out.println();
        out.println("   Arguments:");
        out.println("      none");
    }

    void helpInstall() {
        out.println("   install [<uri>] <contributionURL> [-start] [-metadata <url>] [-duris <uri,uri,...>]");
        out.println();
        out.println("   Creates an installed contribution with a supplied root contribution, installed at abase URI.");
        out.println();
        out.println("   Arguments:");
        out.println("      uri - (optional) the URI (name) to use for the contribution. When no uri is specified");
        out.println("               a default URI is used derived from the contribution URL");
        out.println("      contributionURL - (required) the URL to the contribution to install");
        out.println("      -start - (optional) start any composites listed as deployable in the sca-contribution.xml file");
        out.println("      -metadata <url> - (optional) the URL to an external contribution meta data document that should be");
        out.println("               merged into any existing sca-contributions.xml file within the contribution.");
        out.println("      -duris <uri,uri,...> - (optional) specifies the URIs of contributions that are used to resolve the");
        out.println("               dependencies of the root contribution and other dependent contributions.");
        out.println("               When not specified all installed contributions are used to resolve dependencies.");
    }

    void helpInstalled() {
        out.println("   installed [<contributionURI>]");
        out.println();
        out.println("   Shows information about the contributions installed on this node,");
        out.println("   including the contribution URI and location along with the URI");
        out.println("   and QName of any composites within the contribution");
        out.println();
        out.println("   Arguments:");
        out.println("      contributionURI - (optional) the URI of an installed contribution");
    }

    void helpLoad() {
        out.println("   load <configXmlUrl>");
        out.println();
        out.println("   Shows information about the contributions installed on this node,");
        out.println("   including the contribution URI and location along with the URI");
        out.println("   and QName of any composites within the contribution");
        out.println();
        out.println("   Arguments:");
        out.println("      configXmlUrl - (required) the URL of the config file to load");
    }

    void helpRemove() {
        out.println("   remove <contributionURI>");
        out.println();
        out.println("   Removes an installed contribution");
        out.println();
        out.println("   Arguments:");
        out.println("      contributionURI - (required) the URI of an installed contribution");
    }

    void helpRun() {
        out.println("   run <commandsFileURL>");
        out.println();
        out.println("   Runs shell commands stored in file.");
        out.println("   The file should be a text file with one shell command per line. Blank lines and ");
        out.println("   lines starting with # will be ignored.");
        out.println();
        out.println("   Arguments:");
        out.println("      commandsFileURL - (required) the URL of the commands file to run");
    }

    void helpSave() {
        out.println("   save <directoryPath>");
        out.println();
        out.println("   Saves the current Node state to directory.");
        out.println("   This will include a node-config.xml file and copies of all artifacts");
        out.println("   being used by the Node.");
        out.println();
        out.println("   Arguments:");
        out.println("      directoryPath - (required) the URL of a directory to be used to store the state.");
    }

    void helpServices() {
        out.println("   services");
        out.println();
        out.println("   Lists the components and services available in the Domain.");
        out.println();
        out.println("   Arguments:");
        out.println("      none");
    }

    void helpStarted() {
        out.println("   started [<curi> [<compositeUri>]]");
        out.println();
        out.println("   Shows the status of the Node, listing for each started composite, its");
        out.println("   contribution URI, the composite URI, and the composite QName.");
        out.println();
        out.println("   Arguments:");
        out.println("      curi - (optional) the URI of an installed contribution");
        out.println("      compositeUri - (optional) the URI of a composite");
    }

    void helpStatus() {
        out.println("   status");
        out.println();
        out.println("   Shows the status of the Shell including information on the known domains,");
        out.println("   installed contributions, and started composites");
        out.println();
        out.println("   Arguments:");
        out.println("      none");
    }

    void helpBye() {
        out.println("   bye");
        out.println();
        out.println("   All deployed composites are stopped and the Shell exists.");
        out.println();
        out.println("   Arguments:");
        out.println("      none");
    }

    void helpStartUp() {
        out.println("   Tuscany Shell StartUp Options ");
        out.println();
        out.println("   When starting the Tuscany Shell there are optional arguments that can configure the Shell.");
        out.println();
        out.println("   Arguments:");
        out.println("      <domainURI> (optional) the URI of the domain.");
        out.println("                  When the domainURI is a simple string then the Shell starts a standalone");
        out.println("                  Node using the string as the domain name or 'default' if no name is specified.");
        out.println("                  When the domainURI starts with 'uri:' the Shell starts a distributed Node ");
        out.println("                  and the URI can encode parameters to configure the domain as follows:");
        out.println("                  uri:<domainName?key1=value1&key2=value2&...");
        out.println("                  The keys are optional and some keys are:");
        out.println("                  bind=ip[:port] - defines the local bind address and port, if the port is not specified it");
        out.println("                      defaults 14820 and if that port in use it will try incrementing by one till a free port is found.");
        out.println("                  multicast=groupip:port | off - defines if multicast discovery is used and if so what multicast IP group and port is used.");
        out.println("                      It defaults to 224.5.12.10:51482. A value of 'off' means multicast is disabled.");
        out.println("                  wka=ip[:port] - a comma separated list of ip address and port for remote nodes in");
        out.println("                                  the domain group when multicast is not available. The port defaults to 14820.");
        out.println("                  userid= is the userid other nodes must use to connect to this domain group. The default is the default domain name.");
        out.println("                  password= is the password other nodes must use to connect to this domain group. The default is 'tuscany'.");
        out.println();
        out.println("      -nojline    (optional) use plain Java System.in/out instead of JLine");
        out.println("                             (no tab completion or advanced line editing will be available)");
        out.println("      -noPreprocessor(optional) don't execute conup-preprocess");
    }
}
