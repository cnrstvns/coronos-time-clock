import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.border.Border;
import java.awt.event.*;
import java.util.Timer;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.awt.*;
import java.io.*;


public class CoronosClient implements ActionListener {
    String[] states = {"", "Alabama", "Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia",
            "Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire", "New Jersey", "New Mexico",
            "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
    private JPanel jpUserName, jpPassWord, jpOptions, jpChatPanel, jpActionPanel, jpGridPanel1, jpGridPanel2, jpSendPanel, jpClockPanel, jpContainerPanel, jpAreaPanel;
    private JButton loginButton, showPassword, chatButton, jbReport, jbProfile, jbSave, jbPunchOut, jbViewPunches, jbFormatTime, jbPunchIn, jbHideChat;
    private JLabel userNameLabel, passWordLabel, clockLabelOne,clockLabelTwo;
    private javax.swing.Timer clockTimerOne, clockTimerTwo;
    private SimpleDateFormat hoursFormat = new SimpleDateFormat("hh:mm:ss");
    private JPasswordField passWordField;
    private Boolean isRevealed = false;
    private Boolean clockState = true;
    private JMenu menu, jmFile, jmHelp;
    private JFrame loginFrame, jfFrame;
    private JMenuItem jmExit, jmAbout;
    private JTextField jtfUserNameField;
    private String reason, username;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JTextField jtfChatField;
    private JScrollPane jspChatPane;
    private JTextArea jtaChatArea;
    private JMenuBar jmMenuBar;
    private Employee employee;
    private Font customFont;
    private Boolean allow;
    private Boolean saved;
    private Socket s;

    public CoronosClient(){
        loginBuilder();
        uiBuilder();

        try {
            s = new Socket("localhost", 16789);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            serverListener();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (BindException be) {
            be.printStackTrace();
        } catch(ConnectException ce){
            JOptionPane.showMessageDialog(loginButton, "Failed to connect to server, please contact your System Administrator", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loginBuilder(){
        loginFrame = new JFrame("Coronos Login");
        loginFrame.setLayout(new GridLayout(3, 1));

        jpUserName = new JPanel(new FlowLayout());
        jtfUserNameField = new JTextField(10);
        userNameLabel = new JLabel("Username:");
        userNameLabel.setLabelFor(jtfUserNameField);
        jpUserName.add(userNameLabel);
        jpUserName.add(jtfUserNameField);
        loginFrame.add(jpUserName);
        
        jpPassWord = new JPanel(new FlowLayout());
        passWordField = new JPasswordField(10);
        passWordLabel = new JLabel("Password:");
        passWordField.addActionListener(this);
        passWordField.setActionCommand("Login");
        passWordLabel.setLabelFor(passWordField);
        jpPassWord.add(passWordLabel);
        jpPassWord.add(passWordField);
        loginFrame.add(jpPassWord);
        
        jpOptions = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        showPassword = new JButton("Reveal Password");
        showPassword.addActionListener(this);
        jpOptions.add(loginButton);
        jpOptions.add(showPassword);
        loginFrame.add(jpOptions);
        
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.pack();
    }

    public void uiBuilder(){
        fontLoader();

        jfFrame = new JFrame(String.format("Coronos - %s", username));
        jfFrame.setLayout(new BorderLayout());
        //Instantiate JFrame, add Title, add LayoutManager GridLayout


        jmMenuBar = new JMenuBar();
        //Instantiate JMenuBar

        jmFile = new JMenu("File");
        jmHelp = new JMenu("Help");
        //Adding Menus to JMenuBar

        jmExit = new JMenuItem("Exit");
        jmAbout = new JMenuItem("About");
        //Instantiating Options for JMenus

        jmExit.addActionListener(this);
        jmAbout.addActionListener(this);

        jmFile.add(jmExit);
        jmHelp.add(jmAbout);
        //adding Options to JMenus

        jmMenuBar.add(jmFile);
        jmMenuBar.add(jmHelp);
        //adding JMenus to JMenuBar

        jfFrame.setJMenuBar(jmMenuBar);
        //adding JMenuBar to JFrame

        jpClockPanel = new JPanel(new FlowLayout());
        jpContainerPanel = new JPanel(new FlowLayout());
        Border blackLine = BorderFactory.createTitledBorder("Current Time");
        jpClockPanel.setBorder(blackLine);
        //instantiate JPanel for clock

        clockLabelOne = new JLabel();
        clockLabelOne.setFont(customFont);
        clockLabelOne.setForeground(Color.BLACK);
        jpClockPanel.add(clockLabelOne);
        //instantiating JLabel, setting Font and Color, adding to clockPanel

        clockLabelTwo = new JLabel();
        clockLabelTwo.setFont(customFont);
        clockLabelTwo.setForeground(Color.BLACK);
        jpClockPanel.add(clockLabelTwo);
        clockLabelTwo.setVisible(false);
        //instantiating JLabel, setting Font and Color, adding to clockPanel

        jpGridPanel1 = new JPanel(new GridLayout(4, 1, 15, 15));
        jpGridPanel2 = new JPanel(new GridLayout(4, 1, 15, 15));

        jbPunchIn = new JButton("Punch In");
        jbReport = new JButton("View Report");
        jbProfile = new JButton("View Profile");
        jbSave = new JButton("Save");
        jbPunchOut = new JButton("Punch Out");
        jbViewPunches = new JButton("View Punches");
        jbFormatTime = new JButton("12/24HR Time");
        jbHideChat = new JButton("Hide Chat");

        jbHideChat.setToolTipText("Hate your co-workers? Want to hide from your boss? Just close chat!");

        jbPunchIn.addActionListener(this);
        jbReport.addActionListener(this);
        jbProfile.addActionListener(this);
        jbSave.addActionListener(this);
        jbPunchOut.addActionListener(this);
        jbViewPunches.addActionListener(this);
        jbFormatTime.addActionListener(this);
        jbHideChat.addActionListener(this);

        jpGridPanel1.add(jbPunchIn);
        jpGridPanel1.add(jbReport);
        jpGridPanel1.add(jbProfile);
        jpGridPanel1.add(jbSave);

        jpGridPanel2.add(jbPunchOut);
        jpGridPanel2.add(jbViewPunches);
        jpGridPanel2.add(jbFormatTime);
        jpGridPanel2.add(jbHideChat);

        jpActionPanel = new JPanel(new FlowLayout());
        Border actionBorder = BorderFactory.createTitledBorder("Employee Actions");
        jpActionPanel.setBorder(actionBorder);
        jpActionPanel.add(jpGridPanel1);
        jpActionPanel.add(jpGridPanel2);
        jpContainerPanel.add(jpActionPanel);

        jpChatPanel = new JPanel(new BorderLayout());
        jpAreaPanel = new JPanel(new FlowLayout());
        jtaChatArea = new JTextArea(10, 10);
        jtaChatArea.setLineWrap(true);
        jtaChatArea.setWrapStyleWord(true);
        jtaChatArea.setEditable(false);
        jspChatPane = new JScrollPane(jtaChatArea);
        jtfChatField = new JTextField(12);
        jtfChatField.addActionListener(this);
        jtfChatField.setActionCommand("Send");
        chatButton = new JButton("Send");
        chatButton.addActionListener(this);
        jpSendPanel = new JPanel(new FlowLayout());
        jpSendPanel.add(jtfChatField);
        jpSendPanel.add(chatButton);
        Border chatBorder = BorderFactory.createTitledBorder("Chat");
        jpChatPanel.setBorder(chatBorder);
        jpChatPanel.add(jspChatPane, BorderLayout.NORTH);
        jpChatPanel.add(jpSendPanel, BorderLayout.SOUTH);
        jpContainerPanel.add(jpChatPanel);

       // jfFrame.add(clockPanelTwo, BorderLayout.NORTH);
        jfFrame.add(jpClockPanel, BorderLayout.NORTH);
        jfFrame.add(jpContainerPanel, BorderLayout.SOUTH);

        //adding JPanels to JFrame

        jfFrame.setLocationRelativeTo(null);
        jfFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfFrame.setVisible(false);
        jfFrame.pack();
        jfFrame.setSize(600, 375);
        jfFrame.setResizable(false);
        
        //settings for frame

        ActionListener clockUpdateOne = new ActionListener(){
            /**
             * actionPerformed: A method to handle Events from a Timer
             * @param ae an ActionEvent
             */
            public void actionPerformed(ActionEvent ae){
                Date date = new Date();
                //instantiate new Date object

                DateFormat format = new SimpleDateFormat("E, MMM d y HH:mm:ss");
                //set format of clock

                DateFormat otherFormat = new SimpleDateFormat("E, MMM, d y KK:mm:ss a");
                //12 hour time

                String dateTime = format.format(date);
                //formatting date object using format template

                String otherDateTime = otherFormat.format(date);

                clockLabelOne.setText(dateTime);
                //setting clock text to formatted String
            }
        };
        clockTimerOne = new javax.swing.Timer(0, clockUpdateOne);
        clockTimerOne.start();
        //timer to update clockLabel
        ActionListener clockUpdateTwo = new ActionListener(){
            /**
             * actionPerformed: A method to handle Events from a Timer
             * @param ae an ActionEvent
             */
            public void actionPerformed(ActionEvent ae){
                Date date = new Date();
                //instantiate new Date object

                DateFormat otherFormat = new SimpleDateFormat("E, MMM, d y K:mm:ss a");
                //12 hour time

                String otherDateTime = otherFormat.format(date);

                clockLabelTwo.setText(otherDateTime);
                //setting clock text to formatted String
            }
        };
        clockTimerTwo = new javax.swing.Timer(0, clockUpdateTwo);
        clockTimerTwo.start();

    }

    public void actionPerformed(ActionEvent ae){
        String actionString = ae.getActionCommand();
        System.out.println("[CLIENT] - Action - " + actionString);

        if(actionString.equals("Reveal Password")){
            char[] pass = passWordField.getPassword();
            String password = new String(pass);
            passWordField.setEchoChar((char) 0);
            passWordField.setText(password);
            showPassword.setText("Hide Password");
        }
        else if(actionString.equals("Hide Password")){
            passWordField.setEchoChar('*');
            showPassword.setText("Reveal Password");
        }
        else if(actionString.equals("Login")){
            char[] pass = passWordField.getPassword();
            String password = new String(pass);
            CoronosAuth auth = new CoronosAuth(jtfUserNameField.getText(), password);
            try{
                oos.writeObject(auth);
                oos.flush();
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        else if(actionString.equals("Send")){
            if(jtfChatField.getText().equals("")){
                return;
            }
            else{
                Message msg = new Message(String.format("%s: %s\n", username, jtfChatField.getText()));
                jtfChatField.setText("");
                try{
                    oos.writeObject(msg);
                    oos.flush();
                }
                catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        else if(actionString.equals("Hide Chat")){
            jbHideChat.setText("Show Chat");
            jpContainerPanel.remove(jpChatPanel);
            jpContainerPanel.revalidate();
            jfFrame.repaint();
            jbHideChat.setToolTipText("Want to harass your boss? Enable chat!");
        }
        else if(actionString.equals("Show Chat")){
            jbHideChat.setText("Hide Chat");
            jpContainerPanel.add(jpChatPanel);
            jpContainerPanel.revalidate();
            jfFrame.repaint();
            jbHideChat.setToolTipText("Hate your co-workers? Want to hide from your boss? Just close chat!");
        }
        else if(actionString.equals("View Punches")){
            JSONArray punches = employee.getClockTimes();
            WageFrame wf = new WageFrame(punches);
            wf.setLocationRelativeTo(null);
        }
        else if(actionString.equals("View Profile")){
            JFrame editUser = new JFrame("Coronos - View/Edit Profile");
            editUser.setLayout(new GridLayout(5, 1));

            JSONObject data = employee.getEmployeeData();

            String firstName = (String) data.get("firstName");
            String lastName = (String) data.get("lastName");
            String hourlyPay = Double.toString((double) data.get("wage"));
            String role = (String) data.get("role");
            String address = (String) data.get("address");
            String zip = (String) data.get("zip");
            String state = (String) data.get("state");
            String city = (String) data.get("city");

            JPanel centerPanel = new JPanel(new FlowLayout());
            JPanel bottomPanel = new JPanel(new FlowLayout());
            JPanel panelfour = new JPanel(new FlowLayout());
            JPanel panelfive = new JPanel(new FlowLayout());
            JPanel panelsix = new JPanel(new FlowLayout());

            JLabel wageLabel = new JLabel("Hourly Pay:");
            JTextField wageField = new JTextField(hourlyPay, 10);
            JLabel roleLabel = new JLabel("Role:");
            JTextField roleField = new JTextField(role,10);

            wageField.setEditable(false);
            roleField.setEditable(false);

            JLabel firstNameLabel = new JLabel("First Name:");
            JLabel lastNameLabel = new JLabel("Last Name:");
            JTextField firstNameField = new JTextField(firstName, 10);
            JTextField lastNameField = new JTextField(lastName, 10);

            firstNameField.setEditable(false);
            lastNameField.setEditable(false);

            JLabel addressLabel = new JLabel("Street Address:");
            JTextField addressField = new JTextField(address, 10);

            JLabel cityLabel = new JLabel("City:");
            JTextField cityField = new JTextField(city,10);

            JLabel zipLabel = new JLabel("Zip Code");
            JTextField zipField = new JTextField(zip,10);

            JLabel statesLabel = new JLabel("State:");
            JComboBox statesBox = new JComboBox(states);
            int i = java.util.Arrays.asList(states).indexOf(state);
            statesBox.setSelectedIndex(i);

            JButton saveButton = new JButton("Save");

            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String actionString = ae.getActionCommand();
                    if(actionString.equals("Save")){
                        String address = addressField.getText();
                        String zip = zipField.getText();
                        int selected = statesBox.getSelectedIndex();
                        String selectedState = states[selected];
                        String city = cityField.getText();

                        if(address.equals("") || selectedState.equals("") || city.equals("") || zip.equals("")){
                            JOptionPane.showMessageDialog(null, "Empty Field Detected, ensure all fields are filled.", "User Update Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            try{
                                oos.writeObject(employee);
                                JOptionPane.showMessageDialog(null, "Saved Data to Server.", "Employee Data", JOptionPane.INFORMATION_MESSAGE);
                            }
                            catch(IOException ioe){
                                JOptionPane.showMessageDialog(null, "Failed to send data to server.", "Server Error", JOptionPane.ERROR_MESSAGE);
                            }

                            editUser.setVisible(false);
                        }
                    }
                }
            });

            centerPanel.add(wageLabel);
            centerPanel.add(wageField);
            centerPanel.add(roleLabel);
            centerPanel.add(roleField);

            bottomPanel.add(saveButton);

            panelfour.add(firstNameLabel);
            panelfour.add(firstNameField);
            panelfour.add(lastNameLabel);
            panelfour.add(lastNameField);

            panelfive.add(addressLabel);
            panelfive.add(addressField);

            panelfive.add(cityLabel);
            panelfive.add(cityField);

            panelsix.add(statesLabel);
            panelsix.add(statesBox);

            panelsix.add(zipLabel);
            panelsix.add(zipField);


            editUser.add(panelfour);
            editUser.add(centerPanel);
            editUser.add(panelfive);
            editUser.add(panelsix);
            editUser.add(bottomPanel);
            editUser.setLocationRelativeTo(null);
            editUser.setVisible(true);
            editUser.pack();

        }
        else if(actionString.equals("View Report")){
            JSONArray punches = employee.getClockTimes();
            double total = 0.0;
            int length = punches.size();
            for(Object data : punches){
                JSONObject jsonData = (JSONObject) data;
                try{
                    double payForPeriod = (double) jsonData.get("pay");
                    total += payForPeriod;
                }
                catch(NullPointerException npe){
                    break;
                }
            }
            String dataString = String.format("You have worked %d shifts for a total of $%.2f pay.", length, total);
            JOptionPane.showMessageDialog(null, dataString, "Employee Report", JOptionPane.INFORMATION_MESSAGE);

        }
        else if(actionString.equals("Punch In")){
            if(employee.getClockedIn()){
                JOptionPane.showMessageDialog(null, "You are already clocked in.");
            }
            else{
                punchIn();
                saved = false;
            }
        }
        else if(actionString.equals("Punch Out")){
            if(!employee.getClockedIn()){
                JOptionPane.showMessageDialog(null, "You are already clocked out.");
            }
            else{
                punchOut();
                saved = false;
            }
        }
        else if(actionString.equals("Save")){
            try{
                oos.writeObject((Object) employee);
                oos.flush();
                saved = true;
            }
            catch(IOException ioe){
                System.err.println("Error saving employee data.");
            }
        }
        else if(actionString.equals("12/24HR Time")){
            if (clockState){
                clockLabelOne.setVisible(false);
                clockLabelTwo.setVisible(true);
                clockState = false;
            }
            else{
                clockLabelOne.setVisible(true);
                clockLabelTwo.setVisible(false);
                clockState = true;
            }

        }
        else if(actionString.equals("Exit")){
            TimerTask shutDown = new TimerTask() {
                @Override
                public void run() {
                    if(saved){
                        System.exit(0);
                    }
                    else{
                        int n = JOptionPane.showConfirmDialog(null, "You have not saved your punches. Do you want to save before exiting?", "Unsaved Punches", JOptionPane.YES_NO_OPTION);
                        if(n == 0){
                            System.out.println("User Chose 0");
                            try{
                                oos.writeObject((Object) employee);
                                oos.flush();
                                System.out.println("[EXIT] - [Shutdown] - Shutting down. Save complete.");
                            }
                            catch(IOException ioe){
                                System.err.println("[EXIT] - [Save] - Failed to Write to Server");
                            }
                        }
                        else{
                            System.out.println("[EXIT] - [Shutdown] - Shutting down without saving.");
                            System.exit(0);
                        }
                    }
                }
            };
            Timer timer = new java.util.Timer();
            timer.schedule(shutDown, 500);
        }
        else if(actionString.equals("About")){
            JOptionPane.showMessageDialog(null, "Coronos Time Clock © 2020\nAll Rights Reserved\n\nAuthors:\nConnor Stevens\nEverett Simone\nDalton Kruppenbacher\nBrian Zhu", "About Coronos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void serverListener() {
        while(true){
            try{
                Object obj = ois.readObject();

                if(obj instanceof CoronosAuth){
                    System.out.println("[IO] - [Server] - Received CoronosAuth Object");
                    CoronosAuth returned = (CoronosAuth) obj;

                    allow = returned.getAllow();
                    reason = returned.getReason();
                    username = returned.getUsername();

                    if(allow) {
                        System.out.println("[AUTH] - Successful Login - Authentication Successful!");
                        loginFrame.setVisible(false);
                        String username = jtfUserNameField.getText();
                        jfFrame.setTitle(String.format("Coronos Client - %s", username));
                        jfFrame.setVisible(true);
                        Message message = new Message(String.format("%s has joined\n", username));
                        oos.writeObject((Object) message);
                        //set clientGUI to visible

                    } else {
                        System.out.println("[AUTH] - Failed Login - Authentication Failed!");
                        JOptionPane.showMessageDialog(loginButton, reason, "Login Error", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if(obj instanceof Message){
                    System.out.println("[IO] - [Server] - Received Message Object");
                    Message message = (Message) obj;
                    jtaChatArea.append(String.format("%s", message.toString()));
                }

                else if(obj instanceof Employee){
                    System.out.println("[IO] - [Server] - Received Employee Object");
                    this.employee = (Employee) obj;

                }
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }
            catch(ClassNotFoundException cnfe){
                cnfe.printStackTrace();
            }
            catch(NullPointerException npe){
                npe.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        CoronosClient c = new CoronosClient();
    }

    public void fontLoader(){
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(".\\resources\\DS-DIGI.ttf")).deriveFont(40f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void punchIn(){
        Date currentDate = new Date();
        String currentTime = hoursFormat.format(currentDate);
        String timeString = String.format("You have clocked in.\nThe current time is %s", currentTime);
        JOptionPane.showMessageDialog(null, timeString, "Coronos Time Clock", JOptionPane.INFORMATION_MESSAGE);
        employee.setClockedIn(true);
        JSONArray clockTimes = employee.getClockTimes();
        Date date = new Date();
        long now = date.getTime();
        JSONObject newPunch = new JSONObject();
        newPunch.put("in", now);
        clockTimes.add(newPunch);
        employee.setPunches(clockTimes);
    }

    public void punchOut(){
        Date currentDate = new Date();
        String currentTime = hoursFormat.format(currentDate);
        employee.setClockedIn(false);
        JSONArray clockTimes = employee.getClockTimes();
        Date date = new Date();
        long now = date.getTime();
        int index = clockTimes.size() -1;
        JSONObject newPunch = (JSONObject) clockTimes.get(index);
        long first = (long) newPunch.get("in");
        double pay = employee.calculateForPeriod(first, now);
        newPunch.put("out", now);
        newPunch.put("pay", pay);
        clockTimes.set(index, newPunch);
        employee.setPunches(clockTimes);
        String timeString = String.format("You have clocked out.\nThe current time is %s.\nYou have been paid $%.2f", currentTime, pay);
        JOptionPane.showMessageDialog(null, timeString, "Coronos Time Clock", JOptionPane.INFORMATION_MESSAGE);
    }
}