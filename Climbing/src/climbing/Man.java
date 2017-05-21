package climbing;

public class Man {

	private int lh;
	private int rh;
	private int lf;
	private int rf;
	
	private int tall = 170;
	
	public double getArmMaxLength() { return tall/2 ; }   
	public double getLegMaxLength() { return tall/2 + 15.0; }
	
	public Man(int lh, int rh, int lf, int rf)
	{
		this.lh = lh;
		this.rh = rh;
		this.lf = lf;
		this.rf = rf;
	}
	
	
	public int getLh() {
		return lh;
	}

	public void setLh(int lh) {
		this.lh = lh;
	}

	public int getRh() {
		return rh;
	}

	public void setRh(int rh) {
		this.rh = rh;
	}

	public int getLf() {
		return lf;
	}

	public void setLf(int lf) {
		this.lf = lf;
	}

	public int getRf() {
		return rf;
	}

	public void setRf(int rf) {
		this.rf = rf;
	}

	public String toString() 
	{
		return "MAN: "+ lh + ", " + rh + ", "+lf+", " + rf;
	}
	
}
