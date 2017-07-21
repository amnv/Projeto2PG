package p2;

public class Ponto implements Comparable<Ponto> {
	double x, y, z;
	int id;
	
	public Ponto (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int compareTo(Ponto p) {
		return (int) Math.signum(y - ((Ponto) p).y);
	}
}
