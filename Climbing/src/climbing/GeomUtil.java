package climbing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GeomUtil {

	public static Pnt getCenter(Pnt a, Pnt b) {
		return new Pnt((a.getX() + b.getX()) / 2.0, (a.getY() + b.getY()) / 2.0);
	}

	// a -- aa -- /point/ -- bb -- b
	public static Pnt getMiddlePoint(Pnt a, Pnt b, double aa, double bb) {
		return new Pnt(a.getX() + aa * (b.getX() - a.getX()) / (aa + bb),
				a.getY() + aa * (b.getY() - a.getY()) / (aa + bb));
	}

	// return point r which is far from a
	public static Pnt getProportionalPoint(Pnt a, Pnt b, double leng) {
		double dist = getDistance(a, b);
		Pnt c = b.subtract(a);
		Pnt r = new Pnt(c.getX() / dist * leng, c.getY() / dist * leng);
		return r.add(a);
	}

	public static int ccw(Pnt a, Pnt b, Pnt c) {
		double term = a.getX() * b.getY() + b.getX() * c.getY() + c.getX() * a.getY();
		term -= a.getY() * b.getX() + b.getY() * c.getX() + c.getY() * a.getX();
		if (Math.abs(term) > Math.pow(10, -5))
			return 1;
		else if (Math.abs(term) < Math.pow(10, -5))
			return -1;
		else
			return 0;
	}

	// if type 0 then return the point which is located on the cw direction
	// else if type 1 then return ccw direction
	public static Pnt getCircleIntersection(Pnt a, Pnt b, double aa, double bb, boolean type) 
	{
		double distAB = getDistance(a, b);
		double distAC = (aa * aa - bb * bb + distAB * distAB) / (2 * distAB);
		double distCI = Math.sqrt(Math.abs(aa * aa - distAC * distAC));
		Pnt c = new Pnt(a.getX() + distAC * (b.getX() - a.getX()) / distAB,
				a.getY() + distAC * (b.getY() - a.getY()) / distAB);
		// if meet 1 point
		if (Math.abs(distAB - aa - bb) - Math.pow(10, -5) < 0)
			return c;

		Pnt in1 = new Pnt(c.getX() + distCI * (b.getY() - a.getY()) / distAB,
				c.getY() - distCI * (b.getX() - a.getX()) / distAB);
		Pnt in2 = new Pnt(c.getX() - distCI * (b.getY() - a.getY()) / distAB,
				c.getY() + distCI * (b.getX() - a.getX()) / distAB);

		// System.out.println(temp);

		// System.out.println(a);
		// System.out.println(b);
		// System.out.println(c);
		// System.out.println(in1);
		// System.out.println(in2);
		// System.out.println(getDistance(b, new Pnt(155.551167,266.456229)));
		// System.out.println(getDistance(b, in2));
		//

		if (ccw(a, c, in1) == 1)
			return (type ? in2 : in1);
		else
			return (type ? in1 : in2);
	}
	
	public static ArrayList<Pnt> getCircleIntersection2(Pnt a, Pnt b, double aa, double bb) 
	{
		ArrayList<Pnt> intersectionPnts = new ArrayList<Pnt>();
		double distAB = getDistance(a, b);
		double distAC = (aa * aa - bb * bb + distAB * distAB) / (2 * distAB);
		double distCI = Math.sqrt(Math.abs(aa * aa - distAC * distAC));
		Pnt c = new Pnt(a.getX() + distAC * (b.getX() - a.getX()) / distAB,
				a.getY() + distAC * (b.getY() - a.getY()) / distAB);
		// if meet 1 point
		if (Math.abs(distAB - aa - bb) - Math.pow(10, -5) < 0){
			intersectionPnts.add(c);
			return intersectionPnts;
		}
		Pnt in1 = new Pnt(c.getX() + distCI * (b.getY() - a.getY()) / distAB,
				c.getY() - distCI * (b.getX() - a.getX()) / distAB);
		Pnt in2 = new Pnt(c.getX() - distCI * (b.getY() - a.getY()) / distAB,
				c.getY() + distCI * (b.getX() - a.getX()) / distAB);

		intersectionPnts.add(in1);
		intersectionPnts.add(in2);
		
		return intersectionPnts;
	}
	
	public static double getAngle(Pnt a, Pnt b) {
		double height = b.getY() - a.getY();
		double width = a.getX() - b.getX();
		if (width == 0)
			return 0.0;

		double angle;

		if (width > 0)
			angle = Math.atan(width / height);
		else
			angle = -1 * Math.atan(width * -1.0 / height);

		return angle;
	}

	public static double getDistance(Pnt a, Pnt b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	public static Pnt getRightPointOfCircleAndLine(Pnt center, double radius, double height) {
		if (Math.abs(height - center.getY()) - radius > Math.pow(10, -5))
			return null;
		double delta = Math.sqrt(radius * radius - (height - center.getY()) * (height - center.getY()));
		return new Pnt(center.getX() + delta, height);
	}

	public static Pnt getRightPointOfCircleAndVector(Pnt Rfoot, double radius, Pnt nowHold, Pnt nextHold) {
		if (nowHold.equals(nextHold))
			nextHold = new Pnt(nowHold.getX() + 10.0, nowHold.getY());
		double holdDistance = getDistance(nowHold, nextHold);

		return new Pnt(radius * (nextHold.getX() - nowHold.getX()) / holdDistance + Rfoot.getX(),
				radius * (nextHold.getY() - nowHold.getY()) / holdDistance + Rfoot.getY());
	}

	public static Pnt getCenterPointOfMan(Man man, ArrayList<Pnt> pointList) {
		Pnt manLH = pointList.get(man.getLh());
		Pnt manRH = pointList.get(man.getRh());
		Pnt manLF = pointList.get(man.getLf());
		Pnt manRF = pointList.get(man.getRf());

		Pnt bisectH = GeomUtil.getCenter(manLH, manRH);
		Pnt bisectF = GeomUtil.getCenter(manLF, manRF);
		Pnt center = GeomUtil.getCenter(bisectH, bisectF);

		return center;
	}

	/*
	 * @params center points of circles, radius
	 * 
	 * @return 3 points of a triangle
	 */
	public static ArrayList<Pnt> get3CircleTriangle(Pnt a, Pnt b, Pnt c, double r1, double r2, double r3) {
		ArrayList<Pnt> inner = new ArrayList<Pnt>();
		Pnt[] parr = { a, b, c };
		double[] rarr = { r1, r2, r3 };
		Set<Integer> hash = new HashSet<Integer>();

		for (int i = 0; i < parr.length; i++)
			hash.add(parr[i].getIndex());
		if (hash.size() == 3)
			for (int i = 0; i < parr.length; i++) {
				int j = (i + 1) % parr.length;
				int k = (i + 2) % parr.length;
				Pnt p1 = GeomUtil.getCircleIntersection(parr[i], parr[j], rarr[i], rarr[j], false);
				Pnt p2 = GeomUtil.getCircleIntersection(parr[i], parr[j], rarr[i], rarr[j], true);
				if ((rarr[k] - GeomUtil.getDistance(p1, parr[k])) > Math.pow(10, -5))
					inner.add(p1);
				else
					inner.add(p2);
			}
		else {

			Pnt p1 = null;
			Pnt p2 = null;

			if (a.equals(b)) {
				p1 = GeomUtil.getCircleIntersection(a, c, r1, r3, false);
				p2 = GeomUtil.getCircleIntersection(a, c, r1, r3, true);
			} else if (a.equals(c)) {
				p1 = GeomUtil.getCircleIntersection(a, b, r1, r2, false);
				p2 = GeomUtil.getCircleIntersection(a, b, r1, r2, true);
			} else if (b.equals(c)) {
				p1 = GeomUtil.getCircleIntersection(a, c, r1, r3, false);
				p2 = GeomUtil.getCircleIntersection(a, c, r1, r3, true);
			}

			inner.add(p1);
			inner.add(p2);
			inner.add(null);
		}
		return inner;
	}
	
	public static ArrayList<Pnt> get3CircleTriangle2(Pnt a, Pnt b, Pnt c, double r1, double r2, double r3) {
		ArrayList<Pnt> inner = new ArrayList<Pnt>();
		ArrayList<Pnt> tempPnt = new ArrayList<Pnt>();
		Pnt[] parr = { a, b, c };
		double[] rarr = { r1, r2, r3 };
		Set<Integer> hash = new HashSet<Integer>();

		for (int i = 0; i < parr.length; i++)
			hash.add(parr[i].getIndex());
		if (hash.size() == 3)
			for (int i = 0; i < parr.length; i++) {
				int j = (i + 1) % parr.length;
				int k = (i + 2) % parr.length;
				ArrayList<Pnt> p1 = GeomUtil.getCircleIntersection2(parr[i], parr[j], rarr[i], rarr[j]);
				ArrayList<Pnt> p2 = GeomUtil.getCircleIntersection2(parr[i], parr[j], rarr[i], rarr[j]);
				
				tempPnt.addAll(p1);
				tempPnt.addAll(p2);
				
			}
		else {

			ArrayList<Pnt> p1 = new ArrayList<Pnt>();
			ArrayList<Pnt> p2 = new ArrayList<Pnt>();

			if (a.equals(b)) {
				p1 = GeomUtil.getCircleIntersection2(a, c, r1, r3);
				//p2 = GeomUtil.getCircleIntersection2(a, c, r1, r3);
			} else if (a.equals(c)) {
				p1 = GeomUtil.getCircleIntersection2(a, b, r1, r2);
				//p2 = GeomUtil.getCircleIntersection2(a, b, r1, r2);
			} else if (b.equals(c)) {
				p1 = GeomUtil.getCircleIntersection2(a, c, r1, r3);
				//p2 = GeomUtil.getCircleIntersection2(a, c, r1, r3);
			}

			tempPnt.addAll(p1);
			tempPnt.addAll(p2);
			//inner.add(null);
		}
		
		for(int i = 0; i < tempPnt.size(); i++){
			Pnt intersectionPnt = tempPnt.get(i);
			double distance1 = getDistance(a, intersectionPnt);
			double distance2 = getDistance(b, intersectionPnt);
			double distance3 = getDistance(c, intersectionPnt);
			
			if(distance1 <= r1 && distance2 <= r2 && distance3 <= r3){
				inner.add(tempPnt.get(i));
			}
		}
		return inner;
	}
	
	public static Pnt getCircleCenter(Pnt p1, Pnt p2, Pnt p3) {
		if (p3 == null) {
			return new Pnt(((p1.getX() + p2.getX()) / 2), ((p1.getY() + p2.getY()) / 2));
		}
		double offset = Math.pow(p2.getX(), 2) + Math.pow(p2.getY(), 2);
		double bc = (Math.pow(p1.getX(), 2) + Math.pow(p1.getY(), 2) - offset) / 2.0;
		double cd = (offset - Math.pow(p3.getX(), 2) - Math.pow(p3.getY(), 2)) / 2.0;
		double det = (p1.getX() - p2.getX()) * (p2.getY() - p3.getY())
				- (p2.getX() - p3.getX()) * (p1.getY() - p2.getY());

		if (Math.abs(det) < 0.0000001) {
			throw new IllegalArgumentException("Yeah, lazy.");
		}

		double idet = 1 / det;

		double centerx = (bc * (p2.getY() - p3.getY()) - cd * (p1.getY() - p2.getY())) * idet;
		double centery = (cd * (p1.getX() - p2.getX()) - bc * (p2.getX() - p3.getX())) * idet;

		return new Pnt(centerx, centery);
	}

	/*
	 * getCircleVectorIntersectionPoint Algorithm from
	 * https://stackoverflow.com/questions/13053061/circle-line-intersection-
	 * points
	 * 
	 */
	public static Pnt getCircleVectorIntersectionPoint(Pnt pointA, Pnt pointB, Pnt center, double radius) {
		double baX = pointB.getX() - pointA.getX();
		double baY = pointB.getY() - pointA.getY();
		double caX = center.getX() - pointA.getX();
		double caY = center.getY() - pointA.getY();

		if (baX == 0 && baY == 0)
			baX = 10;

		double a = baX * baX + baY * baY;
		double bBy2 = baX * caX + baY * caY;
		double c = caX * caX + caY * caY - radius * radius;

		double pBy2 = bBy2 / a;
		double q = c / a;

		double disc = pBy2 * pBy2 - q;
		if (disc < 0)
			return null;

		// if disc == 0 ... dealt with later
		double tmpSqrt = Math.sqrt(disc);
		double abScalingFactor1 = -pBy2 + tmpSqrt;
		double abScalingFactor2 = -pBy2 - tmpSqrt;

		if (baX < 0) {
			Pnt p1 = new Pnt(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
			return p1;
		}
		/*
		 * if (disc == 0) { // abScalingFactor1 == abScalingFactor2 return p1; }
		 */
		else {
			Pnt p2 = new Pnt(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
			return p2;
		}
	}
}
