package connect4;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;

	private static HashMap<String, Player> players = new HashMap<String, Player>();
	private static GamePanel gamePanel;

	public static void main(String args[]) {
		Game game = Game.getGame();
		gamePanel = new GamePanel(game);
		
		JPanel uiPanel = new JPanel();
		GridLayout uiPanelLayout = new GridLayout(0,2);
		uiPanel.setLayout(uiPanelLayout);
		uiPanelLayout.setHgap(0);
		
		

		players.put("Iterative AI", new IterativeAI());
		players.put("Random AI", new RandomAIPlayer());
//		players.put("Davids AI - random", new RandomAIPlayer());
//		players.put("Dads AI - random", new RandomAIPlayer());
		players.put("Human Player", new HumanPlayer());
		
		
		gamePanel.addMouseListener((MouseListener) players.get("Human Player"));
		gamePanel.requestFocus();

		frame = new JFrame("Ai Battle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		String[] playerNames = new String[players.size()];
		Set<String> keys = players.keySet();
		int index = 0;
		for (String element : keys)
			playerNames[index++] = element;

		JLabel player1SelectorLabel = new JLabel("Player 1 (red): ");
		uiPanel.add(player1SelectorLabel);
		JComboBox<String> player1Selector = new JComboBox<String>(playerNames);
		uiPanel.add(player1Selector);

		JLabel player2SelectorLabel = new JLabel("Player 2 (black): ");
		uiPanel.add(player2SelectorLabel);
		JComboBox<String> player2Selector = new JComboBox<String>(playerNames);
		uiPanel.add(player2Selector);
		
		JLabel silderLabel = new JLabel("Speed: ");
		uiPanel.add(silderLabel);
		JSlider slider = new JSlider(JSlider.HORIZONTAL,
                0, 1000, 50);
		slider.setMajorTickSpacing(10);
		uiPanel.add(slider);
		
		JButton button = new JButton("Start");
		uiPanel.add(button);
		
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.add(uiPanel, BorderLayout.PAGE_END);
		frame.setSize(600, 700);
		frame.setVisible(true);
		
		gamePanel.setVisible(true);
		
		frame.pack();
		
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

					if(e.getSource() == button) {
						try {
							game.newGame(players.get(player1Selector.getSelectedItem()), players.get(player2Selector.getSelectedItem()), gamePanel);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

			}
			
		};
		
		//player1Selector.addActionListener(listener);
		//player2Selector.addActionListener(listener);
		button.addActionListener(listener);
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Game.getGame().setDelay(1000-slider.getValue());
				
			}
		});
	}

	static class GamePanel extends JPanel {
		public static int TILE_SIZE = 75;
		private Game game;
		
		public static int getColumnForClick(int x) {
			return x/TILE_SIZE;
		}

		GamePanel(Game game) {
			this.game = game;
			setPreferredSize(new Dimension(Game.GameBoardDimension.width*TILE_SIZE, Game.GameBoardDimension.height*TILE_SIZE));
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			int width = Game.GameBoardDimension.width;
			int height = Game.GameBoardDimension.height;

			int longestDimension = getWidth()/width < getHeight()/height ? getWidth(): getHeight();
			TILE_SIZE = longestDimension/(getWidth()/width < getHeight()/height ? width : height);
			
			g.setColor(Color.black);
			int[][] grid = game.getGameBoard();
			for (int x = 0; x < width + 1; ++x) {
				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, height * TILE_SIZE);
			}
			for (int y = 0; y < height + 1; ++y) {
				g.drawLine(0, y * TILE_SIZE, width * TILE_SIZE, y * TILE_SIZE);
			}
			
			int totalGames = game.playerScores.getPlayer1Score()+game.playerScores.getplayer2Score();
			int winBarHeight = TILE_SIZE/7;
//			if(totalGames > 0) {
//				g.setColor(Color.black);
//				g.fillRect(0, 0, width*TILE_SIZE, TILE_SIZE/7);
//				g.setColor(Color.red);
//				g.fillRect(0, 0, width*, TILE_SIZE/7);
//				g.setColor(Color.black);
//				g.fillRect(0, 0, width*((game.playerScores.getPlayer1Score()+game.playerScores.getplayer2Score())/game.playerScores.getplayer2Score()), TILE_SIZE/7);
//			}
			
			if(grid != null) {
				for (int x = 0; x < width; ++x) {
					for (int y = 0; y < height; ++y) {
						if (grid[x][y] != 0) {
							//System.out.println("here");
							g.setColor(grid[x][y] == 1 ? Color.red : Color.black);
							g.fillOval(x * TILE_SIZE,TILE_SIZE * (height-1) - y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
						}
					}
				}
				
				if(Game.getGame().shouldDrawWinLocation()) {
					Point startofLine = new Point(Game.getGame().getStartOfWin().x * TILE_SIZE + TILE_SIZE / 2, TILE_SIZE * (height) - Game.getGame().getStartOfWin().y * TILE_SIZE - TILE_SIZE / 2); 
					if(Game.getGame().shouldDrawWinLocation()) {
						Point endofLine = null;
						switch(Game.getGame().getWinDirection()) {
						case HORIZONTAL:
							endofLine = new Point(startofLine.x + (3 * TILE_SIZE), startofLine.y);
							break;
						case VERTICAL:
							endofLine = new Point(startofLine.x, startofLine.y - (3 * TILE_SIZE));
							break;
						case DIAGONAL:
							endofLine = new Point(startofLine.x + (3*TILE_SIZE), startofLine.y - (3 * TILE_SIZE));
							break;					
						case REVERSE_DIAGONAL:
							endofLine = new Point(startofLine.x - (3*TILE_SIZE), startofLine.y - (3 * TILE_SIZE));
							break;
						default:
							break;
						}
						if(endofLine != null) {
							Graphics2D g2 = (Graphics2D) g;
							g2.setColor(Color.black);
							g2.setStroke(new BasicStroke(10));
							g2.drawLine(startofLine.x, startofLine.y, endofLine.x, endofLine.y);
						}
					}
				}
			}
			
		}
	}

}
