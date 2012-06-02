/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabslx.engine.deliverytestreflection;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.engine.deliverytest.Robot;
import de.xabsl.jxabsl.engine.deliverytest.World;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.engine.ReflectionEngine;
import de.xabsl.jxabslx.io.InputFromMethod;
import de.xabsl.jxabslx.symbols.EnumeratedInputSymbolImpl;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class ReflectionRobot extends Robot {

	public ReflectionRobot(World world) throws FileNotFoundException,
			IntermediateCodeMalformedException, SecurityException,
			NoSuchFieldException, NoSuchMethodException {

		this.world = world;

		engine = new ReflectionEngine(CrashTestDummies.getDebugMessages(),
				CrashTestDummies.systemTimeFunction(), this);

		// This has to be registered because parameter order cannot be resolved
		Method method = this.getClass().getMethod("directionToPoint",
				Integer.TYPE, Integer.TYPE);
		engine.registerEnumeratedInputSymbol("directionToPoint",
				new EnumeratedInputSymbolImpl(engine
						.getEnumeration("Direction"), new InputFromMethod(
						method, this), Conversions
						.getEnumeratedConversion(method.getReturnType()),
						new String[] { "directionToPoint.x",
								"directionToPoint.y" }, engine,
						CrashTestDummies.getDebugMessages()));

		InputSource input = new ScannerInputSource(
				new File(
						"test/de/xabsl/jxabslx/engine/deliverytestreflection/intermediatecode"));

		engine.createOptionGraph(input);

	}

}
