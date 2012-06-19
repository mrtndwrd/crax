/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.bool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class BooleanExpressionTest {

	// Dummies
	private List<Action> actions = new ArrayList<Action>();
	private Symbols symbols = new Symbols(CrashTestDummies.getDebugMessages());
	private OptionParameters op = CrashTestDummies.dummyOptionParameters();
	TimeFunction someTF = CrashTestDummies.systemTimeFunction();

	@Before
	public void setUp() {

	}

	@Test
	public void testCreate() throws IntermediateCodeMalformedException {
		BooleanExpression be;


		// Test AND operator

		be = BooleanExpression.create(new ScannerInputSource(
				"& 3 v true v false v true"), actions, CrashTestDummies
				.getDebugMessages(), op, symbols, someTF, someTF);
		assertFalse(be.getValue());

		be = BooleanExpression.create(new ScannerInputSource(
				"& 3 v true v true v true"), actions, CrashTestDummies
				.getDebugMessages(), op, symbols, someTF, someTF);
		assertTrue(be.getValue());

		// Test OR operator

		be = BooleanExpression.create(new ScannerInputSource(
				"| 3 v true v false v true"), actions, CrashTestDummies
				.getDebugMessages(), op, symbols, someTF, someTF);
		assertTrue(be.getValue());

		be = BooleanExpression.create(new ScannerInputSource(
				"& 3 v false v false v false"), actions, CrashTestDummies
				.getDebugMessages(), op, symbols, someTF, someTF);
		assertFalse(be.getValue());

		// Test NOT operator

		be = BooleanExpression.create(new ScannerInputSource("! v false"),
				actions, CrashTestDummies.getDebugMessages(), op, symbols,
				someTF, someTF);
		assertTrue(be.getValue());

	}

	// ---- Test Exceptions ----

	@Test(expected = IntermediateCodeMalformedException.class)
	public void testIntermediateCodeMalformedException1()
			throws IntermediateCodeMalformedException {
		BooleanExpression.create(new ScannerInputSource("dsfsdf"), actions,
				CrashTestDummies.getDebugMessages(), op, symbols, someTF,
				someTF);
	}

	@Test(expected = IntermediateCodeMalformedException.class)
	public void testIntermediateCodeMalformedException2()
			throws IntermediateCodeMalformedException {
		BooleanExpression.create(new ScannerInputSource("& v true v true"),
				actions, CrashTestDummies.getDebugMessages(), op, symbols,
				someTF, someTF);
	}

	@Test(expected = IntermediateCodeMalformedException.class)
	public void testIntermediateCodeMalformedException3()

	throws IntermediateCodeMalformedException {
		BooleanExpression.create(new ScannerInputSource("& dsf"), actions,
				CrashTestDummies.getDebugMessages(), op, symbols, someTF, someTF);
	}

}
