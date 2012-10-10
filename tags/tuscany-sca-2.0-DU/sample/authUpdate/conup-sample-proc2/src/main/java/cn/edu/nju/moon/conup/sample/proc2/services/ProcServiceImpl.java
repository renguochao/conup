package cn.edu.nju.moon.conup.sample.proc2.services;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

import cn.edu.nju.moon.conup.algorithm.VcAlgorithmImpl;
import cn.edu.nju.moon.conup.container.VcContainerImpl;
import cn.edu.nju.moon.conup.data.InArcRegistryImpl;
import cn.edu.nju.moon.conup.data.OutArcRegistryImpl;
import cn.edu.nju.moon.conup.data.TransactionRegistryImpl;
import cn.edu.nju.moon.conup.listener.ComponentListener;
import cn.edu.nju.moon.conup.listener.ComponentListenerImpl;
import cn.edu.nju.moon.conup.printer.container.ContainerPrinter;

@Service(ProcService.class)
public class ProcServiceImpl implements ProcService {
	private VerificationService verify;
	private DBService db;

	public VerificationService getVerify() {
		return verify;
	}
	@Reference
	public void setVerify(VerificationService verify) {
		this.verify = verify;
	}

	public DBService getDb() {
		return db;
	}

	@Reference
	public void setDb(DBService db) {
		this.db = db;
	}

	public List<String> process(String token, String data) {
//		ComponentListener listener = ComponentListenerImpl.getInstance();
//		Set<String> futureC = new HashSet<String>();
//		futureC.add("DBComponent");
//		futureC.add("AuthComponent");
//		Set<String> pastC = new HashSet<String>();
//		String threadID = new Integer(Thread.currentThread().hashCode()).toString();
		
//		listener.notify("start", threadID, futureC, pastC);
//		
//		listener.notify("running", threadID, futureC, pastC);
		Boolean authResult = verify.verify(token);
		
//		futureC.remove("AuthComponent");
//		pastC.add("AuthComponent");
		
//		listener.notify("running", threadID, futureC, pastC);
		if (authResult) {
			List<String> result = db.dbOperation();
//			futureC.remove("DBComponent");
//			pastC.add("DBComponent");
//			listener.notify("end", threadID, futureC, pastC);
			return result;
		} else{
//			futureC.remove("DBComponent");
//			pastC.add("DBComponent");
//			listener.notify("end", threadID, futureC, pastC);
			return null;
		}
	}
	
}