/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.statement;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.state.State;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class TransitionToStateTest {

	@Test
	public void testTransitionToState()
			throws IntermediateCodeMalformedException {

		TimeFunction tf = new TimeFunction() {
			public long getTime() {
				return 0;
			};
		};

		Map<String, State> states = new HashMap<String, State>();
		State s1 = new State("Drop", CrashTestDummies.getDebugMessages(), tf);
		State s2 = new State("Pick Up", CrashTestDummies.getDebugMessages(), tf);
		State s3 = new State("move", CrashTestDummies.getDebugMessages(), tf);

		states.put("drop", s1);
		states.put("pick_up", s2);
		states.put("move", s1);

		TransitionToState tts = new TransitionToState(new ScannerInputSource(
				"  pick_up "), CrashTestDummies.getDebugMessages(), states);

		Assert.assertEquals(tts.getNextState(), s2);

	}
}
