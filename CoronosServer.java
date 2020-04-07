import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;    //for JFrame, JButton
import java.awt.*; 
import java.awt.event.*;


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

    private JFrame serverFrame;
    private JPanel serverInfo;
    private JLabel serverAddress;
    private JLabel hostLabel;
    private Vector<String> users = new Vector<>();
    private InetAddress localhost;

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
        users.add("connor:demo");
        try{
            localhost = InetAddress.getLocalHost();
        }
        catch(UnknownHostException uhe){}

        serverFrame = new JFrame("Coronos Server");
        serverFrame.setLayout(new BorderLayout());
        serverInfo = new JPanel();
        serverInfo.setLayout(new FlowLayout());
        serverInfo.setBorder(BorderFactory.createTitledBorder("Information"));
        serverFrame.add(serverInfo, BorderLayout.CENTER);

        hostLabel = new JLabel("IP Address: ");
        serverInfo.add(hostLabel);

        serverAddress = new JLabel();
        serverAddress.setText(localhost.toString().split("/")[1]);
        serverInfo.add(serverAddress);

        serverFrame.setSize(400, 100);
        serverFrame.setVisible(true);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setResizable(false);
        serverFrame.setLocationRelativeTo(null);

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

    public static void main(String [] args){
        CoronosServer serv = new CoronosServer();
    }

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
                            CoronosAuth temp = (CoronosAuth) ob;
                            String username = temp.getUsername().toLowerCase();
                            String password = temp.getPassword();
                            String ipTotal = ss.getLocalSocketAddress().toString();
                            String ipS = ipTotal.split("/")[0];
                            System.out.printf("[AUTH] - Attempted Login - %s\n[AUTH] - Username: %s\n[AUTH] - Password: %s\n", ipS, username, password);
                            for(String us : users){
                                String tempUser = us.split(":")[0].toLowerCase();
                                String tempPass = us.split(":")[1];
                                if(tempUser.equals(username) && tempPass.equals(password)){
                                    System.out.println("[AUTH] - Successful Login - Valid user");
                                    temp.setAllow(true);
                                    break;
                                }
                                else if(tempUser.equals(username) && !tempPass.equals(password)){
                                    System.out.println("[AUTH] - Failed Login - Invalid Password");
                                    temp.setAllow(false, "Invalid Password");
                                }
                                else if(!tempUser.equals(username)){
                                    System.out.println("[AUTH] - Failed Login - User Not Found");
                                    temp.setAllow(false, "Username Does Not Exist.");
                                }
                            }
                            oos.writeObject((Object)temp);
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
