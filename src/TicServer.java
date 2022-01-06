import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class responsible for setting the port, creating the game, and creating
 *  and starting client proxies
 */
public class TicServer {
	
    public static final int N3TP_PORT = 8888;
    private static int clientNum = 1;

	public static void main(String[] args) {

		try {
			
		    TicTacToeGame ticGame = new TicTacToeGame();
			ServerSocket ticServer = new ServerSocket(N3TP_PORT);
			System.out.println("Waiting for clients to connect...");
			
		    while (true) {
		    	Socket ticSocket = ticServer.accept();
		    	System.out.println("Client " + clientNum + " connected.");
		    	TicAction proxy = new TicAction(ticSocket, ticGame, clientNum);
		    	Thread proxyGameAction = new Thread(proxy);
		    	proxyGameAction.start();
		    	
		    	clientNum++;
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
