package climbing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class MainForm extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	ClimbingPanel pnClimbing;
    ClimbingControl control;	
	JButton btOne;
	
	public MainForm()
	{
		super("[Team #3] Climbing - for CG 2017 Term Project");
		initUI();		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout());
		
		pnClimbing = new ClimbingPanel();
		pnClimbing.addMouseListener(this);
		this.add(pnClimbing, BorderLayout.CENTER);
		
		JPanel pnControl = new JPanel();
		btOne = new JButton("Action");
		pnControl.add(btOne);
		
		this.add(pnControl, BorderLayout.SOUTH);
		
	}
	
	
	public void loadConf(File file)
	{
		ConfReader cr = new ConfReader();
		cr.parse(new File("Climbing.conf"));
		
		control = new ClimbingControl();
		
		Pnt rectPnt = cr.getBoundRectPnt();
		
		String title = cr.getTitle();
		if(title != null ) pnClimbing.setTitle(title);
		
		this.setSize((int)rectPnt.coord(0), (int)rectPnt.coord(1) + 100);
		
		ArrayList<Pnt> plist = cr.getPointList();
		control.setPointList(plist);
		
		for( Pnt point : plist)
			pnClimbing.addPnt(point);
		
		Man man = cr.getMan();
		if( man != null )
		{
			pnClimbing.setMan(man);
			control.setMan(man);
		}
		
		ArrayList<TargetStep> tslist = cr.getTargetStep();
		control.setTargetList(tslist);
				
		pnClimbing.repaint();
	
	}
	
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
        if (e.getSource() != pnClimbing) return;
        Pnt point = new Pnt(e.getX(), e.getY());
        //System.out.println("Click! " + point.toString());
        pnClimbing.addPnt(point);
        pnClimbing.repaint();
	}
	
		
	public static void main(String[] args) {

		MainForm mf = new MainForm();		
		mf.setSize(1000,600);
		mf.loadConf(new File("Climbing.conf"));
		mf.setVisible(true);

	}

}
