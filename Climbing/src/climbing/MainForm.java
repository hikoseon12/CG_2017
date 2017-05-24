package climbing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.*;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;

	ClimbingPanel pnClimbing;
    ClimbingControl control;	
	JButton btNext;
	JButton btShowDT;
	String confFileName;
	String characterName;
	int    characterIndex;
	boolean showDT = false;
	
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
		
		this.add(pnClimbing, BorderLayout.CENTER);
		
		JPanel pnControl = new JPanel();
		btNext = new JButton("Next");
		btNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				doNextButton();
			}
		});
		pnControl.add(btNext);
		
		btShowDT = new JButton("DT");
		btShowDT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				doDTClick();
			}
		});
		pnControl.add(btShowDT);
		
		this.add(pnControl, BorderLayout.SOUTH);
		
	}
	
	
	public void loadConf()
	{
		
		File file = new File("conf/"+ confFileName);
		ConfReader cr = new ConfReader();
		cr.parse(file);
		
		control = new ClimbingControl();
		
		Pnt rectPnt = cr.getBoundRectPnt();
		
		String title = cr.getTitle();
		if(title != null ) pnClimbing.setTitle(title);
		
		this.setSize((int)rectPnt.coord(0), (int)rectPnt.coord(1) + 100);
		
		ArrayList<Pnt> plist = cr.getPointList();
		control.setPointList(plist);
		control.initDTrangluation();
  
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
			
		pnClimbing.setDtriangle(control.getDelaunayTriangles());
		pnClimbing.repaint();
	}
	
	
	public void doNextButton() {
		 control.doNextStep();
		 pnClimbing.repaint();
	}
	
	public void doDTClick() {
		showDT = !showDT;
		pnClimbing.setDisplayDT(showDT);
		pnClimbing.repaint();
	}
	
	public void showConfDialog()
	{
		DiaForm df = new DiaForm(this);
		df.setSize(300,300);
		df.setVisible(true);
		
		//System.out.println("Selected: " + df.getSelectedFileName());
		
		this.confFileName = df.getSelectedFileName();
		this.characterIndex = df.getSelectedCharacterIndex();
		this.characterName = df.getSelectedCharacterName();
		pnClimbing.setImagePath("character/"+ this.characterName.toLowerCase());
	}
	
	
	public void loadDefaultConf()
	{
		this.confFileName = "Climbing.conf";
		this.characterIndex = 0;
		this.characterName ="Skeleton";
		
		loadConf();
	}
	public static void main(String[] args) {
	
		
		MainForm mf = new MainForm();		
		
		mf.setVisible(true);
		mf.showConfDialog();
		mf.loadConf();
		//mf.loadDefaultConf();
		
		
	}

}
