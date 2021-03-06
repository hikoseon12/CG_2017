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
	private int NotChanged = 1;
	private int changed = 0;
	private int notChanged = 1;
	private int footLeftRight = 0;
	private ArrayList<Pnt> nearFootPnts;
	
	Pnt[] _3CirclePnt;
	double[] _3CircleRad;
	Pnt _ctrPnt;
	double _ctrRad;
	Pnt _next;
	Pnt _idealPnt;
	ArrayList<Pnt> _inner;
	
	public ClimbingControl() {
		nextStepIndex = 0;
		isLFused = false;
		isRFused = false;
		initTri = new Triangle(new Pnt(-initBoardSize, initBoardSize), new Pnt(initBoardSize, initBoardSize),
				new Pnt(0, -initBoardSize));
		dt = new Triangulation(initTri);
		triangleList = new ArrayList<Triangle>();
		vornList = new ArrayList<ArrayList<Pnt>>();
		nearFootPnts = new ArrayList<Pnt>();

		_3CirclePnt = new Pnt[3];
	    _ctrPnt = null;
	    _next = null;
	    _3CircleRad = new double[3];
	    _ctrRad = 0;
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
	public Pnt[] get3CirclePnts(){
		return _3CirclePnt;
		
	}
	public double[] get3CircleRads(){
		return _3CircleRad;
		
	}
	public Pnt getCenterCirclePnt(){
		return _ctrPnt;
	}
	public double getCenterCircleRad(){
		return _ctrRad;
	}
	public ArrayList<Pnt> getNearFootPnts(){
		return nearFootPnts;
	}
	public Pnt getNextStepPnt(){
		return _next;
	}
	public Pnt getIdealPnt(){
		return _idealPnt;
	}
	public ArrayList<Pnt> getInnerPnt(){
		return _inner;
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

	public void initVornoi() {
		HashSet<Pnt> done = new HashSet<Pnt>(initTri);
		vornList = new ArrayList<ArrayList<Pnt>>();
		for (Triangle triangle : dt)
			for (Pnt site : triangle) {
				if (!site.inRange(initBoardSize) || !site.inRange(initBoardSize))
					continue;
				if (done.contains(site))
					continue;
				done.add(site);
				List<Triangle> list = dt.surroundingTriangles(site, triangle);
				vornList.add(new ArrayList<Pnt>());
				for (Triangle tri : list)
					vornList.get(vornList.size() - 1).add(tri.getCircumcenter());
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

	public ArrayList<Pnt> getNearPointsInDT3(int index) {
		ArrayList<Pnt> res = getNearPointsInDT(index);
		Set<Pnt> ret = new HashSet<Pnt>();
		for (Pnt x : res) {
			ret.add(x);
			ret.addAll(getNearPointsInDT2(x.getIndex()));
		}
		return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
	}

	public ArrayList<Pnt> getNearPointsInDT4(int index) {
		ArrayList<Pnt> res = getNearPointsInDT(index);
		Set<Pnt> ret = new HashSet<Pnt>();
		for (Pnt x : res) {
			ret.add(x);
			ret.addAll(getNearPointsInDT3(x.getIndex()));
		}
		return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
	}
	
	public ArrayList<Pnt> getNearPointsInDT5(int index) {
		ArrayList<Pnt> res = getNearPointsInDT(index);
		Set<Pnt> ret = new HashSet<Pnt>();
		for (Pnt x : res) {
			ret.add(x);
			ret.addAll(getNearPointsInDT4(x.getIndex()));
		}
		return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
	}
	
	public ArrayList<Pnt> getNearPointsInDT6(int index) {
		ArrayList<Pnt> res = getNearPointsInDT(index);
		Set<Pnt> ret = new HashSet<Pnt>();
		for (Pnt x : res) {
			ret.add(x);
			ret.addAll(getNearPointsInDT5(x.getIndex()));
		}
		return new ArrayList<Pnt>(Arrays.asList(ret.toArray(new Pnt[0])));
	}
	public int getNearPointsInVornoi(Pnt pnt) {
		Triangle tri = dt.locate(pnt);
		if (tri == null)
			return -1;
		double dist = Math.pow(10, 8);
		Pnt small = null;
		double minDis = 10000.0;
		for (Pnt x : tri.toArray(new Pnt[0])) {
			
			if (GeomUtil.getDistance(x, pnt) < minDis) {
				small = x;
				minDis = GeomUtil.getDistance(x, pnt);
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
	
	public int movingHandStep(TargetStep ns, Pnt nextTarget) {
		Pnt movingH;
		Pnt notmovingH;
		int changed = 0;
		
		nearFootPnts.clear();
		
		if (ns.getHand() == TargetStep.LEFT_HAND) {
			movingH = pointList.get(man.getLh());
			notmovingH = pointList.get(man.getRh());
		} else {
			movingH = pointList.get(man.getRh());
			notmovingH = pointList.get(man.getLh());
		}
	
		if (pointList.get(man.getLf()).getX() <= notmovingH.getX()
				&& notmovingH.getX() <= pointList.get(man.getRf()).getX()
				
			    && man.getMinHandFeetHeight() <= Math.min(pointList.get(man.getRf()).getY()-nextTarget.getY(),
					pointList.get(man.getLf()).getY()-nextTarget.getY())
				&& Math.max(pointList.get(man.getRf()).getY()-nextTarget.getY(),
						pointList.get(man.getLf()).getY())-nextTarget.getY() <= man.getMaxHandFeetHeight())
			
		{
			ArrayList<Pnt> inner = GeomUtil.get3CircleTriangle2(notmovingH, pointList.get(man.getLf()),
					pointList.get(man.getRf()), man.getArmMaxLength(), man.getLegMaxLength(), man.getLegMaxLength());
			
						
			Pnt innerCenter = getCenterOfCircles(inner);
			
			double innerRadius = GeomUtil.getDistance(innerCenter, inner.get(0)) + man.getArmMaxLength();
			System.out.println("innerRadius:" +innerRadius);
			
			_3CirclePnt[0] = notmovingH;
			_3CirclePnt[1] = pointList.get(man.getLf());
			_3CirclePnt[2] = pointList.get(man.getRf());
			_3CircleRad[0] = man.getArmMaxLength();
			_3CircleRad[1] = man.getLegMaxLength();
			_3CircleRad[2] = man.getLegMaxLength();
			_ctrPnt = innerCenter;
			_ctrRad = innerRadius;
			_inner = inner;
			
			int holdNextShowIndex = nextStepIndex+1;
			if(targetList.size() <= holdNextShowIndex)
				holdNextShowIndex -= 1;
			_next = targetList.get(holdNextShowIndex).getPoint();
			
			if (GeomUtil.getDistance(innerCenter, nextTarget) <= innerRadius) {
				if (ns.getHand() == TargetStep.LEFT_HAND)
					man.setLh(ns.getIndex());
				else
					man.setRh(ns.getIndex());
				nextStepIndex++;
				return changed;
			}
		}
		return notChanged;
	}

	public int movingLf() {
		int changed = 0;
		nearFootPnts.clear();
		double LfHeight = pointList.get(man.getLf()).getY();
		
		int sameHandNextHold = Math.max(0, nextStepIndex-2);
		double nowHoldHeight = targetList.get(sameHandNextHold).getPoint().getY();
		
		
		double nextHoldHeight = pointList.get(findNextDiffHoldIndex(targetList.get((int)Math.max(0, nextStepIndex-1)).getIndex())).getY();

		LfHeight -= nowHoldHeight - nextHoldHeight;
		
	
		
		Pnt nowHoldPnt = targetList.get(sameHandNextHold).getPoint();
		Pnt nextHoldPnt = pointList.get(findNextDiffHoldIndex(targetList.get((int)Math.max(0, nextStepIndex-1)).getIndex()));
		double holdsDistance = GeomUtil.getDistance(nextHoldPnt, nowHoldPnt);
		Pnt rfPnt = pointList.get(man.getRf());
		Pnt vtPnt = new Pnt( rfPnt.getX() + man.getPossibleLegLength()*(nextHoldPnt.getX() - nowHoldPnt.getX())/holdsDistance, 
				rfPnt.getY() + man.getPossibleLegLength()*(nextHoldPnt.getY() - nowHoldPnt.getY())/holdsDistance);
		Pnt idealFootPnt = vtPnt;
		
		
		
		ArrayList<Pnt> nearFeet = getNearPointsInDT4(pointList.get(man.getRf()).getIndex());
		nearFootPnts = nearFeet;
		
		double preDistance = 9999999;
		double nextDistance = 0;
		Pnt nextFootPnt = null;
		int NextTargetHold = Math.min(targetList.size()-1, nextStepIndex+1);
		
		double lowHand = Math.max(pointList.get(man.getLh()).getY(), pointList.get(man.getRh()).getY());
		double highHand = Math.min(pointList.get(man.getLh()).getY(), pointList.get(man.getRh()).getY());
		
		
		ArrayList<Pnt> inner = GeomUtil.get3CircleTriangle2(pointList.get(man.getLh()), pointList.get(man.getRh()),
				pointList.get(man.getRf()), man.getArmMaxLength(), man.getArmMaxLength(), man.getLegMaxLength());
		
		Pnt innerCenter = getCenterOfCircles(inner);
		
		double innerCenterMaxRadius = getInnerCenterMaxRadius(innerCenter, inner);
		
		
		
		double innerRadius = innerCenterMaxRadius + man.getLegMaxLength();
		
		for (int index = 0; index < nearFeet.size(); index++) {
			double distanceHoldAndFoot = Math.abs(nearFeet.get(index).getY() - lowHand);
			nextDistance = GeomUtil.getDistance(idealFootPnt, nearFeet.get(index));

			if (nextDistance < preDistance && nearFeet.get(index).getX() < pointList.get(man.getRf()).getX()
					
				&& GeomUtil.getDistance(innerCenter, nearFeet.get(index)) <= innerRadius
				
				&& (nearFeet.get(index).getY()-highHand) <= man.getMaxHandFeetHeight()
				&& man.getMinHandFeetHeight() <= (nearFeet.get(index).getY()-lowHand)
				
				&& nearFeet.get(index).getX() <= pointList.get(man.getRh()).getX())
				
				
			{
				preDistance = nextDistance;
				nextFootPnt = nearFeet.get(index);
			}
		}
		
		_3CirclePnt[0] = pointList.get(man.getLh());
		_3CirclePnt[1] = pointList.get(man.getRh());
		_3CirclePnt[2] = pointList.get(man.getRf());
		_3CircleRad[0] = man.getArmMaxLength();
		_3CircleRad[1] = man.getLegMaxLength();
		_3CircleRad[2] = man.getLegMaxLength();
		_idealPnt = idealFootPnt;
		_ctrPnt = innerCenter;
		_ctrRad = innerRadius;
		_next = targetList.get(nextStepIndex).getPoint();
		_inner = inner;
		
		if(nextFootPnt == null || nextFootPnt == pointList.get(man.getLf()))
		{
			return notChanged;
		}
		man.setLf(nextFootPnt.getIndex());
		return changed;
	}

	public int movingRf() {
		nearFootPnts.clear();
		int sameHandNextHold = Math.max(0, nextStepIndex-2);
		Pnt nowHoldPnt = targetList.get(sameHandNextHold).getPoint();
		
		Pnt nextHoldPnt = pointList.get(findNextDiffHoldIndex(targetList.get((int)Math.max(0, nextStepIndex-1)).getIndex()));

		ArrayList<Pnt> inner = GeomUtil.get3CircleTriangle2(pointList.get(man.getLh()), pointList.get(man.getRh()),
				pointList.get(man.getLf()), man.getArmMaxLength(), man.getArmMaxLength(), man.getLegMaxLength());
		
		
		Pnt innerCenter = getCenterOfCircles(inner);
		
		 
		
		double innerCenterMaxRadius = getInnerCenterMaxRadius(innerCenter, inner);
		double innerRadius = innerCenterMaxRadius + man.getLegMaxLength();
		
		double holdsDistance = GeomUtil.getDistance(nextHoldPnt, nowHoldPnt);
		Pnt rfPnt = pointList.get(man.getRf());
		Pnt vtPnt = new Pnt( rfPnt.getX() + man.getPossibleLegLength()*(nextHoldPnt.getX() - nowHoldPnt.getX())/holdsDistance, 
				rfPnt.getY() + man.getPossibleLegLength()*(nextHoldPnt.getY() - nowHoldPnt.getY())/holdsDistance);
		
		Pnt idealRfPnt = vtPnt;
		
		double lowHand = Math.max(pointList.get(man.getLh()).getY(), pointList.get(man.getRh()).getY());
		double highHand = Math.min(pointList.get(man.getLh()).getY(), pointList.get(man.getRh()).getY());
		
		int idealFootIndex = getNearPointsInVornoi(idealRfPnt);
		ArrayList<Pnt> nearFeet = getNearPointsInDT6(idealFootIndex);
		nearFootPnts = nearFeet; 
		
		double preDistance = 9999999;
		double nextDistance = 0;
		Pnt nextFootPnt = null;//initialized
		int NextTargetHold = Math.min(targetList.size()-1, nextStepIndex+1);
		
		for (int index = 0; index < nearFeet.size(); index++) {
			nextDistance = GeomUtil.getDistance(idealRfPnt, nearFeet.get(index));
			double twoLegDistance = GeomUtil.getDistance(pointList.get(man.getLf()), nearFeet.get(index));

			if (nextDistance < preDistance 
					&& twoLegDistance <= man.getPossibleLegLength()
					
					&& GeomUtil.getDistance(innerCenter, nearFeet.get(index)) <= innerRadius
					
					&& (nearFeet.get(index).getY()-highHand) <= man.getMaxHandFeetHeight()
					&& man.getMinHandFeetHeight() <= (nearFeet.get(index).getY()-lowHand)
					
				
					&& (Math.min(nowHoldPnt.getX(),nextHoldPnt.getX()) -1) <= nearFeet.get(index).getX()
					&& nearFeet.get(index).getX() <= (Math.max(targetList.get((int)Math.max(0, nextStepIndex-1)).getPoint().getX(),nextHoldPnt.getX())+1))
					
					
			{
				preDistance = nextDistance;
				nextFootPnt = nearFeet.get(index);
			}
		}
		_3CirclePnt[0] = pointList.get(man.getLh());
		_3CirclePnt[1] = pointList.get(man.getRh());
		_3CirclePnt[2] = pointList.get(man.getLf());
		_3CircleRad[0] = man.getArmMaxLength();
		_3CircleRad[1] = man.getLegMaxLength();
		_3CircleRad[2] = man.getLegMaxLength();
		_ctrPnt = innerCenter;
		_ctrRad = innerRadius;
		_next = targetList.get(nextStepIndex).getPoint();
		_idealPnt = idealRfPnt;
		_inner = inner;
		
		if(nextFootPnt == null || nextFootPnt == pointList.get(man.getRf()))
		{
			return NotChanged;
		}
		man.setRf(nextFootPnt.getIndex());
		return changed;
	}

	public int doNextStep() {
		int fail = -1;
		int progress = 0;
		int  success = 1;
		int RHmoved = 2;
		int  LHmoved = 3;
		int  RFmoved = 4;
		int LFmoved = 5;
		
		int status = 0;
		_idealPnt = null;
		
		if (nextStepIndex == 0)
			curTarget = pointList.get(man.getLh());
		if (nextStepIndex >= targetList.size()) {
			return success;
		}
		
		TargetStep ns = targetList.get(nextStepIndex);
		Pnt nextTarget = pointList.get(ns.getIndex());
		int movedHand = 0;
		movedHand = movingHandStep(ns, nextTarget);
		NotChanged += movedHand; // FAIL 
		
		if(movedHand == 0)
		{
			int LEFT_HAND  = 0;
			int RIGHT_HAND = 1;
			if(ns.getHand() == LEFT_HAND)
				status = LHmoved;
			else if(ns.getHand() == RIGHT_HAND)
				status = RHmoved;
			NotChanged = 0;
			return status;
		}
		
		if(footLeftRight == 0)
		{	int returnValue = movingLf();
			NotChanged += (returnValue == 0? -NotChanged: returnValue);
			status = LFmoved;
			footLeftRight = 1;
		} 
		else if (footLeftRight == 1) {
			int returnValue = movingRf();
			NotChanged += (returnValue == 0? -NotChanged: returnValue);
			status = RFmoved;
			footLeftRight = 0;
		}
		
		 
		if(NotChanged >= 3)
		{
			status = fail;
		}
		return status; 
	}

	public int findNextDiffHoldIndex(int nowIndex) {
		int nextIndex = nowIndex;
		int i;
		for (i = nextStepIndex; nextIndex == nowIndex && i < targetList.size(); i++) {
			nextIndex = targetList.get(i).getIndex();
		}
		return nextIndex;
	}
	
	public Pnt getCenterOfCircles(ArrayList<Pnt> inner) {
		double centerX = 0.0;
		double centerY = 0.0;
		double sumCenterX = 0.0;
		double sumCenterY = 0.0;
		int numberOfPoints = 0;
		
		System.out.println("ineer: "+ inner);
		for (int i = 0; i < inner.size(); i++) {
			System.out.println(i+". :");
			if(inner.get(i) == null)
				;
			else
			{
				
				sumCenterX += inner.get(i).getX();
				sumCenterY += inner.get(i).getY();
				numberOfPoints++;
			}
		}
		centerX = sumCenterX/numberOfPoints;
		centerY = sumCenterY/numberOfPoints;
		
		return new Pnt(centerX, centerY);
	}
	
	public Pnt getInnerCenterOfCircles(ArrayList<Pnt> inner) {
		double centerX = 0.0;
		double centerY = 0.0;
		double sumCenterX = 0.0;
		double sumCenterY = 0.0;
		
		for (int i = 0; i < inner.size(); i++) {
			sumCenterX += inner.get(i).getX();
			sumCenterY += inner.get(i).getY();
		}
		centerX = sumCenterX/inner.size();
		centerY = sumCenterY/inner.size();
		
		return new Pnt(centerX, centerY);
	}
	
	public double getInnerCenterMaxRadius(Pnt innerCenter, ArrayList<Pnt> inner){
		double maxRadius = 0.0;
		double newRadius = 0.0;
		for (int i = 0; i < inner.size(); i++) {
			if(inner.get(i) != null)
			newRadius = GeomUtil.getDistance(innerCenter, inner.get(i));
			if(newRadius > maxRadius){
				maxRadius = newRadius;
			}
		}
		return maxRadius;
	}
}
