package mini_project;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 1
 * 
 * @author rebeca class of fields, the little buttons in the grid each has four
 *         boolean characteristics bomb = true if is a bomb flag = true if the
 *         flag was set exposed = true if the button was exposed checkIfWon =
 *         these set to true mean that the button has been clicked, if [x][y] =
 *         true then the button has a number on it or it is a bomb (used for
 *         checking if game is over)
 */
public class Field {
	JButton button;
	private boolean bomb;
	private boolean flag;
	private boolean checkIfWon;
	private boolean exposed;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isBomb() {
		return bomb;
	}

	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public boolean isCheckIfWon() {
		return checkIfWon;
	}

	public void setCheckIfWon(boolean checkIfWon) {
		this.checkIfWon = checkIfWon;
	}

	/**
	 * 
	 * @param mainPanel
	 *            create a field button and adds it to the mainPanel
	 */
	public Field(JPanel mainPanel) {
		button = new JButton();
		mainPanel.add(button);
	}
}
