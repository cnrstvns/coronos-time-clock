import javax.swing.*;    //for JFrame, JButton
import java.awt.*;       //for FlowLayout
import java.awt.event.*; //for ActionEvent, ActionListener
import java.io.*;        //for File IO
import java.util.*;      //For Timer
import java.text.*;      //For Formatting
import javax.swing.border.Border;


public class ClientGUI {
    JFrame jfFrame;
    JMenuBar jmMenuBar;
    JMenu jmFile;
    JMenu jmHelp;
    JMenuItem jmExit;
    JMenuItem jmAbout;
    JLabel clockLabel;
    JPanel clockPanel;
    javax.swing.Timer clockTimer;
    Font customFont;


    public ClientGUI() {
        fontLoader();

        jfFrame = new JFrame("Coronos");
        jfFrame.setLayout(new GridLayout(2, 1));
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

        jfFrame.add(jmMenuBar);
        //adding JMenuBar to JFrame

        clockPanel = new JPanel(new FlowLayout());
        Border blackline = BorderFactory.createTitledBorder("Current Time");
        clockPanel.setBorder(blackline);
        //instantiate JPanel for clock

        clockLabel = new JLabel();
        clockLabel.setFont(customFont);
        clockLabel.setForeground(Color.BLACK);
        clockPanel.add(clockLabel);
        //instantiating JLabel, setting Font and Color, adding to clockPanel

        jfFrame.add(clockPanel);
        //adding clockPanel to JFrame

        //adding JPanels to JFrame

        jfFrame.setLocationRelativeTo(null);
        jfFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfFrame.setVisible(true);
        jfFrame.pack();
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

    /**
     * main: A method that instantiates a new TimerFun object
     */
    public static void main(String[] args){ 
        TimerFun tf = new TimerFun();
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