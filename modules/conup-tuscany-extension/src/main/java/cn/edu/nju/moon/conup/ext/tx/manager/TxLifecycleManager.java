/**
 * 
 */
package cn.edu.nju.moon.conup.ext.tx.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * It's supposed to manage the transactions that are running on a tuscany node.
 * @author Jiang Wang
 *
 */
public class TxLifecycleManager {
	public static Map<String, String> TX_IDS = new ConcurrentHashMap<String, String>();
	/**
	 * create transaction id
	 * @param componentName
	 * @return
	 */
	public String createID(String componentName){
		String txID = null;
		
		return txID;
	}
	
	/**
	 * @param id the transaction id that needs to be destroyed
	 */
	public void destroyID(String id){
		
	}
	
	/**
	 * 
	 * @return total transactions that are running
	 */
	public int getTxs(){

		return 0;
	}
}