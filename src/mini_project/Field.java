package mini_project;

import java.awt.Color;

import javax.swing.JButton;

public class Field extends JButton {
	private static final long serialVersionUID = 1L;
	private boolean bomb;
	private boolean flag;
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
	
	public boolean isFlagAndBomb(){ 
		return isFlag() && isBomb();
	}
	public Field() {
		setEnabled(true);
		setText("");
		setBackground(Color.gray);
	}
}
