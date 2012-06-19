/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.bool;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class BooleanValueTest {

	@Test(expected = IntermediateCodeMalformedException.class)
	public void testGetValue() throws IntermediateCodeMalformedException {
		BooleanValue bvf = new BooleanValue(false, CrashTestDummies
				.getDebugMessages());
		BooleanValue bvt = new BooleanValue(true, CrashTestDummies
				.getDebugMessages());

		assertEquals(bvf.getValue(), false);
		assertEquals(bvt.getValue(), true);

		InputSource scan = new ScannerInputSource("   false 	  true  	  gaga  ");

		bvf = new BooleanValue(scan, CrashTestDummies.getDebugMessages());

		bvt = new BooleanValue(scan, CrashTestDummies.getDebugMessages());

		assertEquals(bvf.getValue(), false);
		assertEquals(bvt.getValue(), true);

		// we shall produce an exception now

		bvf = new BooleanValue(scan, CrashTestDummies.getDebugMessages());

	}

	@Test(expected = IntermediateCodeMalformedException.class)
	public void testUnexpectedEndOfFile()
			throws IntermediateCodeMalformedException {

		// There is no token, should throw an exception

		InputSource scan = new ScannerInputSource("	");

		BooleanExpression bvf = new BooleanValue(scan, CrashTestDummies
				.getDebugMessages());

		bvf.getValue();

	}
}
