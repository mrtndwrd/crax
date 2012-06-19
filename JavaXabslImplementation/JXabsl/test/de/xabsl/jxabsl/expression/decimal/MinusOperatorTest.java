/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class MinusOperatorTest {

	@Test
	public void test() {
		DecimalExpression d1 = new DecimalValue(5, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d2 = new DecimalValue(-7, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d3 = new DecimalValue(4, CrashTestDummies
				.getDebugMessages());

		MinusOperator d4 = new MinusOperator(CrashTestDummies
				.getDebugMessages());
		d4.create(d1, d2);

		// Test whether you can pass null to the debug

		MinusOperator ddif = new MinusOperator(CrashTestDummies
				.getDebugMessages());
		ddif.create(d4, d3);

		assertEquals(ddif.getValue(), 8, 0.1);

	}

}
