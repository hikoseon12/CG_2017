package climbing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private ArrayList<ArrayList<Pnt>> vornList;
	HashMap<Pnt, Set<Pnt>> triPntHash;
	private Triangle initTri;
	private int initBoardSize = 10000;
	private int action; /////////////////////////////////이거 추가!!

	public ClimbingControl() {
		nextStepIndex = 0;
		isLFused = false;
		isRFused = false;
		initTri = new Triangle(new Pnt(-initBoardSize, initBoardSize), new Pnt(initBoardSize, initBoardSize),
				new Pnt(0, -initBoardSize));
		dt = new Triangulation(initTri);
		triangleList = new ArrayList<Triangle>();
        vornList = new ArrayList<ArrayList<Pnt>>();
		action = 0; ////////////////////////////////////이거 추가!!
	}

	public ArrayList<Pnt> getPointList() {
		return pointList;
	}

	public ArrayList<Triangle> getDelaunayTriangles() {
		return triangleList;
	}
	public ArrayList<ArrayList<Pnt>> getVornooiList() {
		return vornList;
	}

	public ArrayList<Pnt> getNearestPointList(Pnt pnt) {
		return null;
	}

	public void setPointList(ArrayList<Pnt> pointList) {
		this.pointList = pointList;
	}

	public void initDTrangluation() {
		System.out.print("Initialize the Dtri..");
		for (Pnt pnt : pointList) {
			dt.delaunayPlace(pnt);
		}
		triPntHash = new HashMap<Pnt, Set<Pnt>>();
		for (Triangle triangle : dt) {
			triangleList.add(triangle);
			Pnt[] pArr = (Pnt[]) triangle.toArray(new Pnt[0]);
			for (int i = 0; i < pArr.length; i++) {
				int j = (i + 1) % pArr.length;
				if (!pArr[i].inRange(initBoardSize) || !pArr[j].inRange(initBoardSize))
					continue;
				if (triPntHash.get(pArr[i]) == null)
					triPntHash.put(pArr[i], new HashSet<Pnt>());
				triPntHash.get(pArr[i]).add(pArr[j]);

				if (triPntHash.get(pArr[j]) == null)
					triPntHash.put(pArr[j], new HashSet<Pnt>());
				triPntHash.get(pArr[j]).add(pArr[i]);
			}
		}
		System.out.println("[Done]");
	}
	public void initVornoi(){
		HashSet<Pnt> done = new HashSet<Pnt>(initTri);
        vornList = new ArrayList<ArrayList<Pnt>>();
        for (Triangle triangle : dt)
            for (Pnt site: triangle) {
            	if (!site.inRange(initBoardSize) || !site.inRange(initBoardSize))
					continue;
                if (done.contains(site)) continue;
                done.add(site);
                List<Triangle> list = dt.surroundingTriangles(site, triangle);
                vornList.add(new ArrayList<Pnt>());
                for (Triangle tri: list)
                	vornList.get(vornList.size()-1).add(tri.getCircumcenter());
        }
	}

	public ArrayList<Pnt> getNearPointsInDT(int index) {
		return new ArrayList<Pnt>(Arrays.asList(triPntHash.get(pointList.get(index)).toArray(new Pnt[0])));
	}

	public ArrayList<Pnt> getNearPointsInDT(Pnt pnt) {
		return new ArrayList<Pnt>(Arrays.asList(triPntHash.get(pnt).toArray(new Pnt[1])));
	}

	public ArrayList<Pnt> getNearPointsInDT2(int index) {
		ArrayList<Pnt> res = getNearPointsInDT(index);
		Set<Pnt> ret = new HashSet<Pnt>();
		for (Pnt x : res) {
			ret.add(x);
			ret.addAll(getNearPointsInDT(x));
		}
		return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
	}
  	public int getNearPointsInVornoi(Pnt pnt){
 	     Triangle tri = dt.locate(pnt);
 	     if(tri==null) return -1;
 	     double dist = Math.pow(10,8);
 	     Pnt small = null;
 	     for(Pnt x : tri.toArray(new Pnt[0])){
 	    	 if(GeomUtil.getDistance(x, pnt)-dist<Math.pow(10, -5)){
 	    		 small = x;
 	    	 }
 	     }
 	     return small.getIndex();
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

	public ArrayList<Integer> getNearPoints(int index, double distance) {
		ArrayList<Integer> nearPoints = new ArrayList<Integer>();

		Pnt s = pointList.get(index);

		for (int i = 0; i < pointList.size(); i++) {
			if (i == index)
				continue;
			Pnt t = pointList.get(i);
			if (GeomUtil.getDistance(s, t) <= distance) {
				nearPoints.add(index);
				System.out.println("Near Point:" + i + " ==> " + t.getX() + "," + t.getY());
			}
		}

		return nearPoints;
	}

	public void doNextStep()
	{	
		if( nextStepIndex == 0 ) curTarget = pointList.get(man.getLh()); // 초기에는 두 손으로 시작
		if(nextStepIndex >= targetList.size()) {System.out.println("FINSHED ALL STEP"); return;}
		TargetStep ns = targetList.get(nextStepIndex);
		System.out.println(man);
		Pnt nextTarget = pointList.get(ns.getIndex());
				
		Pnt movingH;
		Pnt notmovingH;
		
		if(action == 0)
		{	
			if( ns.getHand() == TargetStep.LEFT_HAND)
			{
				movingH = pointList.get(man.getLh());
				notmovingH = pointList.get(man.getRh());
			}else
			{
				movingH = pointList.get(man.getRh());
				notmovingH = pointList.get(man.getLh());
			}
			
			//손이 닿는다.
			if(pointList.get(man.getLf()).getX() <= notmovingH.getX() && notmovingH.getX() <= pointList.get(man.getRf()).getX()) //손 한쪽을 땠을 떄 나머지 손이 양발 사이에 있다. 즉, 안정적이다)
			{	
				//현재 포지션에서 무게 중심을 구한다
				ArrayList<Pnt> inner = GeomUtil.get3CircleTriangle(notmovingH, pointList.get(man.getLf()), pointList.get(man.getRf()), man.getArmMaxLength(), man.getLegMaxLength(), man.getLegMaxLength());
				Pnt innerCenter = GeomUtil.getCircleCenter(inner.get(0), inner.get(1), inner.get(2));
				double innerRadius =  GeomUtil.getDistance(innerCenter, inner.get(0)) + man.getArmMaxLength();
				
				System.out.println("GeomUtil.getDistance(innerCenter, nextTarget)"+GeomUtil.getDistance(innerCenter, nextTarget)+"  "+innerRadius);
				if(GeomUtil.getDistance(innerCenter, nextTarget) <=  innerRadius)
				{	System.out.println("GeomUtil.getDistance(i)");
					if(ns.getHand() == TargetStep.LEFT_HAND)
						man.setLh( ns.getIndex());
					else
						man.setRh( ns.getIndex());
					nextStepIndex++;
					
					action = 0;//FINSH ONE ROUND
					return;
				}
			}
			//(손이 안닿으니 발을 움직여야한다)
			// *왼발LF 움직이기      
			double LfHeight = pointList.get(man.getLf()).getY();				
			double nowHoldHeight = targetList.get(nextStepIndex-1).getPoint().getY();
			double nextHoldHeight = pointList.get(findNextDiffHoldIndex(targetList.get(nextStepIndex-1).getIndex())).getY();
			
			LfHeight -= nowHoldHeight - nextHoldHeight;
			System.out.println("nowHoldHeight: "+nowHoldHeight+"nextHoldGap: "+nextHoldHeight+"LfHeight: "+LfHeight);
			LfHeight = Math.min(LfHeight,375);//범위 벗어났을 때 처리
			
			Pnt idealFootPnt =new Pnt(pointList.get(man.getRf()).getX(), LfHeight);
			ArrayList<Pnt> nearFeet = getNearPointsInDT2(pointList.get(man.getRf()).getIndex());
			//셋에서 가상의 점과 가장 가까운 그런데 이때 키 범위에 닿는지 판단
			
			double preDistance = 9999999;
			double nextDistance = 0;
			Pnt nextFootPnt = pointList.get(man.getRf()); //오른쪽 발로 세팅
			
			for(int index = 0; index < nearFeet.size(); index++)	
			{	
				nextDistance = GeomUtil.getDistance(idealFootPnt, nearFeet.get(index));
				if(nextDistance < preDistance && nearFeet.get(index).getX() <= pointList.get(man.getRf()).getX())
				{	
					//consider tall??
					System.out.println(":::nextDistance "+nextDistance+" preDistance "+preDistance);
					System.out.println(":::index "+index+ nearFeet.get(index));
					preDistance = nextDistance;
					nextFootPnt = nearFeet.get(index);
				}
			}	
			man.setLf(nextFootPnt.getIndex());
			action = 1;
		}
		else if(action == 1)
		{	
			double RfHeight = pointList.get(man.getRf()).getY();				
			double nowHoldHeight = targetList.get(nextStepIndex-1).getPoint().getY();
			double nextHoldHeight = pointList.get(findNextDiffHoldIndex(targetList.get(nextStepIndex-1).getIndex())).getY();
			
			RfHeight -= nowHoldHeight - nextHoldHeight;
			System.out.println("nowHoldHeight: "+nowHoldHeight+"nextHoldGap: "+nextHoldHeight);
			System.out.println("RfHeight: "+RfHeight);
			RfHeight = Math.min(RfHeight,375);//범위 벗어났을 때 처리
			
			ArrayList<Pnt> inner = GeomUtil.get3CircleTriangle(pointList.get(man.getLh()), pointList.get(man.getRh()), pointList.get(man.getLf()), man.getArmMaxLength(), man.getArmMaxLength(), man.getLegMaxLength());
			Pnt innerCenter = GeomUtil.getCircleCenter(inner.get(0), inner.get(1), inner.get(2));
			double innerRadius =  GeomUtil.getDistance(innerCenter, inner.get(0)) + man.getArmMaxLength();
			
			Pnt idealRfPnt =GeomUtil.getRightPointOfCircleAndLine(innerCenter, innerRadius, RfHeight);
			int idealFootIndex = getNearPointsInVornoi(idealRfPnt);
			ArrayList<Pnt> nearFeet = getNearPointsInDT2(idealFootIndex);
			if(nearFeet == null)
			{
				//nearFeet = new Pnt();
			}
			
			double preDistance = 9999999;
			double nextDistance = 0;
			Pnt nextFootPnt = pointList.get(man.getRf());//just initialized
			
			for(int index = 0; index < nearFeet.size(); index++)	
			{	
				nextDistance = GeomUtil.getDistance(idealRfPnt, nearFeet.get(index));
				double twoLegDistance = GeomUtil.getDistance(pointList.get(man.getRf()), nearFeet.get(index));
				if(nextDistance < preDistance && twoLegDistance <= man.getPossibleLegLength())
				{
					System.out.println(":::nextDistance "+nextDistance+" preDistance "+preDistance);
					System.out.println(":::index "+index+ nearFeet.get(index));
					preDistance = nextDistance;
					nextFootPnt = nearFeet.get(index);
				}
			}	
			man.setRf(nextFootPnt.getIndex());
			action = 0;
		}		
	}
	
	
	public int findNextDiffHoldIndex(int nowIndex)
	{
		int nextIndex = nowIndex;
		int i;
		for(i = nextStepIndex;  nextIndex == nowIndex && i < targetList.size();i++)
		{
			nextIndex = targetList.get(i).getIndex();
			System.out.println("i "+i+"nextIndex "+nextIndex+" nowIndex "+nowIndex);
		}
		return nextIndex;
	}

}
