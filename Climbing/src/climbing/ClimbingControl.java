package climbing;

import java.util.ArrayList;

public class ClimbingControl {

	private ArrayList<Pnt> pointList;
	private Man man;	
    private ArrayList<TargetStep> targetList;
    private int nextStepIndex;
    private boolean isLFused, isRFused;
    private Pnt curTarget;
    
    public ClimbingControl() {
    	nextStepIndex = 0;
    	isLFused = false;
    	isRFused = false;
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
    
    
	public void doNextStep()
	{	
		if( nextStepIndex == 0 ) curTarget = pointList.get(man.getLh()); /* 초기에는 두 손으로 시작 */
		
		TargetStep ns = targetList.get(nextStepIndex);
		System.out.println(man);
		Pnt nextTarget = pointList.get(ns.getIndex());
				
		/* 두 손이  같은 곳에 있을 때 */
		if( man.getLh() == man.getRh())
		{
			/* To do:  실제로 움직이기 전에 가능한 움직임인지 확인해야 함 */ 
			if( ns.getHand() == TargetStep.LEFT_HAND)
				man.setLh( ns.getIndex());
			else man.setRh( ns.getIndex());
			nextStepIndex++;
			System.out.println(man);
			return;
		}
		
		Pnt curManCenter = GeomUtil.getCenterPointOfMan(man, pointList);
		
		/* 두 발이 같은 곳에 있을 때 */
		if( man.getLf() == man.getRf())
		{
			if( isLFused )
			{ /* 오른발 이동 */
			 
			  
				
			} else {  /* 왼발 이동  */
				
			}
		}
		
		
		
		
	}
	
	
}
