package connect4;

public class RandomAIPlayer implements Player {

	@Override
	public int getPlacement() {
		int[][] grid = Game.getGame().getGameBoard();
		int col;
		do {
			col = (int) (Game.GameBoardDimension.width * Math.random());
		} while (!Game.getGame().canPlaceToken(col));
		return col;
	}

}
