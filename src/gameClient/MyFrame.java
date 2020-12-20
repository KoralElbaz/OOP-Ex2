package gameClient;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a GUI class to present a
 * game on a graph.
 */
public class MyFrame extends JFrame {
	private int _ind;
	private Arena _ar;
	MyPanel jPanel;

	MyFrame(String a)
	{
		super(a);
		int _ind = 0;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	/**
	 * Initializing the _ar and the panel.
	 * @param ar the arena
	 */
	public void update(Arena ar)
	{
		this._ar = ar;
		initPanel();
		jPanel.update();
	}

	/**
	 * Constructor for MyPanel.
	 */
	private void initPanel() {
		jPanel=new MyPanel(_ar);
		this.add(jPanel);
	}

	/**
	 * Calls the panel and update the panel.
	 * @param g
	 */
	public void paint(Graphics g) {
		jPanel.updatePanel();
		jPanel.repaint();
	}


}