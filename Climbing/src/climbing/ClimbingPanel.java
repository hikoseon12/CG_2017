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
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.xml.crypto.Data;

import org.omg.CORBA._PolicyStub;
import org.w3c.dom.events.EventException;



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
    private ArrayList<ArrayList<Pnt>> vornoiList;
   private ArrayList<TargetStep> targetList;
   private ArrayList<Pnt> nearFootList;
    
   private Pnt manLH, manRH, manLF, manRF;
   private Man man;
   private boolean isManDeclared = false;
   private boolean displayDT = false;
   private boolean displayVD = false;
   private boolean displayStatus = false;
   private boolean isImageReady = false;
   private int result = 0;
   
   Pnt[] _3CirclePnt;
   double[] _3CircleRad;
   Pnt _ctrPnt;
   double _ctrRad;
   Pnt _next;
   
   public ClimbingPanel(){
     pointList = new ArrayList<Pnt>();
     triList = new ArrayList<Triangle>();
     vornoiList = new ArrayList<ArrayList<Pnt>>();
     targetList = new ArrayList<TargetStep>();
     nearFootList = new ArrayList<Pnt>();
     title = null;
     _3CirclePnt = new Pnt[3];
     _ctrPnt = null;
     _next = null;
     _3CircleRad = new double[3];
     _ctrRad = 0;
   }
   public void setNearFootList(ArrayList<Pnt> nfl){
	   this.nearFootList = nfl;
   }
   public void set3CircleStatus(Pnt[] parr, double[] rarr){
      for(int i = 0; i < parr.length; i++){
         _3CirclePnt[i]=parr[i];
         _3CircleRad[i]=rarr[i];
      }
   }
   public void setResult(int res){
      this.result=res;
   }
   public void setCenterCircleStatus(Pnt p, double r){
      _ctrPnt = p;
      _ctrRad = r;
   }
   public void setNextStepStatus(Pnt p){
      _next = p;
   }
   
   public void setImagePath(String path){
      imgBuffer = new ArrayList<BufferedImage>();
      String imgNames[] = {"body","lhf","rhf","llb","rlb","lhb","rhb","llf","rlf","head"};
      
      try{
           for(int i = 0; i < imgNames.length; i++){
             imgBuffer.add(ImageIO.read(new File(path+"/body/"+imgNames[i]+".png")));
           }
            imgBuffer.add(ImageIO.read(new File("word/fail.png")));
            imgBuffer.add(ImageIO.read(new File("word/pass.png")));
         } catch (Exception e)
         {
            e.printStackTrace();
         }
        
      isImageReady = true;
   }
   
   public boolean isDisplayDT() {
      return displayDT;
   }



   public void setDisplayDT(boolean displayDT) {
      this.displayDT = displayDT;
   }

   public boolean isDisplayVD() {
      return displayVD;
   }

   public void setDisplayVD(boolean displayVD) {
      this.displayVD = displayVD;
   }
   public void setDisplayStatus(boolean displayStatus){
      this.displayStatus = displayStatus;
   }
   public void setTitle(String title)
   {
      this.title = title;
   }
   
   public void set3StatusCircles(Pnt[] parr, double rarr){
      
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
    public void setVornoiPoint(ArrayList<ArrayList<Pnt>> vp)
    {
     this.vornoiList = vp;
    }
    public void setTargetList(ArrayList<TargetStep> tl){
       this.targetList=tl;
    }
   public void clearMan() { isManDeclared = false; }
   
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        this.g = g;
               
        Color temp = g.getColor();
           
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        drawCurrntStep(result);
                        
        drawTitle(); /* draw Title */

        if(result==1 || result==-1){
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform trans = new AffineTransform();
            trans.setToTranslation(this.getWidth()/4,this.getHeight()/4);
            g2d.drawImage(imgBuffer.get((result==-1?10:11)).getScaledInstance(this.getWidth()/2, this.getHeight()/2, Image.SCALE_DEFAULT), trans, null); 
           return;
        }
        g.setColor(Color.BLUE);
       
        
        g.setColor(Color.BLUE);
        for(Pnt point : pointList)
           draw(point);
        
        if( isManDeclared && isImageReady)   
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
       PntPair head = new PntPair(GeomUtil.getProportionalPoint(center, bisectH, man.getBodyLength()),
                GeomUtil.getProportionalPoint(center, bisectH, man.getBodyLength()/3));
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
         position.add(head);
         drawMan(position, center, angle);
           
        }
        
        
        if( displayDT ) {
           drawDT();
        }
        
        if( displayVD ) {
           drawVD();
        }
        
        if( displayStatus){
        	drawStatus();

            drawLastMovedPoint(result);
        }
        
        for(TargetStep ts : targetList){
           drawCircle(ts.getPoint(),7,3, true, Color.MAGENTA);
        }
        
        g.setColor(temp);
    }
    public void drawDT(){
    	/* Draw Triangulation when enabled */
        for (Triangle triangle : triList) {
          Pnt[] vertices = triangle.toArray(new Pnt[0]);
          drawPolygon(vertices);
        }
    }
    public void drawVD(){
    	/* Draw Voronoi Diagram when enabled */
        for(int i = 0; i < vornoiList.size(); i++){
           drawPolygon(vornoiList.get(i).toArray(new Pnt[0]));          
        }
    }
    public void drawStatus(){
    	if(_ctrPnt == null) return;
    	
	    for(int i = 0; i < _3CirclePnt.length; i++){
	       drawCircle(_3CirclePnt[i],(int)_3CircleRad[i], 3, false, Color.BLUE);
	    }
        drawCircle(_ctrPnt,(int)_ctrRad, 3, false, Color.ORANGE);
        drawLine(_ctrPnt,_next,3,Color.CYAN);

       if(nearFootList.size()!=0){
       	for(int i = 0; i < nearFootList.size(); i++){
       		drawCircle(nearFootList.get(i), 3, 3, true, Color.YELLOW);
       	}
       }

    }
    public void drawLastMovedPoint(int i){
    	if(manLF==null || manRF==null || manLH==null || manRH==null) return;
    	switch(i){
		    case 2: drawCircle(manRH,15,4,false,Color.black); break;
		    case 3: drawCircle(manLH,15,4,false,Color.black); break;
		    case 4: drawCircle(manRF,15,4,false,Color.black); break;
		    case 5: drawCircle(manLF,15,4,false,Color.black); break;
		    default: 
		    	break;
    	}
    }
    
    public void drawCurrntStep(int i){
    	Color temp = g.getColor();
        Font tempFont = g.getFont();
        g.setColor(Color.BLACK);
        Font titleFont = new Font(g.getFont().getName(), Font.BOLD | Font.ITALIC , 15);
        g.setFont(titleFont);
        String stepRes = "";
        switch(i){
	        case 2: stepRes="Last trial : Right Hand"; break;
	        case 3: stepRes="Last trial : Left Hand"; break;
	        case 4: stepRes="Last trial : Right Foot"; break;
	        case 5: stepRes="Last trial : Left Foot"; break;
	        default:stepRes="In process";
        }
        g.drawString(stepRes, this.getWidth()-200, 20);
        
        g.setColor(temp);
        g.setFont(tempFont);
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
    
    public void drawLine(Pnt s, Pnt e, int stroke,Color color){
       Graphics2D g2d = (Graphics2D) g;
       g2d.setStroke(new BasicStroke(stroke));   
       g2d.setColor(color);
       g2d.drawLine((int)s.getX(), (int)s.getY(),
                 (int)e.getX(), (int)e.getY());
    }
    
    public void draw (Pnt point) {
        int r = pointRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }
    public void drawPolygon (Pnt[] polygon) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setStroke(new BasicStroke(1));
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
         drawCircle(pos.get(i).first,(i%2==1?9:11),3,false,(i%2==1)?Color.RED:Color.GREEN);
         
       }

     //System.out.println("�ִ� �ȱ��� : "+man.getArmMaxLength()+" ���ȱ��� : "+man.getBackArmLength()+" ���� ���� : " +man.getFrontArmLength());
     //System.out.println("�ִ� �ٸ����� : "+man.getLegMaxLength()+" �޴ٸ����� : "+man.getBackLegLength()+" �մٸ����� : " +man.getFrontLegLength());
       
       
       Graphics2D g2d = (Graphics2D) g;
       Stroke tempStroke = g2d.getStroke();
       g2d.setStroke(new BasicStroke(3));
       
       for(int i = 0; i < 5; i++){
       g2d.setColor((i==0)?Color.BLACK:Color.DARK_GRAY);
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
     int otherWidth = 20;
     
     AffineTransform trans = new AffineTransform();
     for(int i = 4 ; i >= 0; i--){
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
     int curWidth = otherWidth*2;
     int curHeight = (int) GeomUtil.getDistance(pos.get(5).first, pos.get(5).second);
     Pnt curCenter = GeomUtil.getCenter(pos.get(5).first, pos.get(5).second);
     double curAngle = GeomUtil.getAngle(pos.get(5).first, pos.get(5).second);
     
     trans.setToTranslation(curCenter.getX() - curWidth/2, curCenter.getY()- curHeight/2);
     trans.rotate(curAngle, curWidth/2, curHeight/2);

     g2d.drawImage(imgBuffer.get(9).getScaledInstance(curWidth, curHeight, Image.SCALE_DEFAULT), trans, null); 
     g.setColor(temp);
    }
    
    public void drawHand(Pnt point, int r)
    {
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.drawOval(x-r, y-r, r+r, r+r);        
    }
    public void drawCircle(Pnt point, int r, int stroke, boolean fill, Color color){
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(color);
       g2d.setStroke(new BasicStroke(stroke));
       int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        if(fill)

           g2d.fillOval(x-r, y-r, r+r, r+r);   
        else
           g2d.drawOval(x-r, y-r, r+r, r+r);        
        
    }
    
}