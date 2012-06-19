/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.parameters.DecimalParameter;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabsl.symbols.DecimalInputSymbol;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.symbols.JavaEnumeration;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class DecimalInputSymbolRefTest {

	// test dummy enum
	enum Department {
		Literature, Biology, ComputerScience
	}

	// ---- TEST DUMMY MEMBERS ----

	public double height = 4.277d;

	public int suitedAsDecimalToo = 5;

	public static double somethingStatic = 4.444f;

	public Department dep = Department.ComputerScience;

	// Test dummy: double
	public double sqared(double n) {
		return n * n;
	}

	// Test dummy: boolean
	public boolean xor(boolean a, boolean b) {
		return a ^ b;
	}

	// Static test dummy
	public static double threeTimes(double n) {
		return 3 * n;
	}

	// Test dummy with enumerated in parameter list
	public boolean isComputerScience(Department d) {

		if (d == Department.ComputerScience)
			return true;
		else
			return false;
	}

	// Test dummy with a parameter list where order is important
	public double toThePowerOf(double a, double b) {

		return Math.pow(a, b);
	}

	Enumeration department = new JavaEnumeration("Department",
			Department.class, CrashTestDummies.getDebugMessages());

	// InternalEnumeration department;

	// ---- END TEST DUMMY MEMBERS ----

	// Testing instance
	DecimalInputSymbolRefTest t;

	Symbols symbols;
	
	TimeFunction someTF = CrashTestDummies.systemTimeFunction();


	@Before
	public void setup() {
		t = new DecimalInputSymbolRefTest();

		symbols = new Symbols(CrashTestDummies.getDebugMessages());

	}

	@Test
	public void testFromDoubleField() throws NoSuchFieldException,
			IntermediateCodeMalformedException {

		DecimalInputSymbol inputS = new DecimalInputSymbol() {

			public Parameters getParameters() {
				return new Parameters(CrashTestDummies.getDebugMessages());
			}

			public double getValue() {
				return height;
			}

		};

		symbols.registerDecimalInputSymbol("aRef", inputS);

		InputSource input = new ScannerInputSource("aRef 0");
		OptionParameters optionParameters = new OptionParameters(
				new ScannerInputSource("0"), CrashTestDummies
						.getDebugMessages(), symbols) {

		};

		List<Action> actions = new ArrayList<Action>();

		DecimalInputSymbolRef aRef = new DecimalInputSymbolRef(input,
				optionParameters, symbols, CrashTestDummies.getDebugMessages(),
				someTF, someTF, actions);

		assertEquals((Double) aRef.getValue(), 4.277, 0.1);

	}

	@Test
	public void testFromDoubleFunctionDoubleParameters()
			throws NoSuchFieldException, IntermediateCodeMalformedException {

		LinkedHashMap<String, Class> parameters = new LinkedHashMap<String, Class>();
		parameters.put("base", Double.TYPE);
		parameters.put("exponent", Double.TYPE);

		// Test a function
		DecimalInputSymbol inputS = new DecimalInputSymbol() {

			Object[] paramsArray = new Object[2];
			Parameters params;

			{

				params = new Parameters(CrashTestDummies.getDebugMessages());
				params.registerDecimal("base", new DecimalParameter() {

					public void set(double value) {
						paramsArray[0] = value;
					}

				});

				params.registerDecimal("exponent", new DecimalParameter() {

					public void set(double value) {
						paramsArray[1] = value;

					}

				});
			}

			public Parameters getParameters() {
				return params;
			}

			public double getValue() {

				return toThePowerOf((Double) paramsArray[0],
						(Double) paramsArray[1]);
			}

		};

		symbols.registerDecimalInputSymbol("powerFunction", inputS);

		InputSource input = new ScannerInputSource(
				"powerFunction 2 d exponent v 2 d base v 3");

		OptionParameters optionParameters = CrashTestDummies
				.dummyOptionParameters();

		List<Action> actions = new ArrayList<Action>();

		DecimalInputSymbolRef powerRef = new DecimalInputSymbolRef(input,
				optionParameters, symbols, CrashTestDummies.getDebugMessages(),
				someTF, someTF, actions);

		assertEquals(powerRef.getValue(), 9, 0.1);

		// Test the same function, but with switched parameters

		InputSource input2 = new ScannerInputSource(
				"powerFunction 2 d base v 2 d exponent v 3");

		DecimalInputSymbolRef powerRef2 = new DecimalInputSymbolRef(input2,
				optionParameters, symbols, CrashTestDummies.getDebugMessages(),
				someTF, someTF, actions);

		assertEquals(powerRef2.getValue(), 8, 0.1);

	}
	// @Test
	// public void testFromBooleanFunction() throws NoSuchFieldException {
	//
	// // Test a function
	//
	// LinkedHashMap<String, Class> parameters = new LinkedHashMap<String,
	// Class>();
	// parameters.put("aParameterName", Boolean.TYPE);
	// parameters.put("anotherName", Boolean.TYPE);
	//
	// InputSymbol<Boolean> inputS = new InputSymbol<Boolean>("xor()",
	// parameters, t, CrashTestDummies.getDebugMessages());
	// assertEquals(inputS.getValue(new Boolean[] { true, false }), true);
	// assertEquals(inputS.getValue(new Boolean[] { false, false }), false);
	// assertEquals(inputS.getValue(new Boolean[] { true, true }), false);
	//
	// }
	//
	// @Test
	// public void testFromStaticDoubleFunction() throws NoSuchFieldException {
	// // Test a static function
	//
	// LinkedHashMap<String, Class> parameters = new LinkedHashMap<String,
	// Class>();
	// parameters.put("aParameterName", Double.TYPE);
	// InputSymbol<Double> inputS = new InputSymbol<Double>("threeTimes()",
	// parameters, this.getClass(), CrashTestDummies.getDebugMessages());
	//
	// assertEquals(inputS.getValue(new Double[] { 9d }), 27, 0.1);
	//
	// }

}
