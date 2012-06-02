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
import de.xabsl.jxabslx.io.Output;
import de.xabsl.jxabslx.symbols.DecimalOutputSymbolImpl;

public class DecimalOutputSymbolImplTest {

	// ---- TEST DUMMIES ----

	public double testVar = 5d;

	// ---- END TEST DUMMIES ----

	DecimalOutputSymbolImplTest t;

	@Before
	public void setUp() {
		t = new DecimalOutputSymbolImplTest();
	}

	@Test
	public void testDecimalOutputSymbolImpl() {

		Output output = new Output() {

			public void setValue(Object value) {
				t.testVar = (Double) value;
			}

		};

		Input input = new Input() {

			public Object getValue() {
				return t.testVar;
			}

			public Class<?>[] getParamTypes() {
				return new Class[0];
			}

			public Object[] getParameters() {
				return new Object[0];
			}

		};

		DecimalOutputSymbol out = new DecimalOutputSymbolImpl(output, input,
				Conversions.getDecimalConversion(Double.class),
				CrashTestDummies.getDebugMessages());

		out.setValue(52d);
		assertEquals(t.testVar, 52d, 0.1d);
	}

}
