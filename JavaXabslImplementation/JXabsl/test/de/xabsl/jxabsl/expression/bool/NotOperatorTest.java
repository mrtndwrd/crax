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

public class NotOperatorTest {

	@Test
	public void testGetValue() {
		// Test whether you can pass null for the debug stream
		assertTrue(new NotOperator(new BooleanValue(false, CrashTestDummies
				.getDebugMessages()), null).getValue());

		assertFalse(new NotOperator(new NotOperator(new BooleanValue(false,
				CrashTestDummies.getDebugMessages()), CrashTestDummies
				.getDebugMessages()), CrashTestDummies.getDebugMessages())
				.getValue());

	}

}
