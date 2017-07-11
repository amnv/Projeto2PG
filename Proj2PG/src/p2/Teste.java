package p2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Teste extends JFrame {
	static Teste info;
	public Teste(Camera camera, Objeto objeto, Iluminacao luz) {
		Tela(camera, objeto, luz);
	}

	private void Tela(Camera camera, Objeto objeto, Iluminacao luz) {
		setTitle("PG PROJETO 2 - RUGOSIDADE");
		add(new Tela(objeto, luz,camera));
		setSize(800, 600);
		setResizable(false);	
	}
	
	
	
	public static void StartAmbiente(BufferedReader pCamera, BufferedReader pIluminacao, BufferedReader pObjeto,
			String cameraEntrada, String objetoEntrada, String iluminacaoEntrada, double rugosidade, Camera camera, Objeto objeto, Iluminacao luz) throws NumberFormatException,
			IOException {

		String entradaCamera = cameraEntrada + ".cfg";
		String entradaObjeto = objetoEntrada + ".byu";
		String entradaIluminacao = iluminacaoEntrada;
		
		pCamera = new BufferedReader(new FileReader(entradaCamera));
		pIluminacao = new BufferedReader(new FileReader(entradaIluminacao));
		pObjeto = new BufferedReader(new FileReader(entradaObjeto));
		camera = PreProcessamento.getCamera(pCamera);
		luz = PreProcessamento.getIluminacao(camera, pIluminacao);
		objeto = PreProcessamento.getObjeto(camera, pObjeto, rugosidade);
		

		info = new Teste(camera, objeto, luz);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				info.setLocation(250, 50);
				info.setVisible(true);
			}
		});
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		Camera camera = null;
		Objeto objeto = null;
		Iluminacao luz = null;
		BufferedReader pCamera = null;
		BufferedReader pIluminacao = null;
		BufferedReader pObjeto = null;
		System.out.println("Entre com o nome da camera");
		Scanner scan = new Scanner(System.in);
		String variaveisCamera = scan.nextLine();
		System.out.println("Entre com o nome do objeto");
		String variaveisObjeto = scan.nextLine();
		System.out.println("Entre com o nome da iluminacao");
		String variaveisIluminacao = scan.nextLine();
	    System.out.println("Entre com o parametro de rugosidade");
	    double rugosidade = scan.nextDouble();
		StartAmbiente(pCamera, pIluminacao, pObjeto, variaveisCamera, variaveisObjeto,variaveisIluminacao, rugosidade, camera, objeto, luz);
	}
}
