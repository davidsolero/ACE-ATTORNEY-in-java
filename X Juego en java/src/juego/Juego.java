package juego;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import control.Teclado;

public class Juego extends Canvas implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ANCHO_VENTANA = 1920, ALTO_VENTANA = 1080;
	private static final String NOMBRE_VENTANA = "Ace Attorney: Galactic intermission";
	private static int aps = 0, fps = 0;
	private static final Font FONT = new Font("Arial", Font.BOLD, 35);

	private static JFrame ventana;
	private static volatile boolean enFuncionamiento = false; // juego ejecutandose (volatile hace que no se pueda usar
																// en dos funciones a la vez)

	private static EstadoJuego Estadoactual = EstadoJuego.MenuPrincipal;

	private static Thread thread; // proceso pa los graficos

	private static Teclado teclado; // clase teclado en control

	private Juego() {
		setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));

		teclado = new Teclado();
		addKeyListener(teclado);

		ventana = new JFrame(NOMBRE_VENTANA); // nombre ventana
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // para proceso al salir
		ventana.setResizable(false); // no se puede mover tamaño
		ventana.setLayout(new BorderLayout()); //
		ventana.add(this, BorderLayout.CENTER); // centra la ventana
		ventana.pack(); // hace que no se raye el tamaño
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true); // visible
	}

	public static void main(String[] args) {
		Juego juego = new Juego();
		juego.iniciar(); // inicia los graficos como un proceso desde iniciar

	}

	private synchronized void iniciar() { // sync para no rayar cosas a la vez
		enFuncionamiento = true;

		thread = new Thread(this, "Graficos");
		thread.start(); // ejecuta run
	}

	private synchronized void detener() { // sync
		enFuncionamiento = false;

		try {
			thread.join(); // espera a que deje de usarse en run()
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizar() {// actualiza graficos y de mas
		teclado.actualizar(); // detecta teclado

		if (Estadoactual == EstadoJuego.MenuPrincipal) {
			mostrarMenu(getGraphics());
		}

		if (teclado.w) {
			System.out.println("w");
		}

		if (teclado.a) {

		}
		if (teclado.s) {
			System.out.println("S");
		}
		if (teclado.d) {
			System.out.println("D");
		}
		if (teclado.esc) {
			System.out.println("ESC");
			borrarMenu(getGraphics());
			Estadoactual = EstadoJuego.MenuOpciones;
		}
		if (teclado.enter) {
			System.out.println("ENTER");

		}

		aps++;
	}

	private void mostrar() {// los muestra los graficos tras actualizar
		fps++;
	}

	private void mostrarMenu(Graphics g) {
		String titulo = "ACE ATTORNEY: ";
		String titulo2 = "GALACTIC INTERMISSION";
		String opcion1 = "Juego Nuevo";
		String opcion2 = "Continuar";
		String opcion3 = "Opciones";
		String opcion4 = "Salir";

		FontMetrics fm = g.getFontMetrics(FONT);
		int x = (getWidth() - fm.stringWidth(titulo)) / 2;
		int y = 100;

		Font FUENTETITULO = FONT.deriveFont(Font.BOLD, 70);

		g.setColor(Color.MAGENTA);
		g.setFont(FUENTETITULO);
		g.drawString(titulo, x - 500, y);
		y+=75;
		g.drawString(titulo2, x - 500, y);
		
		
		
		g.setColor(Color.BLACK);
		g.setFont(FONT);
		y += 250;
		g.drawString(opcion1, x, y);

		y += 35;
		g.drawString(opcion2, x, y);

		y += 35;
		g.drawString(opcion3, x, y);

		y += 35;
		g.drawString(opcion4, x, y);
	}

	@Override
	public void run() {
		final int NS_POR_SEGUNDO = 1000000000;
		final byte APS_OBJETIVO = 60; // actualizaciones por segundo (fps es pa mostrar aps es pa poner)
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO; // aps por segundo en nano

		long referenciaActualizacion = System.nanoTime(); // una referencia
		long referenciaContador = System.nanoTime();

		double tiempoTranscurrido;
		double delta = 0; // cantidad de tiempo hasta actualizar

		requestFocus();// pantalla en primer plano (visual)

		while (enFuncionamiento) { // bucle principal de juego
			final long inicioBucle = System.nanoTime(); // != de referencia,

			tiempoTranscurrido = inicioBucle - referenciaActualizacion; // tiempo de ejecucion
			referenciaActualizacion = inicioBucle; // se resetea para seguir midiendo tiempo de ejecucion (nueva ref pa
													// restar)
			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

			while (delta >= 1) {
				actualizar();
				delta--;
			}

			mostrar();
			if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) { // contador se actualiza cada segundo
				ventana.setTitle(NOMBRE_VENTANA + " || APS: " + aps + "|| FPS: " + fps);
				aps = 0;
				fps = 0;
				referenciaContador = System.nanoTime();
			}
		}
	}

	private void borrarMenu(Graphics g) {
		// Lógica para borrar el menú mostrado en la pantalla

		// Obtén las dimensiones del área del menú
		int x = 0; // Coordenada x del área del menú
		int y = 100; // Coordenada y del área del menú
		int width = getWidth(); // Ancho del área del menú (ancho de la ventana)
		int height = y + 250 + 35*3; // Alto del área del menú (incluye las cuatro opciones)

		// Utiliza el método clearRect() para limpiar el área del menú con el color de
		// fondo
		g.clearRect(x, y, width, height);

		// También puedes utilizar otros métodos o técnicas según tus necesidades y cómo
		// hayas implementado el menú en el método mostrarMenu().

		// Además de borrar los elementos del menú en la pantalla, puedes realizar otras
		// acciones necesarias,
		// como cambiar el estado del juego o reiniciar variables según tus necesidades.
	}

	public void keyPressed(KeyEvent e) {
		if (Estadoactual == EstadoJuego.MenuPrincipal) {
			if (e.getKeyCode() == KeyEvent.VK_1) {
				// Opción 1: Juego Nuevo
				iniciarNuevoJuego();
			} else if (e.getKeyCode() == KeyEvent.VK_2) {
				// Opción 2: Continuar
				continuarJuego();
			} else if (e.getKeyCode() == KeyEvent.VK_3) {
				// Opción 3: Opciones
				mostrarOpciones();
			} else if (e.getKeyCode() == KeyEvent.VK_4) {
				// Opción 4: Salir
				detener();
			}
		} else if (Estadoactual == EstadoJuego.Jugando) {
			// Manejar eventos de teclado en el juego
		} else if (Estadoactual == EstadoJuego.MenuOpciones) {
			// Manejar eventos de teclado en las opciones
		}
	}

	private void iniciarNuevoJuego() {
		// Lógica para iniciar un nuevo juego
		// Puede incluir reiniciar variables, cargar niveles, etc.
	}

	private void continuarJuego() {
		// Lógica para continuar el juego guardado
		// Puede incluir cargar el estado guardado, restaurar posición del jugador, etc.
	}

	private void mostrarOpciones() {
		// Lógica para mostrar el menú de opciones
		// Puede incluir cambiar el estado del juego a OPCIONES, mostrar
		// configuraciones, etc.
	}
}
