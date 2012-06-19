package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;
import jxi.behaviors.StandardBehavior;
import jxi.connection.ConnectionHandler;

/**
 * Very simple behavior 
 */
public class TestBehavior extends StandardBehavior
{
    Parameters myParameters;
    MyDecimalParameter x;
    MyDecimalParameter y;

    public TestBehavior(String name, PrintStreamDebug myDebug, ConnectionHandler usarConnection, double xIn, double yIn)
    {
        super(name, myDebug, usarConnection);
        this.x = new MyDecimalParameter();
        this.x.set(xIn);
        this.y = new MyDecimalParameter();
        this.y.set(yIn);
        myParameters.registerDecimal("test.x", this.x);
        myParameters.registerDecimal("test.y", this.y);
    }
        
        @Override
        public void execute() 
        {
            System.out.println("executing test");
        }
    };
