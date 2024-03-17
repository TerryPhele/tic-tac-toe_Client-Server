package Client;

import message.Request;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private char name = ' ';
    private BufferedWriter requestOutput;
    private BufferedReader  responseInput;
    private Socket socket;

    private Request message;
    
    public  Client(){
        message = new Request();
    }


    public  void sendMessage(){

        while (socket.isConnected()){
            Scanner input = new Scanner(System.in);
            String request = input.nextLine();
            try{
                this.requestOutput.write( request);
                this.requestOutput.newLine();
                this.requestOutput.flush();
            } catch( IOException e){}
        }


    }



    public  String getResponse()
    {
        String response = "";
        try{
            response = this.responseInput.readLine();
        } catch( IOException e){}
        return  response;
    }

    public  void  listenServer(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected())
                {
                    System.out.println( getResponse() );
                }
            }
        }).start();
    }


    public  void connect( int port, String ip ){

        try{
            this.socket = new Socket( ip, port);
            this.requestOutput = new BufferedWriter( new OutputStreamWriter(this.socket.getOutputStream()));
            this.responseInput = new BufferedReader( new InputStreamReader( this.socket.getInputStream()));
        }catch( IOException e){}
    }


    public  boolean connected(){
        return  this.socket.isConnected();
    }

    public static void main(String[] args) {
            Client player = new Client();
            player.connect( 5000, "localhost");
            if(player.connected()){
                player.listenServer();
                player.sendMessage();
            }

    }
}
