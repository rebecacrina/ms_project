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
	
	// TODO: maybe bombsCount?
	private int count = 0; // variable used for counting bombs
	private int bombsRemaining; // counting the number of bombs placed
	
	// TODO: these certainly do not have to be class variables
	private int randomXCoordinate, randomYCoordinate; // random coordinates for
	
	// TODO: also, this does not have to be a class variable
	private JOptionPane frame = new JOptionPane(); 
	private int columns, rows; // number of rows, columns, and bombs

	// TODO: yes, but it's a label, try renaming it to labelBombs or bombsLabel
	// or something, else it might be confusing
	private JLabel bombs; // panel where the number of bombs remained is shown

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
		this.bombs = bombs;
		this.setColumns(columns);
		this.setRows(rows);
		setBombsRemaining(numberOfBombs);
		fields = new Field[rows][columns];

		// TODO: move all the code bellow in a separate method and dispose of
		// the "initField" method
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				fields[i][j] = new Field();
				initField(fields[i][j]);
			}
		}
		// adds the bombs to random places on the grid
		while (count < numberOfBombs) {
			randomXCoordinate = (int) (Math.random() * (rows));
			randomYCoordinate = (int) (Math.random() * (columns));
			if (!fields[randomXCoordinate][randomYCoordinate].isBomb()) {
				placeBomb(fields, randomXCoordinate, randomYCoordinate);
				count++;
			}
		}
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

	// TODO: method does only one thing. not really worth having it
	public void initField(Field f) {
		f.addMouseListener(new ClassThatListensToTheFieldButtons());
	}

	public void placeBomb(Field[][] fields, int x, int y) {
		fields[x][y].setBomb(true);
		fields[x][x].setCheckIfWon(true);
	}

	// TODO: no comments. let the code talk for itself
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
							fields[x][y].setCheckIfWon(true);
							setImage(fields[x][y], surroundingBombs(x, y));

							if (surroundingBombs(x, y) == 0) {
								checkZeroAndExpose(x, y);
							}
						} else if (e.getButton() == MouseEvent.BUTTON3) {
							if (fields[x][y].isFlag()) {
								fields[x][y].setIcon(null);
								fields[x][y].setText("");
								fields[x][y].setFlag(false);
								fields[x][y].setCheckIfWon(false);
								setBombsRemaining(getBombsRemaining() + 1);
							}

							else if (!fields[x][y].isCheckIfWon() || fields[x][y].isBomb()) {
								fields[x][y].setIcon(new ImageIcon("images/flag.png"));
								fields[x][y].setFlag(true);
								fields[x][y].setCheckIfWon(true);
								setBombsRemaining(getBombsRemaining() - 1);
							}
							bombs.setText("  " + Integer.toString(getBombsRemaining()) + "  bombs  ");
						}
						// TODO: uhm..stii tu.
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
				if (f.isCheckIfWon() && !f.isFlag() && !f.isBomb())
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
				while (true) {// makes sure that it wont have an error for
								// buttons next to the wall
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
				while (true) { // makes sure that it wont have an error for
								// buttons next to the wall
					if (q < 0 || w < 0 || q >= getRows() || w >= getColumns())
						break;
					if (fields[q][w].isFlag())
						break;
					fields[q][w].setCheckIfWon(true);
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
					// makes sure that it wont have an error for buttons next to
					// the wall
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

	/**
	 * first calls the exposeTheSurroundingField because field[x][y] has
	 * surroundingBombs(x,y)=0 then calls the surrounding bombs method to check
	 * if the newly exposed fields still have surroundingBombs(x,y)=0\ the
	 * process is repeated until there are no more fields with 0 unexposed
	 * 
	 * @param x
	 * @param y
	 *            the coordinates for the field
	 */
	public void checkZeroAndExpose(int x, int y) {
		exposeTheSurroundingFields(x, y);
		checkSurroudingFieldForZeroAndExpose(x, y);
	}

	/**
	 * checks if all the button without bombs have been pressed If so the game
	 * is won
	 */
	public void checkPressedButtonsWin() {
		boolean allExposed = true;
		for (Field[] x : fields) {
			for (Field f : x) {// if there is a flag and no bomb => all are not
								// exposed
				if (f.isFlag() && !f.isBomb()) {
					allExposed = false;
				}
				if (!f.isCheckIfWon()) {
					allExposed = false;
					break;
				}
			}
		}// if all are exposed then the game is won
		if (allExposed) {
			gameOverWon();
		}
	}

	/**
	 * this method is called if bomb is clicked or on the double click if flag
	 * is not on a bomb this method exposes all bombs and a message will appear
	 */
	public void gameOverWon() {
		gameOverExposeAllBombs();
		JOptionPane.showMessageDialog(this.frame, "Gongratulations! You won!", "Game won!", JOptionPane.PLAIN_MESSAGE);
	}

	public void gameOverLost() {
		gameOverExposeAllBombs();
		JOptionPane.showMessageDialog(this.frame, "Sorry, but you lost!", "Game lost!", JOptionPane.PLAIN_MESSAGE);
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
