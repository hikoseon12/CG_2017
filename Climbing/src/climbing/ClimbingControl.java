package climbing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

public class ClimbingControl {
	  private static final boolean Pnt = false;
   private ArrayList<Pnt> pointList;
	  private Man man;	
   private ArrayList<TargetStep> targetList;
   private int nextStepIndex;
   private boolean isLFused, isRFused;
   private Pnt curTarget;
   private Triangulation dt;
   private ArrayList<Triangle> triangleList;
   HashMap<Pnt, Set<Pnt>> triPntHash;
   private int initBoardSize = 10000;
   public ClimbingControl() {
   	nextStepIndex = 0;
   	isLFused = false;
   	isRFused = false;
    Triangle tri = new Triangle(new Pnt(-initBoardSize,initBoardSize),
                                new Pnt(initBoardSize,initBoardSize),
                                new Pnt(0,-initBoardSize));
    dt = new Triangulation(tri);
    triangleList = new ArrayList<Triangle>();
   }    
   public ArrayList<Pnt> getPointList() {
   		return pointList;
   }
  	public ArrayList<Triangle> getDelaunayTriangles () {
  	  return triangleList;
   }
  	public ArrayList<Pnt> getNearestPointList(Pnt pnt){
  	  return null;
  	}
  	public void setPointList(ArrayList<Pnt> pointList) {
    		this.pointList = pointList;
  	}
  	public void initDTrangluation(){
  	  System.out.print("Initialize the Dtri..");
     for (Pnt pnt : pointList) {
       dt.delaunayPlace(pnt);
     }
     triPntHash = new HashMap<Pnt, Set<Pnt>>();
     for (Triangle triangle : dt){
       triangleList.add(triangle);
       Pnt[] pArr = (Pnt[])triangle.toArray(new Pnt[0]);
       for(int i = 0 ; i < pArr.length; i++){
         int j = (i+1)%pArr.length;
         if(!pArr[i].inRange(initBoardSize)||
            !pArr[j].inRange(initBoardSize)) continue;
         if(triPntHash.get(pArr[i])==null)
           triPntHash.put(pArr[i], new HashSet<Pnt>());
         triPntHash.get(pArr[i]).add(pArr[j]);
         
         if(triPntHash.get(pArr[j])==null)
           triPntHash.put(pArr[j], new HashSet<Pnt>());
         triPntHash.get(pArr[j]).add(pArr[i]);
       }
     }
     System.out.println("[Done]");
  	}
  
  	public ArrayList<Pnt> getNearPointsInDT(int index){
     return new ArrayList<Pnt>(
                  Arrays.asList(
                      triPntHash.get(pointList.get(index))
                      .toArray(new Pnt[0])));
   }
  	public ArrayList<Pnt> getNearPointsInDT(Pnt pnt){
     return new ArrayList<Pnt>(
         Arrays.asList(
             triPntHash.get(pnt)
             .toArray(new Pnt[1])));
   }
  	public ArrayList<Pnt> getNearPointsInDT2(int index){
     ArrayList<Pnt> res = getNearPointsInDT(index);
     Set<Pnt> ret = new HashSet<Pnt>();
     for(Pnt x : res){
       ret.addAll(getNearPointsInDT(x));
     }
     return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
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
  		System.out.println(getNearPointsInDT2(0));
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
