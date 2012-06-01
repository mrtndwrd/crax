/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.action;

import org.junit.Test;

import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.expression.decimal.DecimalExpression;
import de.xabsl.jxabsl.expression.decimal.DecimalValue;
import de.xabsl.jxabsl.symbols.DecimalOutputSymbol;
import de.xabsl.jxabsl.test.CrashTestDummies;

public class DecimalOutputSymbolActionTest {

	public double testDouble = 0;

	@Test
	public void testDecimalOutputSymbolAction() throws NoSuchFieldException {

		DecimalExpression testExp = new DecimalValue(5.2d, CrashTestDummies
				.getDebugMessages());

		DecimalOutputSymbol testOutputSymbol = new DecimalOutputSymbol() {

			public double getValue() {
				return testDouble;
			}

			public void reset() {
			}

			public void setValue(double value) {
				testDouble = value;
			}

		};

		ActionDecimalOutputSymbol testOutputSymbolAction = new ActionDecimalOutputSymbol(
				new TimeFunction() {
					public long getTime() {
						return 0;
					}
				}, testOutputSymbol, testExp);

		testOutputSymbolAction.execute();

		org.junit.Assert.assertEquals(5.2, testDouble, 0.1);

	}
}
