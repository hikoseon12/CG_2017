package climbing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;


public class ClimbingPanel extends JPanel {

	/**
	 * ClimbingPanel
	 */
	private static final long serialVersionUID = 1L;
	private Graphics g;
	public static int pointRadius = 3;
	public static int handRadius  = 5;

	
	private ArrayList<Pnt> pointList;
	
	private Pnt manLH, manRH, manLF, manRF;
	private boolean isManDeclared = false;
	
	public ClimbingPanel()
	{
		pointList = new ArrayList<Pnt>();
	}
	
	public void addPnt(Pnt point)
	{
		pointList.add(point);
	}
	
	public void setMan(Pnt lh, Pnt rh, Pnt lf, Pnt rf)
	{
		manLH = lh;
		manRH = rh;
		manLF = lf;
		manRF = rf;
		isManDeclared = true;
	}
	
	public void clearMan() { isManDeclared = false; }
	
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        this.g = g;
        
        Color temp = g.getColor();
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                
        g.setColor(Color.BLUE);
        for(Pnt point : pointList)
        	draw(point);
        
        if( isManDeclared ) drawMan(manLH, manRH, manLF, manRF);
        
        g.setColor(temp);
    }
    
    public void draw (Pnt point) {
        int r = pointRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }
    
    
    public void drawMan(Pnt leftHand, Pnt rightHand, Pnt leftFoot, Pnt rightFoot)
    {
    	Color temp = g.getColor();
    	
    	g.setColor(Color.RED);
    	
    	drawHand(leftHand);
    	drawHand(rightHand);
    	drawHand(leftFoot);
    	drawHand(rightFoot);
    	
    	g.setColor(temp);
    	
    }
    
    public void drawHand(Pnt point)
    {
    	int r = handRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.drawOval(x-r, y-r, r+r, r+r);        
    }
    
}
