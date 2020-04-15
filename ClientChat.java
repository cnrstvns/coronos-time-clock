import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * Course: ISTE121
 * Instructor: David Patric
 * ISTE 121.01
 * Lab07: Chat Client
 * @author Connor Stevens
 * @version 04012020
 */


public class ClientChat extends JFrame implements ActionListener {

    //GUI Attributes
    private JTextField jtfInput;
    private JTextArea jtaWindow;
    
    private Socket s;
    private String username;


    /**
     * ChatClient: Constructor
     * @param ipAddress, target server IP Address
     * @param userName, client's username
     */
    public ClientChat(Socket s, String username) {
        //Constructor Attributes
        System.out.println("Creating Chat.");
        this.s = s;
        this.username = username;

        jtfInput = new JTextField();
        add(jtfInput, BorderLayout.SOUTH);

        jtaWindow = new JTextArea();
        jtaWindow.setEditable(false);
        add(new JScrollPane(jtaWindow), BorderLayout.CENTER);

        setTitle(String.format("Coronos Chat - Logged in as %s", username));
        setSize(450, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent ae){
        String actionString = ae.getActionCommand();
        System.out.println(actionString);
        String msg = String.format("%s: %s", username, jtfInput.getText());
        Message message = new Message(msg);
    }

}

