package appendix;

class Lib {

	final double PI = Math.PI;		

	public double normalAngle(double kaku) {
		if(kaku > PI) {
			kaku -= PI * 2;
		}
		if(kaku < -PI) {
			kaku += PI * 2;
		}
		return kaku;
	}

	public double getRange( double x1,double y1, double x2,double y2 )
	{
		double xo = x2-x1;
		double yo = y2-y1;
		double h = Math.sqrt( xo*xo + yo*yo );
		return h;	
	}

}