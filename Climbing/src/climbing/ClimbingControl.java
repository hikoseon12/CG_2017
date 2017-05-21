package climbing;

import java.util.ArrayList;

public class ClimbingControl {

	private ArrayList<Pnt> pointList;
	private Man man;	
    private ArrayList<TargetStep> targetList;
	
    public ClimbingControl() {
    	
    }
    
    
    
    
	public ArrayList<Pnt> getPointList() {
		return pointList;
	}

	public void setPointList(ArrayList<Pnt> pointList) {
		this.pointList = pointList;
	}

	public Man getMan() {
		return man;
	}

	public void setMan(Man man) {
		this.man = man;
	}

	public ArrayList<TargetStep> getTargetList() {
		return targetList;
	}

	public void setTargetList(ArrayList<TargetStep> targetList) {
		this.targetList = targetList;
	}

    
	public ArrayList<Integer> getNearPoints(int index, double distance)
	{
		ArrayList<Integer> nearPoints = new ArrayList<Integer>();		
	    
		Pnt s = pointList.get(index);
		
		for(int i=0;i< pointList.size();i++)
		{
			if( i == index ) continue;
			Pnt t = pointList.get(i);
			if( GeomUtil.getDistance(s, t) <= distance ) 
			{ 
				nearPoints.add(index);
				System.out.println("Near Point:" + i + " ==> " + t.getX() + "," + t.getY());
			}
		}
		
		return nearPoints;
	}
    

	
	
}
