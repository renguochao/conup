package cn.edu.nju.moon.conup.core.utils;

import cn.edu.nju.moon.conup.spi.utils.OperationType;
import cn.edu.nju.moon.conup.spi.utils.PayloadType;

/**
 * @author rgc
 * @version Dec 18, 2012 4:24:19 PM
 */
public class QuiescencePayloadCreator {

	/**
	 * 
	 * @param srcComp
	 * @param targetComp
	 * @param operation
	 * @return
	 */
	public static String createPayload(String srcComp, String targetComp, OperationType operation){
		String result = null;
		
		result = PayloadType.SRC_COMPONENT + ":" + srcComp + "," +
				PayloadType.TARGET_COMPONENT + ":" + targetComp + "," +
				PayloadType.OPERATION_TYPE + ":" + operation + "," ;
		
		return result;
	}
	
	/**
	 * payload contains parent_tx and sub_tx
	 * these msgs should be in payload when operationType is ACK_SUBTX_INIT
	 * @param srcComp
	 * @param targetComp
	 * @param rootTx
	 * @param operation
	 * @param parentTxID
	 * @param subTxID
	 * @return
	 */
	public static String createPayload(String srcComp, String targetComp, String rootTx,
			OperationType operation, String parentTxID, String subTxID){
		String result = null;
		
		result = PayloadType.SRC_COMPONENT + ":" + srcComp + "," +
				PayloadType.TARGET_COMPONENT + ":" + targetComp + "," +
				PayloadType.ROOT_TX + ":" + rootTx + "," +
				PayloadType.OPERATION_TYPE + ":" + operation + "," +
				PayloadType.PARENT_TX + ":" + parentTxID + "," +
				PayloadType.SUB_TX + ":" + subTxID;
		
		return result;
	}

	public static String createRootTxEndPayload(String srcComp,
			String targetComp, String rootTx,
			OperationType operation) {
		
		String result = null;
		
		result = PayloadType.SRC_COMPONENT + ":" + srcComp + "," +
				PayloadType.TARGET_COMPONENT + ":" + targetComp + "," +
				PayloadType.ROOT_TX + ":" + rootTx + "," +
				PayloadType.OPERATION_TYPE + ":" + operation;
		
		return result;
	}
	
}
