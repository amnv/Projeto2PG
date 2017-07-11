package p2;

public class Camera {
	
	//foco da câmera
	Ponto C;
	
	/*Sistema de vista*/
	
	// eixo oy
	Ponto V;
	// eixo oz
	Ponto N;
	// eixo ox (igual a VxN)
	Ponto U; 
	
	/*Área do sistema de vista*/
	double hx;
	double hy;
	
	// distância do foco até o plano de vista
	double d; 
	
	public Camera(Ponto c, Ponto v, Ponto n, Ponto u, double hx, double hy, double d) {		
		this.C = c;
		this.V = v;
		this.N = n;
		this.U = u;
		this.hx = hx;
		this.hy = hy;
		this.d = d;
	}

}
