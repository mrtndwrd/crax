/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class ConditionalDecimalExpressionTest {

	// dummy
	private List<Action> actions = new ArrayList<Action>();

	// dummy
	private OptionParameters parameters = CrashTestDummies
			.dummyOptionParameters();

	// dummy
	private Symbols symbols = new Symbols(CrashTestDummies.getDebugMessages());

	@Test
	public void testConditionalDecimalExpression()
			throws IntermediateCodeMalformedException {

		ConditionalDecimalExpression conditional;

		conditional = new ConditionalDecimalExpression(new ScannerInputSource(
				"v true v 5 v 3"), actions,
				CrashTestDummies.getDebugMessages(), parameters, symbols,
				CrashTestDummies.systemTimeFunction(), CrashTestDummies
						.systemTimeFunction());

		assertEquals(conditional.getValue(), 5, .1);

		conditional = new ConditionalDecimalExpression(new ScannerInputSource(
				"v false v 5 v 3"), actions, CrashTestDummies
				.getDebugMessages(), parameters, symbols, CrashTestDummies
				.systemTimeFunction(), CrashTestDummies.systemTimeFunction());

		assertEquals(conditional.getValue(), 3, .1);
	}

}