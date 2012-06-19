package jxi.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Specialized bufferedReader to directly parse messages from a 
 * socket.
 * 
 * @author Maarten Inja
 * @author Maarten de Waard
 */
public class MessageReader extends BufferedReader {

    /**
     * 
     * @param in
     */
    public MessageReader(InputStream in) 
    {
        super(new InputStreamReader(in));
    }

    /**
     * Reads a message and parses it.
     */
    public String readMessage() throws IOException 
    {
       String line = null;
       boolean succeeded = false;

        while (!succeeded) {
            try {
                line = readLine();
                //line = Character.toString((char) read());
                succeeded = true;
            } catch (SocketTimeoutException ex) {
                throw ex;
            }
        } 
        return line;
    }
}
