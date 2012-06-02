/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class MultiplyOperatorTest {

	@Test
	public void test() {
		DecimalExpression d1 = new DecimalValue(5, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d2 = new DecimalValue(-7, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d3 = new DecimalValue(4, CrashTestDummies
				.getDebugMessages());

		// Test whether you can pass null to the debug

		MultiplyOperator d4 = new MultiplyOperator(CrashTestDummies
				.getDebugMessages());
		d4.create(d1, d2);

		MultiplyOperator dprod = new MultiplyOperator(CrashTestDummies
				.getDebugMessages());
		dprod.create(d4, d3);

		assertEquals(dprod.getValue(), -140, 0.1);

	}

}
