import javax.swing.*;    //for JFrame, JButton
import java.awt.*;       //for FlowLayout
import java.awt.event.*; //for ActionEvent, ActionListener
import java.io.*;        //for File IO
import java.util.*;      //For Timer
import java.text.*;      //For Formatting
import javax.swing.border.Border;
//Importing necessary modules for program.

/**
 * TimerFun: A class that is terrible and I hope nobody ever has to do again and was not Fun. Timers are okay, I guess.
 * Course ISTE121-01
 * Assignment: HW04
 * @version 02122020
 * @author Connor Stevens
 * 
 */
public class Login implements ActionListener{
    JFrame loginFrame;
    JPanel userName;
    JTextField userNameField;
    JLabel userNameLabel;

    JPanel passWord;
    JPasswordField passWordField;
    JLabel passWordLabel;

    JPanel options;
    JButton loginButton;
    JButton showPassword;
    Boolean isRevealed = false;
    
    public Login(){
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

    }


    public void actionPerformed(ActionEvent ae){
        String actionString = ae.getActionCommand();
        System.out.println(actionString);

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
    }

    public static void main(String[] args){
        Login loginWindow = new Login();
    }
}