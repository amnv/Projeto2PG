package p2;

import java.awt.BorderLayout;

import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Main principal;
	private BufferedReader pCamera;
	private BufferedReader pIluminacao;
	private BufferedReader pObjeto;
	private Camera camera;
	private Iluminacao iluminacao;
	private Objeto objeto;
	private double rugosidade;
	
	
	final TextField tfCamera = new TextField("calice2");
	final TextField tfIluminacao = new TextField("iluminacao.txt");
	final TextField tfObjeto = new TextField("calice2");
	final TextField tfRugosidade = new TextField("1000");
	
	public static void main(String[] args) {
		
		
        principal = new Main();
        principal.setTitle("PG - Rugosidade através de perturbações de normais");
        principal.setVisible(true);
		principal.setSize(800, 600);
		principal.setResizable(false);
		principal.setLocationRelativeTo(null);
		principal.tfCamera.setSize(150, 150);
    }
	Main(){
	//paineis para configurações da curva
    Panel painel = new Panel();
    Panel painel2 = new Panel();

    Label lbCamera = new Label("Nome da Câmera:");
    painel.add(lbCamera);
    painel.add(tfCamera);
    
    Label lbIluminacao = new Label("Arquivo de iluminação:");
    painel.add(lbIluminacao);
    painel.add(tfIluminacao);
    
    Label lbObjeto = new Label("Nome do objeto:");
    painel2.add(lbObjeto);
    painel2.add(tfObjeto);
    
    Label lbRugosidade = new Label("Parâmetro:");
    painel2.add(lbRugosidade);
    painel2.add(tfRugosidade);
    
    Button btTela = new Button("Atualizar tela");
    painel2.add(btTela);
    
    btTela.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
        	String cam = tfCamera.getText();
        	String ilum = tfIluminacao.getText();
        	String obj = tfObjeto.getText();
        	double rug = Double.parseDouble(tfRugosidade.getText().toString());
        	String entradaCamera = "entradas//" + cam + ".cfg";
    		String entradaObjeto = "entradas//" + obj + ".byu";
    		String entradaIluminacao = "entradas//"+ ilum;
    		
    		try {
				pCamera = new BufferedReader(new FileReader(entradaCamera));
				pIluminacao = new BufferedReader(new FileReader(entradaIluminacao));
	    		pObjeto = new BufferedReader(new FileReader(entradaObjeto));
	    		rugosidade = rug;
	    		camera = PreProcessamento.getCamera(pCamera);
	    		iluminacao = PreProcessamento.getIluminacao(camera, pIluminacao);
	    		objeto = PreProcessamento.getObjeto(camera, pObjeto, rugosidade);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		

    		add(new Tela(objeto, iluminacao ,camera, rugosidade));
			principal.setVisible(true);
    		

        }
    });
    
    Color c = new Color(79, 148, 205);
    //adicionando o painel ao Frame
    painel.setBackground(c);
    add(painel, BorderLayout.PAGE_START);

    painel2.setBackground(c);
    add(painel2, BorderLayout.PAGE_END);

    //pra quando fechar a janela parar de rodar
    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent evt) {
            System.exit(0);
        }
    });
	}
}
