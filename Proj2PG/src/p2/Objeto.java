package p2;

public class Objeto {
	
	// pontos do objeto em coordenadas de vista
	Ponto pontosVisao[];
	// pontos do objeto em coordenadas de tela (2D)
	Ponto pontosTela[];
	
	// partes do pre-processamento
	Ponto normaisTriangulos[];
	Ponto normaisVertices[];
	
	int triangulos[][];
	
	public Objeto(Ponto[] pontosVisao, Ponto[] pontosTela, Ponto[] normaisTriangulos, Ponto[] normaisVertices,
	int[][] triangulos) {
		this.pontosVisao = pontosVisao;
		this.pontosTela = pontosTela;                  
		this.normaisTriangulos = normaisTriangulos;
		this.normaisVertices = normaisVertices;
		this.triangulos = triangulos;		
	}

}
