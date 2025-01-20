package dev.jeslynlu.tictactoe;

/**
 * Jeslyn Lu
 * lu196
 * TicTacToeUpdated is a text-based game of TicTacToe that supports human and computer two-way game play. Computer players take their turn with a random move strategy whereas human players use text-based input of a row-column pair. 
 * It is updated to allow for more extensibility and maintainability by allowing for any sized grid game and a user set winning condition.
 */

import java.util.*;

public class TicTacToeUpdated {
    public static void main(String[] args) {
	    Board board = new FlexibleBoard(5);
            Player player1 = new ComputerPlayer('X');
	    Player player2 = new ComputerPlayer('O');
	    Game game = new AdvancedGame(board, player1, player2, 3);
	    game.play();
    }
}

// FlexibleBoard extends Board to allow for a board size given by user who initializes the game
class FlexibleBoard extends Board{
	// has its own constructor that takes a board size input
	public FlexibleBoard(int size){
		this.size = size;
		this.grid = new char[this.size][this.size];
		resetBoard(); // start with an empty board
	}
}


// AdvancedGame extends Game to allow for winning condition given by user who initializes the game
class AdvancedGame extends Game{
	private int winningNum; // player wins if they place a winning number of symbols consecutively

	// constructor also takes in a winning condition number
	public AdvancedGame(Board board, Player player1, Player player2, int winningNum){
		super(board, player1, player2);
		this.winningNum = winningNum;
	}

	// checkWin overrides the Game class's checkWin to count a win as the first player who has the winning number of markers in a straight-line
	public boolean checkWin(int [] move, char symbol){
	    	int size = board.getSize();
		int x = move[0];
		int y = move[1];
		int rowCount = 0; // number of consecutive symbol matches per row
		int colCount = 0; // number of consecutive symbol matches per col
		int leftDiagCount = 0; // matches per top left to bottom right diagonal
		int rightDiagCount = 0; // matches per top right to bottom left diagonal
		
		// check for row win - player has winning number of symbols in row
		for(int i=0; i<size; i++){
			if(board.getSymbol(x,i) == symbol){ // if symbol in row does not match		
				rowCount += 1;

				if(rowCount >= winningNum){ // if player has winning number of symbols in a striaght-line
					return true;
				}
			}
			else{
				rowCount = 0; // reset count if mismatch
			}
		}

		// check for col win - player has winning number of symbols in a column
		for(int j=0; j<size; j++){
			if(board.getSymbol(j,y) == symbol){ // if symbol in row does not match		
				colCount += 1;

				if(colCount >= winningNum){ // if player has winning number of symbols in a striaght-line
					return true;
				}
			}
			else{
				colCount = 0; // reset count if mismatch
			}
		}	
		
		// check for diagonal from top left to bottom right
		if(x==y){ // if current move was made on diagonal
			for(int k=0; k<size; k++){
				if(board.getSymbol(k,k) == symbol){ 	
					leftDiagCount += 1;

					if(leftDiagCount >= winningNum){ 
						return true;
					}
				}
				else{
					leftDiagCount = 0; // reset count if mismatch
				}
			}
		}

		// check for diagonal from top right to bottom left
		if(x+y==size-1){ // if current move was made on diagonal
			for(int k=0; k<size; k++){
				if(board.getSymbol(k,(size-1)-k) == symbol){ 	
					rightDiagCount += 1;

					if(rightDiagCount >= winningNum){ 
						return true;
					}
				}
				else{
					rightDiagCount = 0; // reset count if mismatch
				}
			}
		}
		return false;
	}
}



	