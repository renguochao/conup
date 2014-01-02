package cn.edu.nju.moon.conup.spi.tx;

import java.util.Map;

import cn.edu.nju.moon.conup.spi.datamodel.InvocationContext;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionContext;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionRegistry;

public interface TxLifecycleManager {

	/**
	 * create transaction id
	 * @return
	 */
	public String createID();

	/**
	 * create a temporary transaction id
	 * @return
	 */
	public String createFakeTxId();

	/**
	 * @param id the transaction id that needs to be destroyed
	 */
	public void destroyID(String txId);

	/**
	 * @return total transactions that are running
	 */
//	public int getTxs();
	public Map<String, TransactionContext> getTxs();
	
	/**
	 * the host component started a sub-transaction on a remote component
	 * @param subComp
	 * @param curComp
	 * @param rootTx
	 * @param parentTx
	 * @param subTx
	 * @return
	 */
//	public boolean startRemoteSubTx(String subComp, String curComp, String rootTx,
//			String parentTx, String subTx);
	
	/**
	 * a sub-transaction just ended and returned from a remote component 
	 * @param subComp
	 * @param curComp
	 * @param rootTx
	 * @param parentTx
	 * @param subTx
	 * @return
	 */
//	public boolean endRemoteSubTx(String subComp, String curComp, String rootTx,
//			String parentTx, String subTx);
	
	/**
	 * the host component is going to init a sub-transaction for another component.
	 * However, the sub-transaction has not truely been started.
	 * 
	 * @param hostComp
	 * @param fakeSubTx the fake tx id
	 * @param rootTx
	 * @param rootComp
	 * @param parentTx
	 * @param parentComp
	 * @return
	 */
//	public boolean initLocalSubTx(String hostComp, String fakeSubTx, String rootTx, String rootComp, String parentTx, String parentComp);
	
	/**
	 * the host component is going to init a sub-transaction for another component.
	 * However, the sub-transaction has not truely been started.
	 * 
	 * @param hostComp
	 * @param fakeSubTx
	 * @param txCtxInCache
	 * @return
	 */
	public boolean initLocalSubTx(String hostComp, String fakeSubTx, TransactionContext txCtxInCache);
	
	/**
	 * 
	 * @param hostComp
	 * @param fakeSubTx
	 * @return
	 */
	public boolean endLocalSubTx(String hostComp, String fakeSubTx);
	
	/**
	 * when a root tx ends, TxDepMonitor should be notified.
	 * Given a parentTxId, which means that only the root Txs associated with the parentTxId
	 * @param hostComp
	 * @param rootTxId
	 * @return
	 */
	public void rootTxEnd(String hostComp, String rootTxId);
	
	/**
	 * because every component type 1 to 1 correspondence to a TxLifecycleManager
	 * @return component identifier
	 */
	public String getCompIdentifier();

	/**
	 * get current component's txRegistry
	 * @return
	 */
	public TransactionRegistry getTxRegistry();

	void resolveInvocationContext(InvocationContext invocationContext,
			String hostComponent);

	public InvocationContext createInvocationCtx(String hostComponent, String serviceName,
			TxDepMonitor txDepMonitor);

	/**
	 * a sub-transaction just ended and returned from a remote component
	 * @param invocationCtx
	 * @return
	 */
	public boolean endRemoteSubTx(InvocationContext invocationCtx);

	public void updateTxContext(String currentTxID, TransactionContext txContext);

	public TransactionContext getTransactionContext(String curTxID);

	public void removeTransactionContext(String curTxID);

}