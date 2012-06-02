/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.enumerated;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class ConditionalEnumeratedExpressionTest {

	private Enumeration animals;

	// dummy
	private List<Action> actions = new ArrayList<Action>();

	// dummy
	private OptionParameters parameters = CrashTestDummies
			.dummyOptionParameters();

	// dummy
	private Symbols symbols = new Symbols(CrashTestDummies.getDebugMessages());

	@Before
	public void setUp() {
		animals = new Enumeration("animals", CrashTestDummies
				.getDebugMessages());
		animals.add("cat");
		animals.add("dog");
		animals.add("squirrel");

		symbols.registerEnumeration(animals);

	}

	@Test
	public void testConditionalEnumeratedExpression()
			throws IntermediateCodeMalformedException {

		TimeFunction someTF = CrashTestDummies.systemTimeFunction();

		// Test creation
		EnumeratedExpression cond = new ConditionalEnumeratedExpression(
				animals, new ScannerInputSource(
						" v true v animals.dog v animals.cat  "), actions,
				CrashTestDummies.getDebugMessages(), parameters, symbols,
				someTF, someTF);

		assertEquals("dog", cond.getValue());

		cond = new ConditionalEnumeratedExpression(
				animals,
				new ScannerInputSource(" v false v animals.dog v animals.cat  "),
				actions, CrashTestDummies.getDebugMessages(), parameters,
				symbols, someTF, someTF);

		assertEquals("cat", cond.getValue());

	}

}
