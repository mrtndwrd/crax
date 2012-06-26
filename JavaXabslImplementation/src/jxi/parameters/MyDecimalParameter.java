package jxi.parameters;

import de.xabsl.jxabsl.parameters.DecimalParameter;

/** An implementation of the DecimalParameter class, because 
 *  the implementation is not shipped with the JXabsl Engine package.
 */
public class MyDecimalParameter implements DecimalParameter
{
    public double parameter;

    public MyDecimalParameter()
    {
        this.parameter = 0;
    }

    /** 
     * Sets the value of the parameter 
     */
    @Override
    public void set(double value)
    {
        this.parameter = value;
    }
    
    /** Returns the value of the parameter */
    public double get()
    {
        return parameter;
    }
}
