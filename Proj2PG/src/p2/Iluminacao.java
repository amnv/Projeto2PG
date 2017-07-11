package p2;

public class Iluminacao {
	
	// Reflexão ambiental
	double ka;
	// Constante difusa
	double kd;
	// Parte especular
	double ks;
	// Constante de rugosidade
	double n;
	
	// Coordenadas da fonte de luz
	Ponto Pl;
	// Vetor da cor ambiental
	double[] Ia;
	// Vetor difuso
	double[] Od;
	// Cor da fonte de luz (em RGB)
	double[] Il;
	
	public Iluminacao(double ka, double kd, double ks, double n, Ponto pl, double[] ia, double[] od, double[] il) {
		
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.n = n;
		this.Pl = pl;
		this.Ia = ia;
		this.Od = od;
		this.Il = il;
				
	}

}
