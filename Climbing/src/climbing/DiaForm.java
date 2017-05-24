package climbing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.*;

public class DiaForm extends JFrame  {

	private static final long serialVersionUID = 1L;

	JComboBox confFiles;
	JComboBox characters;
	
	public DiaForm()
	{
		super("Select Configuration File and Target Character");
		initUI();		
		
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout());
		
		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(2,1));
			
	}
	
		
	

}
