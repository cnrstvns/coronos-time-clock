import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


/**
 * @author Dalton Kruppenbacher - Server Setup/Initial Commit
 * @author Connor Stevens - GUI
 * @author Brian Zhu
 * @author Everett Simone
 * @version 1.0
 * Revision Notes: Full Release
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

    String[] states = {"", "Alabama", "Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia",
            "Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire", "New Jersey", "New Mexico",
            "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
    private JPanel serverInfo, encompassPanel, chatPanel, opsPanel, chatBar;
    private JLabel serverAddress, hostLabel, portLabel, connectedUsers;
    private Vector<ObjectOutputStream> messageStreams = new Vector<>();
    private JButton jbAdd, jbEnable, jbDisable;
    private Vector<String> users = new Vector<>();
    private final int PORT_NUMBER = 16789;
    private JMenu jmFile, jmHelp;
    private JMenuBar jmMenuBar;
    private JMenuItem jmAbout, jmShutdown;
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

        //builds the server GUI and populates a list of users
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

    /**
     * Method to handle Button actions
     * @param ae An Action Event
     */
    public void actionPerformed(ActionEvent ae){
        String actionString = ae.getActionCommand();
        System.out.println(String.format("[ACTION] - %s", actionString));

        /*
            If the Add Employee Button is pressed:
                GUI to create a new employee generated
                Save Button saves Employee
                Data validation for values
         */
        if(actionString.equals("Add Employee")){
            //create and parse through a list of users
            ArrayList<String> usernames = new ArrayList<>();
            for(String user:users){
                usernames.add(user.split(":")[0]);
            }

            //GUI creation
            JFrame addUser = new JFrame("Coronos - Add User");
            addUser.setLayout(new GridLayout(7, 1));
            JPanel topPanel = new JPanel(new FlowLayout());
            JPanel centerPanel = new JPanel(new FlowLayout());
            JPanel bottomPanel = new JPanel(new FlowLayout());
            JPanel panelFour = new JPanel(new FlowLayout());
            JPanel panelFive = new JPanel(new FlowLayout());
            JPanel panelSix = new JPanel(new FlowLayout());

            JLabel usernameLabel = new JLabel("Username:");
            JTextField usernameField = new JTextField(10);
            JLabel passwordLabel = new JLabel("Password:");
            JTextField passwordField = new JTextField(10);
            JLabel wageLabel = new JLabel("Hourly Pay:");
            JTextField wageField = new JTextField(10);
            JLabel roleLabel = new JLabel("Role:");
            JTextField roleField = new JTextField(10);

            JLabel firstNameLabel = new JLabel("First Name:");
            JLabel lastNameLabel = new JLabel("Last Name:");
            JTextField firstNameField = new JTextField(10);
            JTextField lastNameField = new JTextField(10);

            JLabel addressLabel = new JLabel("Street Address:");
            JTextField addressField = new JTextField(10);

            JLabel cityLabel = new JLabel("City:");
            JTextField cityField = new JTextField(10);

            JLabel zipLabel = new JLabel("Zip Code");
            JTextField zipField = new JTextField(10);

            JLabel statesLabel = new JLabel("State:");
            JComboBox statesBox = new JComboBox(states);
            statesBox.setSelectedIndex(0);

            //creates a save button to save employee
            JButton saveButton = new JButton("Save Employee");

            /*
                Inner ActionListener to handle Save Employee Button
                    Gets all input
                    Assigns an ID
                    Validates data
             */
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String actionString = ae.getActionCommand();
                    if(actionString.equals("Save Employee")){
                        //get text input
                        String newUsername = usernameField.getText().toLowerCase();
                        String newPassword = passwordField.getText();
                        String role = roleField.getText();
                        String firstName = firstNameField.getText();
                        String lastName = lastNameField.getText();
                        String address = addressField.getText();
                        String zip = zipField.getText();
                        int state = statesBox.getSelectedIndex();
                        String selectedState = states[state];
                        String city = cityField.getText();
                        double wage;
                        try{
                            wage = Double.parseDouble(wageField.getText());
                        }
                        catch(NumberFormatException nfe){
                            wage = 0.0;
                        }

                        //assign ID to employee
                        int newID = users.size();

                        //check to make sure the New Employee Profile doesn't match another employee profile
                        if(usernames.contains(newUsername)){
                            JOptionPane.showMessageDialog(serverFrame, "User Already Exists", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                        }

                        //check password for length requirement
                        else if(newPassword.length() < 8){
                            JOptionPane.showMessageDialog(serverFrame, "Password must be longer than 7 characters", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                        }

                        //check to make sure a data field isn't empty
                        else if(firstName.equals("") || lastName.equals("") || address.equals("") || selectedState.equals("") || city.equals("") || newUsername.equals("") || role.equals("")){
                            JOptionPane.showMessageDialog(serverFrame, "Empty Field Detected, ensure all fields are filled.", "User Registration Error", JOptionPane.ERROR_MESSAGE);
                        }

                        //if all validation passes, then we create a new record
                        else{
                            addUser(newUsername, newPassword, newID);
                            createRecord(newID, wage, role, firstName, lastName, address, zip, city, selectedState);
                            users.clear();
                            usersBuilder();
                            addUser.setVisible(false);
                        }
                    }
                }
            });

            //add JComponents to JPanels
            topPanel.add(usernameLabel);
            topPanel.add(usernameField);
            topPanel.add(passwordLabel);
            topPanel.add(passwordField);

            centerPanel.add(wageLabel);
            centerPanel.add(wageField);
            centerPanel.add(roleLabel);
            centerPanel.add(roleField);

            bottomPanel.add(saveButton);

            panelFour.add(firstNameLabel);
            panelFour.add(firstNameField);
            panelFour.add(lastNameLabel);
            panelFour.add(lastNameField);

            panelFive.add(addressLabel);
            panelFive.add(addressField);

            panelFive.add(cityLabel);
            panelFive.add(cityField);

            panelSix.add(statesLabel);
            panelSix.add(statesBox);

            panelSix.add(zipLabel);
            panelSix.add(zipField);

            //add JPanels to a JFrame
            addUser.add(panelFour);
            addUser.add(topPanel);
            addUser.add(centerPanel);
            addUser.add(panelFive);
            addUser.add(panelSix);
            addUser.add(bottomPanel);
            addUser.setLocationRelativeTo(null);
            addUser.setVisible(true);
            addUser.pack();

        }//end if Add Employee

        /*
            If the Disable Employee Button is pressed:
                GUI to disable a employee generated
                Disables Employee's account
         */
        if(actionString.equals("Disable Employee")){
            //GUI creation
            JFrame disableFrame = new JFrame("Disable Employee");
            disableFrame.setLayout(new GridLayout(2, 1));
            JPanel topPanel = new JPanel(new FlowLayout());
            JPanel bottomPanel = new JPanel(new FlowLayout());
            JLabel disableLabel = new JLabel("Disable User:");
            JTextField disableField = new JTextField(10);
            JButton disableButton = new JButton("Disable");

            /*
                Inner ActionListener to handle Disable Employee Button
                    Gets all input
                    Checks ActionString
                    Disables the account
             */
            disableButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    //get users, try to find them in a list
                    String actionString = ae.getActionCommand();
                    System.out.println(actionString);
                    String requested = disableField.getText().toLowerCase();
                    String name = null;
                    String id = null;
                    boolean success = false;
                    if(actionString.equals("Disable")) {
                        for (String user : users) {
                            name = user.split(":")[0];
                            id = user.split(":")[2];
                            //open file, delete them
                            success = requested.equals(name);
                            System.out.println(Boolean.toString(success));
                        }

                        /*
                            If the User was found
                                Boolean for Active account is set to false in the JSON database
                         */
                        if (success) {
                            try {
                                //TODO: custom dialog with icon
                                Reader reader = new FileReader(String.format(".\\data\\records\\%s.json", id));
                                JSONParser parser = new JSONParser();
                                JSONObject jsonObject = (JSONObject) parser.parse(reader);
                                reader.close();
                                System.out.println(jsonObject.get("active"));
                                jsonObject.put("active", false);
                                System.out.println(jsonObject.get("active"));
                                Writer writer = new FileWriter(String.format(".\\data\\records\\%s.json", id));
                                writer.write(jsonObject.toJSONString());
                                writer.close();
                                JOptionPane.showMessageDialog(null, String.format("%s has been disabled.", name));
                                disableFrame.setVisible(false);
                            } catch (FileNotFoundException fnfe) {
                                //TODO ERROR DIALOG
                                fnfe.printStackTrace();
                            } catch (IOException ioe) {
                                //TODO ERROR DIALOG
                                ioe.printStackTrace();
                            } catch (ParseException pe) {
                                //TODO ERROR DIALOG
                                pe.printStackTrace();
                            }

                        }//end if

                        //else, prompt user that an error occured
                        else {
                            //TODO: custom dialog with ICON
                            JOptionPane.showMessageDialog(serverFrame, "Could not find user record.", "Error Disabling Employee", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });

            //Add JComponents to JPanels
            //Add JPanels to JFrame
            topPanel.add(disableLabel);
            topPanel.add(disableField);
            bottomPanel.add(disableButton);
            disableFrame.add(topPanel);
            disableFrame.add(bottomPanel);
            disableFrame.setVisible(true);
            disableFrame.pack();
            disableFrame.setLocationRelativeTo(null);
        }//end Disable Employee

        /*
            If the Enable Employee Button is pressed:
                GUI to Enable a employee generated
                Enables Employee's account
         */
        if(actionString.equals("Enable Employee")){
            //GUI creation
            JFrame enableFrame = new JFrame("Enable Employee");
            enableFrame.setLayout(new GridLayout(2, 1));
            JPanel topPanel = new JPanel(new FlowLayout());
            JPanel bottomPanel = new JPanel(new FlowLayout());
            JLabel enableLabel = new JLabel("Enable User:");
            JTextField enableField = new JTextField(10);
            JButton enableButton = new JButton("Enable");

            /*
                Inner ActionListener to handle Enable Employee Button
                    Gets all input
                    Checks ActionString
                    Enables the account
             */
            enableButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String actionString = ae.getActionCommand();
                    System.out.println(actionString);
                    String requested = enableField.getText().toLowerCase();
                    String name = null;
                    String id = null;
                    boolean success = false;


                    if(actionString.equals("Enable")) {
                        for (String user : users) {
                            name = user.split(":")[0];
                            id = user.split(":")[2];
                            //open file, delete them
                            success = requested.equals(name);
                            System.out.println(Boolean.toString(success));
                        }

                        /*
                            If the User was found
                                Boolean for Active account is set to false in the JSON database
                         */
                        if (success) {
                            try {
                                //TODO: custom dialog with icon
                                Reader reader = new FileReader(String.format(".\\data\\records\\%s.json", id));
                                JSONParser parser = new JSONParser();
                                JSONObject jsonObject = (JSONObject) parser.parse(reader);
                                reader.close();
                                System.out.println(jsonObject.get("active"));
                                jsonObject.put("active", true);
                                System.out.println(jsonObject.get("active"));
                                Writer writer = new FileWriter(String.format(".\\data\\records\\%s.json", id));
                                writer.write(jsonObject.toJSONString());
                                writer.close();
                                JOptionPane.showMessageDialog(null, String.format("%s has been enabled.", name));
                                enableFrame.setVisible(false);
                            } catch (FileNotFoundException fnfe) {
                                //TODO ERROR DIALOG
                                fnfe.printStackTrace();
                            } catch (IOException ioe) {
                                //TODO ERROR DIALOG
                                ioe.printStackTrace();
                            } catch (ParseException pe) {
                                //TODO ERROR DIALOG
                                pe.printStackTrace();
                            }

                        }

                        //else, prompt user that an error occurred
                        else {
                            //TODO: custom dialog with ICON
                            JOptionPane.showMessageDialog(serverFrame, "Could not find user record.", "Error Enabling Employee", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });

            //Add JComponents to JPanels
            //Add JPanels to JFrame
            topPanel.add(enableLabel);
            topPanel.add(enableField);
            bottomPanel.add(enableButton);
            enableFrame.add(topPanel);
            enableFrame.add(bottomPanel);
            enableFrame.setVisible(true);
            enableFrame.pack();
            enableFrame.setLocationRelativeTo(null);
        }//end Enable Employee

        /*
            If the Shutdown Button is pressed:
                Verify that no users are connected to the server
                Wait two seconds before shutting down
                Shutdown the server
         */
        if(actionString.equals("Shut Down")){
            //verify closed status
            if(connected > 0){
                System.out.println(String.format("[SHUTDOWN] - [%d Users Connected] - Aborting Shutdown...", connected));
                JOptionPane.showMessageDialog(serverFrame, String.format("Cannot Shut Down, %d clients are connected. Please notify them of downtime before closing the server.", connected), "Shutdown Error", JOptionPane.WARNING_MESSAGE);
            }

            //shut down the server
            else{
                System.out.println("[SHUTDOWN] - [Timeout] - Waiting to Shut Down...");
                Timer timer = new Timer();
                TimerTask time = new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("[SHUTDOWN] - [Timer Complete] - Shutting Down...");
                        System.exit(0);
                    }
                };
                timer.schedule(time, 2000);
            }
        }//end shutdown

        /*
            If the About Button is pressed
                Shows crediting information
         */
        if(actionString.equals("About")){
            JOptionPane.showMessageDialog(null, "Coronos Time Clock © 2020\nAll Rights Reserved\n\nAuthors:\nConnor Stevens\nEverett Simone\nDalton Kruppenbacher\nBrian Zhu", "About Coronos", JOptionPane.INFORMATION_MESSAGE);
        }

        /*
            If the Send button is pressed
                Gets message from a JTextField
                Sends it to all clients
         */
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
    }//end Send

    /**
     * Method to handle building the GUI of the Coronos Server
     */
    public void uiBuilder(){
        jmMenuBar = new JMenuBar();
        //Instantiate JMenuBar

        jmFile = new JMenu("File");
        jmHelp = new JMenu("Help");
        //Adding Menus to JMenuBar

        jmAbout = new JMenuItem("About");
        jmShutdown = new JMenuItem("Shut Down");

        jmAbout.addActionListener(this);
        jmShutdown.addActionListener(this);
        //Instantiating Options for JMenus

        jmFile.add(jmShutdown);
        jmHelp.add(jmAbout);
        //adding Options to JMenus

        jmMenuBar.add(jmFile);
        jmMenuBar.add(jmHelp);
        //adding JMenus to JMenuBar

        jbAdd = new JButton("Add Employee");
        jbAdd.addActionListener(this);
        jbEnable = new JButton("Enable Employee");
        jbEnable.addActionListener(this);
        jbDisable = new JButton("Disable Employee");
        jbDisable.addActionListener(this);

        serverFrame = new JFrame("Coronos Server");
        serverFrame.setJMenuBar(jmMenuBar);
        serverFrame.setLayout(new BorderLayout());

        serverInfo = new JPanel();
        serverInfo.setLayout(new FlowLayout());
        serverInfo.setBorder(BorderFactory.createTitledBorder("Information"));
        serverFrame.add(serverInfo, BorderLayout.NORTH);

        encompassPanel = new JPanel(new FlowLayout());

        opsPanel = new JPanel();
        opsPanel.setLayout(new GridLayout(3, 1, 15, 5));
        opsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        opsPanel.add(jbAdd);
        opsPanel.add(jbEnable);
        opsPanel.add(jbDisable);

        encompassPanel.add(opsPanel);

        chatPanel = new JPanel();
        chatPanel.setLayout(new FlowLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        chatArea = new JTextArea(9, 25);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatPanel.add(new JScrollPane(chatArea));
        encompassPanel.add(chatPanel);

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
        sendField.addActionListener(this);
        sendField.setActionCommand("Send");
        chatBar.add(send);
        chatBar.add(sendField);
        serverFrame.add(chatBar, BorderLayout.SOUTH);


        serverFrame.setSize(400, 100);
        serverFrame.setVisible(true);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //serverFrame.setResizable(false);
        serverFrame.pack();
        serverFrame.setLocationRelativeTo(null);
    }//end uiBuilder()

    /**
     * Method to build a list of users from a JSON directory
     *      Reads through JSON File
     *      Updates an ArrayList of users
     */
    public void usersBuilder(){
        JSONParser parser = new JSONParser();
        
        try {
            Reader reader = new FileReader(".\\data\\users.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.printf("[USERS] - [userBuilder] - %s\n", jsonObject);
            JSONArray storedUsers = (JSONArray) jsonObject.get("users");
            System.out.printf("[USERS] - [userBuilder] - Loaded %d users.\n", storedUsers.size());
            users.addAll(storedUsers);
            reader.close();
        }
        
        catch(FileNotFoundException fnfe){
            System.err.println("[ERROR] - [usersBuilder] - File Not Found");
        }

        catch(ParseException pe) {
            System.err.println("[ERROR] - [usersBuilder] - Exception occurred while parsing.");
        }

        catch(IOException ioe){
            System.err.println("[ERROR] - [usersBuilder] - IO Exception.");
        }
    }//end userBuilder()

    /**
     * Method to create a new User Login
     * @param username The person's Username
     * @param password The person's password
     * @param id The person's ID number
     */
    public void addUser(String username, String password, int id){
        JSONParser parser = new JSONParser();

        try {
            Reader reader = new FileReader(".\\data\\users.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray storedUsers = (JSONArray) jsonObject.get("users");
            System.out.printf("Loaded %d users.\n", storedUsers.size());
            String newUser = String.format("%s:%s:%d", username, password, id);
            storedUsers.add(newUser);
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("users", storedUsers);
            Writer writer = new FileWriter(".\\data\\users.json");
            writer.write(newJsonObject.toJSONString());
            System.out.printf("Saved %d users.\n", storedUsers.size());
            reader.close();
            writer.close();
        }

        catch(FileNotFoundException fnfe){
            System.err.println("[ERROR] - [usersBuilder] - File Not Found.");
        }

        catch(ParseException pe) {
            System.err.println("[ERROR] - [usersBuilder] - Exception occurred while parsing.");
        }

        catch(IOException ioe){
            System.err.println("[ERROR] - [usersBuilder] - IO Exception.");
        }
    }//end addUser()


    /**
     * Method to create a new Employee Record; stores data in a JSON directory
     * @param id An employee's ID
     * @param pay An Employee's pay rate
     * @param role An employee's job title / position
     * @param firstName An employee's first name
     * @param lastName An employee's last name
     * @param address An employee's address
     * @param zip An employee's ZIP code
     * @param city An employee's city
     * @param state An employee's state
     */
    public void createRecord(int id, double pay, String role, String firstName, String lastName, String address, String zip, String city, String state){
        try{
            String filename = String.format(".\\data\\records\\%d.json", id);
            File file = new File(filename);
            Writer writer = new FileWriter(filename);
            JSONObject obj = new JSONObject();
            obj.put("active", true);
            obj.put("clockedIn", false);
            obj.put("wage", pay);
            obj.put("role", role);
            obj.put("firstName", firstName);
            obj.put("lastName", lastName);
            obj.put("address", address);
            obj.put("zip", zip);
            obj.put("city", city);
            obj.put("state", state);
            writer.write(obj.toJSONString());
            writer.close();
        }
        catch(IOException ioe) {
            System.err.println("[ERROR] - [createRecord] - IO Error");
        }
    }//end createRecord

    /**
     * Main Method
     * @param args
     */
    public static void main(String [] args){
        CoronosServer serv = new CoronosServer();
    }//end main

    /**
     * Inner Class for Threading
     * @version 1.0
     */
    class InnerThread extends Thread {
        //attributes
        private Socket threadSocket;

        /**
         * Constructor
         * @param s A Socket
         */
        public InnerThread(Socket s) {
            threadSocket = s;
        }//end constructor

        /**
         * Method to run the Thread
         */
        public void run() {
            //increment user count
            connected++;
            connectedUsers.setText(Integer.toString(connected));

            //set up streams
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

                            /*
                                If the Object read in is of the CoronosAuth class
                                    Verify username and password
                                    Allow the user to log in
                                    If not successful, prompt reason why
                             */
                            if (ob instanceof CoronosAuth) {
                                CoronosAuth temp = (CoronosAuth) ob;
                                int userID = 0;
                                String username = temp.getUsername().toLowerCase();
                                String password = temp.getPassword();
                                String ipTotal = ss.getLocalSocketAddress().toString();
                                String ipS = ipTotal.split("/")[0];
                                Boolean enabled = false;
                                JSONObject userJson = null;
                                System.out.printf("[AUTH] - Attempted Login - %s\n[AUTH] - Username: %s\n[AUTH] - Password: %s\n", ipS, username, password);

                                for (String user : users) {
                                    String userName = user.split(":")[0];
                                    String passWord = user.split(":")[1];
                                    //determine if username and password are valid, first username then password if username valid
                                    if (username.equalsIgnoreCase(userName)) {
                                        if (password.equals(passWord)) {
                                            String id = user.split(":")[2];
                                            userID = Integer.parseInt(id);
                                            JSONParser parser = new JSONParser();
                                            try{
                                                Reader reader = new FileReader(String.format(".\\data\\records\\%s.json", id));
                                                userJson = (JSONObject) parser.parse(reader);
                                                enabled = (Boolean) userJson.get("active");
                                            }
                                            catch(ParseException pe){
                                                System.err.println("[ERROR] - [AUTH] - Login Parse Error");
                                            }
                                            catch(IOException ioe){
                                                System.err.println("[ERROR] - [AUTH] - Login IO Error");
                                            }
                                            if(enabled){
                                                System.out.println("[AUTH] - Successful Login - Valid user");
                                                temp.setAllow(true);
                                                break;

                                            }
                                            else{
                                                System.out.println("[AUTH] - Failed Login - Deactivated user");
                                                temp.setAllow(false, "Account Deactivated");
                                            }
                                        }
                                        else {
                                            System.out.println("[AUTH] - Failed Login - Invalid Password");
                                            temp.setAllow(false, "Invalid Password");
                                        }
                                    }
                                    else {
                                        System.out.println("[AUTH] - Failed Login - User Not Found");
                                        temp.setAllow(false, "Invalid Username");
                                    }
                                }

                                //send authentication status to client
                                oos.writeObject(temp);
                                oos.flush();
                                //if employee is enabled, send data
                                if(enabled){
                                    Employee out = new Employee(userJson);
                                    out.setId(userID);
                                    oos.writeObject((Object) out);
                                    oos.flush();
                                }
                            }

                            /*
                             * If the Object is of the Message Class
                             *      Read in the object
                             *      Append it to the chat
                             */
                            else if (ob instanceof Message) {
                                Message temp = (Message) ob;
                                System.out.printf("[MESSAGE] - [Received] - %s\n", temp.toString());
                                chatArea.append(temp.toString());
                                for (ObjectOutputStream oos : messageStreams) {
                                    System.out.print("[MESSAGE] - Sent\n");
                                    oos.writeObject(temp);
                                    oos.flush();
                                }
                            }

                            /*
                                If the object is of the Employee Class
                                    Read in the object
                                    Save it to the JSON Directory
                             */
                            else if (ob instanceof Employee){
                                System.out.println("Got employee...");
                                System.out.println("Saving Employee...");
                                Employee temp = (Employee) ob;
                                int id = temp.getId();
                                Writer writer = new FileWriter(String.format(".\\data\\records\\%s.json", id));
                                String data = temp.getEmployeeData().toJSONString();
                                writer.write(data);
                                writer.flush();
                                writer.close();
                            }

                    }

                    catch(ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }

                    catch(EOFException eofe){

                    }

                    catch(SocketException se){
                        break;
                    }

                    catch(IOException ioe) {
                    }
            }

            //decrement counter
            connected--;

            //remove user from chat list
            messageStreams.remove(oos);
            connectedUsers.setText(String.format("%d", connected));
            try{
                s.close();
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }

        }//end run()
    }//end InnerThread
}//end CoronosServer
