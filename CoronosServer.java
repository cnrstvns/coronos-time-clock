import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; 


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
    private JPanel encompassPanel;
    private JPanel chatPanel;
    private JPanel opsPanel;
    private JPanel settingsPanel;
    private JPanel chatBar;
    private JLabel hostLabel;
    private JLabel portLabel;
    public int connected = 0;
    public JLabel connectedUsers;


    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private JButton b5;

    private JButton b6;
    private JButton b7;
    private JButton b8;
    private JButton b9;
    private JButton b0;

    JLabel t1;
    JLabel t2;
    JLabel t3;
    JLabel t4;
    JLabel t5;
    
    JLabel t6;
    JLabel t7;
    JLabel t8;
    JLabel t9;
    JLabel t0;


    private JTextArea chatArea;
    private Vector<String> users = new Vector<>();
    private Vector<ObjectOutputStream> messageStreams = new Vector<>();
    private InetAddress localhost;

    //global Networking / IO declaration
    private ServerSocket ss;
    public Socket s;
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
        users.add("connor");
        users.add("demo");
        try{
            localhost = InetAddress.getLocalHost();
        }
        catch(UnknownHostException uhe){}

        t1 = new JLabel("Test");
        t2 = new JLabel("Test");
        t3 = new JLabel("Test");
        t4 = new JLabel("Test");
        t5 = new JLabel("Test");

        t6 = new JLabel("Test");
        t7 = new JLabel("Test");
        t8 = new JLabel("Test");
        t9 = new JLabel("Test");
        t0 = new JLabel("Test");




        b1 = new JButton("Test:");
        b2 = new JButton("Test");
        b3 = new JButton("Test");
        b4 = new JButton("Test");
        b5 = new JButton("Test");

        b6 = new JButton("Test");
        b7 = new JButton("Test");
        b8 = new JButton("Test");
        b9 = new JButton("Test");
        b0 = new JButton("Test");



        serverFrame = new JFrame("Coronos Server");
        serverFrame.setLayout(new BorderLayout());

        serverInfo = new JPanel();
        serverInfo.setLayout(new FlowLayout());
        serverInfo.setBorder(BorderFactory.createTitledBorder("Information"));
        serverFrame.add(serverInfo, BorderLayout.NORTH);

        encompassPanel = new JPanel(new FlowLayout());

        opsPanel = new JPanel();
        opsPanel.setLayout(new GridLayout(5, 1));
        opsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        opsPanel.add(t1);
        opsPanel.add(b1);
        opsPanel.add(t2);
        opsPanel.add(b2);
        opsPanel.add(t3);
        opsPanel.add(b3);
        opsPanel.add(t4);
        opsPanel.add(b4);
        opsPanel.add(t5);
        opsPanel.add(b5);


        encompassPanel.add(opsPanel);

        chatPanel = new JPanel();
        chatPanel.setLayout(new FlowLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        chatArea = new JTextArea("Chat Will be here.\n\n\n\n\n\n");
        chatArea.setEditable(false);
        chatPanel.add(chatArea);
        encompassPanel.add(chatPanel);

        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(5, 1));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));


        settingsPanel.add(b6);
        settingsPanel.add(b7);
        settingsPanel.add(b8);
        settingsPanel.add(b9);
        settingsPanel.add(b0);



        encompassPanel.add(settingsPanel);

        serverFrame.add(encompassPanel);

        hostLabel = new JLabel("IP Address:");
        serverInfo.add(hostLabel);

        serverAddress = new JLabel();
        serverAddress.setText(localhost.toString().split("/")[1]);
        serverInfo.add(serverAddress);

        portLabel = new JLabel((Integer.toString(PORT_NUMBER)));
        JLabel port = new JLabel("Port: ");

        connectedUsers = new JLabel(Integer.toString(connected));
        JLabel connectedLabel = new JLabel("Connected Users: ");
        serverInfo.add(port);
        serverInfo.add(portLabel);
        serverInfo.add(connectedLabel);
        serverInfo.add(connectedUsers);


        chatBar = new JPanel(new FlowLayout());
        JButton send = new JButton("Send");
        JTextField sendField = new JTextField(40);
        chatBar.add(send);
        chatBar.add(sendField);
        serverFrame.add(chatBar, BorderLayout.SOUTH);


        serverFrame.setSize(400, 100);
        serverFrame.setVisible(true);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //serverFrame.setResizable(false);
        serverFrame.pack();
        serverFrame.setLocationRelativeTo(null);

        try {
            //create a serversocket
            ss = new ServerSocket(PORT_NUMBER);
            while(true){
                s = ss.accept();
                InnerThread it = new InnerThread(s);
                //start the threaded object
                it.start();
                //create a InnerThread Object
            }
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

        private Socket threadSocket;

        public InnerThread(Socket s) {
            threadSocket = s;
        }

        public void run() {
            connected++;
            connectedUsers.setText(Integer.toString(connected));
            try{
                oos = new ObjectOutputStream(threadSocket.getOutputStream());
                messageStreams.add(oos);
                ois = new ObjectInputStream(threadSocket.getInputStream());
            }
            catch(IOException ioe){
                System.err.println("[ERROR]: " + ioe);
            }
            while(true) {

                    try {
                            Object ob = (Object) ois.readObject();

                            if(ob instanceof CoronosAuth) {
                                CoronosAuth temp = (CoronosAuth) ob;

                                String username = temp.getUsername().toLowerCase();
                                String password = temp.getPassword();
                                String ipTotal = ss.getLocalSocketAddress().toString();
                                String ipS = ipTotal.split("/")[0];
                                System.out.printf("[AUTH] - Attempted Login - %s\n[AUTH] - Username: %s\n[AUTH] - Password: %s\n", ipS, username, password);

                                if(users.contains(username)) {
                                    String tempPassword = users.get(users.indexOf(username) + 1);

                                    if(password.equals(tempPassword)) {
                                        System.out.println("[AUTH] - Successful Login - Valid user");
                                        temp.setAllow(true);

                                    } else {
                                        System.out.println("[AUTH] - Failed Login - Invalid Password");
                                        temp.setAllow(false, "Invalid Password");
                                    }
                                } else {
                                    System.out.println("[AUTH] - Failed Login - User Not Found");
                                    temp.setAllow(false, "Invalid Username");
                                }
                                oos.writeObject(temp);
                                oos.flush();
                            }

                            else if(ob instanceof Message){

                                Message temp = (Message) ob;

                                for(ObjectOutputStream oos: messageStreams){
                                    oos.writeObject(temp.toString());
                                }
                            }
                    } catch(ClassNotFoundException cnfe){
                        cnfe.printStackTrace();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
            }



        }//end run()
    }//end InnerThread
}//end CoronosServer
