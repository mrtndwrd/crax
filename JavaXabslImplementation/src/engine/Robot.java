package jxi.engine;

/** Class that instantiates everything needed for a robo
 * Of course, for now it is only a test class...
 * Based on the Robot.java test class in engine/deliveryTest/
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.parameters.EnumeratedParameter;
import de.xabsl.jxabsl.parameters.DecimalParameter;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabsl.symbols.BooleanInputSymbol;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.InputFromField;
import de.xabsl.jxabslx.io.InputFromMethod;
import de.xabsl.jxabslx.io.OutputToField;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;
import de.xabsl.jxabslx.symbols.DecimalOutputSymbolImpl;
import de.xabsl.jxabslx.symbols.EnumeratedInputSymbolImpl;
import de.xabsl.jxabslx.symbols.JavaEnumeration;
import de.xabsl.jxabslx.utils.ScannerInputSource;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import java.util.concurrent.ArrayBlockingQueue;

import jxi.util.MyTimeFunction;
import jxi.behaviors.TestBehavior;

public class Robot {
    /* TODO Probably not needed:
	public enum Direction {
		north, east, south, west
	};

	private Vector2 packet;
	private Direction direction = Direction.north;
	protected World world;
    */
    
    /** Capacity for the receiveQueue */
    public static final int RQCAPACITY = 10;
	private float x;
	private float y;
	protected Engine engine;
    /** A queue for all the messages that are to be parsed: */
    ArrayBlockingQueue<String> receiveQueue;
    

	public Robot() throws FileNotFoundException,
			NoSuchFieldException, IntermediateCodeMalformedException,
			SecurityException, NoSuchMethodException 
    {
        receiveQueue = new ArrayBlockingQueue<String>(RQCAPACITY);

        // I won't be needing a world representation hiero...
		//this.world = world;

        // My own 'debug stream', maybe later this should go to a file or
        // something...
        PrintStreamDebug myDebug = new PrintStreamDebug(System.out, System.out);
        MyTimeFunction myTimeFunction = new MyTimeFunction();
		engine = new Engine(myDebug,
				myTimeFunction);

		// register symbols

		//final Enumeration enumDirection = new JavaEnumeration("Direction",
		//		Direction.class, myDebug);
		//engine.registerEnumeration(enumDirection);

		Field field;

        // Register x as a decimal input symbol
		Class thisClass = this.getClass();
		Method method = thisClass.getMethod("currentX");
		engine.registerDecimalInputSymbol("x",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
       
		method = thisClass.getMethod("currentY");
		engine.registerDecimalInputSymbol("y",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
		engine.registerBasicBehavior(createTestBehavior(myDebug));

		try
        {
            File ic = new File(
                "intermediateCode/intermediate-code.dat");
            engine.createOptionGraph(new ScannerInputSource(ic));
        }
        catch(Exception e)
        {
            System.out.println("Cought exception in accessing intermediate code");
            e.printStackTrace();
        }
        System.out.println("Na maken engine\n");
    }

    public BasicBehavior createTestBehavior(PrintStreamDebug myDebug)
    {
        return new TestBehavior("test", myDebug, x, y);
    }
    
    public float currentX()
    {
        return this.x;
    }
    public float currentY()
    {
        return this.y;
    }

    public ArrayBlockingQueue<String> getReceiveQueue()
    {
        return receiveQueue;
    }
}


