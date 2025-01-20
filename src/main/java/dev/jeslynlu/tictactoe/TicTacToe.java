package dev.jeslynlu.tictactoe;

/**
 * Jeslyn Lu
 * lu196
 * TicTacToe is a text-based game of TicTacToe that supports human and computer two-way game play. Computer players take their turn with a random move strategy whereas human players use text-based input of a row-column pair. 
 */

import java.util.*;


// Game class manages the necessary functions to run and complete a game
class Game{
	protected Board board;
	protected Player player1;
	protected Player player2;

	// constructor
	public Game(Board board, Player player1, Player player2) {
        	this.board = board;
        	this.player1 = player1;
        	this.player2 = player2;
		    start();
    }

	// start prints the beginning text
	public void start(){
		 System.out.println("Welcome to Tic Tac Toe!");
	}

	// play is the main game loop that alternates between players until the game is over
	public void play(){
		boolean gameOver = false; // flag for if game is finished
		Player currentPlayer = player1; // player1 always starts first
		
		while(!gameOver){
			char currentSymbol = currentPlayer.getSymbol();
			board.displayBoard();
			System.out.println("\nPlayer " + currentSymbol + "'s turn:");

			int [] move = currentPlayer.makeMove(board);
			board.placeSymbol(move, currentSymbol);
			
			// checking for game ending conditions
			if(checkWin(move, currentSymbol)){
                		board.displayBoard();
                		System.out.println("Player " + currentSymbol + " wins!");
                		gameOver = true;
			}

			else if(checkDraw()){
                		board.displayBoard();
                		System.out.println("The game ends in a draw!");
                		gameOver = true;
           		}

			else{
				currentPlayer = (currentPlayer == player1) ? player2 : player1;  // alternate player for each turn
			}
        }
        restart(); // check if player wants to replay game
	}


	// checkWin returns if player has won the game. checks for a win condition after every move made by player
	public boolean checkWin(int [] move, char symbol){
		int x = move[0];
		int y = move[1];
		int size = board.getSize();

		// check for row win - symbol has filled all columns in the given row
		for(int i=0; i<size; i++){
			if(board.getSymbol(x,i) != symbol){ // if symbol in row does not match
				break;
			}
			if(i == size-1){ // if all symbols in row matches, player has won
				return true;
			}
		}

		// check for col win - symbol has filled all columns in the given column
		for(int j=0; j<size; j++){
			if(board.getSymbol(j,y) != symbol){
				break;
			}
			if(j == size-1){
				return true;
			}
		}
		
		// check for diagonal from top left to bottom right
		if(x==y){ // if current move was made on diagonal
			for(int k=0; k<size; k++){
				if(board.getSymbol(k,k) != symbol){
					break;
				}
				if(k == size-1){
					return true;
				}
			}
		}

		// check for diagonal from top right to bottom left
		if(x+y==size-1){ // if current move was made on diagonal
			for(int k=0; k<size; k++){
				if(board.getSymbol(k,(size-1)-k) != symbol){
					break;
				}
				if(k == size-1){
					return true;
				}
			}
		}
		return false;
	}

	// checkDraw returns if round ends in a draw - if niether player has won and all spots are full
	public boolean checkDraw(){
		if(board.isFull()){
			return true;
		}
		else{
			return false;
		}
	}

	// restart checks if player wants to start a new round, and resets board and starts new game loop if player wants to do so
	public void restart(){
		while(true){ // will keep asking for input until it's valid
			Scanner sc = new Scanner(System.in);
        		System.out.print("Would you like to play again? (y/n): ");
			String input = sc.nextLine();
			try {
                		if(input.equals("y")){
					board.resetBoard();
					play(); // start new game loop
					return;
				}
				else if(input.equals("n")){
					System.out.println("Thanks for playing!");
					return;
				}
				else{
                    			throw new IllegalArgumentException("Invalid input. Try again.");
                		}
				

            		} catch (IllegalArgumentException e) {
                		System.out.println(e.getMessage());
			}
		}

    }
}

		
	
// Player class contains methods to play a move on the board
abstract class Player{
	protected char symbol; // either 'X' or 'O'

	// constructor
	public Player(char symbol){
		this.symbol = symbol;
	}

	// getSymbol returns symbol of player
	public char getSymbol(){
		return this.symbol;
	}

	// abstract method for making a move
	public abstract int[] makeMove(Board board);
	
}

// HumanPlayer extends Player to get user input from a real human to play the game
class HumanPlayer extends Player{
	public HumanPlayer(char symbol){
		super(symbol);
	}


	// makeMove gets the move from the player as a string input, ensures input format is valid and is a valid move, and returns move as an integer array		
	public int[] makeMove(Board board){
		while(true){ // will keep asking for input until it's valid in format
			Scanner sc = new Scanner(System.in);
        		System.out.print("Enter a valid move (row,col): ");
			String input = sc.nextLine();
			try {
                		// proccessing input string
                		String[] inputParts = input.split(",");

                		// ensure exactly a row and column are provided
                		if (inputParts.length != 2) {
                    			throw new IllegalArgumentException("Invalid input. Move must be entered in form (row, column).");
                		}

                		// convert input to be stored in an int array
                		int row = Integer.parseInt(inputParts[0].trim());  
                		int col = Integer.parseInt(inputParts[1].trim()); 
				
				int [] move = new int[]{row-1, col-1};

				if (!board.isMoveValid(move)) { // checks if move is valid
                    			throw new IllegalArgumentException("Invalid move.");
                		}
				
				return move;

			} catch (NumberFormatException e) {
                		System.out.println("Invalid input. Please enter numbers for row and column.");
            		} catch (IllegalArgumentException e) {
                		System.out.println(e.getMessage());
			}
		}
	}
		
}

// ComputerPlayer extends Player to automate the player
class ComputerPlayer extends Player{
	public ComputerPlayer(char symbol){
		super(symbol);
	}

	// makeMove randomly returns an integer array move
	public int[] makeMove(Board board){
	        Random random = new Random();
        	int size = board.getSize();
        	int[] move;

        	do {
            		int row = random.nextInt(size); // random row
            		int col = random.nextInt(size); // random column
            		move = new int[]{row, col};
        	} while (!board.isMoveValid(move)); // loop until a valid move is chosen

        	System.out.println("Computer chooses: (" + (move[0]+1) + "," + (move[1]+1) + ")");
        	return move;
    }
}

// Board class maintains the grid board. It creates and displays the board.
class Board{
	protected char[][] grid;
	protected int size; // grid size

	public Board(){
		// default grid size of board is 3x3
		this.size = 3;
		this.grid = new char[3][3];
		resetBoard(); // start with an empty board
	}

	// resetBoard will make every board value to be empty
	public void resetBoard(){
        	for(int i=0; i<size; i++){
            		for (int j=0; j<size; j++){
                		this.grid[i][j] = ' ';
            		}
        	}
    	}

	// getSymbol returns the marker at a specific spot on the board
	public char getSymbol(int [] coords){
		int x = coords[0];
		int y = coords[1];
    		return grid[x][y];
  	}

	// getSymbol returns the marker at a specific spot on the board
	public char getSymbol(int x, int y){
    		return grid[x][y];
  	}

	// getSize returns size of board
	public int getSize() {
        	return this.size;
    	}

	// isSpotEmpty takes takes an array of x and y coordinates and checks if the spot is empty, meaning has no symbol on it yet
	public boolean isSpotEmpty(int [] coords){
		int x = coords[0];
		int y = coords[1];
		if(grid[x][y] == ' '){ // empty spot
			return true;
		}

		else{
			return false;
		}
	}

	// isFull returns whether or not board has all spots filled with symbols
	public boolean isFull(){
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if(grid[i][j] == ' '){ // if there is a single empty spot, board is not full
					return false;
				}
			}
		}
		return true;
	}

	// isMoveValid returns if the move is within the board boundaries and the spot is empty
    	public boolean isMoveValid(int[] coords) {
        	int row = coords[0];
        	int col = coords[1];

        	if (row < 0 || row >= size || col < 0 || col >= size) {
            		return false; // move is out of bounds
        	}

        	return isSpotEmpty(coords); // spot must be empty
    	}

	// placeSymbol takes an array of x and y coordinates to place the inputted symbol on the board
	public void placeSymbol(int [] coords, char symbol){
		int x = coords[0];
		int y = coords[1];
		grid[x][y] = symbol; // place symbol on board
	}

	// displayBoard prints out the current board
	public void displayBoard(){
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				System.out.print(" "+grid[i][j]);
				if (j < size-1){ // print col dividers
                    			System.out.print(" |");
                		}
			}
			System.out.println();

			// print row dividers
        		if(i < size-1){
            			for (int k = 0; k < size; k++) {
                			System.out.print("---");
                			if (k < size-1) {
                    				System.out.print("+");
                			}
            			}
            		System.out.println();
		        }
	    }
    }
}


	