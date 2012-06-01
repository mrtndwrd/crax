/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.bool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.xabsl.jxabsl.expression.decimal.DecimalExpression;
import de.xabsl.jxabsl.expression.decimal.DecimalValue;
import de.xabsl.jxabsl.test.CrashTestDummies;

public class GreaterThanOperatorTest {

	@Test
	public void testGetValue() {

		DecimalExpression de1 = new DecimalValue(4.2, CrashTestDummies
				.getDebugMessages());

		DecimalExpression de2 = new DecimalValue(Double.NEGATIVE_INFINITY,
				CrashTestDummies.getDebugMessages());

		// Test whether you can pass null for the debug stream
		GreaterThanOperator operator = new GreaterThanOperator(CrashTestDummies
				.getDebugMessages());

		operator.create(de1, de2);

		assertTrue(operator.getValue());

		operator.create(de2, de1);

		assertFalse(operator.getValue());

	}
}
