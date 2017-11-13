package com.princekumar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    //create objects of componets to be used in the application
    static JFrame     chatWindow = new JFrame("Chat Application"); //window
    static JTextArea  chatArea   = new JTextArea(22,40);  //to display text, that area
    static JTextField textField  = new JTextField(40);          //typing area
    static JTextField textField2 = new JTextField(40);          //typing area
    static JLabel     blankLabel = new JLabel("          ");
    static JButton    sendButton = new JButton("Send");            //SEND button
    static JLabel     nameLabel  = new JLabel("          ");

    static BufferedReader in;
    static PrintWriter out;

    public ChatClient() {
        //define how components will be arranged onto the Frame(window)
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(nameLabel);

        //add components in the window
        chatWindow.add(new JScrollPane(chatArea));                 //adds a scroller for the chat area
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the window, when cross clicked.
        chatWindow.setSize(500,475);
        chatWindow.setVisible(true);                               //else, window won't be visible

        textField.setEditable(false); //don't want the user to type something unless the connection is established with the server
        //will make it textField.setEditable -> true, once connection established.
        chatArea.setEditable(false); //as we don't want the user to type anything directly on the chat area.

        sendButton.addActionListener(new Listener()); //when user clicks the send button
        textField.addActionListener(new Listener());  //when user hits ENTER on keyboard while typing on the textField
    }

    void startChat() throws Exception{

        //Will return the value which the user will enter into his text field.
        //Here, to get the ip address, we are using a dialog box
        String ipAddress = JOptionPane.showInputDialog(chatWindow,
                "Enter Ip Address: ",
                "IP Address Required!!",
                JOptionPane.PLAIN_MESSAGE
        );

        Socket soc = new Socket(ipAddress, 8080);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);


        while(true){
            //store the response msg sent by the server, after entering the username by the user
            String str = in.readLine();


            if(str.equals("NAME_REQUIRED")) //startWith, since we're sending two strings from the server
            {
                String name = JOptionPane.showInputDialog(chatWindow,
                        "Enter username: ",
                        "Name Required!!",
                        JOptionPane.PLAIN_MESSAGE);
                out.println(name); //send the entered name to the server to check whether it's unique or not
            }
            else if(str.equals("NAME_ALREADY_EXISTS"))
            {
                String name = JOptionPane.showInputDialog(chatWindow,
                        "Enter NEW unique username: ",
                        "Name Already Exists!!",
                        JOptionPane.WARNING_MESSAGE);
                out.println(name); //again send the new entered name to the server to check whether it's unique or not
            }
            else if(str.startsWith("NAME_ACCEPTED"))
            {
                //now, make the textField editable
                textField.setEditable(true);
                nameLabel.setText("You're logged in as: "+str.substring(13));
            }
            else{
                //if the server sends any other text than the above three, it's actullay a msg and display it on the screen
                chatArea.append(str+ "\n");
            }
        }

    }


    public static void main(String[] args) throws Exception{
        ChatClient client = new ChatClient();
        client.startChat();
    }
}


class Listener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
        //send whatever the client types to the Server and then server will send it to others.
        ChatClient.out.println(ChatClient.textField.getText());
        ChatClient.textField.setText("");
    }
}






