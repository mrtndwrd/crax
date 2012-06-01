/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabslx.io;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.engine.ReflectionEngine;

public class TestMethodBasicBehavior {

	public enum Bit {
		zero, one
	}

	private Engine engine;

	private static Bit bit;

	private static MethodBasicBehavior behavior;

	public static void xor(Bit a, Bit b) {
		boolean result = (a == Bit.zero ? false : true)
				^ (b == Bit.zero ? false : true);
		bit = result == false ? Bit.zero : Bit.one;
	}

	@Before
	public void setup() throws SecurityException, NoSuchMethodException {

		engine = new ReflectionEngine(CrashTestDummies.getDebugMessages(),
				CrashTestDummies.systemTimeFunction(), this);

		behavior = new MethodBasicBehavior("xor", this.getClass().getMethod(
				"xor", new Class<?>[] { Bit.class, Bit.class }), new String[] {
				"a", "b" }, this, engine, CrashTestDummies.getDebugMessages());

	}

	@Test
	public void testExecute() {
		int posA = behavior.getParameters().getEnumeratedPosition("a");
		int posB = behavior.getParameters().getEnumeratedPosition("b");

		behavior.getParameters().setEnumerated(posA, Bit.zero);
		behavior.getParameters().setEnumerated(posB, Bit.one);

		assertEquals(engine.getEnumeration("Bit"), behavior.getParameters()
				.getEnumeration("a"));

		assertEquals(engine.getEnumeration("Bit"), behavior.getParameters()
				.getEnumeration("b"));

		behavior.execute();

		assertEquals(Bit.one, bit);
	}
}
