package connect4;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IterativeAI implements Player {

	int i = 0;
	@Override
	public int getPlacement() {
//		int[][] grid = Game.getGame().getGameBoard();
//		int col;
//		do {
//			col = (int) (Game.GameBoardDimension.width * Math.random());
//		} while (!Game.getGame().canPlaceToken(col));
//		return col;
		do {
			i = ++i >= 7 ? 0 : i;
		} while (!Game.getGame().canPlaceToken(i));
		
		return i;
	}

}
