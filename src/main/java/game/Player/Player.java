package game.Player;


import game.Board;

public class Player {

    private final char name;
    private Board board; //remove
    private String message;

    private final int[] firstPossibleWin = {1,2,3};
    private final int[] secondPossibleWin = {4,1,7};
    private final int[] thirdPossibleWin = {4,5,6};
    private final int[] fourthPossibleWin = {7,8,9};
    private final int[] fivethPossibleWin = {2,5,8};
    private final int[] sixthPossibleWin = {3,6,9};
    private final int[] seventhPossibleWin = {7,5,3};
    private final int[] eighthPossibleWin = {1,5,9};


    public Player( char name, Board board){
        this.name = name;
        this.board = board; // remove
        this.message= "";
    }

    /**
     * checks if this player has won the game by verifying if any three consecutive position's values
     *  are the same.
     *
     * @param first
     * @param second
     * @param third
     * @return true if the values of first, second and third are the same as this.name
     */
    private boolean isPossibleWin(int first, int second, int third){
        char firstValue = this.board.getPlayerName(first);
        char secondValue = this.board.getPlayerName(second);
        char thirdValue = this.board.getPlayerName(third);

        return ( firstValue == secondValue && secondValue == thirdValue);
    }

    /**
     * Determines if the player has won based on all eight possible winning conditions
     * @return true if one of the possible wins is true else false
     */
    public boolean won()
    {
        boolean firstWin = isPossibleWin( firstPossibleWin[0],firstPossibleWin[1], firstPossibleWin[2]);
        boolean secondWin = isPossibleWin( secondPossibleWin[0], secondPossibleWin[1], secondPossibleWin[2]);
        boolean thirdWin = isPossibleWin(thirdPossibleWin[0], thirdPossibleWin[1], thirdPossibleWin[2]);
        boolean fourthWin = isPossibleWin(fourthPossibleWin[0], fourthPossibleWin[1], fourthPossibleWin[2]);
        boolean fivethWin = isPossibleWin( fivethPossibleWin[0], fivethPossibleWin[1], fivethPossibleWin[2]);
        boolean sixthWin = isPossibleWin(sixthPossibleWin[0], sixthPossibleWin[1], sixthPossibleWin[2] );
        boolean seventhWin = isPossibleWin( seventhPossibleWin[0], seventhPossibleWin[1], seventhPossibleWin[2]);
        boolean eighthWin = isPossibleWin(eighthPossibleWin[0], eighthPossibleWin[1], eighthPossibleWin[2]);

        return ( firstWin || secondWin || thirdWin || fourthWin || fivethWin || sixthWin || seventhWin || eighthWin );
    }


    public char getName() {
        return this.name;
    }

    //remove
    public Board getBoard() {
        return this.board;
    }


    public  String getMessage(){
        return  this.message;
    }
}
