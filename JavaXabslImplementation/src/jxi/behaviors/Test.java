
package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;
import jxi.behaviors.StandardBehavior;
import jxi.connection.ConnectionHandler;

/**
 * Behavior that will turn the robot.
 */
public class Test extends StandardBehavior
{
    MyDecimalParameter degrees;

    public Test(String name, PrintStreamDebug myDebug, 
        ConnectionHandler usarConnection, double degreesIn)
    {
        super(name, myDebug, usarConnection);
        this.degrees = new MyDecimalParameter();
        this.degrees.set(degreesIn);
        myParameters.registerDecimal("turn.degrees", this.degrees);
    }
        
    @Override
    public void execute() 
    {
        System.out.println("executing test");
    }
};
