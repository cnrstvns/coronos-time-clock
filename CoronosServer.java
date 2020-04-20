import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.json.simple.*;
import org.json.simple.parser.*;


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
public class CoronosServer implements ActionListener {

    private JPanel serverInfo, encompassPanel, chatPanel, opsPanel, settingsPanel, chatBar;
    private JLabel serverAddress, hostLabel, portLabel, connectedUsers;
    private Vector<ObjectOutputStream> messageStreams = new Vector<>();
    private JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b0;
    private Vector<String> users = new Vector<>();
    private final int PORT_NUMBER = 16789;
    private javax.swing.Timer shutdownTimer;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private InetAddress localhost;
    private JTextField sendField;
    private JTextArea chatArea;
    private JFrame serverFrame;
    public int connected = 0;
    private ServerSocket ss;
    public Socket s;

    /**
     * Default Constructor of the CoronosServer class
     *      Creates the GUI
     *      Creates and starts a Thread
     * @since 0.1
     */
    public CoronosServer() {

        try{
            localhost = InetAddress.getLocalHost();
        }
        catch(UnknownHostException uhe){}

        uiBuilder();
        usersBuilder();
        
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

    public void actionPerformed(ActionEvent ae){
        String actionString = ae.getActionCommand();
        System.out.println(String.format("[ACTION] - %s", actionString));

        if(actionString.equals("Add Employee")){
            String newUsername;
            String newPassword;
            System.out.println(String.format("[%s]", actionString));
            ArrayList<String> usernames = new ArrayList<>();
            for(String user:users){
                usernames.add(user.split(":")[0]);
            }
            while(true){
                newUsername = JOptionPane.showInputDialog("Username");
                if(usernames.contains(newUsername)){
                    JOptionPane.showMessageDialog(serverFrame, "User Already Exists", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                else if(newUsername.equals("")){
                    JOptionPane.showMessageDialog(serverFrame, "Username Cannot be null", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    break;
                }
            }

            while(true){
                newPassword = JOptionPane.showInputDialog("Password");
                if(newPassword.length() < 8){
                    JOptionPane.showMessageDialog(serverFrame, "Password must be longer than 7 characters", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    break;
                }
            }

            int newID = users.size();
            addUser(newUsername, newPassword, newID);
            users.clear();
            usersBuilder();
            createRecord(newID);

        }

        if(actionString.equals("Modify Employee")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Delete Employee")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Generate Report")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Reset Password")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Edit Credentials")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Wtf does this do")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Shut Down Server")){

            System.out.println(String.format("[%s]", actionString));
            if(connected > 0){
                System.out.println(String.format("[SHUTDOWN] - [%d Users Connected] - Aborting Shutdown...", connected));
                JOptionPane.showMessageDialog(serverFrame, String.format("Cannot Shut Down, %d clients are connected. Please notify them of downtime before closing the server.", connected), "Shutdown Error", JOptionPane.WARNING_MESSAGE);
            }
            else{
                try{
                    System.err.println(String.format("[SHUTDOWN] - [%d Users Connected] - Aborting Shutdown...", connected));
                    System.err.println(String.format("[SHUTDOWN] - [Timeout] - Waiting to Shut Down...", connected));
                    Thread.sleep(2000);
                    System.err.println(String.format("[SHUTDOWN] - [Timer Complete] - Shutting Down...", connected));
                }
                catch(InterruptedException ie){}
                System.exit(0);
            }
        }

        if(actionString.equals("About")){
            System.out.println(String.format("[%s]", actionString));
        }

        if(actionString.equals("Test")){
            System.out.println(String.format("[%s]", actionString));
        }
        
        if(actionString.equals("Send")){
            System.out.println(String.format("[%s] - [ADMIN] - %s", actionString, sendField.getText()));
            Message msg = new Message(String.format("ADMIN: %s\n", sendField.getText()));
            sendField.setText("");
            chatArea.append(msg.toString());
            if(messageStreams.size() == 0){
                chatArea.append("***Server Message: No clients are connected. Are you lonely?\n");
            }
            for(ObjectOutputStream client : messageStreams){
                try{
                    client.writeObject(msg);
                    client.flush();
                }
                catch(IOException ioe) {
                    System.err.println("[CHAT] - [SEND] - Error sending chat.");
                }
            }
        }
    }

    public void uiBuilder(){
        b1 = new JButton("Add Employee");
        b1.addActionListener(this);
        b2 = new JButton("Modify Employee");
        b2.addActionListener(this);
        b3 = new JButton("Delete Employee");
        b3.addActionListener(this);
        b4 = new JButton("Generate Report");
        b4.addActionListener(this);
        b5 = new JButton("Reset Password");
        b5.addActionListener(this);

        b6 = new JButton("Edit Credentials");
        b6.addActionListener(this);
        b7 = new JButton("Wtf does this do");
        b7.addActionListener(this);
        b8 = new JButton("Shut Down Server");
        b8.addActionListener(this);
        b9 = new JButton("About");
        b9.addActionListener(this);
        b0 = new JButton("Test");
        b0.addActionListener(this);

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


        opsPanel.add(b1);
        opsPanel.add(b2);
        opsPanel.add(b3);
        opsPanel.add(b4);
        opsPanel.add(b5);


        encompassPanel.add(opsPanel);

        chatPanel = new JPanel();
        chatPanel.setLayout(new FlowLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        chatArea = new JTextArea(10, 10);
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
        send.addActionListener(this);
        sendField = new JTextField(40);
        chatBar.add(send);
        chatBar.add(sendField);
        serverFrame.add(chatBar, BorderLayout.SOUTH);


        serverFrame.setSize(400, 100);
        serverFrame.setVisible(true);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //serverFrame.setResizable(false);
        serverFrame.pack();
        serverFrame.setLocationRelativeTo(null);
    }

    public void usersBuilder(){
        JSONParser parser = new JSONParser();
        
        try {
            Reader reader = new FileReader(".\\data\\users.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);
            JSONArray storedUsers = (JSONArray) jsonObject.get("users");
            System.out.printf("Loaded %d users.\n", storedUsers.size());
            Iterator<String> iterator = storedUsers.iterator();
            while(iterator.hasNext()){
                String storedUser = iterator.next();
                users.add(storedUser);
            }
            reader.close();
        }
        
        catch(FileNotFoundException fnfe){
            System.err.println("[ERROR] - [usersBuilder] - File Not Found");
        }

        catch(ParseException pe) {
            System.err.println("[ERROR] - [usersBuilder] - Exception occurred while parsing.");
            System.out.println(pe);
        }

        catch(IOException ioe){
            System.err.println("[ERROR] - [usersBuilder] - IO Exception.");
        }
    }

    public void addUser(String username, String password, int id){
        JSONParser parser = new JSONParser();

        try {
            Reader reader = new FileReader(".\\data\\users.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);
            JSONArray storedUsers = (JSONArray) jsonObject.get("users");
            System.out.printf("Loaded %d users.\n", storedUsers.size());
            String newUser = String.format("%s:%s:%d", username, password, id);
            storedUsers.add(newUser);
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("users", storedUsers);
            System.out.println(storedUsers);
            System.out.println(newJsonObject);
            Writer writer = new FileWriter(".\\data\\users.json");
            writer.write(newJsonObject.toJSONString());
            System.out.printf("Saved %d users.\n", storedUsers.size());
            reader.close();
            writer.close();
        }

        catch(FileNotFoundException fnfe){
            System.err.println("[ERROR] - [usersBuilder] - File Not Found");
        }

        catch(ParseException pe) {
            System.err.println("[ERROR] - [usersBuilder] - Exception occurred while parsing.");
            System.out.println(pe);
        }

        catch(IOException ioe){
            System.err.println("[ERROR] - [usersBuilder] - IO Exception.");
        }
    }

    public void createRecord(int id){
        try{
            String filename = String.format(".\\data\\records\\%d.json", id);
            File file = new File(filename);
            Writer writer = new FileWriter(filename);
            JSONObject obj = new JSONObject();
            writer.write(obj.toJSONString());
            writer.close();
        }
        catch(IOException ioe) {
            System.err.println("[ERROR] - [createRecord] - IO Error");
        }
    }

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

                            if (ob instanceof CoronosAuth) {
                                CoronosAuth temp = (CoronosAuth) ob;

                                String username = temp.getUsername().toLowerCase();
                                String password = temp.getPassword();
                                String ipTotal = ss.getLocalSocketAddress().toString();
                                String ipS = ipTotal.split("/")[0];
                                System.out.printf("[AUTH] - Attempted Login - %s\n[AUTH] - Username: %s\n[AUTH] - Password: %s\n", ipS, username, password);

                                for (String user : users) {
                                    String userName = user.split(":")[0];
                                    String passWord = user.split(":")[1];
                                    if (username.equalsIgnoreCase(userName)) {
                                        if (password.equals(passWord)) {
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
                                }

                                oos.writeObject(temp);
                                oos.flush();
                            } else if (ob instanceof Message) {
                                Message temp = (Message) ob;
                                System.out.printf("[MESSAGE] - [Received] - %s\n", temp.toString());
                                chatArea.append(temp.toString());
                                for (ObjectOutputStream oos : messageStreams) {
                                    System.out.printf("[MESSAGE] - Sent\n");
                                    oos.writeObject(temp);
                                }
                            }

                    }

                    catch(ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }

                    catch(EOFException eofe){
                        connected--;
                    }

                    catch(SocketException se){
                        break;
                    }

                    catch(IOException ioe) {
                    }
            }
            connected--;
            messageStreams.remove(oos);
            connectedUsers.setText(String.format("%d", connected));
            try{
                s.close();
            }
            catch(IOException ioe){}




        }//end run()
    }//end InnerThread
}//end CoronosServer
