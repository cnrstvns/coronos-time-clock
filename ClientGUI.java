import javax.swing.*;    //for JFrame, JButton
import java.awt.*;       //for FlowLayout
import java.awt.event.*; //for ActionEvent, ActionListener
import java.io.*;        //for File IO
import java.net.*;
import java.util.*;      //For Timer
import java.text.*;      //For Formatting
import javax.swing.border.Border;


public class ClientGUI implements ActionListener{
    JFrame jfFrame;
    JMenu menu;
    JMenuBar jmMenuBar;
    JMenu jmFile;
    JMenu jmHelp;
    JTextArea jtaWindow;
    JMenuItem jmExit;
    JMenuItem jmAbout;
    JLabel clockLabel;
    JPanel clockPanel;
    javax.swing.Timer clockTimer;
    Font customFont;
    private String username;
    private JPanel chatPanel;
    private JPanel actionPanel;
    private JPanel gridPanel1;
    private JPanel gridPanel2;

    private JButton one;
    private JButton two;
    private JButton three;
    private JButton four;
    private JButton five;
    
    private JButton six;
    private JButton seven;
    private JButton eight;
    private JButton nine;
    private JButton ten;
    
    private JTextArea chatArea;
    private JTextField chatField;
    private JButton chatButton;
    private JPanel areaPanel;
    private JPanel sendPanel;

    private JPanel containerPanel;
    private JScrollPane chatPane;

    public ClientGUI(String username) {

        fontLoader();

        this.username = username;

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

        gridPanel1 = new JPanel(new GridLayout(5, 1));
        gridPanel2 = new JPanel(new GridLayout(5, 1));

        one = new JButton("test");
        two = new JButton("test");
        three = new JButton("test");
        four = new JButton("test");
        five = new JButton("test");

        six = new JButton("test");
        seven = new JButton("test");
        eight = new JButton("test");
        nine = new JButton("test");
        ten = new JButton("test");

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
        chatArea = new JTextArea("What");
        chatPane = new JScrollPane(chatArea);
        chatField = new JTextField(12);
        chatButton = new JButton("Send");
        sendPanel = new JPanel(new FlowLayout());
        sendPanel.add(chatField);
        sendPanel.add(chatButton);
        Border chatBorder = BorderFactory.createTitledBorder("Chat Window");
        chatPanel.setBorder(chatBorder);
        chatPanel = new JPanel(new FlowLayout());
        areaPanel.add(chatPane);
        chatPanel.add(areaPanel, BorderLayout.NORTH);
        chatPanel.add(sendPanel, BorderLayout.SOUTH);
        containerPanel.add(chatPanel);

        jfFrame.add(clockPanel, BorderLayout.NORTH);
        jfFrame.add(containerPanel, BorderLayout.SOUTH);
        //jfFrame.add(actionPanel, BorderLayout.WEST);
        //jfFrame.add(chatPanel, BorderLayout.EAST);
        //adding clockPanel to JFrame

        //adding JPanels to JFrame

        jfFrame.setLocationRelativeTo(null);
        jfFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfFrame.setVisible(true);
        jfFrame.pack();
		jfFrame.setSize(500, 200);
        
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
        System.out.println(actionString);
    }
	
	public static void main(String[] args){
		ClientGUI cg = new ClientGUI("Demo");
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