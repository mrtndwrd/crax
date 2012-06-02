package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;

/**
 * Very simple behavior 
 */
public class TestBehavior extends BasicBehavior
{
    Parameters myParameters;
    MyDecimalParameter x;
    MyDecimalParameter y;

    public TestBehavior(String name, PrintStreamDebug myDebug, double xIn, double yIn)
    {
        super(name, myDebug);
        myParameters = new Parameters(myDebug);
        this.x = new MyDecimalParameter();
        this.x.set(xIn);
        this.y = new MyDecimalParameter();
        this.y.set(yIn);
        myParameters.registerDecimal("test.x", this.x);
        myParameters.registerDecimal("test.y", this.y);

    }
        
        @Override
        public Parameters getParameters() 
        {
            return myParameters;
        }

        @Override
        public void execute() 
        {
            System.out.println("executing test");
        }
    };
