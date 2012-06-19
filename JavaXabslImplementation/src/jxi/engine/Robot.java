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
import de.xabsl.jxabslx.symbols.JavaEnumeration;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;
import de.xabsl.jxabslx.symbols.EnumeratedInputSymbolImpl;
import de.xabsl.jxabslx.utils.ScannerInputSource;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import java.util.concurrent.ArrayBlockingQueue;

import jxi.util.MyTimeFunction;
// Import ALL behaviors
import jxi.behaviors.*;
import jxi.connection.ConnectionHandler;
import jxi.world.World;

/** Class that instantiates everything needed for a robo
 * Of course, for now it is only a test class...
 * Based on the Robot.java test class in engine/deliveryTest/
 */
public class Robot {
    /** Capacity for the receiveQueue */
    public static final int RQCAPACITY = 10;
    
    /** Decides once in how many parsed messages the engine executes */
    public static final int runEngineAfter = 5;

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

    /** Internal world representation */
    World world;
    
    /**
     * Constructor, socket connection is now hardcoded to 127.0.0.1:7001
     */
	public Robot() throws FileNotFoundException,
			NoSuchFieldException, IntermediateCodeMalformedException,
			SecurityException, NoSuchMethodException 
    {
        this(7001);
    }
    
    /** 
     * Constructor, creates a serversocket on this machine.
     * @param port: The port to connect with UsarCommander
     */
	public Robot(int port) throws FileNotFoundException,
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
            ss = new ServerSocket(port);
            System.out.println("[ROBOT] Waiting for connection to UsarCommander");
            socket = ss.accept();
            System.out.printf("[ROBOT] Accepted connection from ip %s:%d\n", socket.getInetAddress().toString(), socket.getPort());
        }
        catch(Exception e)
        {
            System.out.println("[ROBOT] Couldn't make socket");
            e.printStackTrace();
        }
        world = new World();
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
            parseMessage(receiveQueue.take());
            // Sleep, because initialization takes some time
            Thread.sleep(5000);
           }
        catch (Exception e){}
        System.out.println("[ROBOT] done sleeping");

        // My own 'debug stream', maybe later this should go to a file or
        // something...
        PrintStreamDebug myDebug = new PrintStreamDebug(System.out, System.out);
        MyTimeFunction myTimeFunction = new MyTimeFunction();
		engine = new Engine(myDebug,
				myTimeFunction);

		// register symbols
        
        final JavaEnumeration behaviors = new JavaEnumeration("behaviors", 
            World.Behaviors.class, myDebug);
        engine.registerEnumeration(behaviors);

        // Register ammount_turned as a decimal input symbol
		Class<? extends World> theWorld = world.getClass();
		Method method = theWorld.getMethod("getAmmount_turned");
		engine.registerDecimalInputSymbol("ammount_turned",
				new DecimalInputSymbolImpl(new InputFromMethod(method, world),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
       
		method = theWorld.getMethod("getStayed");
		engine.registerDecimalInputSymbol("stayed",
				new DecimalInputSymbolImpl(new InputFromMethod(method, world),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
		method = theWorld.getMethod("getCurrent_behavior");
		engine.registerEnumeratedInputSymbol("current_behavior",
				new EnumeratedInputSymbolImpl(behaviors, 
                    new InputFromMethod(method, world),
						Conversions
								.getEnumeratedConversion(method.getReturnType()),
						new String[]{}, engine, myDebug));
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
            System.exit(-1);
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
                for (int i=0;i<runEngineAfter;i++)
                {
                    parseMessage(receiveQueue.take());
                }
                // if(receiveQueue.peek() != null)
                // {
                //     while(receiveQueue.peek() != null)
                //     {
                //         parseMessage(receiveQueue.take());
                //     }
                //     System.out.printf("ammount turned is now %f\n", world.getAmmount_turned());         
                // }
                // else 
                // {
                //     // To not eat all of UsarCommanders resources:
                //     Thread.sleep(500);
                // }
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

    public ArrayBlockingQueue<String> getReceiveQueue()
    {
        return receiveQueue;
    }

// ParseMessage {{{
    /** 
     * Parses messages sent from UsarCommander. Possible messages are the following:
     * - GROUNDTRUTH:X,Y,Yaw
     * - LASERSENSOR:minWNW,minNW,minNNW,minN,minNNE,minNE,minENE
     * - BEHAVIOR:<behavior type in world.Behaviors>
     */
    public void parseMessage(String message)
    {
        System.out.printf("Parsing message %s\n", message);
        String messageArray[] = message.split(":");
        if(messageArray[0].equals("GROUNDTRUTH"))
        {
            processGroundTruth(messageArray);
        }
        else if(messageArray[0].equals("LASERSENSOR"))
        {
            processLaserSensor(messageArray);
        }
        else if(messageArray[0].equals("BEHAVIOR"))
        {
            world.setCurrent_behavior(messageArray[1]);
            System.out.printf("Setting current behavior to '%s'\n", messageArray[1]);
        }
        else
        {
            System.out.printf("Couldn't parse\n");
        }
    }
// ParseMessage }}}

// LaserSensor {{{
    /**
     * Processes the laser sensor data, not all laser data is sent, so only 6 values have to be processed.
     */
    public void processLaserSensor(String[] messageArray)
    {
        String[] parameters = messageArray[1].split(",");
        world.setLaserMinWNW(Double.parseDouble(parameters[0]));
        world.setLaserMinNW(Double.parseDouble(parameters[1]));
        world.setLaserMinNNW(Double.parseDouble(parameters[2]));
        world.setLaserMinNNE(Double.parseDouble(parameters[3]));
        world.setLaserMinNE(Double.parseDouble(parameters[4]));
        world.setLaserMinENE(Double.parseDouble(parameters[5]));
        System.out.println("set all relevant laser parameters");
    }
// LaserSensor }}}

// GroundTruth {{{
    public void processGroundTruth(String[] messageArray)
    {
        double currentAngle = world.getLastAngle();
        String[] parameters = messageArray[1].split(",");
        world.setX(Double.parseDouble(parameters[0]));
        world.setY(Double.parseDouble(parameters[1]));
        world.setYaw(Double.parseDouble(parameters[2]));
        // Part used for drive_circle 
        double angle = world.getYaw();
        if(currentAngle != 0)
        {
            double angleDiff = Math.abs(currentAngle - angle);
            // current angle reached 360 and went to 0
            if (angleDiff > 300 && angle < 60)
            {
                angleDiff = Math.abs(currentAngle - 360 - angle);
            }
            // current angle reached 0 and went to 360
            else if (angleDiff > 300 && angle > 300)
            {
                angleDiff = Math.abs(currentAngle + 360-angle);
            }
            world.setAmmount_turned(world.getAmmount_turned() + angleDiff);
            System.out.println("changing ammount_turned to " + world.getAmmount_turned());
        }
        world.setLastAngle(angle);
    }
// GroundTruth }}}
}


