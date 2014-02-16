package cn.edu.nju.moon.conup.spi.datamodel;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TransactionContextTest {

	@Test
	public void testGetInvokeSequence() {
		TransactionContext tc = new TransactionContext();
		String invocationSequence = "A:a358ab2d-cf1a-4e7e-857a-d89c2db40102>E:d130a1f8-657c-4791-8fae-f1cda8d2dd53";
		tc.setInvocationSequence(invocationSequence);
		assertEquals("A:a358ab2d-cf1a-4e7e-857a-d89c2db40102>E:d130a1f8-657c-4791-8fae-f1cda8d2dd53", tc.getInvocationSequence());
	}

	@Test
	public void testGetProxyRootTxId() {
		testcase1();

		testcase2();

		testcase3();
	}

	private void testcase3() {
		Scope scope = new Scope();
		Set<String> parentComps = new HashSet<String>();
		Set<String> subComps = new HashSet<String>();
		Set<String> targetComps = new HashSet<String>();

		parentComps.clear();
		subComps.clear();
		targetComps.clear();
		scope = new Scope();

		// D component
		parentComps.add("C");
		parentComps.add("E");
		scope.addComponent("D", parentComps, subComps);

		// C component
		parentComps.clear();
		subComps.clear();
		parentComps.add("B");
		subComps.add("D");
		scope.addComponent("C", parentComps, subComps);

		// E component
		parentComps.clear();
		subComps.clear();
		subComps.add("D");
		scope.addComponent("E", parentComps, subComps);

		// B component
		parentComps.clear();
		subComps.clear();
		subComps.add("C");
		scope.addComponent("B", parentComps, subComps);

		scope.setSpecifiedScope(true);

		TransactionContext tc = new TransactionContext();
		tc.setCurrentTx("657c-4791-4791-8fae-f1cda8d2dd53");
		tc.setHostComponent("D");
		tc.setParentComponent("E");
		tc.setParentTx("d130a1f8-657c-4791-8fae-f1cda8d2dd53");
		tc.setRootComponent("A");
		tc.setRootTx("a358ab2d-cf1a-4e7e-857a-d89c2db40102");
		String invocationSequence = "A:a358ab2d-cf1a-4e7e-857a-d89c2db40102>E:d130a1f8-657c-4791-8fae-f1cda8d2dd53";
		tc.setInvocationSequence(invocationSequence);
		assertEquals("d130a1f8-657c-4791-8fae-f1cda8d2dd53", tc.getProxyRootTxId(scope));
	}

	private void testcase1() {
		String toString = "ProcComponent<PortalComponent#"
				+ "AuthComponent<ProcComponent#"
				+ "AuthComponent<PortalComponent#"
				+ "ProcComponent>AuthComponent#"
				+ "PortalComponent>ProcComponent#"
				+ "PortalComponent>AuthComponent#"
				+ "TARGET_COMP@AuthComponent#" + "SCOPE_FLAG&false";
		Scope scope = Scope.inverse(toString);
		TransactionContext tc = new TransactionContext();
		tc.setCurrentTx("657c-4791-4791-8fae-f1cda8d2dd53");
		tc.setHostComponent("AuthComponent");
		tc.setParentComponent("ProcComponent");
		tc.setParentTx("d130a1f8-657c-4791-8fae-f1cda8d2dd53");
		tc.setRootComponent("PortalComponent");
		tc.setRootTx("a358ab2d-cf1a-4e7e-857a-d89c2db40102");

		assertFalse(scope.isSpecifiedScope());
		assertEquals("a358ab2d-cf1a-4e7e-857a-d89c2db40102",
				tc.getProxyRootTxId(scope));
	}

	private void testcase2() {
		Scope scope = new Scope();
		Set<String> parentComps = new HashSet<String>();
		Set<String> subComps = new HashSet<String>();
		Set<String> targetComps = new HashSet<String>();

		// TEST CASE 2
		parentComps.clear();
		subComps.clear();
		targetComps.clear();
		scope = new Scope();

		// D component
		parentComps.add("C");
		parentComps.add("E");
		scope.addComponent("D", parentComps, subComps);
		targetComps.add("D");
		scope.setTarget(targetComps);

		// C component
		parentComps.clear();
		subComps.clear();
		parentComps.add("B");
		subComps.add("D");
		scope.addComponent("C", parentComps, subComps);

		// E component
		parentComps.clear();
		subComps.clear();
		parentComps.add("B");
		subComps.add("D");
		scope.addComponent("E", parentComps, subComps);

		// B component
		parentComps.clear();
		subComps.clear();
		subComps.add("C");
		subComps.add("E");
		scope.addComponent("B", parentComps, subComps);

		scope.setSpecifiedScope(true);

		TransactionContext tc = new TransactionContext();
		tc.setCurrentTx("657c-4791-4791-8fae-f1cda8d2dd53");
		tc.setHostComponent("D");
		tc.setParentComponent("E");
		tc.setParentTx("d130a1f8-657c-4791-8fae-f1cda8d2dd53");
		tc.setRootComponent("A");
		tc.setRootTx("a358ab2d-cf1a-4e7e-857a-d89c2db40102");
		String invocationSequence = "A:a358ab2d-cf1a-4e7e-857a-d89c2db40102>B:xxxx-xxxx-xxxx-xxx>E:d130a1f8-657c-4791-8fae-f1cda8d2dd53";
		tc.setInvocationSequence(invocationSequence);
		assertEquals("xxxx-xxxx-xxxx-xxx", tc.getProxyRootTxId(scope));
	}

}
