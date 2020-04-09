import javax.swing.*;    //for JFrame, JButton
import java.awt.*;       //for FlowLayout
import java.awt.event.*; //for ActionEvent, ActionListener
import java.io.*;        //for File IO
import java.util.*;      //For Timer
import java.text.*;      //For Formatting
import javax.swing.border.Border;
import java.net.*;
//Importing necessary modules for program.

/**
 * TimerFun: A class that is terrible and I hope nobody ever has to do again and was not Fun. Timers are okay, I guess.
 * Course ISTE121-01
 * Assignment: HW04
 * @version 02122020
 * @author Connor Stevens
 * 
 */
public class ClientLogin implements ActionListener{
    JLabel userNameLabel, passWordLabel;
    JPanel userName, passWord, options;
    JButton loginButton, showPassword;
    JPasswordField passWordField;
    Boolean isRevealed = false;
    JTextField userNameField;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    JFrame loginFrame;
    Boolean allow;
    String reason;
    
    public ClientLogin(){
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

        try {
            Socket s = new Socket(InetAddress.getLocalHost(), 16789);
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
            } catch(IOException ioe){
                ioe.printStackTrace();
            }
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

                    if(allow) {
                        System.out.println("[AUTH] - Successful Login - Authentication Successful!");
                        loginFrame.setVisible(false);
                        ClientGUI cg = new ClientGUI();
                    } else {
                        System.out.println("[AUTH] - Failed Login - Authentication Failed!");
                        JOptionPane.showMessageDialog(loginButton, reason, "Login Error", JOptionPane.WARNING_MESSAGE);
                    }
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
        ClientLogin loginWindow = new ClientLogin();
    }
}