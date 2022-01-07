# Internet Tic-tac-toe
Java program for enabling two automated clients to play a game of tic-tac-toe over the internet. The game takes place over localhost and continues until either a player wins or all board positions have been taken and a draw is declared.
## Network Tic-Tac-Toe Protocol (N3TP)
Used for the server and clients to communicate with one another
| Client Message | Server Response | Meaning |
| -------------- | --------------- | ------- |
| Hello | New player command for player 1 <br /> or <br /> New player command for player 2 | First player joining the game <br /> <br /> Second player joining the game |
| move<int><int><int> | The new board state <br /> or <br /> Player <int> won!!! <br /> or <br /> The game is a draw | Legal move - not the end of the game <br /> <br /> Player <int> is the Winner <br /> <br /> Draw |
## Sample Run
TicServer.java
```
Waiting for clients to connect...
Client 1 connected.
**Command is [hello] by player 1
Client 2 connected.
**Command is [hello] by player 2
**Command is [move121] by player 1
The board Currently:
0 0 0 
0 0 0 
0 1 0 
game continues
**Command is [move211] by player 2
The board Currently:
0 0 0 
0 2 0 
0 1 0 
game continues
**Command is [move110] by player 1
The board Currently:
0 0 0 
1 2 0 
0 1 0 
game continues
**Command is [move222] by player 2
The board Currently:
0 0 0 
1 2 0 
0 1 2 
game continues
**Command is [move100] by player 1
The board Currently:
1 0 0 
1 2 0 
0 1 2 
game continues
**Command is [move202] by player 2
The board Currently:
1 0 2 
1 2 0 
0 1 2 
game continues
**Command is [move112] by player 1
The board Currently:
1 0 2 
1 2 1 
0 1 2 
game continues
**Command is [move220] by player 2
The final board:
1 0 2 
1 2 1 
2 1 2 
Player 2 has won the game.
```
TicClient.java (first client to join)
```
Sending: [hello]
Receiving: [new player command for player 1]
-------the game is starting-------
I am player 1
0 0 0 
0 0 0 
0 1 0 
I am player 1
0 0 0 
1 2 0 
0 1 0 
I am player 1
1 0 0 
1 2 0 
0 1 2 
I am player 1
1 0 2 
1 2 1 
0 1 2 
Game Is Over!
```
TicClient.java (second client to join)
```
Sending: [hello]
Receiving: [new player command for player 2]
-------the game is starting-------
I am player 2
0 0 0 
0 2 0 
0 1 0 
I am player 2
0 0 0 
1 2 0 
0 1 2 
I am player 2
1 0 2 
1 2 0 
0 1 2 
I am player 2
1 0 2 
1 2 1 
2 1 2 
Game Is Over!
```
