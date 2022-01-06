import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class responsible for maintaining the board on the server, checking
 *  for wins, and assigning player numbers. Also is able to send moves to
 *  the other player.
 */
public class TicTacToeGame {
	
	public static final int BOARD_SIZE = 3;
	private final int BOARD_NUM_ROWS = BOARD_SIZE;
	private final int BOARD_NUM_COLS = BOARD_SIZE;
	
	private int[][] board;
	private int numOfActivePlayers;
	private Lock gameActionLock = new ReentrantLock();
	
	private Socket player1;
	private Socket player2;
	private PrintWriter player1Output;
	private PrintWriter player2Output;
	
	public TicTacToeGame() {
		this.board = new int[BOARD_NUM_ROWS][BOARD_NUM_COLS];
		this.numOfActivePlayers = 0;
	}
	
	public int addPlayer() {
		this.numOfActivePlayers++;
		return numOfActivePlayers;
	}
	
	public void makeMove(int playerNumber, int rowSelection, int columnSelection) {
		board[rowSelection][columnSelection] = playerNumber;
	}
	
	public boolean isGameOver() {
		
		if(getWinnerNumber() != -1) {
			return true;
		}
		else {
			return isboardFull();
		}
		
	}
	
	public boolean isboardFull() {
		
		for(int r = 0; r < BOARD_NUM_ROWS; r++) {
			for(int c = 0; c < BOARD_NUM_COLS; c++) {
				if(board[r][c] == 0) {
					return false;
				}
			}
		}
		return true;
		
	}
	
	public int getWinnerNumber() {
		int playerWinnerNum = -1;
		
		playerWinnerNum = checkRowsForWinner();
		if(playerWinnerNum != -1)
			return playerWinnerNum;
		
		playerWinnerNum = checkColumnsForWinner();
		if(playerWinnerNum != -1)
			return playerWinnerNum;
		
		playerWinnerNum = checkDiagonalsForWinner();
		if(playerWinnerNum != -1)
			return playerWinnerNum;

		return playerWinnerNum;
	}
	
	private int checkRowsForWinner() {
		int playerWinnerNum = -1;
		boolean playerWon = false;
		
		for(int r = 0; r < BOARD_NUM_ROWS; ++r) {
			playerWon = true;
			
			for(int c = 1; c < BOARD_NUM_COLS; ++c) {
				if(board[r][c] != board[r][0] || board[r][c] == 0) {
					playerWon = false;
					break;
				}
			}
			
			if(playerWon) {
				playerWinnerNum = board[r][0];
				return playerWinnerNum;
			}
		}
		
		return playerWinnerNum;
	}
	
	private int checkColumnsForWinner() {
		int playerWinnerNum = -1;
		boolean playerWon = false;

		for(int c = 0; c < BOARD_NUM_COLS; ++c) {
			playerWon = true;
			
			for(int r = 1; r < BOARD_NUM_ROWS; ++r) {
				if(board[r][c] != board[0][c] || board[r][c] == 0) {
					playerWon = false;
					break;
				}
			}
			
			if(playerWon) {
				playerWinnerNum = board[0][c];
				return playerWinnerNum;
			}
		}
		
		return playerWinnerNum;
	}
	
	private int checkDiagonalsForWinner() {
		int playerWinnerNum = -1;
		boolean playerWon = false;
		
		//check top left to bottom right diagonal
		playerWon = true;
		for(int r = 1; r < BOARD_NUM_ROWS; r++) {
			int c = r;
			if(board[r][c] != board[0][0] || board[r][c] == 0) {
				playerWon = false;
				break;
			}
		}
		
		if(playerWon) {
			playerWinnerNum = board[0][0];
			return playerWinnerNum;
		}
		
		//check top right to bottom left diagonal
		playerWon = true;
		int columnCounter = -1;
		for(int r = BOARD_NUM_ROWS - 1; r > -1; r--) {
			columnCounter++;
			if(board[r][columnCounter] != board[0][BOARD_NUM_COLS - 1] || board[r][columnCounter] == 0) {
				playerWon = false;
				break;
			}
		}
		
		if(playerWon) {
			playerWinnerNum = board[0][BOARD_NUM_COLS - 1];
			return playerWinnerNum;
		}
		
		return playerWinnerNum;
	}
	
	public String boardToString() {
		String boardString = "";
		
		for(int r = 0; r < BOARD_NUM_ROWS; r++) {
			for(int c = 0; c < BOARD_NUM_COLS; c++) {
				boardString += board[r][c] + " ";
			}
			if(r != BOARD_NUM_ROWS - 1)
				boardString += "\n";
		}
		return boardString;
	}
	
	public void sendMessageToOtherPlayer(int myPlayerNum, String message) {
		
		if(myPlayerNum == 1) {
			player2Output.println(message);
			player2Output.flush();
		}
		else {
			player1Output.println(message);
			player1Output.flush();
		}
	}
			
	public int[][] getboard() {
		return board;
	}

	public void setboard(int[][] board) {
		this.board = board;
	}

	public int getNumOfActivePlayers() {
		return numOfActivePlayers;
	}

	public void setNumOfActivePlayers(int numOfActivePlayers) {
		this.numOfActivePlayers = numOfActivePlayers;
	}

	public Lock getGameActionLock() {
		return gameActionLock;
	}

	public void setGameActionLock(Lock gameActionLock) {
		this.gameActionLock = gameActionLock;
	}

	public Socket getPlayer1() {
		return player1;
	}

	public void setPlayer1(Socket player1) {
		this.player1 = player1;
		try { this.player1Output = new PrintWriter(player1.getOutputStream()); }
		catch (IOException e) { e.printStackTrace(); }
	}

	public Socket getPlayer2() {
		return player2;
	}

	public void setPlayer2(Socket player2) {
		this.player2 = player2;
		try { this.player2Output = new PrintWriter(player2.getOutputStream()); }
		catch (IOException e) { e.printStackTrace(); }
	}

}
