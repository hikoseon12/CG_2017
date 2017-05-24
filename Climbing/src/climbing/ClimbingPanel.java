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


class PntPair{
  Pnt first, second;
  public PntPair(Pnt first, Pnt second){
    this.first=first;this.second=second;
  }
};

public class ClimbingPanel extends JPanel {

	/**
	 * ClimbingPanel
	 */
	private static final long serialVersionUID = 1L;
	private Graphics g;
	public static int pointRadius = 3;
	public static int handRadius  = 5;

 private ArrayList<BufferedImage> imgBuffer;
	AffineTransform transIdentity = new AffineTransform();
	
	private String title;

 private ArrayList<Pnt> pointList;
 private ArrayList<Triangle> triList;
	
	private Pnt manLH, manRH, manLF, manRF;
	private Man man;
	private boolean isManDeclared = false;
	private boolean displayDT = false;
	
	public ClimbingPanel(String path)
	{
	  pointList = new ArrayList<Pnt>();
	  triList = new ArrayList<Triangle>();
		imgBuffer = new ArrayList<BufferedImage>();
		String imgNames[] = {"body","lhf","rhf","llb","rlb","lhb","rhb","llf","rlf"};
		title = null;
		try{
		  for(int i = 0; i < imgNames.length; i++){
		    imgBuffer.add(ImageIO.read(new File(path+"/body/"+imgNames[i]+".png")));
		  }
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public boolean isDisplayDT() {
		return displayDT;
	}



	public void setDisplayDT(boolean displayDT) {
		this.displayDT = displayDT;
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

 public void setDtriangle(ArrayList<Triangle> dtr)
 {
  this.triList = dtr;
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
         
        	
         PntPair body = new PntPair(GeomUtil.getProportionalPoint(center, bisectH, man.getBodyLength()/2),
                                    GeomUtil.getProportionalPoint(center, bisectF, man.getBodyLength()/2));
         PntPair LH = new PntPair(manLH, GeomUtil.getCircleIntersection(body.first,manLH,man.getFrontArmLength(),man.getBackArmLength(),false));
         PntPair RH = new PntPair(manRH, GeomUtil.getCircleIntersection(body.first,manRH,man.getFrontArmLength(),man.getBackArmLength(),true));
         PntPair LF = new PntPair(manLF, GeomUtil.getCircleIntersection(body.second,manLF,man.getFrontLegLength(),man.getBackLegLength(),true));
         PntPair RF = new PntPair(manRF, GeomUtil.getCircleIntersection(body.second,manRF,man.getFrontLegLength(),man.getBackLegLength(),false));

         ArrayList<PntPair> position = new ArrayList<PntPair>();
         position.add(body);
         position.add(LH);
         position.add(RH);
         position.add(LF);
         position.add(RF);
         drawMan(position, center, angle);
        	
        }
        
        if( displayDT ) {
        	/* Draw Triangulation when enabled */
	        for (Triangle triangle : triList) {
	          Pnt[] vertices = triangle.toArray(new Pnt[0]);
	          drawPolygon(vertices);
	        }
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
    public void drawPolygon (Pnt[] polygon) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setStroke(new BasicStroke(3));
      g2d.setColor(Color.RED);
      int[] x = new int[polygon.length];
      int[] y = new int[polygon.length];
      for (int i = 0; i < polygon.length; i++) {
          x[i] = (int) polygon[i].coord(0);
          y[i] = (int) polygon[i].coord(1);
      }
      g.drawPolygon(x, y, polygon.length);
    }
    
    public void drawMan(ArrayList<PntPair> pos, Pnt center, double angle)
    {
    	Color temp = g.getColor();
    	
    	for(int i = 1; i < 5; i++){
    	  g.setColor((i%2==1)?Color.RED:Color.GREEN);     
    	  drawHand(pos.get(i).first,(i%2==1?5:7));
    	}

     //System.out.println("�ִ� �ȱ��� : "+man.getArmMaxLength()+" ���ȱ��� : "+man.getBackArmLength()+" ���� ���� : " +man.getFrontArmLength());
     //System.out.println("�ִ� �ٸ����� : "+man.getLegMaxLength()+" �޴ٸ����� : "+man.getBackLegLength()+" �մٸ����� : " +man.getFrontLegLength());
    	
    	
    	Graphics2D g2d = (Graphics2D) g;
    	Stroke tempStroke = g2d.getStroke();
    	g2d.setStroke(new BasicStroke(3));
    	
    	for(int i = 0; i < 5; i++){
       g2d.setColor((i==0)?Color.BLACK:Color.GREEN);
       g2d.drawLine((int)pos.get(i).first.getX(), (int)pos.get(i).first.getY(),
                    (int)pos.get(i).second.getX(), (int)pos.get(i).second.getY());
     
       if(i!=0){
         g2d.drawLine((int)pos.get(i).second.getX(), (int)pos.get(i).second.getY(),
                      (int)(i<3?pos.get(0).first:pos.get(0).second).getX(),
                      (int)(i<3?pos.get(0).first:pos.get(0).second).getY());
       }
     }
     g2d.setStroke(tempStroke);

     int bodyWidth = 30;
     int otherWidth = 15;
     
     AffineTransform trans = new AffineTransform();
     for(int i = 0 ; i < 5; i++){
       int curWidth = (i==0?bodyWidth:otherWidth);
       int curHeight = (int) GeomUtil.getDistance(pos.get(i).first, pos.get(i).second);
       Pnt curCenter = GeomUtil.getCenter(pos.get(i).first, pos.get(i).second);
       double curAngle = GeomUtil.getAngle(pos.get(i).first, pos.get(i).second);
       
       trans.setToTranslation(curCenter.getX() - curWidth/2, curCenter.getY()- curHeight/2);
       trans.rotate(curAngle, curWidth/2, curHeight/2);
       g2d.drawImage(imgBuffer.get(i).getScaledInstance(curWidth, curHeight, Image.SCALE_DEFAULT), trans, null);
       
       if(i!=0){
         curHeight = (int) GeomUtil.getDistance(pos.get(i).second, (i<3?pos.get(0).first:pos.get(0).second));
         curCenter = GeomUtil.getCenter(pos.get(i).second, (i<3?pos.get(0).first:pos.get(0).second));
         curAngle = GeomUtil.getAngle(pos.get(i).second, (i<3?pos.get(0).first:pos.get(0).second));
         trans.setToTranslation(curCenter.getX() - curWidth/2, curCenter.getY()- curHeight/2);
         trans.rotate(curAngle, curWidth/2, curHeight/2);
         g2d.drawImage(imgBuffer.get(i+4).getScaledInstance(curWidth, curHeight, Image.SCALE_DEFAULT), trans, null);
         
       }
     }
    	g.setColor(temp);
    }
    
    public void drawHand(Pnt point, int r)
    {
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.drawOval(x-r, y-r, r+r, r+r);        
    }
    
}
