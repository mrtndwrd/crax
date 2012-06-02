/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabslx.engine;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.symbols.DecimalInputSymbol;
import de.xabsl.jxabsl.test.CrashTestDummies;

public class ReflectionEngineTest {

	// returns 2000
	public double return2000() {
		return 2000d;
	}

	// ---

	@Test
	public void testDecimalInputSymbolFromMethod()
			throws IntermediateCodeMalformedException {
		ReflectionEngine engine = new ReflectionEngine(CrashTestDummies
				.getDebugMessages(), new TimeFunction() {

			// (Java 6) @Override
			public long getTime() {
				return 0;
			}

		}, new ReflectionEngineTest());

		java.util.Set<String> emptySet = new HashSet<String>();
		DecimalInputSymbol inputSymbol = engine.getDecimalInputSymbol(
				"return2000", emptySet, emptySet, emptySet);

		assertEquals(2000d, inputSymbol.getValue(), 0.1);
	}

}
