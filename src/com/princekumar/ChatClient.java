package com.princekumar;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    //create objects of componets to be used in the application
    static JFrame     chatWindow = new JFrame("Chat Application"); //window
    static JTextArea  chatArea   = new JTextArea(22,40);  //to display text, that area
    static JTextField textField  = new JTextField(40);          //typing area
    static JLabel     blankLabel = new JLabel("          ");
    static JButton    sendButton = new JButton("Send");            //SEND button

    static BufferedReader in;
    static PrintWriter out;

    public ChatClient() {
        //define how components will be arranged onto the Frame(window)
        chatWindow.setLayout(new FlowLayout());


        //add components in the window
        chatWindow.add(new JScrollPane(chatArea));                 //adds a scroller for the chat area
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the window, when cross clicked.
        chatWindow.setSize(500,475);
        chatWindow.setVisible(true);                               //else, window won't be visible

        chatArea.setEditable(false); //as we don't want the user to type anything directly on the chat area.
        textField.setEditable(false); //don't want the user to type something unless the connection is established with the server


    }

    void startChat() throws Exception{

        //Will return the value which the user will enter into his text field.
        String ipAddress = JOptionPane.showInputDialog(chatWindow,
                "Enter Ip Address: ",
                "IP Address Required!!",
                JOptionPane.PLAIN_MESSAGE
        );

        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream());

        while(true){

        }

    }


    public static void main(String[] args) throws Exception{
        ChatClient client = new ChatClient();
        client.startChat();
    }
}
