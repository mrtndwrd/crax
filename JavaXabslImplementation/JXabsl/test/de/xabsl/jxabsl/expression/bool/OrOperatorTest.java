/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.bool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class OrOperatorTest {

	@Test
	public void testGetValue() {
		BooleanValue bvf = new BooleanValue(false, CrashTestDummies
				.getDebugMessages());
		BooleanValue bvt = new BooleanValue(true, CrashTestDummies
				.getDebugMessages());

		// Test whether you can pass null for the debug stream
		OrOperator result = new OrOperator(CrashTestDummies.getDebugMessages());
		result.addOperand(bvf);

		assertFalse(result.getValue());

		result.addOperand(bvt);

		assertTrue(result.getValue());

		result = new OrOperator(CrashTestDummies.getDebugMessages());
		result.addOperand(bvt);

		assertTrue(result.getValue());

		result.addOperand(bvt);

		assertTrue(result.getValue());

		result.addOperand(bvf);

		assertTrue(result.getValue());

	}

}
