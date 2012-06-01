/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;

public class ModOperatorTest {

	@Test
	public void test() {
		DecimalExpression d1 = new DecimalValue(121.2, CrashTestDummies
				.getDebugMessages());
		DecimalExpression d2 = new DecimalValue(1.5, CrashTestDummies
				.getDebugMessages());

		ModOperator d4 = new ModOperator(CrashTestDummies.getDebugMessages());
		d4.create(d1, d2);

		assertEquals(d4.getValue(), 1.2, 0.1);

	}

}
