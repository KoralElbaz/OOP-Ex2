package gameClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
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
	public void update(Arena ar)
	{
		this._ar = ar;
		initPanel();
		jPanel.update();
	}
	private void initPanel() {
		jPanel=new MyPanel(_ar);
		this.add(jPanel);
	}
	public void paint(Graphics g) {
		jPanel.updatePanel();
		jPanel.repaint();
	}


}
