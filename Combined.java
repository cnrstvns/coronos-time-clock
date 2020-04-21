import javax.swing.*;    //for JFrame, JButton
import java.awt.*;       //for FlowLayout
import java.awt.event.*; //for ActionEvent, ActionListener
import java.io.*;        //for File IO
import java.util.*;      //For Timer
import java.text.*;      //For Formatting
import javax.swing.border.Border;
import java.net.*;

public class Combined implements ActionListener {
    private JPanel userName, passWord, options, chatPanel, actionPanel, gridPanel1, gridPanel2, sendPanel, clockPanel, containerPanel, areaPanel;
    private JButton loginButton, showPassword, chatButton, one, two, three, four, five, six, seven, eight, nine, ten;
    private JLabel userNameLabel, passWordLabel, clockLabel;
    private JPasswordField passWordField;
    private javax.swing.Timer clockTimer;
    private Boolean isRevealed = false;
    private JMenu menu, jmFile, jmHelp;
    private JFrame loginFrame, jfFrame;
    private JMenuItem jmExit, jmAbout;
    private JTextField userNameField;
    private String reason, username;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JTextField chatField;
    private JScrollPane chatPane;
    private JTextArea jtaWindow;
    private JTextArea chatArea;
    private JMenuBar jmMenuBar;
    private Font customFont;
    private Boolean allow;
    private Socket s;

    public Combined(){
        loginBuilder();
        uiBuilder();

        try {
            s = new Socket(InetAddress.getLocalHost(), 16789);
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

        userName = new JPanel(new FlowLayout());
        userNameField = new JTextField(10);
        userNameLabel = new JLabel("Username:");
        userNameLabel.setLabelFor(userNameField);
        userName.add(userNameLabel);
        userName.add(userNameField);
        loginFrame.add(userName);
        
        passWord = new JPanel(new FlowLayout());
        passWordField = new JPasswordField(10);
        passWordLabel = new JLabel("Password:");
        passWordField.addActionListener(this);
        passWordField.setActionCommand("Login");
        passWordLabel.setLabelFor(passWordField);
        passWord.add(passWordLabel);
        passWord.add(passWordField);
        loginFrame.add(passWord);
        
        options = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        showPassword = new JButton("Reveal Password");
        showPassword.addActionListener(this);
        options.add(loginButton);
        options.add(showPassword);
        loginFrame.add(options);
        
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

        jmFile.add(jmExit);
        jmHelp.add(jmAbout);
        //adding Options to JMenus

        jmMenuBar.add(jmFile);
        jmMenuBar.add(jmHelp);
        //adding JMenus to JMenuBar

        jfFrame.setJMenuBar(jmMenuBar);
        //adding JMenuBar to JFrame

        clockPanel = new JPanel(new FlowLayout());
        containerPanel = new JPanel(new FlowLayout());
        Border blackline = BorderFactory.createTitledBorder("Current Time");
        clockPanel.setBorder(blackline);
        //instantiate JPanel for clock

        clockLabel = new JLabel();
        clockLabel.setFont(customFont);
        clockLabel.setForeground(Color.BLACK);
        clockPanel.add(clockLabel);
        //instantiating JLabel, setting Font and Color, adding to clockPanel\

        gridPanel1 = new JPanel(new GridLayout(5, 1, 15, 15));
        gridPanel2 = new JPanel(new GridLayout(5, 1, 15, 15));

        one = new JButton("Punch In");
        one.addActionListener(this);
        two = new JButton("View Report");
        two.addActionListener(this);
        three = new JButton("Current Period");
        three.addActionListener(this);
        four = new JButton("test");
        four.addActionListener(this);
        five = new JButton("test");
        five.addActionListener(this);

        six = new JButton("Punch Out");
        six.addActionListener(this);
        seven = new JButton("View Punches");
        seven.addActionListener(this);
        eight = new JButton("View Paystub");
        eight.addActionListener(this);
        nine = new JButton("test");
        nine.addActionListener(this);
        ten = new JButton("Hide Chat");
        ten.addActionListener(this);
        ten.setToolTipText("Hate your co-workers? Want to hide from your boss? Just close chat!");

        gridPanel1.add(one);
        gridPanel1.add(two);
        gridPanel1.add(three);
        gridPanel1.add(four);
        gridPanel1.add(five);

        gridPanel2.add(six);
        gridPanel2.add(seven);
        gridPanel2.add(eight);
        gridPanel2.add(nine);
        gridPanel2.add(ten);

        actionPanel = new JPanel(new FlowLayout());
        Border actionBorder = BorderFactory.createTitledBorder("Employee Actions");
        actionPanel.setBorder(actionBorder);
        actionPanel.add(gridPanel1);
        actionPanel.add(gridPanel2);
        containerPanel.add(actionPanel);

        chatPanel = new JPanel(new BorderLayout());
        areaPanel = new JPanel(new FlowLayout());
        chatArea = new JTextArea(10, 10);
        chatArea.setEditable(false);
        chatPane = new JScrollPane(chatArea);
        chatField = new JTextField(12);
        chatButton = new JButton("Send");
        chatButton.addActionListener(this);
        sendPanel = new JPanel(new FlowLayout());
        sendPanel.add(chatField);
        sendPanel.add(chatButton);
        Border chatBorder = BorderFactory.createTitledBorder("Chat");
        chatPanel.setBorder(chatBorder);
        chatPanel.add(chatPane, BorderLayout.NORTH);
        chatPanel.add(sendPanel, BorderLayout.SOUTH);
        containerPanel.add(chatPanel);

        jfFrame.add(clockPanel, BorderLayout.NORTH);
        jfFrame.add(containerPanel, BorderLayout.SOUTH);

        //adding JPanels to JFrame

        jfFrame.setLocationRelativeTo(null);
        jfFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfFrame.setVisible(false);
        jfFrame.pack();
        jfFrame.setSize(600, 375);
        jfFrame.setResizable(false);
        
        //settings for frame

        ActionListener clockUpdate = new ActionListener(){
            /**
             * actionPerformed: A method to handle Events from a Timer
             * @param ae an ActionEvent
             */
            public void actionPerformed(ActionEvent ae){
                Date date = new Date();
                //instantiate new Date object

                DateFormat format = new SimpleDateFormat("E, MMM d y HH:mm:ss");
                //set format of clock

                String dateTime = format.format(date);
                //formatting date object using format template

                clockLabel.setText(dateTime);
                //setting clock text to formatted String
            }
        };
        clockTimer = new javax.swing.Timer(0, clockUpdate);
        clockTimer.start();
        //timer to update clockLabel
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
            CoronosAuth auth = new CoronosAuth(userNameField.getText(), password);
            try{
                oos.writeObject(auth);
                oos.flush();
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        else if(actionString.equals("Send")){
            if(chatField.getText().equals("")){
                return;
            }
            else{
                Message msg = new Message(String.format("%s: %s\n", username, chatField.getText()));
                chatField.setText("");
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
            ten.setText("Show Chat");
            containerPanel.remove(chatPanel);
            containerPanel.revalidate();
            jfFrame.repaint();
            ten.setToolTipText("Want to harass your boss? Enable chat!");
        }
        else if(actionString.equals("Show Chat")){
            ten.setText("Hide Chat");
            containerPanel.add(chatPanel);
            containerPanel.revalidate();
            jfFrame.repaint();
            ten.setToolTipText("Hate your co-workers? Want to hide from your boss? Just close chat!");
        }
    }

    public void serverListener() {
        while(true){
            try{
                Object obj = ois.readObject();

                if(obj instanceof CoronosAuth){
                    CoronosAuth returned = (CoronosAuth) obj;

                    allow = returned.getAllow();
                    reason = returned.getReason();
                    username = returned.getUsername();

                    if(allow) {
                        System.out.println("[AUTH] - Successful Login - Authentication Successful!");
                        loginFrame.setVisible(false);
                        String username = userNameField.getText();
                        jfFrame.setTitle(String.format("Coronos Client - %s", username));
                        jfFrame.setVisible(true);
                        //set clientGUI to visible

                    } else {
                        System.out.println("[AUTH] - Failed Login - Authentication Failed!");
                        JOptionPane.showMessageDialog(loginButton, reason, "Login Error", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if(obj instanceof Message){
                    Message message = (Message) obj;
                    chatArea.append(String.format("%s", message.toString()));
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
        Combined c = new Combined();
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
}