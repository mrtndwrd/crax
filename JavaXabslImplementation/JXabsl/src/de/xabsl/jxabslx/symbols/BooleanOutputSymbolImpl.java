/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabslx.symbols;

import de.xabsl.jxabsl.EngineInitializationException;
import de.xabsl.jxabsl.symbols.BooleanOutputSymbol;
import de.xabsl.jxabsl.utils.DebugMessages;
import de.xabsl.jxabslx.conversions.BooleanConversion;
import de.xabsl.jxabslx.io.Input;
import de.xabsl.jxabslx.io.Output;

/**
 * An implementation for a boolean output symbol. Takes its values from an Input
 * object, writes values via an Output object and converts via a
 * BooleanConversion.
 */

public class BooleanOutputSymbolImpl implements BooleanOutputSymbol {

	private Output output;
	private Input input;

	private BooleanConversion conversion;

	/**
	 * Constructor
	 * 
	 * @param output
	 *            a value goes here
	 * @param input
	 *            a value comes from here
	 * @param conversion
	 *            is converted via this conversion
	 * @param debug
	 *            for debugging output
	 */
	public BooleanOutputSymbolImpl(Output output, Input input,
			BooleanConversion conversion, DebugMessages debug) {

		this.output = output;
		this.input = input;
		if (input.getParameters().length != 0) {
			throw new EngineInitializationException(
					"Number of parameters must be 0: An output symbol does not have parameters");
		}

		this.conversion = conversion;

	}

	public boolean getValue() {
		return conversion.from(input.getValue());
	}

	public void setValue(boolean value) {
		output.setValue(conversion.to(value));
	}

}
