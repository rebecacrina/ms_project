package mini_project;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static mini_project.GameConfigurationsHolder.GCs;

@SuppressWarnings("serial")
public class Main extends JApplet {
	private Board board; // board object
	private int sizeX, sizeY;
	private Font font;
	private JPanel mainPanel, panel, restartButtonsPanel, newGamePanel;
	private JLabel remainingBombsLabel, newGameLabel;
	private Button[] restartButtons = new Button[3];
	private GridLayout grid;
	private int rows, columns, numberOfBombs;

	public Main() {
	}

	class ClickChangeDifficulty implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			for (int i = 0; i < restartButtons.length; i++) {
				if (e.getSource() == restartButtons[i]) {

					board.rows = GCs[i].getBoardRows();
					board.columns = GCs[i].getBoardColumns();
					numberOfBombs = GCs[i].getNumberOfBombs();
					sizeX = GCs[i].getSizeX();
					sizeY = GCs[i].getSizeY();
					restartGame(board.rows, board.columns, numberOfBombs,
							sizeX, sizeY);
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * init method for the applet set the gui for the game
	 */
	public void init() {
		setLayout(new BorderLayout());
		setInitialDifficulty(Difficulties.HARD.ordinal()); // hmmm super tare
		remainingBombsLabel = new JLabel();
		grid = new GridLayout(rows, columns);
		mainPanel = new JPanel(grid);
		font = new Font("ComicSans", Font.BOLD, 18);
		setFont(font);
		board = new Board(remainingBombsLabel, mainPanel, rows, columns,
				numberOfBombs);
		panel = new JPanel(new BorderLayout());
		newGamePanel = new JPanel();
		newGameLabel = new JLabel("New Game");
		initRestartButtons();
		add(panel, "North");
		add(mainPanel, "Center");
		panel.add(remainingBombsLabel, "West");
		panel.add(newGamePanel, "North");
		newGamePanel.add(newGameLabel);
		restartButtonsPanel = new JPanel();
		panel.add(restartButtonsPanel, "Center");
		restartButtonsPanel.add(restartButtons[0]);
		restartButtonsPanel.add(restartButtons[1]);
		restartButtonsPanel.add(restartButtons[2]);
		panel.setBackground(Color.lightGray);
		newGameLabel.setBackground(Color.lightGray);
		newGameLabel.setForeground(Color.black);
		remainingBombsLabel.setBackground(Color.lightGray);
		remainingBombsLabel.setForeground(Color.white);
		restartGame(rows, columns, numberOfBombs, sizeX, sizeY);
	}

	private void setInitialDifficulty(int d) {
		rows = GCs[d].getBoardRows();
		columns = GCs[d].getBoardColumns();
		numberOfBombs = GCs[d].getNumberOfBombs();
		sizeX = GCs[d].getSizeX();
		sizeY = GCs[d].getSizeY();
	}

	private void initRestartButtons() {
		restartButtons[0] = new Button("Easy");
		restartButtons[0].addMouseListener(new ClickChangeDifficulty());
		restartButtons[1] = new Button("Medium");
		restartButtons[1].addMouseListener(new ClickChangeDifficulty());
		restartButtons[2] = new Button("Hard");
		restartButtons[2].addMouseListener(new ClickChangeDifficulty());
	}

	/**
	 * method that restarts the game with the corresponding values for the
	 * difficulty level
	 * 
	 * @param rows
	 *            for the new game
	 * @param columns
	 *            for the new game
	 * @param numberOfBombs
	 *            for the new game
	 * @param sizex
	 *            the newly values corresponding to the difficulty level
	 * @param sizey
	 *            the newly values corresponding to the difficulty level
	 */
	public void restartGame(int rows, int columns, int numberOfBombs,
			int sizex, int sizey) {
		setSize(sizex, sizey);
		grid.setRows(board.rows);
		grid.setColumns(board.columns);

		board.bombsRemaining = numberOfBombs;
		remainingBombsLabel.setText("  "
				+ Integer.toString(board.bombsRemaining) + "  bombs  ");
		
		// TODO: the code bellow will not give you errors, yes!
		// what I want you to do is move this whole logic in the Board class where it actually belongs.
		// I don't think the 'main' class should know so much about the 'board' class. it should only 
		// be able to call a method like board.restartGame(...) and not have to deal with all this logic
		// this way, you hide "fields" with the 'private' access modifier
		for (Field[] x : board.fields) {
			for (Field f : x) {
				mainPanel.remove(f.button);
			}
		}
		board = new Board(remainingBombsLabel, mainPanel, rows, columns,
				numberOfBombs);

		for (Field[] x : board.fields) {
			for (Field f : x) {
				initButton(f.button);
			}
		}
		setSize(sizex, sizey);
		invalidate();
		validate();

	}

	private void initButton(JButton button) {
		button.setEnabled(true);
		button.setText("");
		button.setBackground(Color.gray);
	}
}
