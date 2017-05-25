package climbing;

public class Man {

	private int lh;
	private int rh;
	private int lf;
	private int rf;
	
	private int tall = 210;
	
	public double getArmMaxLength() { return 0.6*tall ; }
	public double getFrontArmLength() { return 0.4*getArmMaxLength() ; } 
	public double getBackArmLength() { return 0.6*getArmMaxLength() ;  }
	public double getLegMaxLength() { return 0.6*tall; }
    public double getFrontLegLength() { return 0.4*getLegMaxLength() ; } 
    public double getBackLegLength() { return 0.6*getLegMaxLength() ;  }
    public double getBodyLength() { return 0.28*tall ; } 
    public double getPossibleLegLength() { return 0.6*tall ; } 
    public double getMinHandFeetHeight() {return 0.5*tall ; } 
     
	public Man(int lh, int rh, int lf, int rf)
	{
		this.lh = lh;
		this.rh = rh;
		this.lf = lf;
		this.rf = rf;
	}
	
	public void setTall(int px)
	{
		this.tall = px;
	}
	public void setRealTall(int cm)
	{
		this.tall = (int)( cm / 18.0 * 25.0);
	}
	
	public int getTall() { return this.tall; }
	public int getRealTall()  {return (int) (this.tall * 25.0 / 18.0); } 
	
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
