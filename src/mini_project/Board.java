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

	 Field[][] fields;
	private int count = 0; // variable used for counting bombs
	int bombsRemaining; // counting the number of bombs placed
	private int randomXCoordinate, randomYCoordinate; // random coordinates for
														// bombs
	int columns, rows, numberOfBombs; // number of rows, columns, and bombs
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

	public Board(JLabel bombs, JPanel mainPanel, int rows, int columns,
			int numberOfBombs) {
		this.bombs = bombs;
		this.columns = columns;
		this.rows = rows;
		this.numberOfBombs = numberOfBombs;
		bombsRemaining = numberOfBombs;
		fields = new Field[rows][columns];
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
	public  void addAllButtons(JPanel mainPanel){
		for (Field[] x : fields) {
			for (Field f : x) {
				mainPanel.add(f.button);
			}
		}
	}
	public void removeAllButtons(JPanel mainPanel){
		for (Field[] x : fields) {
			for (Field f : x) {
				mainPanel.remove(f.button);
			}
		}
	}
	public void initField(Field f) {
		f.button.addMouseListener(new ClassThatListensToTheFieldButtons());
	}

	public void placeBomb(Field[][] fields, int x, int y) {
		fields[x][y].setBomb(true);
		fields[x][x].setCheckIfWon(true);
	}

	class ClassThatListensToTheFieldButtons implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean gameOver = false; // boolean variable set if there is a game
										// over or not
			for (int x = 0; x < rows; x++) {
				for (int y = 0; y < columns; y++) {
					if (e.getSource() == fields[x][y].button) {
						if (e.getButton() == MouseEvent.BUTTON1  && !fields[x][y].isFlag()) // if left click, and there is no flag on the button
						{
							if (fields[x][y].isBomb()) // if you you click on a bomb, results in game over
							{
								fields[x][y].button.setIcon(new ImageIcon(	"images/bomba.png"));
								gameOverLost(); // the gameOverLost method is called
								gameOver = true;
								break;
							}
							// otherwise there is a number so the field will be simply exposed
							fields[x][y].setExposed(true);
							fields[x][y].setCheckIfWon(true); // these set to true mean that the button has been clicked
							// this multiple if statement decide which image to set, depending on the number of the surrounding bombs
							setImage(fields[x][y], surroundingBombs(x, y));

							if (surroundingBombs(x, y) == 0) {
								checkZeroAndExpose(x, y); // calls a recursive method so that if a "0" is there the surrounding 8 buttons must be
															// exposed and if one of those is "0" it too must be exposed and so on
							}
						} else if (e.getButton() == MouseEvent.BUTTON3) // if it is a right click
						{
							if (fields[x][y].isFlag()) // if there is a flag already present set it so that there is no flag							
							{
								fields[x][y].button.setIcon(null);
								fields[x][y].button.setText("");
								fields[x][y].setFlag(false); // no flag
								fields[x][y].setCheckIfWon(false);
								bombsRemaining++; // because when set flag bombsRemaining--, player supposed there is bomb
							}
							// else there is bomb, or a number but no flag, set flag is allowed
							else if (!fields[x][y].isCheckIfWon()
									|| fields[x][y].isBomb()) // if there is no flag, set it so there is a flag		
							{
								fields[x][y].button.setIcon(new ImageIcon("images/flag.png"));
								fields[x][y].setFlag(true);
								fields[x][y].setCheckIfWon(true); // because we know that there is a bomb or a number							
								bombsRemaining--;
							}
							bombs.setText("  " + Integer.toString(bombsRemaining) + "  bombs  ");

						} else if (e.getButton() == MouseEvent.BUTTON2
								&& !fields[x][y].isFlag() 
								&& fields[x][y].isCheckIfWon()
								&& !fields[x][y].isBomb())
						// if both left and right click at the same time
						{ // only executes if there is no flag, it has been exposed, and no bomb					
							if (getNumberOfSurroundingFlags(x, y) == surroundingBombs(x, y)) // checks that the number of flags around [x][y] = the number of bombs around [x][y]
							{// now check all fields surrounding
								for (int q = x - 1; q <= x + 1; q++) {
									for (int w = y - 1; w <= y + 1; w++) {
										if (q < 0 || w < 0 || q >= rows || w >= columns)
											// makes sure that it won't have an error for buttons next to the wall
											break;
										if (!fields[q][w].isBomb() && fields[q][w].isFlag())
										// if there is no bomb, but there is a flag its game over
										{
											gameOverLost();
											gameOver = true;
											break;
										}
									}
								}
								if (gameOver == false) {
									exposeTheSurroundingFields(x, y); // exposes the surrounding 8 buttons
									checkZeroAndExpose(x, y); // checks if any of those are "0" and calls the recursive method
								}
							}
						}
						if (gameOver == false) // this just calls the method for changing the colors of the buttons if they have been clicked
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
		if(number!=0)
			f.button.setIcon(Image[number-1]);
		else
			f.button.setText("");
	}
		

	/**
	 * changes the color of the buttons and if [x][y] is "0" set the label to
	 * nothing
	 */
	public void clickedNoFlagNoBomb() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (fields[x][y].isCheckIfWon() && !fields[x][y].isFlag() && !fields[x][y].isBomb())
					fields[x][y].button.setBackground(Color.darkGray);
				if (!fields[x][y].isFlag() && surroundingBombs(x, y) == 0)
					fields[x][y].button.setText("");
			}
		}

	}

	/**
	 * get the number of surrounding flags
	 * 
	 * @param x
	 *            the coordinates for the field for which it give the number of
	 *            set flags
	 * @param y
	 * @return the number of the surrounding flags
	 */
	public int getNumberOfSurroundingFlags(int x, int y) {
		int surFlags = 0;
		for (int q = x - 1; q <= x + 1; q++) {
			for (int w = y - 1; w <= y + 1; w++) {
				while (true) {// makes sure that it wont have an error for buttons next to the wall
					if (q < 0 || w < 0 || q >= rows || w >= columns)
						break;
					if (fields[q][w].isFlag()) {
						surFlags++;
					}
					break;
				}
			}
		}
		return surFlags;
	}

	/**
	 * checks surrounding 8 squares for number of bombs (it does include
	 * itself, but has already been checked for a bomb so it won't matter)
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
				while (true) {// makes sure that it wont have an error for buttons next to the wall
					if (q < 0 || w < 0 || q >= rows || w >= columns)
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
	 *  exposes the surrounding 8 buttons, because the field has
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
				while (true) {	// makes sure that it wont have an error for buttons next to the wall
					if (q < 0 || w < 0 || q >= rows || w >= columns)
						break;
					if (fields[q][w].isFlag())
						break;
					fields[q][w].setCheckIfWon(true);
					setImage(fields[q][w], surroundingBombs(q,w));
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
					if (q < 0 || w < 0 || q >= rows || w >= columns)
						break;
					if (fields[q][w].isFlag())
						break;
					if (!fields[q][w].isExposed() && surroundingBombs(q, w) == 0) {
						exposeTheSurroundingFields(q, w);
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
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {// if there is a flag but no bomb all are not exposed
				if (fields[x][y].isFlag() && !fields[x][y].isBomb() ) {
					allExposed = false;
				}
				if (!fields[x][y].isCheckIfWon()) {
					allExposed = false;
					break;
				}
			}
		}// if all are exposed then the game is won
		if (allExposed) {
			gameOverWon();
			JOptionPane fra = new JOptionPane();
			JOptionPane.showMessageDialog(fra,
					"Gongratulations! You won!", "Game won!",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * this method exposes all the bombs, that have been avoided
	 */
	public void gameOverWon() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (fields[x][y].isBomb()) {
					fields[x][y].button.setIcon(new ImageIcon(	"images/bomba.png"));
					fields[x][y].button.setBackground(Color.red);
				} else	// disable all buttons
					fields[x][y].button.setEnabled(false);
			}
		}
	}

	/**
	 * this method is called if bomb is clicked or on the double click if flag
	 * is not on a bomb this method exposes all bombs and a message will appear
	 */
	public void gameOverLost() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (fields[x][y].isBomb()) {		// exposes all bombs
					fields[x][y].button.setIcon(new ImageIcon("images/bomba.png"));
					fields[x][y].button.setBackground(Color.red);
				} else   // disable all fields
					fields[x][y].button.setEnabled(false);
			}
		}

		JOptionPane frame = new JOptionPane();
		JOptionPane.showMessageDialog(	frame,
						"Sorry, but you lost!",
						"Game lost!", JOptionPane.PLAIN_MESSAGE);
	}

}
