package mini_project;

public class GameConfiguration {

	private final int boardRows;
	private final int boardColumns;
	private final int numberOfBombs;
	private final int sizeX;
	private final int sizeY;

	public GameConfiguration(int boardRows, int boardColumns,
			int numberOfBombs, int sizeX, int sizeY) {
		this.boardRows = boardRows;
		this.boardColumns = boardColumns;
		this.numberOfBombs = numberOfBombs;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public int getBoardRows() {
		return boardRows;
	}

	public int getBoardColumns() {
		return boardColumns;
	}

	public int getNumberOfBombs() {
		return numberOfBombs;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}
}
