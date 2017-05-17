package climbing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;


public class ClimbingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Graphics g;
	public static int pointRadius = 3;
	
	private ArrayList<Pnt> pointList;
	
	public ClimbingPanel()
	{
		pointList = new ArrayList<Pnt>();
	}
	
	public void addPnt(Pnt point)
	{
		pointList.add(point);
	}
	
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        this.g = g;
        
        Color temp = g.getColor();
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                
        g.setColor(Color.BLUE);
        for(Pnt point : pointList)
        	draw(point);
        
        g.setColor(temp);
    }
    
    public void draw (Pnt point) {
        int r = pointRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }
    

}
