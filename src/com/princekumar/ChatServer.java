package com.princekumar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    static ArrayList<String> userNames = new ArrayList<String>();
    //when a client sends a msg to the server, it needs to send that msg to all the clients, so it needs the printwriters of all clients
    static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception{
        System.out.println("Server Started.");
        System.out.println("Waiting for clients.");
        ServerSocket ss = new ServerSocket(9806);

        while(true){
            Socket soc = ss.accept();
            System.out.println("Connection Established.");

            //create the thread, and pass the socket
            ConversationHandler handler = new ConversationHandler(soc);
            //start the thread created
            handler.start();
        }
    }
}

//create a new thread class
class ConversationHandler extends Thread{
    Socket socket;      //socket for we'll be creating this thread
    BufferedReader in;  //to get input from server
    PrintWriter out;    //to send data to server
    String name;

    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //ask the user for a unique name, then add the username and its 'out' to the arraylists created
            int count = 0;
            while(true){
                if(count > 0)
                    out.println("NAMEALREADYEXISTS");
                else
                    out.println("NAMEREQUIRED");

                name = in.readLine();
                if(name == null)
                    return;

                if(!ChatServer.userNames.contains(name)){
                    ChatServer.userNames.add(name);
                    break;
                }
                count++;
            }

            out.println("NAMEACCEPTED");
            ChatServer.printWriters.add(out);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}