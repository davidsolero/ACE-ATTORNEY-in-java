package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener{
	
	private final static int numeroTeclas=120;
	private boolean[] teclas = new boolean[numeroTeclas];
	
	public boolean w;  //arriba
	public boolean a;	//izq
	public boolean s;	//abaj
	public boolean d;	//derch
	public boolean enter;
	public boolean esc;
	
	public void actualizar() {
		w=teclas[KeyEvent.VK_W];
		a=teclas[KeyEvent.VK_A];
		s=teclas[KeyEvent.VK_S];
		d=teclas[KeyEvent.VK_D];
		enter=teclas[KeyEvent.VK_ENTER];
		esc=teclas[KeyEvent.VK_ESCAPE];
	}
	
	public void keyTyped(KeyEvent e) {
		
	}

	
	public void keyPressed(KeyEvent e) {
		teclas[e.getKeyCode()]=true;
		
	}

	
	public void keyReleased(KeyEvent e) {
		teclas[e.getKeyCode()]=false;
	}

}
