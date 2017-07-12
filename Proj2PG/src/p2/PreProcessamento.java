package p2;

import java.io.BufferedReader;
import java.io.IOException;

/*
 * Classe para inicializar (...) dada a entrada do usuário
 */
public class PreProcessamento {
	
	/*Leitura dos dados da camera*/
	/*
	 * 
	 * Formato de entrada do arquivo de câmera (camera.cfg):
		/---------------------------------------\
		| -200 -50 300                          | ; C - Posicao da camera em coordenadas de mundo
		| 0.667 0.172 -1                        | ; Vetor N
		| 0 3 0                                 | ; Vetor V
		| 65 0.5 0.6                            | ; d hx hy
		|                                       |
		\---------------------------------------/
	 */
	public static Camera getCamera(BufferedReader camera) throws IOException {
		Ponto C, V, N, U;
		double d = 0, hx = 0, hy = 0;
		String x, y, z, linha;
		String[] temp;

		if (camera.ready()) {
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			C = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			N = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			V = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			d = Double.valueOf(temp[0]);
			hx = Double.valueOf(temp[1]);
			hy = Double.valueOf(temp[2]);

			camera.close();
			
			
			Auxiliar.normalizar(N);
			
			double coef = (Auxiliar.produtoEscalar(V, N) / Auxiliar.produtoEscalar(N, N));

			V.x = V.x - (coef * N.x);
			V.y = V.y - (coef * N.y);
			V.z = V.z - (coef * N.z);
			
			Auxiliar.normalizar(V);
			
			U = Auxiliar.produtoVetorial(N, V);
			/*-----------------------------------------------------------*/
			
			Camera cam = new Camera(C, V, N, U, hx, hy, d);

			return cam;
		} else {
			System.out.println("Erro ao ler arquivo de câmera!");
			return null;
		}

	}
	
	
	
	/*
	 * Leitura dos dados da iluminação
	 * 
	 * Formato de entrada do arquivo de iluminação(iluminacao.txt):
		/---------------------------------------\
		| -200 -50 300                          | ; Pl - Posicao da luz em coordenadas de mundo
		| 1                                     | ; ka - reflexao ambiental
		| 2 2 2                                 | ; Ia - vetor cor ambiental
		| 1                                     | ; kd - constante difusa
		| 1 1 1                                 | ; Od - vetor difuso
		| 0.5                                   | ; ks - parte especular
		| 0 255 0                               | ; Il - cor da fonte de luz
		| 2                                     | ; n  - constante de rugosidade
		|                                       |
		\---------------------------------------/

	 */
	public static Iluminacao getIluminacao(Camera camera, BufferedReader iluminacao) throws NumberFormatException, IOException {
		String x, y, z, linha, temp[];
		
		if (iluminacao.ready()) {
			Ponto pl;
			double ka, kd, ks, n;
			double[] ia = new double[3];
			double[] od = new double[3];
			double[] il = new double[3];
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			pl = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = iluminacao.readLine();
			ka = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			ia[0] = Double.valueOf(x);
			ia[1] = Double.valueOf(y);
			ia[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			kd = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			od[0] = Double.valueOf(x);
			od[1] = Double.valueOf(y);
			od[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			ks = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			il[0] = Double.valueOf(x);
			il[1] = Double.valueOf(y);
			il[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			n = Double.valueOf(linha);

			pl.x -= camera.C.x;
			pl.y -= camera.C.y;
			pl.z -= camera.C.z;

			double x_visao = (pl.x * camera.U.x) + (pl.y * camera.U.y) + (pl.z * camera.U.z);
			double y_visao = (pl.x * camera.V.x) + (pl.y * camera.V.y) + (pl.z * camera.V.z);
			double z_visao = (pl.x * camera.N.x) + (pl.y * camera.N.y) + (pl.z * camera.N.z);

			pl.x = x_visao;
			pl.y = y_visao;
			pl.z = z_visao;
			
			Iluminacao ilum = new Iluminacao(ka, kd, ks, n, pl, ia, od, il);
			return ilum;
		} else {
			System.out.println("Erro ao abrir a iluminação");
			return null;
		}
	}
	
	
	/*Leitura dos dados do objeto
	 * 
	 * Formato de entrada do arquivo de objeto(objeto.byu):
	 *                                 ; pontos do objeto (em coordenadas de mundo) e triangulos
		/---------------------------------------\
		| 3 1                                   | ; 3 pontos e 1 triangulo
		| 50.0000 0.0000 0.000                  | ; ponto 1: P1(50, 0, 0)
		| 0 50 0                                | ; ponto 2: P2(0, 50, 0)
		| 0 0 50                                | ; ponto 3: P3(0, 0, 50)
		| 1 2 3                                 | ; triangulo 1: formado pelos vertices 1, 2 e 3
		|                                       |
		\---------------------------------------/
	 */
	public static Objeto getObjeto(Camera camera, BufferedReader objeto, double rugosidade) throws IOException {

		Ponto pontos_visao [] = null, pontos[] = null;
		int qnt_pontos, qnt_triangulos, triangulos[][] = null;
		Ponto normais_triangulos[] = null, normais_vertices[] = null;
		String x, y, z, linha, temp[];
		if (objeto.ready()) {
			
			linha = objeto.readLine();
			qnt_pontos = Integer.valueOf(linha.substring(0, linha.indexOf(' ')));
			qnt_triangulos = Integer.valueOf(linha.substring(linha.indexOf(' ') + 1));

			pontos = new Ponto[qnt_pontos];
			pontos_visao = new Ponto[qnt_pontos];
			triangulos = new int[qnt_triangulos][3];
			
			/*Leitura dos pontos*/
			for (int i = 0; i < qnt_pontos; i++) {
				linha = objeto.readLine();
				temp = linha.split(" ");
				if (linha.charAt(0) == ' ') {
					x = temp[1];
					y = temp[2];
					z = temp[3];
				} else {
					x = temp[0];
					y = temp[1];
					z = temp[2];
				}
				pontos[i] = new Ponto(Double.valueOf(x), Double.valueOf(y),	Double.valueOf(z));
				pontos[i].id = i;
			}
			
			/*Leitura dos triangulos*/
			for (int i = 0; i < qnt_triangulos; i++) {
				do {
					linha = objeto.readLine();
				} while (linha.isEmpty());

				temp = linha.split(" ");
				if (linha.charAt(0) == ' ') {
					x = temp[1];
					y = temp[2];
					z = temp[3];
				} else {
					x = temp[0];
					y = temp[1];
					z = temp[2];
				}
				/*A indexacao é a partir do 0*/
				triangulos[i][0] = Integer.valueOf(x) - 1;
				triangulos[i][1] = Integer.valueOf(y) - 1;
				triangulos[i][2] = Integer.valueOf(z) - 1;
			}

			/*Converte os pontos para o sistema de coordenadas de vista*/
			for (int i = 0; i < qnt_pontos; i++) {
				pontos[i].x -= camera.C.x;
				pontos[i].y -= camera.C.y;
				pontos[i].z -= camera.C.z;

				double x_visao = (pontos[i].x * camera.U.x) + (pontos[i].y * camera.U.y) + (pontos[i].z * camera.U.z);
				double y_visao = (pontos[i].x * camera.V.x) + (pontos[i].y * camera.V.y) + (pontos[i].z * camera.V.z);
				double z_visao = (pontos[i].x * camera.N.x) + (pontos[i].y * camera.N.y) + (pontos[i].z * camera.N.z);

				pontos_visao[i] = new Ponto(x_visao, y_visao, z_visao);
				pontos_visao[i].id = i;
			}
			
			/*Faz a projecao em perspectiva dos pontos*/
			for (int i = 0; i < qnt_pontos; i++) {
				double x_temp, y_temp, z_temp;
				
				x_temp = pontos_visao[i].x;
				y_temp = pontos_visao[i].y;
				z_temp = pontos_visao[i].z;

				pontos[i].x = (camera.d / camera.hx) * (x_temp / z_temp);
				pontos[i].y = (camera.d / camera.hy) * (y_temp / z_temp);
				pontos[i].z = 0;
				
				pontos[i].id = i;
			}

			objeto.close();
			
			/*calculo das normais de cada vertice*/
			Ponto v1 = new Ponto(0.0, 0.0, 0.0), v2 = new Ponto(1.0, 1.0, 1.0);
			normais_triangulos = new Ponto[qnt_triangulos];
			normais_vertices = new Ponto[qnt_pontos];

			for (int i = 0; i < qnt_pontos; i++) {
				normais_vertices[i] = new Ponto(0.0, 0.0, 0.0);
			}
			
			for (int i = 0; i < qnt_triangulos; i++) {
				Ponto p1 = pontos_visao[triangulos[i][0]];
				Ponto p2 = pontos_visao[triangulos[i][1]];
				Ponto p3 = pontos_visao[triangulos[i][2]];

				v1.x = p2.x - p1.x;
				v1.y = p2.y - p1.y;
				v1.z = p2.z - p1.z;
				
				v2.x = p3.x - p1.x;
				v2.y = p3.y - p1.y;
				v2.z = p3.z - p1.z;

				//normais_triangulos[i] = Auxiliar.perturbarNormal(v1, v2, Auxiliar.produtoVetorial(v1, v2), rugosidade); // Vetor normal ao plano
				normais_triangulos[i] = Auxiliar.produtoVetorial(v1, v2);
				Auxiliar.normalizar(normais_triangulos[i]);
				
				/*Atualiza cada normal, somando o vetor normal ao plano determinado pelos três vértices*/
				normais_vertices[triangulos[i][0]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][0]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][0]].z += normais_triangulos[i].z;
			
				normais_vertices[triangulos[i][1]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][1]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][1]].z += normais_triangulos[i].z;

				normais_vertices[triangulos[i][2]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][2]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][2]].z += normais_triangulos[i].z;
				//
				
			}
			
			for (int i = 0; i < qnt_pontos; i++) {
				Auxiliar.normalizar(normais_vertices[i]);
			}
			
			Objeto obj = new Objeto(pontos_visao, pontos, normais_triangulos, normais_vertices, triangulos);
			return obj;
		} else {
			System.out.println("Erro ao abrir o Objeto!");
			return null;
		}
	}
	
	

}
