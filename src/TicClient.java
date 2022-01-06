import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Class responsible for playing the game and communicating with it's proxy.
 */
public class TicClient {

	private int playerNum;
	private int[][] currentBoard = new int[TicTacToeGame.BOARD_SIZE][TicTacToeGame.BOARD_SIZE];
	
	private Socket client;
	
	private Scanner input;
	private PrintWriter output;
    
	public static void main(String[] args) {
		new TicClient();
	}
	
	public TicClient() {
		
		try {
		    client = new Socket("localhost", TicServer.N3TP_PORT);
		    
		    waitForRandomAmountOfTime();
			setUpCommunications();
			assignPlayerNumber();
			
			System.out.println("-------the game is starting-------");
			
			if(playerNum == 1) {
				sleep(1000);
			}
			else {
				
				String otherPlayerMoveResponse = input.nextLine();
				
				handleOtherPlayerBoardUpdateResponse(otherPlayerMoveResponse);
				
				sleep(1000);
			}
			
			playGame();
			
			input.close();
			output.close();
			client.close();
		}
		catch (IOException e) {
			System.out.println("Error connecting to server");
			e.printStackTrace();
		}
		
	}
	
	public void setUpCommunications() throws IOException {
	      InputStream instream = client.getInputStream();
	      OutputStream outstream = client.getOutputStream();
	      
	      this.input = new Scanner(instream);
	      this.output = new PrintWriter(outstream);
	}
	
	public void assignPlayerNumber() {
		
		String command = "hello";
		System.out.println("Sending: [" + command + "]");
		output.println(command);
		output.flush();
	      
		String response = input.nextLine();
		System.out.println("Receiving: [" + response + "]");
	      
		if(response.contains("1")) {
			this.playerNum = 1;
		}
		else {
			this.playerNum = 2;
		}
		
	}
	
	public void playGame() {
		while(true) {
			
			//handle my move
			doTurn();
			
			String myMoveResponse = input.nextLine();

			if (myMoveResponse.contains("won") || myMoveResponse.equals("draw")) {
				System.out.println("Game Is Over!");
				return;
			}
			
			//handle response for other player move			
			String otherPlayerMoveResponse = input.nextLine();
			
			if(otherPlayerMoveResponse.contains("Board update")) {
				handleOtherPlayerBoardUpdateResponse(otherPlayerMoveResponse);
				sleep(1000);
			}
			else if (otherPlayerMoveResponse.contains("won") || otherPlayerMoveResponse.contains("draw")) {
				System.out.println("Game Is Over!");
				
				JOptionPane winMessageJOption = new JOptionPane();
				winMessageJOption.showMessageDialog(winMessageJOption, otherPlayerMoveResponse);
				return;
			}

		}
	}
	
	public void doTurn() {
		
		System.out.println("I am player " + playerNum);
		makeLegalMove();
		printCurrentBoard();
		
	}
	
	public void handleOtherPlayerBoardUpdateResponse(String playerMoveResponse) {
		
		int playerNumber = Character.getNumericValue(playerMoveResponse.charAt(13));
		int playerRowSelection = Character.getNumericValue(playerMoveResponse.charAt(14));
		int playerColumnSelection = Character.getNumericValue(playerMoveResponse.charAt(15));
		
		updateCurrentBoard(playerNumber, playerRowSelection, playerColumnSelection);
		
	}
	
	public void makeLegalMove() {
		
		int rowSelection = -1;
		int columnSelection = -1;
		
		boolean isLegalMove = false;
		Random rand = new Random();
		
		while(!isLegalMove) {
			rowSelection = rand.nextInt(TicTacToeGame.BOARD_SIZE);
			columnSelection = rand.nextInt(TicTacToeGame.BOARD_SIZE);
			
			isLegalMove = isSpotAvailableInCurrentBoard(rowSelection, columnSelection);
		}
		
		String command = "move" + playerNum + rowSelection + columnSelection;
		
		output.println(command);
		output.flush();
		
		updateCurrentBoard(playerNum, rowSelection, columnSelection);
		
	}
	
	public boolean isSpotAvailableInCurrentBoard(int row, int column) {
		return (currentBoard[row][column] == 0);
	}
	
	public void updateCurrentBoard(int playerNum, int row, int column) {
		currentBoard[row][column] = playerNum;
	}
	
	public void printCurrentBoard() {
		for(int r = 0; r < TicTacToeGame.BOARD_SIZE; r++) {
			for(int c = 0; c < TicTacToeGame.BOARD_SIZE; c++) {
				System.out.print(currentBoard[r][c] + " ");
			}
			System.out.println();
		}
	}
	
	public void waitForRandomAmountOfTime() {
		Random rand = new Random();
		
		int waitTime = rand.nextInt(50);
		
		try { Thread.sleep(waitTime); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
	}
	
	public void sleep(int waitTime) {
		try { Thread.sleep(waitTime); }
		catch (InterruptedException e) { e.printStackTrace(); }
	}
	
}