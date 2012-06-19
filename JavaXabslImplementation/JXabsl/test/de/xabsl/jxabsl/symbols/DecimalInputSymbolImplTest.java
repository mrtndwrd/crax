/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.symbols;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.Input;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;

public class DecimalInputSymbolImplTest {

	// ---- TEST DUMMY MEMBERS ----

	public double height = 4.277d;

	// ---- END TEST DUMMY MEMBERS ----

	// Testing instance
	DecimalInputSymbolImplTest t;

	@Before
	public void setup() {
		t = new DecimalInputSymbolImplTest();
	}

	@Test
	public void testFromDoubleField() throws NoSuchFieldException {

		Input input = new Input() {

			public Object getValue() {
				return t.height;
			}

			public Class<?>[] getParamTypes() {
				return new Class[0];
			}

			public Object[] getParameters() {
				return new Object[0];
			}

		};

		DecimalInputSymbolImpl inputS = new DecimalInputSymbolImpl(input,
				Conversions.getDecimalConversion(Double.class), new String[0],
				CrashTestDummies.emptySymbols, CrashTestDummies
						.getDebugMessages());

		assertEquals((Double) inputS.getValue(), 4.277, 0.1);

	}

}