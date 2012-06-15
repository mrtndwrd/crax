package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;
import jxi.connection.ConnectionHandler;

/**
 * What every basic behavior should do: Implements getParameters, 
 * and handles the connection with UsarConnector;
 */
public abstract class StandardBehavior extends BasicBehavior
{
    /** All parameters */
    Parameters myParameters;
    /** Connection with UsarCommander */
    ConnectionHandler usarConnection;

    /** Constructor:
     * @param name: Name of the behavior
     * @param myDebug: a debug printstream
     * @param usarConnection: A connection handler that is connected to UsarCommander
     */
    public StandardBehavior(String name, PrintStreamDebug myDebug, 
        ConnectionHandler usarConnection)
    {
        super(name, myDebug);
        myParameters = new Parameters(myDebug);
        this.usarConnection = usarConnection;
    }

    @Override
    public Parameters getParameters() 
    {
        return myParameters;
    }

    public void sendMessage(String message)
    {
        usarConnection.sendMessage(message);
    }
}
