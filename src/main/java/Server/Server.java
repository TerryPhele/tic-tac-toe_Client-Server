package Server;

import game.Board;
import game.Player.Player;
import message.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Server  implements  Runnable{

    private Response responder;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;
    private Socket socket;

    private volatile static List<Socket> PlayerSockets  =  new ArrayList<>();
    private static Player player1 = null;
    private static Player player2 = null;

    private static Board board = new Board();
    public  volatile static  int countXPlacements = 0;

    public static volatile  boolean player1Turn = true;

    static {
        PlayerSockets.add(0,null);
        PlayerSockets.add(1,null);
    }

    public Server( Socket socket){

        this.socket = socket;
        this.initialiseIOstreams();
        responder = new Response();
    }

    private void initialiseIOstreams(){
        try{
            this.outputStream = new BufferedWriter( new OutputStreamWriter( this.socket.getOutputStream()));
            this.inputStream = new BufferedReader( new InputStreamReader( this.socket.getInputStream()));
        }catch (IOException e){
            System.out.println( "Error occurred!!, something is wrong with socket initialising "+ e.getMessage());
            closeSocket();
        }
    }

    private void closeSocket(){
        try{
            if( this.socket != null) this.socket.close();
        }catch ( IOException e){
            System.out.println("Error occurred!!,Socket might be null ");
        }
    }

    public void sendMassage( String messageToSend){
        try{
            this.outputStream.write( messageToSend);
            this.outputStream.newLine();
            this.outputStream.flush();
        }catch ( IOException e){
            System.out.println("Error occurred!!,something is wrong with writting to the Player!");
        }
    }

    public  String recieveMessage(){
        String messageFromClient = null;
        try{
            messageFromClient = this.inputStream.readLine();
        }catch ( IOException e){
            System.out.println("Error occurred!!,something is wrong with read from the Player! ");
        }

        return  messageFromClient;
    }



    public  char getFirstPlayerName() {
        char playerName = ' ';
        this.sendMassage("Welcome to Tic-Tac-Toe\n");
        this.sendMassage("Please choose the character you want to be [ 'X'  or 'O']? : ");
        playerName = Character.toUpperCase(this.recieveMessage().charAt(0));
        while( (playerName != 'X' ) && (playerName != 'O')){
            this.sendMassage("You chose wrong character please select from ['X' or 'O'] respectively :");
            playerName = Character.toUpperCase(this.recieveMessage().charAt(0));
        }
        return  playerName;
    }



    public char sendSecondPlayerName(){
        char playerName = player1.getName() == 'X' ? 'O' : 'X';
        this.sendMassage("Welcome to Tic-Tac-Toe\n" + this.player1.getName() + " is connected.");
        this.sendMassage(String.format("You are player-%s", playerName));

        return  playerName;
    }


    public  void connectPlayerOne( char playerName){
        if( this.noPlayerConnected() ){
            PlayerSockets.set(0 , this.socket);
            player1 = new Player( playerName, Server.board);
        }
    }

    public  void connectPlayerTwo( char playerName){
        if( this.onePlayerConnected()){
            PlayerSockets.set(1,  this.socket);
            player2 = new Player( playerName, Server.board);
        }
    }

    private boolean noPlayerConnected(){
        return  player1 == null && player2 == null;
    }

    private boolean onePlayerConnected(){
        return player1 != null;
    }

    public void startCommunication() throws IOException {

        if( countXPlacements < 6){
            if( player1Turn){
                this.communicatePlacements(  PlayerSockets.get(0), player1);

            }else{
                this.communicatePlacements( PlayerSockets.get(1), player2);
            }

        }else{
            if( player1Turn){
                this.communicateMovement( PlayerSockets.get(0), player1);
            }else{
                this.communicateMovement( PlayerSockets.get(1), player2);
            }
        }
    }

    public  void communicateMovement( Socket socket, Player player) throws IOException {

        if( socket == this.socket){
            sendMassage("------BOARD-----");
            sendMassage( displayBoard());
            this.sendMassage("its You turn to make a move, input format is 'x,y' :");
            String[] command = this.recieveMessage().split(",");
            boolean player1Moved = board.moved( Integer.parseInt( command[0]), Integer.parseInt( command[1]),player.getName());
            this.sendMassage( this.responder.send(board.getMessage()));

            if( player1Moved) {
                board.updateBoard(Integer.parseInt( command[0]), ' ');
//                board.removePosition( Integer.parseInt( command[0] ) );
                board.addNewPosition( Integer.parseInt( command[1]));
                this.switchTurn();
            }
        }

    }


    public void communicatePlacements(Socket socket, Player player) throws IOException {
        if( socket == this.socket) {
            sendMassage("------BOARD-----");
            sendMassage( displayBoard());
            this.sendMassage("its You turn to make a place, input format is 'x' :");
            int position = Integer.parseInt(this.recieveMessage());
            boolean positionValid = Server.board.updateBoard(position, player.getName());
            this.sendMassage(this.responder.send(board.getMessage()));

            if (positionValid) {
                board.addNewPosition(position);
                countXPlacements += 1;
                this.switchTurn();

            }
        }
    }

    public  String displayBoard(){
        return  String.format(
                "|  %S  |  %s  |  %s  |" + "\n" + "|  %S  |  %s  |  %s  |\n" + "|  %S  |  %s  |  %s  |"
                        , board.getPlayerName(1),
                board.getPlayerName(2),
                board.getPlayerName(3),
                board.getPlayerName(4),
                board.getPlayerName(5),
                board.getPlayerName(6),
                board.getPlayerName(7),
                board.getPlayerName(8),
                board.getPlayerName(9));

    }


    private void switchTurn()
    {
        if( player1Turn)
            player1Turn = false;
        else
            player1Turn = true;
    }


    @Override
    public void run() {
        int waitingPeriod = 0;
        if( Server.PlayerSockets.get(0) == null){
            System.out.println(( "client 1 is nuuuulllll"));
            connectPlayerOne( getFirstPlayerName());
        }else{
            connectPlayerTwo( sendSecondPlayerName() );
        }

        while (  PlayerSockets.get(0).isConnected()){
            try {
                this.startCommunication();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        try (
            ServerSocket serverSocket = new ServerSocket(5000);
        ){
            while ( !serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                Server server = new Server( socket);

                Thread task = new Thread( server);
                task.start();
            }

        }catch ( IOException e){
            System.out.println("Error occurred! something is wrong with port or severSocket");
        }

    }
}