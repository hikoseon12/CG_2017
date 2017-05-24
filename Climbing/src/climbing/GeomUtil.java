package climbing;

import java.util.ArrayList;

public class GeomUtil {

    public static Pnt getCenter(Pnt a, Pnt b)
    {
    	return new Pnt( (a.getX() + b.getX())/2.0, (a.getY() + b.getY())/2.0 ); 
    }
    // a -- aa -- /point/ -- bb -- b 
    public static Pnt getMiddlePoint(Pnt a, Pnt b, double aa, double bb){
     return new Pnt(a.getX()+aa*(b.getX()-a.getX())/(aa+bb),
                    a.getY()+aa*(b.getY()-a.getY())/(aa+bb)); 
    }
    // return point r which is far from a  
    public static Pnt getProportionalPoint(Pnt a, Pnt b, double leng){
      double dist = getDistance(a,b);
      Pnt c = b.subtract(a);
      Pnt r = new Pnt(c.getX()/dist*leng,c.getY()/dist*leng);
      return r.add(a);
    }
    public static int ccw(Pnt a, Pnt b, Pnt c){
      double term = a.getX()*b.getY()+b.getX()*c.getY()+c.getX()*a.getY();
      term -= a.getY()*b.getX()+b.getY()*c.getX()+c.getY()*a.getX();
      if (Math.abs(term)>Math.pow(10, -5)) return 1;
      else if (Math.abs(term)<Math.pow(10, -5))return -1;
      else return 0;
    }
    // if type 0 then return the point which is located on the cw direction
    // else if type 1 then return ccw direction
    public static Pnt getCircleIntersection(Pnt a, Pnt b, double aa, double bb, boolean type){
      double distAB = getDistance(a, b);
      double distAC = (aa*aa-bb*bb+distAB*distAB)/(2*distAB);
      double distCI = Math.sqrt(Math.abs(aa*aa-distAC*distAC));
      Pnt c = new Pnt(a.getX()+distAC*(b.getX()-a.getX())/distAB,
                  a.getY()+distAC*(b.getY()-a.getY())/distAB);
      // if meet 1 point
      if(Math.abs(distAB-aa-bb)-Math.pow(10, -5)<0) return c;
      
      Pnt in1 = new Pnt(c.getX()+distCI*(b.getY()-a.getY())/distAB,
                        c.getY()-distCI*(b.getX()-a.getX())/distAB);
      Pnt in2 = new Pnt(c.getX()-distCI*(b.getY()-a.getY())/distAB,
                        c.getY()+distCI*(b.getX()-a.getX())/distAB);
//      String temp = "a~ " + getDistance(a, in1)
//                    +" b~ "  + getDistance(b, in1)
//                    +" 직선거리 " + distAB;
//      System.out.println(temp);

//      System.out.println(a);
//      System.out.println(b);
//      System.out.println(c);
//      System.out.println(in1);
//      System.out.println(in2);
//      System.out.println(getDistance(b, new Pnt(155.551167,266.456229)));
//      System.out.println(getDistance(b, in2));
//      
      
      if(ccw(a,c,in1)==1)
        return (type?in2:in1);
      else  
        return (type?in1:in2);
    }
	/* getAngle(Pnt a, Pnt b)
	 * 점 b를 지나는 수직선을 기준으로, 선분 ab가 이루는 각도. 단위는 라디안	 
	 */
    public static double getAngle(Pnt a, Pnt b)
    {    	
    	double height = b.getY() - a.getY();
    	double width  = a.getX() - b.getX();
    	if( width == 0 ) return 0.0;
    	
    	double angle;
    	
    	if( width > 0 ) angle = Math.atan( width / height);
    	else angle = -1 * Math.atan( width*-1.0 / height);
    	   	
    	return angle;       	
    }
    
    public static double getDistance(Pnt a, Pnt b)
    {
    	return Math.sqrt( Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) );
    }
	
    public static Pnt getCenterPointOfMan(Man man, ArrayList<Pnt> pointList)
    {
    	Pnt manLH = pointList.get(man.getLh());
   		Pnt manRH = pointList.get(man.getRh());
   		Pnt manLF = pointList.get(man.getLf());
   		Pnt manRF = pointList.get(man.getRf());
       	
    	Pnt bisectH = GeomUtil.getCenter(manLH, manRH);
    	Pnt bisectF = GeomUtil.getCenter(manLF, manRF);
    	Pnt center = GeomUtil.getCenter(bisectH, bisectF);
    	
    	return center;
    }
    
}
