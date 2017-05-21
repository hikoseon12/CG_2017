package climbing;

import java.util.ArrayList;

public class GeomUtil {

	public static Pnt getCenter(Pnt a, Pnt b)
    {
    	return new Pnt( (a.getX() + b.getX())/2.0, (a.getY() + b.getY())/2.0 ); 
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
