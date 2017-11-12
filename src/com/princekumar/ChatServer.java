package com.princekumar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
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

    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}