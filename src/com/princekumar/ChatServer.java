package com.princekumar;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    static ArrayList<String> userNames = new ArrayList<String>();
    //when a client sends a msg to the server, it needs to send that msg to all the clients, so it needs the printwriters of all clients
    static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception{
        System.out.println("Server Started.");
        System.out.println("Waiting for clients...");
        ServerSocket ss = new ServerSocket(8080);

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
    //for SERVER
    BufferedReader in;  //to get input from server
    PrintWriter out;    //to send data to server

    String name;

    //for FILE
    PrintWriter pw;     //to send data to the file
    static FileWriter fw; //write char by char to a file - one byte at a time
    static BufferedWriter bw; //--no idea--

    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
        fw = new FileWriter("/Users/princekumar/Desktop/ChatServer-Logs.txt",true);
        //true - so it appends texts at the end and not write from the start
        bw = new BufferedWriter(fw); //we need string at a time, not byte by byte, so this.
        pw = new PrintWriter(bw,true);
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //ask the user for a unique name, then add the username and its 'out' to the arraylists created
            int count = 0;
            while(true){
                if(count > 0) {
                    out.println("NAME_ALREADY_EXISTS");
                }
                else{
                    out.println("NAME_REQUIRED");
                }

                name = in.readLine();
                if(name == null)
                    return;

                if(!ChatServer.userNames.contains(name)){
                    ChatServer.userNames.add(name);
                    break;
                }
                count++;
            }

            out.println("NAME_ACCEPTED"+name);
            ChatServer.printWriters.add(out);

            //read msgs from client and send it to all the other clients
            while(true){
                String message = in.readLine();
                if(message == null){
                    return;
                }
                pw.println(name+": "+message);

                for(PrintWriter writer : ChatServer.printWriters){
                    //for each PrintWriter obj in printWriters array, do the following
                    writer.println(name+ ": "+message);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}