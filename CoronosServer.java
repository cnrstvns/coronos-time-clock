import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Dalton Kruppenbacher - Server Setup/Initial Commit
 * @author Connor Stevens - GUI
 * @author Brian Zhu
 * @author Everett Simone
 * @version 0.1
 * Revision Notes: Initial Commit (Server Setup)
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The CoronosServer is the Server of the Coronos Timeclock Solution. The server will host all of
 *                    the Employee objects and allow an Administrator to adjust settings of both the Server and the
 *                    Employees.
 *
 *                    The duplication of this code without written consent of the authors is strictly prohibited.
 *
 */
public class CoronosServer {
    //global constants
    private final int PORT_NUMBER = 16789;

    //global Networking / IO declaration
    private ServerSocket ss;
    private Socket s;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    //global GUI attribute declaration

    /**
     * Default Constructor of the CoronosServer class
     *      Creates the GUI
     *      Creates and starts a Thread
     * @since 0.1
     */
    public CoronosServer() {
        //GUI stuff

        try {
            //create a serversocket
            ss = new ServerSocket(PORT_NUMBER);
            //create a InnerThread Object
            InnerThread it = new InnerThread();
            //start the threaded object
            it.start();
        } catch(BindException be) {
            System.err.println("EXCEPTION: CoronosServer BindException, " +
                    "something is running on port " + PORT_NUMBER);
        } catch(SocketException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }//end try catch block
    }//end constructor

    class InnerThread extends Thread {
        public void run() {
            while(true) {
                try {
                    s = ss.accept();
                    oos = new ObjectOutputStream(s.getOutputStream());
                    ois = new ObjectInputStream(s.getInputStream());
					try {
                        Object ob;
                        ob = (Object) ois.readObject();

                        if(ob instanceof CoronosAuth) {

                        }
                    }
                    catch(ClassNotFoundException cnfe){
                        cnfe.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }//end first try catch block
            }//end while loop
        }//end run()
    }//end InnerThread
}//end CoronosServer
