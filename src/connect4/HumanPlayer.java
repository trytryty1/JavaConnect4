package connect4;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HumanPlayer implements Player, MouseListener {
	int i = 0;
	@Override
	public int getPlacement() {
//		int[][] grid = Game.getGame().getGameBoard();
//		int col;
//		do {
//			col = (int) (Game.GameBoardDimension.width * Math.random());
//		} while (!Game.getGame().canPlaceToken(col));
//		return col;
		while(true) {
			try {
				if(i != -1) {

					if (Game.getGame().canPlaceToken(i)) {
						int temp = i;
						i = -1;
						return temp;
					} else {
						i = -1;
					}
				}
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("clicked: " + e.getX() + "      " + e.getY());
		if(i == -1)
		i = Main.GamePanel.getColumnForClick(e.getX());
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
