/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.enumerated;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.engine.SymbolNotRegisteredException;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class EnumeratedValueTest {

	private Enumeration animals;

	private Symbols symbols;

	@Before
	public void setUp() {
		animals = new Enumeration("animals", CrashTestDummies
				.getDebugMessages());
		animals.add("cat");
		animals.add("dog");
		animals.add("squirrel");

		symbols = new Symbols(CrashTestDummies.getDebugMessages());

		symbols.registerEnumeration(animals);

	}

	@Test
	public void testEnumeratedValue() throws IntermediateCodeMalformedException {

		// Test creation with a string argument
		EnumeratedValue enval = new EnumeratedValue(animals, "dog",
				CrashTestDummies.getDebugMessages());

		assertEquals("dog", enval.getValue());

		// Test creation with a scanner input
		enval = new EnumeratedValue(animals, new ScannerInputSource(
				"  animals." + "squirrel  "), symbols, CrashTestDummies
				.getDebugMessages());

	}

	// ---Test exceptions that must occur---

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorString() {
		// Element is not in Enumeration exception

		new EnumeratedValue(animals, "animals.duck", CrashTestDummies
				.getDebugMessages());
	}

	@Test(expected = SymbolNotRegisteredException.class)
	public void testConstructorInputNotInEnum()
			throws IntermediateCodeMalformedException, IllegalArgumentException {

		// Element is not in Enumeration
		new EnumeratedValue(animals, new ScannerInputSource("  animals.duck "),
				symbols, CrashTestDummies.getDebugMessages());
	}

}
