import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Holds the ticSocket to communicate with the client, receives moves
 *  from the client, and sends the player's move to the other client.
 *  Also updates the game board with the player's move.
 */
public class TicAction implements Runnable {
	
	private Socket ticSocket;
	private TicTacToeGame game;
	private Scanner clientInput;
	private PrintWriter clientOutput;
	
	private int clientNum;
	
	public TicAction(Socket ticSocket, TicTacToeGame aGame, int clientNum) {
		this.ticSocket = ticSocket;
		this.game = aGame;
		this.clientNum = clientNum;
		
		if(this.clientNum == 1) {
			game.setPlayer1(ticSocket);
		}
		else {
			game.setPlayer2(ticSocket);
		}
	}
	
	@Override
	public void run() {
		try {
			try {
				clientInput = new Scanner(ticSocket.getInputStream());
	            clientOutput = new PrintWriter(ticSocket.getOutputStream());
	            doAction();           
			}
			finally {
				ticSocket.close();
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		
	}
	
	public void doAction() throws IOException {
		
		while (true) {  
			if (!clientInput.hasNextLine()) {
				return;
			}
			
			String command = clientInput.nextLine();  
			handleGameActionCommand(command);
		}
	}
	
	public void handleGameActionCommand(String command) {
		
		game.getGameActionLock().lock();
		
		System.out.println("**Command is [" + command +"] by player " + clientNum);
		
		if (command.equals("hello")) {
			game.setNumOfActivePlayers(game.getNumOfActivePlayers() + 1);
			clientOutput.println("new player command for player " + game.getNumOfActivePlayers());
			clientOutput.flush();
		}
		else if (command.contains("move")) {
			
			int playerNumber = Character.getNumericValue(command.charAt(4));
			int rowSelection = Character.getNumericValue(command.charAt(5));
			int columnSelection = Character.getNumericValue(command.charAt(6));
			
			game.makeMove(playerNumber, rowSelection, columnSelection);
			
			if(game.isGameOver()) {
				
				handleGameOverResponse();
				
			}
			else {
				
				handleBoardUpdateResponse(playerNumber, rowSelection, columnSelection);
				
			}
			
		}
		else {
			clientOutput.println("Invalid command recieved");
			clientOutput.flush();
			return;
		}
		
		game.getGameActionLock().unlock();
		
	}
	
	public void handleGameOverResponse() {
		
		int winnerNumber = game.getWinnerNumber();
		String gameOverMessage;
		
		System.out.println("The final board:");
		System.out.println(game.boardToString());
		
		if(winnerNumber != -1) {
			gameOverMessage = "Player " + winnerNumber + " won!!!";
			clientOutput.println(gameOverMessage);
			clientOutput.flush();
			
			game.sendMessageToOtherPlayer(clientNum, gameOverMessage);
			
			System.out.println("Player " + winnerNumber + " has won the game.");
		}
		else {
			gameOverMessage = "The game has ended in a draw";
			clientOutput.println(gameOverMessage);
			clientOutput.flush();
			
			System.out.println("The game is a draw");
			
			game.sendMessageToOtherPlayer(clientNum, gameOverMessage);
		}
	}
	
	public void handleBoardUpdateResponse(int playerNumber, int rowSelection, int columnSelection) {
		
		String boardUpdateMessage;
		
		System.out.println("The board Currently:");
		System.out.println(game.boardToString());
		System.out.println("game continues");
		
		boardUpdateMessage = "Board update(" + playerNumber + rowSelection + columnSelection + ")";
		
		clientOutput.println(boardUpdateMessage);
		clientOutput.flush();
		game.sendMessageToOtherPlayer(clientNum, boardUpdateMessage);
	}

}
