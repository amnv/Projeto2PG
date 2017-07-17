package p2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;

public class Tela extends JPanel{
	
	private static final long serialVersionUID = 1L;

	//Informacoes da cena
	private Camera camera;
	private Objeto objeto;
	private Iluminacao iluminacao;
	private double r;

	//Alg. do Pintor
	private double[][] zbuffer;
	private Graphics2D graphics;

	public Tela(Objeto objeto, Iluminacao iluminacao, Camera camera, double r) {
		this.camera = camera;
		this.objeto = objeto;
		this.iluminacao = iluminacao;
		this.r = r;
	}

	private void pintor(Objeto obj, Ponto A, Ponto B, Ponto C, double xMin, double xMax, double y, Ponto[] w) {
		double a, b, c;
		double[] coords;

		Ponto P1, P2, P3, P;
		Ponto N1, N2, N3, N;
		Ponto V;

		double[] color;

		Ponto pixel;

		for (int x = (int)Math.round(xMin); x <= Math.round(xMax); x++) {

			pixel = new Ponto(x, y, 0); //Ponto dentro do triangulo que vai ser pintado

			//Calcula o pixel em coordenadas de visao
			coords = Auxiliar.coordenadasBaricentricas(A, B, C, pixel);

			P1 = obj.pontosVisao[A.id];
			P2 = obj.pontosVisao[B.id];
			P3 = obj.pontosVisao[C.id];

			a = coords[0]*P1.x + coords[1]*P2.x + coords[2]*P3.x;
			b = coords[0]*P1.y + coords[1]*P2.y + coords[2]*P3.y;
			c = coords[0]*P1.z + coords[1]*P2.z + coords[2]*P3.z;

			P = new Ponto(a, b, c);

			//Consulta o z-buffer
			if (x >= 0 && y >= 0 && x < zbuffer.length && y < zbuffer[0].length && P.z > 0 && P.z < zbuffer[(int) x][(int) y]) {
				
				zbuffer[(int) x][(int) y] = P.z;
				
				Ponto v1 = new Ponto(0,0,0);
				Ponto v2 = new Ponto(0,0,0);
				
				//Normais dos vertices do triangulo
				N1 = obj.normaisVertices[A.id];
				N2 = obj.normaisVertices[B.id];
				N3 = obj.normaisVertices[C.id];
				
				v1.x = N2.x - N1.x;
				v1.y = N2.y - N1.y;
				v1.z = N2.z - N1.z;
				
				v2.x = N3.x - N1.x;
				v2.y = N3.y - N1.y;
				v2.z = N3.z - N1.z;
				
				//Normal do ponto atual
				a = coords[0]*N1.x + coords[1]*N2.x + coords[2]*N3.x;
				b = coords[0]*N1.y + coords[1]*N2.y + coords[2]*N3.y;
				c = coords[0]*N1.z + coords[1]*N2.z + coords[2]*N3.z;

				N = new Ponto(a, b, c);
				//System.out.println("Antes: x: " + N.x + ", y: " + N.y + ", z: " + N.z);
				 
				//System.out.println("Depois: x: " + N.x + ", y: " + N.y + ", z: " + N.z);
				N = Auxiliar.perturbarNormal(v1, v2, N, this.r);
				Auxiliar.normalizar(N);

				V = new Ponto(-P.x, -P.y, -P.z);

				if (Auxiliar.produtoEscalar(N, V) < 0) { //Garantir que aponta na direcao do observador
					N = new Ponto(-N.x, -N.y, -N.z);
				}

				//Determina a cor do pixel
				color = phong(P, N);

				//Pinta o pixel
				graphics.setColor(new Color((int) color[0], (int)color[1], (int)color[2]));
				graphics.drawLine((int) x, (int) y, (int) x, (int) y);
				
			}
		}
	}

	//Iluminacao de Phong. 
	private double[] phong(Ponto pixel, Ponto normal) {

		double[] cor = new double[3];

		double ka = iluminacao.ka;

		/*Componente do Ambiente*/
		cor[0] = ka*iluminacao.Ia[0];
		cor[1] = ka*iluminacao.Ia[1];
		cor[2] = ka*iluminacao.Ia[2];

		/*Vetor L*/
		Ponto L = new Ponto(iluminacao.Pl.x - pixel.x, iluminacao.Pl.y - pixel.y, iluminacao.Pl.z - pixel.z);
		Auxiliar.normalizar(L);

		/*Componente difusa*/
		double escalarLNormal = Auxiliar.produtoEscalar(L, normal);

		if (escalarLNormal > 0) {
			cor[0] += iluminacao.Od[0]*iluminacao.Il[0]*iluminacao.kd*escalarLNormal;
			cor[1] += iluminacao.Od[1]*iluminacao.Il[1]*iluminacao.kd*escalarLNormal;
			cor[2] += iluminacao.Od[2]*iluminacao.Il[2]*iluminacao.kd*escalarLNormal;
		}

		double aux = escalarLNormal;

		/*Vetor R*/
		Ponto R = new Ponto(2*aux*normal.x - L.x, 2*aux*normal.y - L.y, 2*aux*normal.z - L.z);
		
		/*Vetor V*/
		Ponto V = new Ponto(-pixel.x, -pixel.y, -pixel.z);
		Auxiliar.normalizar(V);

		double escalarVR = Auxiliar.produtoEscalar(V, R);
		//escalarVR = Auxiliar.perturbarNormal(V, R, Auxiliar.produtoVetorial(V, R), this.r);

		/*Componente Especular*/
		if (escalarVR > 0) {
			double rugosidade = iluminacao.ks*Math.pow(escalarVR, iluminacao.n);
			//rugosidade = iluminacao.n;
			cor[0] += iluminacao.Il[0]*rugosidade;
			cor[1] += iluminacao.Il[1]*rugosidade;
			cor[2] += iluminacao.Il[2]*rugosidade;
		}

		cor[0] = Math.min(cor[0], 255);
		cor[1] = Math.min(cor[1], 255);
		cor[2] = Math.min(cor[2], 255);

		return cor;
	}

	//Scanline - Algoritmo de Preenchimento	 
	private void scanline(Graphics g) {

		this.graphics = (Graphics2D) g;

		Dimension d = getSize();
		Insets i = getInsets();

		int v_height = d.height - i.top - i.bottom;
		int v_width = d.width - i.left - i.right;

		updateCoords(v_width, v_height);
		initZ_Buffer(v_width, v_height);

			for (int k = 0; k < objeto.triangulos.length; k++) {
				double xMin, xMax, yMin, yMax;
				double a1, a2, a3;
				Ponto[] P = new Ponto[3];
				Ponto A, B, C;
				boolean changed = false;

				P[0] = objeto.pontosTela[objeto.triangulos[k][0]];
				P[1] = objeto.pontosTela[objeto.triangulos[k][1]];
				P[2] = objeto.pontosTela[objeto.triangulos[k][2]];

				Arrays.sort(P);

				A = P[0];

				int orient = Auxiliar.orientacao(A.x, A.y, P[1].x, P[1].y, P[2].x, P[2].y);

				if (orient < 0) { /*Sentido horario*/
					B = P[1];
					C = P[2];
				} else if (orient > 0) { /*Sentido anti-horario*/
					B = P[2];
					C = P[1];
				} else if (P[1].x < A.x && P[2].x < A.x) {
					B = P[1];
					C = P[2];
				} else if (P[1].x > A.x && P[2].x > A.x) {
					B = P[2];
					C = P[1];
				} else if (P[1].x < P[2].x) {
					B = P[1];
					C = P[2];
				} else {
					B = P[2];
					C = P[1];
				}

				if (Auxiliar.isTriangle(A, B, C)) {
					
					if ((A.x < 0 && B.x < 0 && C.x < 0) 
							|| (A.y < 0 && B.y < 0 && C.y < 0)
							|| (A.x > zbuffer.length && B.x > zbuffer.length && C.x > zbuffer.length)
							|| (A.y > zbuffer[0].length && B.y > zbuffer[0].length && C.y > zbuffer[0].length)){
					
						//triangulo nao vai ser varrido pq ele esta fora da tela
					}else{
						
						yMax = P[2].y;
						yMin = A.y;

						a1 = ((double) ((int) B.y - (int) A.y) / (double) ((int) B.x - (int) A.x));
						a2 = ((double) ((int) C.y - (int) A.y) / (double) ((int) C.x - (int) A.x));
						a3 = ((double) ((int) C.y - (int) B.y) / (double) ((int) C.x - (int) B.x));

						xMin = xMax = A.x;

						if (Math.abs(A.y - B.y) == 0) {
							xMin = Math.min(A.x, B.x);
							xMax = Math.max(A.x, B.x);
							a1 = a3;
						} else if (Math.abs(A.y - C.y) == 0) {
							changed = true;
							xMin = Math.min(A.x, C.x);
							xMax = Math.max(A.x, C.x);
							a2 = a3;
						}
						
						
						for (int y = (int) yMin; y <= (int) yMax; y++) {

							pintor(objeto, A, B, C, xMin, xMax, y, P);

							if (!changed && (y == (int) B.y || y == (int) C.y)) {
								if (Math.abs(y - B.y) == 0) {
									a1 = a3;
								} else {
									a2 = a3;
								}
								changed = true;
							}
							
							//calculando o proximo ponto do triangulo a partir do xMin atual
							if (a1 != Double.POSITIVE_INFINITY && a1 != Double.NEGATIVE_INFINITY && a1 != 0
									&& a1 != Double.NaN)  { 
								xMin += 1 / a1;
							}
							
							//calculando o proximo ponto do triangulo a partir do xMax atual
							if (a2 != Double.POSITIVE_INFINITY && a2 != Double.NEGATIVE_INFINITY && a2 != 0
									&& a2 != Double.NaN) {
								xMax += 1 / a2;
							}
						}
					}
				}
			} 
			
	}

	/*Inicializa o Z-Buffer*/
	private void initZ_Buffer(int width, int height) {
		this.zbuffer = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.zbuffer[i][j] = Double.MAX_VALUE;
			}
		}
	}

	/*Coloca as coordenadas dos pontos em Coordenadas de Tela*/
	private void updateCoords(int width, int height) {
	
			for (int i = 0; i < objeto.pontosTela.length; i++) {
				objeto.pontosTela[i].x = (int) ((objeto.pontosTela[i].x + 1) * width / 2);
				objeto.pontosTela[i].y = (int) ((1 - objeto.pontosTela[i].y) * height / 2);
			}
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scanline(g);
	}


}
