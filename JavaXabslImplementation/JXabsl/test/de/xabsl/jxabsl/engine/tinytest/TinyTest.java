/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.tinytest;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.InputFromField;
import de.xabsl.jxabslx.io.Output;
import de.xabsl.jxabslx.io.OutputToField;
import de.xabsl.jxabslx.symbols.DecimalOutputSymbolImpl;
import de.xabsl.jxabslx.utils.ScannerInputSource;

/**
 * A minimal test for the engine
 */
public class TinyTest {

	public double i = 0;

	@Test
	public void testEngine() throws IntermediateCodeMalformedException,
			FileNotFoundException, NoSuchFieldException {

		TinyTest t = new TinyTest();

		TimeFunction tf = CrashTestDummies.systemTimeFunction();

		Engine engine = new Engine(CrashTestDummies.getDebugMessages(), tf);

		InputSource input = new ScannerInputSource(new File(
				"test/de/xabsl/jxabsl/engine/tinytest/intermediatecode"));

		Field field = this.getClass().getField("i");
		Output output = new OutputToField(field, t);
		de.xabsl.jxabslx.io.Input dInput = new InputFromField(field, t);
		DecimalOutputSymbolImpl os = new DecimalOutputSymbolImpl(output,
				dInput, Conversions.getDecimalConversion(field.getType()),
				CrashTestDummies.getDebugMessages());

		engine.registerDecimalOutputSymbol("i", os);
		engine.createOptionGraph(input);

		engine.execute();

		org.junit.Assert.assertEquals(t.i, 3);

	}
}
