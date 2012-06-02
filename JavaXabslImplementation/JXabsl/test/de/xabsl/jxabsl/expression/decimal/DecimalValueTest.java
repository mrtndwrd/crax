/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class DecimalValueTest {

	@Test
	public void test() {
		DecimalValue decval = new DecimalValue(5.0, CrashTestDummies
				.getDebugMessages());
		assertEquals(5, decval.getValue(), .1);

		InputSource input = new ScannerInputSource("   6    ");

		try {
			decval = new DecimalValue(input, CrashTestDummies
					.getDebugMessages());
		} catch (IntermediateCodeMalformedException ex) {
			fail("An exception has occured! " + ex);

		}
		assertEquals(6, decval.getValue(), .1);

	}

}