/** A stupid implementation of the DecimalParameter class, because 
 * for some reason nobody cared to implement it themselves...
 */

import de.xabsl.jxabsl.parameters.DecimalParameter;

public class MyDecimalParameter implements DecimalParameter
{
    public double parameter;

    public MyDecimalParameter()
    {
        this.parameter = 0;
    }

    @Override
    public void set(double value)
    {
        this.parameter = value;
    }

    public double get(Object value)
    {
        return parameter;
    }
}
