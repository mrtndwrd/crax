package jxi.engine;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.io.IOException;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.engine.Engine;

import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.InputFromMethod;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;
import de.xabsl.jxabslx.utils.ScannerInputSource;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import java.util.concurrent.ArrayBlockingQueue;

import jxi.util.MyTimeFunction;
// Import ALL behaviors
import jxi.behaviors.*;
import jxi.connection.ConnectionHandler;

/** Class that instantiates everything needed for a robo
 * Of course, for now it is only a test class...
 * Based on the Robot.java test class in engine/deliveryTest/
 */
public class Robot {
    /** Capacity for the receiveQueue */
    public static final int RQCAPACITY = 10;
    /** some x */
	private float x;
    /** some y */
	private float y;
    /** The XabslEngine on which this robot bases its behavior */
	protected Engine engine;
    /** A queue for all the messages that are to be parsed: */
    ArrayBlockingQueue<String> receiveQueue;
    /** ConnectionHandler for the connection with UsarCommander */
    ConnectionHandler usarConnection;
    
    /** 
     * Constructor, socket connection is now hardcoded to 127.0.0.1:5005
     */
	public Robot() throws FileNotFoundException,
			NoSuchFieldException, IntermediateCodeMalformedException,
			SecurityException, NoSuchMethodException 
    {
        // Instantiate receiveQueue
        receiveQueue = new ArrayBlockingQueue<String>(RQCAPACITY);
        // The socket needed for the connection with UsarCommander:
        // TODO: This socket is now hardcoded, that's not desirable!
        Socket socket = null;
        try
        {
            socket = new Socket("127.0.0.1", 5000);
        }
        catch(Exception e)
        {
            System.out.println("Couldn't make socket");
            e.printStackTrace();
        }
        // Instantiate connection
        try
        {
            usarConnection = new ConnectionHandler(this, true, socket);
        }
        catch(IOException e)
        {
            System.out.println("Couldn't make any connection with UsarCommander");
            e.printStackTrace();
        }


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
		// TODO: Probably not needed.
		//Field field;

        // Register x as a decimal input symbol
		Class<? extends Robot> thisClass = this.getClass();
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
        // TODO: Make degrees the right kind of registered symbol
        double degrees = 10;
		engine.registerBasicBehavior(new Turn("turn", myDebug, usarConnection, degrees));

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
        usarConnection.sendMessage("Done initializing");

        System.out.println("Na maken engine\n");
    }

    public BasicBehavior createTestBehavior(PrintStreamDebug myDebug)
    {
        return new TestBehavior("test", myDebug, usarConnection, x, y);
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


