/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.enumtest;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.utils.ScannerInputSource;

/**
 * A minimal test for the engine
 */
public class EnumTest {

	public enum Planet {
		mars, venus, pluto
	}

	public double i = 0;

	@Test
	public void enumTest() throws IntermediateCodeMalformedException,
			FileNotFoundException, NoSuchFieldException {

		EnumTest t = new EnumTest();

		TimeFunction tf = CrashTestDummies.systemTimeFunction();

		Engine engine = new Engine(CrashTestDummies.getDebugMessages(), tf);

		InputSource input = new ScannerInputSource(new File(
				"test/de/xabsl/jxabsl/engine/enumtest/intermediatecode"));

		engine.createOptionGraph(input);

		engine.execute();

		// good if we made it this far.
		org.junit.Assert.assertTrue(true);

	}
}
