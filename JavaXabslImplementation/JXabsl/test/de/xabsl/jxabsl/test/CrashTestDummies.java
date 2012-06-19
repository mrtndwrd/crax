/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.test;

import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.behavior.OptionParameters;
import de.xabsl.jxabsl.engine.Symbols;
import de.xabsl.jxabsl.utils.DebugMessages;
import de.xabsl.jxabslx.utils.PrintStreamDebug;
import de.xabsl.jxabslx.utils.ScannerInputSource;

/**
 * 
 * Constructors often demand all kinds of objects, get them here for testing.
 */
public class CrashTestDummies {

	public static OptionParameters dummyOptionParameters() {
		Symbols symbols = new Symbols(getDebugMessages());
		return new OptionParameters(new ScannerInputSource("0"),
				getDebugMessages(), symbols);
	}

	public static TimeFunction systemTimeFunction() {
		return new TimeFunction() {
			public long getTime() {
				return System.currentTimeMillis();
			}
		};

	}

	public static Symbols emptySymbols = new Symbols(getDebugMessages());

	public static DebugMessages debug;

	public static DebugMessages getDebugMessages() {

		if (debug == null)
			debug = new PrintStreamDebug(System.out, System.err);

		return debug;

	}
}
