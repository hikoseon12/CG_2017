package climbing;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class MainForm extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	ClimbingPanel pnClimbing;
	
	public MainForm()
	{
		super("Climbing - for CG 2017 Term Project");
		initUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout());
		
		pnClimbing = new ClimbingPanel();
		pnClimbing.addMouseListener(this);
		this.add(pnClimbing, BorderLayout.CENTER);
		
	}
	
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
        if (e.getSource() != pnClimbing) return;
        Pnt point = new Pnt(e.getX(), e.getY());
        pnClimbing.addPnt(point);
        pnClimbing.repaint();
	}
	
		
	public static void main(String[] args) {

		MainForm mf = new MainForm();
		mf.setSize(800,800);
		mf.setVisible(true);

	}

}
