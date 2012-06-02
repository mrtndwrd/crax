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

public class AndOperatorTest {

	@Test
	public void testGetValue() {
		BooleanValue bvf = new BooleanValue(false, CrashTestDummies
				.getDebugMessages());
		BooleanValue bvt = new BooleanValue(true, CrashTestDummies
				.getDebugMessages());

		AndOperator result = new AndOperator(CrashTestDummies
				.getDebugMessages());
		result.addOperand(bvf);

		assertFalse(result.getValue());

		result.addOperand(bvt);

		assertFalse(result.getValue());

		// Test whether you can pass null to the debug

		result = new AndOperator(CrashTestDummies.getDebugMessages());
		result.addOperand(bvt);

		assertTrue(result.getValue());

		result.addOperand(bvt);

		assertTrue(result.getValue());

		result.addOperand(bvf);

		assertFalse(result.getValue());

	}

}
