/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.state.State;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class IfElseBlockTest {

	@Test
	public void testIfElseBlock() throws IntermediateCodeMalformedException {

		List<Action> actions = new ArrayList<Action>();
		Map<String, State> states = new HashMap<String, State>();

		TimeFunction tf = new TimeFunction() {
			public long getTime() {
				return 0;
			};
		};

		State state1 = new State("State 1",
				CrashTestDummies.getDebugMessages(), tf);
		State state2 = new State("State 2 ", CrashTestDummies
				.getDebugMessages(), tf);
		State state3 = new State("State 3",
				CrashTestDummies.getDebugMessages(), tf);

		states.put("state1", state1);
		states.put("state2", state2);
		states.put("state3", state3);

		OptionParameters parameters = CrashTestDummies.dummyOptionParameters();
		Symbols symbols = new Symbols(CrashTestDummies.getDebugMessages());
		TimeFunction someTF = CrashTestDummies.systemTimeFunction();

		
		IfElseBlock ieb = new IfElseBlock(new ScannerInputSource(
				"v false t state1 i  v false t state2  t state3"), actions,
				CrashTestDummies.getDebugMessages(), states, parameters,
				symbols, someTF, someTF);

		Assert.assertEquals(ieb.getNextState(), state3);

		IfElseBlock ieb2 = new IfElseBlock(new ScannerInputSource(
				"v false t state1 i  v true t state2  t state3"), actions,
				CrashTestDummies.getDebugMessages(), states, parameters,
				symbols, someTF, someTF);

		Assert.assertEquals(ieb2.getNextState(), state2);

	}
}
