package jxi.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import jxi.engine.*;

/**
 * This class represents a connection with another module. It continues to
 * listen for incoming messages and puts them in the BlockingQueue of the
 * abstract base. There the message will wait until it can be processed by the
 * Logic thread. <br />
 * <br />
 * Besides listening for message, it can also send messages.
 * 
 * @author Maarten Inja
 * @author Maarten de Waard
 */
public class ConnectionHandler extends Thread {

	private Socket socket;
    private Robot robot;
	private boolean bidirectional;
	private boolean passive;
	
	private PrintWriter out;
	private MessageReader in;
	private boolean alive = true;
    /** New line character, to make it work under windows */
    public static String newline = System.getProperty("line.separator");

    /** Variable to make sure the same message is never sent twice */
    private String lastSentMessage;

	/**
	 * The constructor, saves arguments and reads in- and outcoming streams from
	 * the socket.
	 * 
	 * This constructor is for the base-class self-started connections.
	 * 
	 * @throws IOException
	 */
	public ConnectionHandler(Robot robot, boolean bidirection, Socket socket) throws IOException 
    {
		this.socket = socket;
        this.robot = robot;
		setPassive(false);
		setBidirectional(bidirection);
        setConnAlive(true);
		out = new PrintWriter(socket.getOutputStream(), true);
        in = new MessageReader(socket.getInputStream());
	}

	/** Thread logic. */
	public void run() {
		while (true) {
			try {
				String incomingMessage = in.readMessage();
				robot.getReceiveQueue().put(incomingMessage);
			} catch (SocketTimeoutException e) {
				System.out.println(String.format(
						"Time out receiving from: %s",
						socket.getRemoteSocketAddress()));
				if (!socket.isConnected())
                { 
					setConnAlive(false);
					break;
                }
            } catch(SocketException e) {
                System.out.println("Socket down, exiting");
                setConnAlive(false);
                System.exit(-1);
			} catch (Exception e) {
				//System.out.println(String
				//    .format("%s IN ConnectionHandler.run, attempting to read message.",
				//						e.getMessage()));
				e.printStackTrace();
                setConnAlive(false);
			}
		}
	}

	/**
	 * Sends a message object, this is currently only the message object
	 * toString().
     * Sleeps a second after sending, to work with Visual Basics 
     * poor network stream reading
	 */
	public void sendMessage(String message) {
        if (!message.equals(lastSentMessage))
        {
            out.println(message + newline);
            /* This was needed for when too many messages are sent. 
             * Let's hope that doesn't happen :( 
            try{
                Thread.sleep(1000);
            } catch(Exception e){}
            */
            System.out.printf("CH: Sent message %s\n", message);
            lastSentMessage = message;
        }
	}

	public boolean isConnAlive() {
		return alive;
	}

	private void setConnAlive(boolean value) {
		alive = value;
	}

	public int getPort() {
		return this.socket.getPort();
	}

	public String getAddress() {
		return this.socket.getInetAddress().getHostAddress();
	}

	/**
	 * @param bidirectional the bidirectional to set
	 */
	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}

	/**
	 * @return the bidirectional
	 */
	public boolean isBidirectional() {
		return bidirectional;
	}

	/**
	 * @param passive the passive to set
	 */
	public void setPassive(boolean passive) {
		this.passive = passive;
	}

	/**
	 * @return the passive
	 */
	public boolean isPassive() {
		return passive;
	}
}
