package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;
import jxi.behaviors.StandardBehavior;
import jxi.connection.ConnectionHandler;

/**
 * Halts the system for <i>time</i> seconds
 * @param time Number of seconds to halt
 */
public class Wait extends StandardBehavior
{
    MyDecimalParameter time;

    public Wait(String name, PrintStreamDebug myDebug, 
        ConnectionHandler usarConnection, double timeIn)
    {
        super(name, myDebug, usarConnection);
        this.time = new MyDecimalParameter();
        this.time.set(timeIn);
        myParameters.registerDecimal("wait.time", this.time);
    }
        
    @Override
    public void execute() 
    {
        System.out.println("[WAIT] executing, will sleep " + time.get() + " seconds");
        try
        {
            Thread.sleep(1000 * (int) time.get());
        }
        catch(InterruptedException e)
        {
            System.out.println("[WAIT] Interrupted while sleeping!");
        }
    }
};
