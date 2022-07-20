package connect4;

import java.awt.Dimension;

import connect4.Main.GamePanel;
import java.awt.Point;

public class Game implements Runnable {

	public enum Turn {
		Player1, Player2, Empty, StaleMate
	}

	public enum WinDirection {
		HORIZONTAL, VERTICAL, DIAGONAL, REVERSE_DIAGONAL, NONE
	}
	
	public static Game game;
	
	public static Game getGame() {
		if(game == null) {
			game = new Game();
		}
		return game;
	}
	
	public static class Scores {
		private int score1, score2;

		public void resetScores() {
			score1 = 0;
			score2 = 0;
		}
		
		public void incrementScore1() {
			this.score1++;
			logScores();
		}
		
		public void incrementScore2() {
			this.score2++;
			logScores();
		}
		
		public void logScores() {
			System.out.println("Player 1 - " + score1 + "   :    Player 2 - " + score2);			
		}
		public int getPlayer1Score() {
			return score1;
		}

		public int getplayer2Score() {
			return score2;
		}
	}

	Thread thread;

	private GamePanel gamePanel;
	
	private boolean threadShouldRun = true;
	
	static Scores playerScores = new Scores();

	public static Dimension GameBoardDimension = new Dimension(7, 6);

	private int[][] gameBoard = new int[GameBoardDimension.width][GameBoardDimension.height];

	Player player1;
	Player player2;

	
	private Point startOfWin;
	
	public boolean shouldDrawWinLocation() {
		return winDirection != WinDirection.NONE;
	}
	public Point getStartOfWin() {
		return startOfWin;
	}



	public WinDirection getWinDirection() {
		return winDirection;
	}

	private WinDirection winDirection = WinDirection.NONE;
	
	
	private int delay;
	
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	private void startGame() throws PositionOutOfBoundsError {

		while(threadShouldRun) {
			resetBoard();
			Turn turn = Turn.Player1;
	
			while (threadShouldRun) {
				if (turn == Turn.Player1) {
					placeToken(player1.getPlacement(), Turn.Player1);
				} else {
					placeToken(player2.getPlacement(), Turn.Player2);
				}
				Main.frame.repaint();
				gamePanel.repaint();
				
				if (hasGameEnded() != Turn.Empty) {
					break;
				}
				turn = turn == Turn.Player1 ? Turn.Player2 : Turn.Player1;

				try {
					thread.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (turn == Turn.Player1) {
				playerScores.incrementScore1();
			} else if (turn == Turn.Player2) {
				playerScores.incrementScore2();
			} else {
				System.out.println("stalemate");
			}
			
			try {
				thread.sleep(delay * 2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	

	public boolean canPlaceToken(int column) {
		return gameBoard[column][GameBoardDimension.height -1] == getBoardValueForPlayer(Turn.Empty);
	}

	// return either the player who won or empty if no one one
	private Turn hasGameEnded() {

		// check horizontal win
		for (int row = 0; row < GameBoardDimension.height; ++row) {
			for (int col = 0; col < GameBoardDimension.width - 3; ++col) {
				if (gameBoard[col][row] != getBoardValueForPlayer(Turn.Empty)
						&& gameBoard[col][row] == gameBoard[col + 1][row]
						&& gameBoard[col][row] == gameBoard[col + 2][row]
						&& gameBoard[col][row] == gameBoard[col + 3][row]) {
					System.out.println("horizontal");
					winDirection = WinDirection.HORIZONTAL;
					startOfWin = new Point(col,row);
					if (gameBoard[col][row] == getBoardValueForPlayer(Turn.Player1)) {
						return Turn.Player1;
					} else {
						return Turn.Player2;
					}					
				}
			}
		}

		// check vertical win
		for (int col = 0; col < GameBoardDimension.width; ++col) {
			for (int row = 0; row < GameBoardDimension.height - 3; ++row) {
				if (gameBoard[col][row] != getBoardValueForPlayer(Turn.Empty)
						&& gameBoard[col][row] == gameBoard[col][row+1]
						&& gameBoard[col][row] == gameBoard[col][row+2]
						&& gameBoard[col][row] == gameBoard[col][row+3]) {
					if (gameBoard[col][row] == getBoardValueForPlayer(Turn.Player1)) {
						System.out.println("vertical");
						winDirection = WinDirection.VERTICAL;
						startOfWin = new Point(col,row);

						return Turn.Player1;
					} else {
						return Turn.Player2;
					}					
				}
			}
		}

		// check diagonal win
		for (int row = 0; row < GameBoardDimension.height - 3; ++row) {
			for (int col = 0; col < GameBoardDimension.width - 3; ++col) {
				if (gameBoard[col][row] != getBoardValueForPlayer(Turn.Empty)
						&& gameBoard[col][row] == gameBoard[col + 1][row + 1]
						&& gameBoard[col][row] == gameBoard[col + 2][row + 2]
						&& gameBoard[col][row] == gameBoard[col + 3][row + 3]) {
					System.out.println("diagonal");
					winDirection = WinDirection.DIAGONAL;
					startOfWin = new Point(col,row);

					if (gameBoard[col][row] == getBoardValueForPlayer(Turn.Player1)) {
						return Turn.Player1;
					} else {
						return Turn.Player2;
					}
				}
			}
		}
		
		// check diagonal win
		for (int row = 0; row < GameBoardDimension.height - 3; ++row) {
			for (int col = 3; col < GameBoardDimension.width; ++col) {
				if (gameBoard[col][row] != getBoardValueForPlayer(Turn.Empty)
						&& gameBoard[col][row] == gameBoard[col - 1][row + 1]
						&& gameBoard[col][row] == gameBoard[col - 2][row + 2]
						&& gameBoard[col][row] == gameBoard[col - 3][row + 3]) {
					System.out.println("diagonal reverse");
					winDirection = WinDirection.REVERSE_DIAGONAL;
					startOfWin = new Point(col,row);

					if (gameBoard[col][row] == getBoardValueForPlayer(Turn.Player1)) {
						return Turn.Player1;
					} else {
						return Turn.Player2;
					}
				}
			}
		}
		
		winDirection = WinDirection.NONE;

		// check for stalemate
		for (int row = 0; row < GameBoardDimension.height; ++row) {
			for (int col = 0; col < GameBoardDimension.width; ++col) {
				if (gameBoard[col][row] == getBoardValueForPlayer(Turn.Empty)) {
					return Turn.Empty;
				}
			}
		}
		System.out.println("Stalemate");
		return Turn.StaleMate;

	}

	private void placeToken(int column, Turn turn) throws PositionOutOfBoundsError {
		if (canPlaceToken(column)) {
			for (int row = 0; row < GameBoardDimension.height; ++row) {
				if (isBoardValueAtCoordinateEmpty(column, row)) {
					setBoardValueAtCoordinate(column, row, turn);
					return;
				}
			}
		}

		throw new PositionOutOfBoundsError();
	}

	private int getBoardValueForPlayer(Turn turn) {
		switch (turn) {
		case Player1:
			return -1;
		case Player2:
			return 1;
		case Empty:
			return 0;
		}
		return 0;
	}

	private void setBoardValueAtCoordinate(int column, int row, Turn turn) {
		gameBoard[column][row] = getBoardValueForPlayer(turn);
	}

	private int getBoardValueAtCoordinate(int column, int row) {
		return gameBoard[column][row];
	}

	private boolean isBoardValueAtCoordinateEmpty(int column, int row) {
		return getBoardValueAtCoordinate(column, row) == 0;
	}

	public int[][] getGameBoard() {
		return gameBoard;
	}

	public void reset() {
		Game.playerScores.resetScores();
		resetBoard();
		winDirection = WinDirection.NONE;
		
	}

	private void resetBoard() {
		winDirection = WinDirection.NONE;
		for(int row = 0; row < GameBoardDimension.height; ++ row) { 
			for (int col = 0; col < GameBoardDimension.width; ++col) {
				gameBoard[col][row] = getBoardValueForPlayer(Turn.Empty);
				
			}
		}		
		
	}
	public void setPlayer1(Player player) {
		player1 = player;
	}

	public void newGame(Player player, Player player3, GamePanel gamePanel) throws InterruptedException {
		if(thread != null && thread.isAlive()) {
			threadShouldRun = false;
			while(thread.isAlive()) {
				thread.sleep(100);
			}
		}
		
		thread = new Thread(this);
		this.player1 = player;
		this.player2 = player3;
		this.gamePanel = gamePanel;
		
		threadShouldRun = true;
		reset();
		thread.start();
	}
	
	public void run() {
		try {
			this.startGame();
		} catch (PositionOutOfBoundsError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
