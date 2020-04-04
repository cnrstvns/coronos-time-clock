import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Dalton Kruppenbacher - Server Setup/Initial Commit
 * @author Connor Stevens - GUI
 * @author Brian Zhu
 * @author Everett Simone
 * @version 0.1
 * Revision Notes: Initial Commit (Client Setup)
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The CoronosClient is the Client of the Coronos Timeclock Solution. The client will allow an
 *                    employee to log-in, log a time punch, access past time punches, access past pay records, and view
 *                    current pay period data.
 *
 *                    The duplication of this code without written consent of the authors is strictly prohibited.
 *
 */
public class CoronosClient {
    //global constants
    private final String IP_ADDRESS = "localhost";
    private final int PORT_NUMBER = 16789;

    //global Networking / IO declaration
    private Socket s;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    //global GUI attribute declaration

    /**
     * Default Constructor of the SalesClient class
     *      Creates the GUI
     *      Creates the Socket for connection
     *      Creates the Object Streams
     * @since 0.1
     */
    public CoronosClient() {
        //GUI stuff

        //establish a connection
        try {
            s = new Socket(IP_ADDRESS, PORT_NUMBER);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            serverListener();
        } catch(UnknownHostException uhe) {
            System.err.println("EXCEPTION: CoronosClient UnknownHostException, error with localhost");
        } catch(BindException be) {
            be.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }//end try catch block
    }//end constructor

    /**
     * Method to handle reading commands and objects from the server
     * @since 0.1
     */
    public void serverListener() {
        while (true) {

        }
    }//end serverListener()

    /**
     * Main Method
     * @param args Arguments of the Main Method
     */
    public static void main(String[] args) {
        CoronosClient ins = new CoronosClient();
    }//end main
}//end CoronosClient
