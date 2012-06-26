/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class PlusOperatorTest {

	@Test
	public void test() {
		DecimalExpression d1 = new DecimalValue(5, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d2 = new DecimalValue(-7, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d3 = new DecimalValue(4, CrashTestDummies
				.getDebugMessages());

		PlusOperator d4 = new PlusOperator(CrashTestDummies.getDebugMessages());
		d4.create(d1, d2);

		// Test whether you can pass null to the debug
		PlusOperator dsum = new PlusOperator(CrashTestDummies
				.getDebugMessages());
		dsum.create(d4, d3);

		assertEquals(dsum.getValue(), 2, 0.1);

	}

}
