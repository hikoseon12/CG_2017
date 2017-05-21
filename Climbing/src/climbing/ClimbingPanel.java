package climbing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ClimbingPanel extends JPanel {

	/**
	 * ClimbingPanel
	 */
	private static final long serialVersionUID = 1L;
	private Graphics g;
	public static int pointRadius = 3;
	public static int handRadius  = 5;

	private BufferedImage imgAndroid;
	AffineTransform transIdentity = new AffineTransform();
	
	private String title;
	
	private ArrayList<Pnt> pointList;
	
	private Pnt manLH, manRH, manLF, manRF;
	private Man man;
	private boolean isManDeclared = false;
	
	public ClimbingPanel()
	{
		pointList = new ArrayList<Pnt>();
		title = null;
		try{
			imgAndroid = ImageIO.read(new File("android_torso.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void addPnt(Pnt point)
	{
		pointList.add(point);
	}
	
	public void setMan(Man man)
	{
		this.man = man;
		isManDeclared = true;
	}
	
	public void clearMan() { isManDeclared = false; }
	
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        this.g = g;
               
        Color temp = g.getColor();
           
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        
        drawTitle(); /* draw Title */
        
        g.setColor(Color.BLUE);
        for(Pnt point : pointList)
        	draw(point);
        
        if( isManDeclared )   
        {	
        	manLH = pointList.get(this.man.getLh());
    		manRH = pointList.get(this.man.getRh());
    		manLF = pointList.get(this.man.getLf());
    		manRF = pointList.get(this.man.getRf());
        	
        	Pnt bisectH = GeomUtil.getCenter(manLH, manRH);
        	Pnt bisectF = GeomUtil.getCenter(manLF, manRF);
        	Pnt center = GeomUtil.getCenter(bisectH, bisectF);
        	Double angle = GeomUtil.getAngle(bisectH, bisectF);
        	
        	drawMan(manLH, manRH, manLF, manRF, center, angle);
        }
        
        g.setColor(temp);
    }
    
    
    public void drawTitle() {
    	if( title == null ) return;
    	
    	Color temp = g.getColor();
    	Font tempFont = g.getFont();
    	
    	g.setColor(Color.BLACK);
    	Font titleFont = new Font(g.getFont().getName(), Font.BOLD | Font.ITALIC , 15);
    	g.setFont(titleFont);
    	g.drawString(title, 25, 20);
    	
    	g.setColor(temp);
    	g.setFont(tempFont);
    }
    
    public void draw (Pnt point) {
        int r = pointRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }
    
    
    public void drawMan(Pnt leftHand, Pnt rightHand, Pnt leftFoot, Pnt rightFoot, Pnt center, double angle)
    {
    	Color temp = g.getColor();
    	
    	g.setColor(Color.RED);    	
    	drawHand(leftHand,5);
    	g.setColor(Color.GREEN);
    	drawHand(rightHand,7);
    	g.setColor(Color.RED); 
    	drawHand(leftFoot,5);
    	g.setColor(Color.GREEN);
    	drawHand(rightFoot,7);
    	    	
    	int maWidth = 50;
    	int maHeight = 90;
    	Image miniAndroid = imgAndroid.getScaledInstance(50, 90, Image.SCALE_DEFAULT);
    	
    	Graphics2D g2d = (Graphics2D) g;
    	Stroke tempStroke = g2d.getStroke();
    	g2d.setStroke(new BasicStroke(5));
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)leftHand.getX(), (int)leftHand.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g.drawLine((int)center.getX(), (int)center.getY(), (int)rightHand.getX(), (int)rightHand.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)leftFoot.getX(), (int)leftFoot.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)rightFoot.getX(), (int)rightFoot.getY());
    	
    	g2d.setStroke(tempStroke);
    	
    	AffineTransform trans = new AffineTransform();
    	trans.setToTranslation(center.getX() - maWidth/2, center.getY()- maHeight/2);
    	trans.rotate( angle, maWidth/2, maHeight/2); 	
    	    	
    	g2d.drawImage(miniAndroid, trans, null);
    	   	
    	g.setColor(temp);
    }
    
    public void drawHand(Pnt point, int r)
    {
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.drawOval(x-r, y-r, r+r, r+r);        
    }
    
}
