package p2;

public class Auxiliar {
	
	public static double produtoEscalar(Ponto s, Ponto t) {
		double r = (s.x*t.x) + (s.y*t.y) + (s.z*t.z);
		return r;
	}
	
	static void normalizar(Ponto v){
		double produtoInterno = Auxiliar.produtoEscalar(v, v);
		double norma = Math.sqrt(produtoInterno);
		v.x = v.x / norma;
		v.y = v.y / norma;
		v.z = v.z / norma;
	}
	
	public static Ponto produtoVetorial(Ponto s, Ponto t) {
		double i = (s.y*t.z) - (s.z*t.y);
		double j = (s.z*t.x) - (s.x*t.z);
		double k = (s.x*t.y) - (s.y*t.x);
		
		return new Ponto(i, j, k);
	}
	
	static Ponto perturbarNormal(Ponto A,Ponto B, Ponto N, double rugosidade){
        Ponto C = new Ponto(0,0,0);
        
        double r1 = (double) (Math.random() * rugosidade);
        double r2 = (double) (Math.random() * rugosidade);
        C.x = ( (B.x * r1) + (A.x * r2) ) + N.x; 
        C.y = ( (B.y * r1) + (A.y * r2) ) + N.y; 
        C.z = ( (B.z * r1) + (A.z * r2) ) + N.z;
        
        return C;
   }
	
	public static double[] coordenadasBaricentricas(Ponto A, Ponto B, Ponto C, Ponto p) {
		double xA = A.x, yA = A.y;
		double xB = B.x, yB = B.y;
		double xC = C.x, yC = C.y;
		
		double x = p.x, y = p.y;
		
		double a_b_c[] = new double[3];
		double mult = (xA - xC)*(yB - yC) - (yA - yC)*(xB - xC);
		
		a_b_c[0] = ((yB - yC)*(x - xC) - (xC - xB)*(yC - y))/mult;
		a_b_c[1] = ((yC - yA)*(x - xC) - (xA - xC)*(yC - y))/mult;
		a_b_c[2] = 1 - a_b_c[0] - a_b_c[1];
		
		return a_b_c;
	}
	
	//Auxiliar p/ o Scanline
		public static int orientacao(double xA, double yA, double xB, double yB, double xC, double yC) {
			return (int) ((xB-xA)*(yC-yA) - (yB-yA)*(xC-xA));
		}
		
		public static boolean isTriangle(Ponto A, Ponto B, Ponto C) {
			return !(((int)A.x == (int)B.x && (int)A.y == (int)B.y) || ((int)A.x == (int)C.x && (int)A.y == (int)C.y) || ((int)B.x == (int)C.x && (int)B.y == (int)C.y));
		}

}
