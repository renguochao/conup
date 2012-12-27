package cn.edu.nju.moon.conup.core.utils;

/**
 * @author rgc
 * @version Nov 24, 2012 10:32:37 AM
 */
public enum ConsistencyOperationType {
	//Tx event type
	/** 
	 * if a root tx ends while the component status is normal, root component should recursively 
	 * notify its sub components to make sure each component's txRegistry is correct.
	 */
	NORMAL_ROOT_TX_END,
	//dep event type
	NOTIFY_FUTURE_CREATE,
	ACK_FUTURE_CREATE,
	NOTIFY_FUTURE_REMOVE,
	ACK_SUBTX_INIT,
	NOTIFY_SUBTX_END,
	NOTIFY_PAST_CREATE,
	NOTIFY_PAST_REMOVE,
	//ondemand event type
	REQ_ONDEMAND_SETUP,
	CONFIRM_ONDEMAND_SETUP,
	NOTIFY_FUTURE_ONDEMAND,
	NOTIFY_PAST_ONDEMAND,
	NOTIFY_SUB_FUTURE_ONDEMAND,
	NOTIFY_SUB_PAST_ONDEMAND,
	ACK_FUTURE_ONDEMAND,
	ACK_PAST_ONDEMAND,
	ACK_SUB_FUTURE_ONDEMAND,
	ACK_SUB_PAST_ONDEMAND,
}
