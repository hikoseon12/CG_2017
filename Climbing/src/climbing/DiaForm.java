package climbing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.*;

public class DiaForm extends JDialog  {

	private static final long serialVersionUID = 1L;

	JComboBox<String> confFiles;
	JComboBox<String> characters;
	
	String selectedFileName;
	String selectedCharacterName;
	int    selectedCharacterIndex;
	
	public DiaForm(JFrame parent)
	{
		super(parent, "Select Configurations", Dialog.ModalityType.DOCUMENT_MODAL);
		initUI();		
		
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout());
		
		JButton btConfirm = new JButton("OK");
		btConfirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				doOK();
			}
		});
		this.add(btConfirm, BorderLayout.SOUTH);
		
		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(2,1));
		
		confFiles =  new JComboBox<String>();
	    ArrayList<String> filenames = _FileReader.findFiles("conf", "conf");	
	    for(String name: filenames)
	    {
	    	confFiles.addItem(name);
	    }
        
	    pnMain.add(confFiles);
	    
	    characters = new JComboBox<String>();
	    characters.addItem("Skeleton");
	    characters.addItem("IronMan");
	    
	    pnMain.add(characters);
	    
	    this.add(pnMain, BorderLayout.CENTER);
	    
	}
	
	private void doOK()
	{
		selectedFileName = (String) confFiles.getSelectedItem();
		selectedCharacterName = (String) characters.getSelectedItem();
		selectedCharacterIndex = characters.getSelectedIndex();
		this.setVisible(false);
	}

	public String getSelectedFileName() {
		return selectedFileName;
	}

	public String getSelectedCharacterName() {
		return selectedCharacterName;
	}

	public int getSelectedCharacterIndex() {
		return selectedCharacterIndex;
	}
	

}
