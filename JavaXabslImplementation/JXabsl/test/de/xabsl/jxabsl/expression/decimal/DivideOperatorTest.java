/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class DivideOperatorTest {

	@Test
	public void test() {
		DecimalExpression d1 = new DecimalValue(5, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d2 = new DecimalValue(-7, CrashTestDummies
				.getDebugMessages());

		DecimalExpression d3 = new DecimalValue(4, CrashTestDummies
				.getDebugMessages());

		// Test whether you can pass null to the debug
		DivideOperator d4 = new DivideOperator(CrashTestDummies
				.getDebugMessages());
		d4.create(d1, d2);

		DivideOperator dquot = new DivideOperator(CrashTestDummies
				.getDebugMessages());
		dquot.create(d4, d3);

		assertEquals(dquot.getValue(), ((5.0 / (-7.0)) / 4.0), 0.1);

	}

}
