/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.parameters;

/**
 * An interface that represents a boolean parameter for an input symbol of a
 * basic behavior
 */
public interface BooleanParameter {

	/**
	 * Set the parameter before execution of the behavior or input symbol
	 * 
	 * @param value
	 *            The value of the parameter in the next call
	 */
	public void set(boolean value);

}
