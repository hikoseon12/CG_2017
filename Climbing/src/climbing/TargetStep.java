package climbing;

public class TargetStep {
	private int index;
	private Pnt point;
	private int hand; /* left : 0, right: 1 */
	
	static final int LEFT_HAND  =0;
	static final int RIGHT_HAND =1;
	
	public TargetStep(int index, Pnt point, int hand)
	{
		this.index = index;
		this.point = point;
		this.hand = hand;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Pnt getPoint() {
		return point;
	}

	public void setPoint(Pnt point) {
		this.point = point;
	}

	public int getHand() {
		return hand;
	}

	public void setHand(int hand) {
		this.hand = hand;
	}
	
	public String toString() { return "TargetStep: index="+index+" (" + point.getX() + "," + point.getY() + ") , " + (hand==0? "left" : "right"); }
}
