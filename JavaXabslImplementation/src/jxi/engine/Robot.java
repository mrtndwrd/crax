package jxi.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.lang.String;

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

    /** Variables that can be accesed by the engine: */
    /** some ammount_turned */
	private float ammount_turned;
    /** some stayed */
	private float stayed;
    /** The XabslEngine on which this robot bases its behavior */
	protected Engine engine;
    /** A queue for all the messages that are to be parsed: */
    ArrayBlockingQueue<String> receiveQueue;
    /** ConnectionHandler for the connection with UsarCommander */
    ConnectionHandler usarConnection;
    
    /** Variables that are accesed by behaviors: */
    /** Speed for DifferentialDrive */
    double differentialDriveSpeed = 0;
    /** Turning speed for DifferentialDrive */
    double differentialDriveTurningSpeed = 0;
    /** Time for 'wait' */
    double waitTime = 0;

    /** Internal world variables 
     *  Maybe later I should put them in a seperate object 
     */
    double currentAngle;

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
        // TODO: This sockets is now hardcoded, that's not desirable!
        ServerSocket ss = null;
        Socket socket = null;
        try
        {
            ss = new ServerSocket(7001);
            System.out.println("[ROBOT] Waiting for connection to UsarCommander");
            socket = ss.accept();
            System.out.printf("[ROBOT] Accepted connection from ip %s:%d\n", socket.getInetAddress().toString(), socket.getPort());
        }
        catch(Exception e)
        {
            System.out.println("[ROBOT] Couldn't make socket");
            e.printStackTrace();
        }
        // Instantiate connection
        try
        {
            usarConnection = new ConnectionHandler(this, true, socket);
        }
        catch(IOException e)
        {
            System.out.println("[ROBOT] Couldn't make any connection with UsarCommander");
            e.printStackTrace();
        }
        usarConnection.start();
        System.out.println("[ROBOT] Started listening to UsarCommander, Sending first message");
        usarConnection.sendMessage("Robot connection accepted");
        System.out.println("[ROBOT] Waiting for first reaction");
        try{
            Thread.sleep(10000);
            //System.out.println(receiveQueue.take());
           }
        catch (Exception e){}
        System.out.println("[ROBOT] done sleeping");
        //usarConnection.sendMessage("DRIVE:10");

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

        // Register ammount_turned as a decimal input symbol
		Class<? extends Robot> thisClass = this.getClass();
		Method method = thisClass.getMethod("get_ammount_turned");
		engine.registerDecimalInputSymbol("ammount_turned",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
       
		method = thisClass.getMethod("get_stayed");
		engine.registerDecimalInputSymbol("stayed",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
        //// TODO: Make degrees the right kind of registered symbol
        //double degrees = 10;
		engine.registerBasicBehavior(new DifferentialDrive("differential_drive", myDebug, usarConnection, differentialDriveSpeed, differentialDriveTurningSpeed));
		engine.registerBasicBehavior(new Wait("wait", myDebug, usarConnection, waitTime));

		try
        {
            File ic = new File(
                "intermediateCode/intermediate-code.dat");
            engine.createOptionGraph(new ScannerInputSource(ic));
        }
        catch(Exception e)
        {
            System.out.println("[ROBOT] Cought exception in accessing intermediate code");
            e.printStackTrace();
        }
        while(usarConnection.isConnAlive())
        {
            // Remove all the sensor data from ancient history, since
            // we are not going to use that for reasoning. The first data to
            // come in will be used by parseMessage.
            receiveQueue.clear();
            try
            {
                System.out.println("[ROBOT] Executing engine");
                engine.execute();
                System.out.println("asking for groundTruth");
                parseMessage(receiveQueue.take());
                System.out.printf("ammount turned is now %f\n", ammount_turned);
                //ammount_turned += 10;
            }
            catch (Exception e)
            {
                System.out.println("Something went wrong while executing the engine");
                e.printStackTrace();
            }
        }
        if(usarConnection.isConnAlive())
            System.out.println("Connection is still alive");
        System.out.println("[ROBOT] done");
    }

    public BasicBehavior createTestBehavior(PrintStreamDebug myDebug)
    {
        return new TestBehavior("test", myDebug, usarConnection, ammount_turned, stayed);
    }
    
    public float get_ammount_turned()
    {
        return this.ammount_turned;
    }
    public float get_stayed()
    {
        return this.stayed;
    }

    public ArrayBlockingQueue<String> getReceiveQueue()
    {
        return receiveQueue;
    }

    public void parseMessage(String message)
    {
        System.out.printf("Parsing message %s\n", message);
        String messageArray[] = message.split(":");
        if(messageArray[0].equals("GROUNDTRUTH"))
        {
            double angle = Double.parseDouble(messageArray[1]);
            if(currentAngle != 0)
            {
                double angleDiff = Math.abs(currentAngle - angle);
                // Probably reached 360)
                if (angleDiff > 300)
                {
                    angleDiff -= 360;
                }
                ammount_turned += angleDiff;
                System.out.println("changing ammount_turned to " + ammount_turned);
            }
            currentAngle = angle;
        }
    }
}


