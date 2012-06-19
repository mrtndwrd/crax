/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.debugtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;
import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.action.ActionBehavior;
import de.xabsl.jxabsl.agent.Agent;
import de.xabsl.jxabsl.behavior.Behavior;
import de.xabsl.jxabsl.behavior.Option;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.state.State;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.engine.DebugPrinter;
import de.xabsl.jxabslx.engine.ReflectionEngine;
import de.xabsl.jxabslx.utils.ScannerInputSource;

/**
 * A test for the complete engine (not really a unit test). Tests if all debug
 * information can be accessed properly
 * 
 */
public class DebugTest {

	public static void behavior1() {
	}

	public static void behavior2() {
	}

	public static void behavior3() {
	}

	@Test
	// (timeout = 2000)
	public void runTest() throws FileNotFoundException, NoSuchFieldException,
			IntermediateCodeMalformedException, SecurityException,
			NoSuchMethodException {

		Engine engine = new ReflectionEngine(CrashTestDummies
				.getDebugMessages(), CrashTestDummies.systemTimeFunction(),
				DebugTest.class);

		File ic = new File(
				"test/de/xabsl/jxabsl/engine/debugtest/intermediatecode");

		engine.createOptionGraph(new ScannerInputSource(ic));

		System.out.println("*** DEBUGGING TEST ***");

		assertEquals(1, engine.getAgents().keySet().size());
		assertTrue(engine.getAgents().keySet().contains("robot"));

		DebugPrinter.printDebug(System.out, engine);

		engine.execute();
		// engine.execute();

		DebugPrinter.printDebug(System.out, engine);

		for (Option o : engine.getOptions().values()) {
			if (o.getName().equals("option1")) {
				for (Action a : o.getActiveState().getActions()) {
					if (a instanceof ActionBehavior
							&& ((ActionBehavior) a).getBehavior().getName()
									.equals("option3")) {
	.					assertEquals(((ActionBehavior) a)
								.getParameterAssignment()
								.getEnumeratedDebugValues()[0], "value4");
					}
				}
			}
		}

	}
}
