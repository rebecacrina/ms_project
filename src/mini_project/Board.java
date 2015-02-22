package mini_project;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static mini_project.ImagesHolder.Image;

public class Board {

	private Field[][] fields;
	private int bombsCounter = 0; 
	private int numberOfBombs = 0;
	private int bombsRemaining; 
	private int columns, rows; 
	private JLabel bombsLabel; 

	/**
	 * constructor
	 * 
	 * @param bombs
	 *            set the initial number of bombs and the number will be updated
	 *            each time a flag is set
	 * @param mainPanel
	 *            when creating field, so it can be added to maiPanel
	 * @param rows
	 *            for the number of rows in the fields matrix
	 * @param columns
	 *            for the number of columns in the fields matrix
	 * @param numberOfBombs
	 *            for the number of bombs
	 */

	public Board(JLabel bombs, JPanel mainPanel, int rows, int columns, int numberOfBombs) {
		this.bombsLabel = bombs;
		this.setColumns(columns);
		this.setRows(rows);
		this.numberOfBombs = numberOfBombs;
		setBombsRemaining(numberOfBombs);
		fields = new Field[rows][columns];
		createFields();
		placeRandomBombs();
	}

	/**
	 * class for field that implements MouseListener must implement/override the
	 * all methods
	 */
	public void addAllButtons(JPanel mainPanel) {
		for (Field[] x : fields) {
			for (Field f : x) {
				mainPanel.add(f);
			}
		}
	}

	public void removeAllButtons(JPanel mainPanel) {
		for (Field[] x : fields) {
			for (Field f : x) {
				mainPanel.remove(f);
			}
		}
	}

	
	public void createFields() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				fields[i][j] = new Field();
				fields[i][j].addMouseListener(new ClassThatListensToTheFieldButtons());
			}
		}
	}
	
	public void placeRandomBombs(){
		while ( bombsCounter < numberOfBombs) {
			int randomXCoordinate = (int) (Math.random() * (rows));
			int randomYCoordinate = (int) (Math.random() * (columns));
			if (!fields[randomXCoordinate][randomYCoordinate].isBomb()) {
				fields[randomXCoordinate][randomYCoordinate].setBomb(true);
				
				bombsCounter++;
			}
		}
	}

	class ClassThatListensToTheFieldButtons implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean gameOver = false;
			for (int x = 0; x < getRows(); x++) {
				for (int y = 0; y < getColumns(); y++) {
					if (e.getSource() == fields[x][y]) {
						if (e.getButton() == MouseEvent.BUTTON1 && !fields[x][y].isFlag()) {
							if (fields[x][y].isBomb()) {
								fields[x][y].setIcon(new ImageIcon("images/bomba.png"));
								gameOverLost();
								gameOver = true;
								break;
							}
							fields[x][y].setExposed(true);
							setImage(fields[x][y], surroundingBombs(x, y));

							if (surroundingBombs(x, y) == 0) {
								checkZeroAndExpose(x, y);
							}
						} else if (e.getButton() == MouseEvent.BUTTON3) {
							if (fields[x][y].isFlag()) {
								fields[x][y].setIcon(null);
								fields[x][y].setText("");
								fields[x][y].setFlag(false);
								setBombsRemaining(getBombsRemaining() + 1);
							}

							else if (!fields[x][y].isFlagAndBomb()) {
								fields[x][y].setIcon(new ImageIcon("images/flag.png"));
								fields[x][y].setFlag(true);
								setBombsRemaining(getBombsRemaining() - 1);
							}
							bombsLabel.setText("  " + Integer.toString(getBombsRemaining()) + "  bombs  ");
						}
						
						if (gameOver == false)
							clickedNoFlagNoBomb();
					}
				}
			}
			checkPressedButtonsWin();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void setImage(Field f, int number) {
		if (number != 0)
			f.setIcon(Image[number - 1]);
		else
			f.setText("");
	}

	/**
	 * changes the color of the buttons and if [x][y] is "0" set the label to
	 * nothing
	 */
	public void clickedNoFlagNoBomb() {
		for (Field[] x : fields) {
			for (Field f : x) {
				if (!f.isFlag() && !f.isBomb())
					f.setBackground(Color.darkGray);
			}
		}
	}

	/**
	 * checks surrounding 8 squares for number of bombs (it does include itself,
	 * but has already been checked for a bomb so it won't matter)
	 * 
	 * @param x
	 * @param y
	 *            the coordinates for the field for which it give the number of
	 *            set bombs
	 * @return the number of the surrounding bombs
	 */
	public int surroundingBombs(int x, int y) {
		int surroundingBombs = 0;
		for (int q = x - 1; q <= x + 1; q++) {
			for (int w = y - 1; w <= y + 1; w++) {
				while (true) {
					if (q < 0 || w < 0 || q >= getRows() || w >= getColumns())
						break;
					if (fields[q][w].isBomb())
						surroundingBombs++;
					break;
				}
			}
		}
		return surroundingBombs;
	}

	/**
	 * exposes the surrounding 8 buttons, because the field has
	 * surroungdingBomms = 0, which means there is no bomb can be exposed
	 * 
	 * @param x
	 *            the coordinates for the field for which it will exposed the
	 *            surrounding fields
	 * @param y
	 */
	public void exposeTheSurroundingFields(int x, int y) {
		fields[x][y].setExposed(true);
		for (int q = x - 1; q <= x + 1; q++) {
			for (int w = y - 1; w <= y + 1; w++) {
				while (true) { 
					if (q < 0 || w < 0 || q >= getRows() || w >= getColumns())
						break;
					if (fields[q][w].isFlag())
						break;
					setImage(fields[q][w], surroundingBombs(q, w));
					break;
				}
			}
		}
	}

	/**
	 * this is what checks if a surrounding button has "0" is so expose it and
	 * checkZeroAndExpose around the exposed buttons until there is no more
	 * "0"'s
	 * 
	 * @param x
	 * @param y
	 *            the coordinates for the field
	 */
	public void checkSurroudingFieldForZeroAndExpose(int x, int y) {
		for (int q = x - 1; q <= x + 1; q++) {
			for (int w = y - 1; w <= y + 1; w++) {
				while (true) {
					if (q < 0 || w < 0 || q >= getRows() || w >= getColumns())
						break;
					if (fields[q][w].isFlag())
						break;
					if (!fields[q][w].isExposed() && surroundingBombs(q, w) == 0) {
						checkZeroAndExpose(q, w);
					}
					break;
				}
			}
		}
	}

	public void checkZeroAndExpose(int x, int y) {
		exposeTheSurroundingFields(x, y);
		checkSurroudingFieldForZeroAndExpose(x, y);
	}

	public void checkPressedButtonsWin() {
		boolean allExposed = true;
		for (Field[] x : fields) {
			for (Field f : x) {
				if (f.isFlag() && !f.isBomb()) {
					allExposed = false;
				}
				if (!f.isFlagAndBomb()) {
					allExposed = false;
					break;
				}
			}
		}
		if (allExposed) {
			gameOverWon();
		}
	}

	
	public void gameOverWon() {
		gameOverExposeAllBombs();
		JOptionPane frame = new JOptionPane(); 
		JOptionPane.showMessageDialog(frame, "Gongratulations! You won!", "Game won!", JOptionPane.PLAIN_MESSAGE);
	}

	public void gameOverLost() {
		gameOverExposeAllBombs();
		JOptionPane frame = new JOptionPane(); 
		JOptionPane.showMessageDialog(frame, "Sorry, but you lost!", "Game lost!", JOptionPane.PLAIN_MESSAGE);
	}

	public void gameOverExposeAllBombs() {
		for (Field[] x : fields) {
			for (Field f : x) {
				if (f.isBomb()) {
					f.setIcon(new ImageIcon("images/bomba.png"));
					f.setBackground(Color.red);
				} else
					// / disable all buttons
					f.setEnabled(false);
			}
		}
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getBombsRemaining() {
		return bombsRemaining;
	}

	public void setBombsRemaining(int bombsRemaining) {
		this.bombsRemaining = bombsRemaining;
	}
}
