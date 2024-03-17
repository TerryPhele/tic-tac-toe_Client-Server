package game;

import java.util.*;

public class Board {

    private Map<Integer, Character> board;
    private List<Integer> occupiedPositions;
    private String message;

    public Board(){
        this.board = new HashMap<>();
        this.occupiedPositions = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard(){
        for(int x = 1; x <= 9; x++ ){
            updateBoard(x, ' ');
        }
    }

    public String getMessage() {
        return this.message;
    }


    public boolean updateBoard(int position, char playerName ) {

        if (!positionBlocked(position)) {
            this.board.put(position, playerName);
            this.message = String.format("You place at this position %d.", position);
            return true;
        }
        return  false;
    }


    public char getPlayerName( int position){
        return  this.board.get(position);
    }


    private boolean positionBlocked( int newPosition){

        if( this.occupiedPositions.contains(newPosition) )
        {
            this.message = String.format("Could not make a move, this position %d is occupied.", newPosition);
            return  true;
        }
        return false;
    }


    private boolean validPlayer( int currentPosition ,char playerName){
        if(  this.board.get(currentPosition) == playerName ){
            return  true;
        }
        this.message = String.format("Could not make a move, you are not on this %d position.",currentPosition);
        return false;
    }



    public  boolean movementValid( int currentPosition, int newPosition, char playerName){
        return  validPlayer(currentPosition, playerName) && !positionBlocked( newPosition);
    }


    public  void  addNewPosition( int pos){
        occupiedPositions.add( pos);
    }


    public  void removePosition( int position){
      Iterator<Integer> iterable = occupiedPositions.iterator();
      while( iterable.hasNext()){
          int pos = iterable.next();
          if( pos == position){
              iterable.remove();
          }
      }
    }

    public boolean moved( int currentPosition, int newPosition, char name){
        if (this.movementValid( currentPosition, newPosition,name)) {
            this.removePosition( currentPosition);
            this.updateBoard(currentPosition, ' ');
            this.updateBoard(newPosition, name);
            this.message = String.format("You have moved from %d to %d", currentPosition, newPosition);
            return true;
        }
        return  false;
    }
}
