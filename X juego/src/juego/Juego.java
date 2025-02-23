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
	private static JFrame ventana;
	private static volatile boolean enFuncionamiento = false; // juego ejecutandose (volatile hace que no se pueda usar
																// en dos funciones a la vez)

	private static Thread thread; // proceso pa los graficos

	private Selector selector; // selector menu
	private OpcionMenu[] opcionesMenu;
	private int opcionActual;
	private static final Font FONT = new Font("Arial", Font.BOLD, 35);

	private static EstadoJuego Estadoactual = EstadoJuego.MenuPrincipal;

	private static Teclado teclado; // clase teclado en control

	private Juego() {
		setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));

		teclado = new Teclado();
		addKeyListener(teclado);

		ventana = new JFrame(NOMBRE_VENTANA); // nombre ventana
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // para proceso al salir
		ventana.setResizable(false); // no se puede mover tama�o
		ventana.setLayout(new BorderLayout()); //
		ventana.add(this, BorderLayout.CENTER); // centra la ventana
		ventana.pack(); // hace que no se raye el tama�o
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true); // visible

		opcionesMenu = new OpcionMenu[4];
		opcionesMenu[0] = new OpcionMenu("Juego Nuevo");
		opcionesMenu[1] = new OpcionMenu("Continuar");
		opcionesMenu[2] = new OpcionMenu("Opciones");
		opcionesMenu[3] = new OpcionMenu("Salir");

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

	private void inicializarSelector() { //ESTO SIRVE PARA TODO SI HOMBRE
		selector = new Selector(opcionesMenu);
	}

	private void actualizar() {// actualiza graficos y de mas
		teclado.actualizar(); // detecta teclado
		inicializarSelector();
		if (Estadoactual == EstadoJuego.MenuPrincipal) {
			mostrarMenu(getGraphics());
		}

		if (teclado.w) {
			selector.seleccionarAnterior();
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
		// PARTE TITULO
		String titulo = "ACE ATTORNEY: ";
		String titulo2 = "GALACTIC INTERMISSION";

		FontMetrics fm = g.getFontMetrics(FONT);
		int x = (getWidth() - fm.stringWidth(titulo)) / 2;
		int y = 100;

		Font FUENTETITULO = FONT.deriveFont(Font.BOLD, 70);

		g.setColor(Color.MAGENTA);
		g.setFont(FUENTETITULO);
		g.drawString(titulo, x - 500, y);
		y += 75;
		g.drawString(titulo2, x - 500, y);

		// PARTE OPCIONES
		g.setFont(FONT);
		y += 250;
		for (int i = 0; i < opcionesMenu.length; i++) {
			OpcionMenu opcion = opcionesMenu[i];
			int posY = y + (i + 1) * 35; // Ajuste: utilizar 35 en lugar de 30

			if (opcion.estaSeleccionada()) {
				g.setColor(Color.RED); // Resaltar la opci�n seleccionada con color rojo
			} else {
				g.setColor(Color.BLACK);
			}

			g.drawString(opcion.getTexto(), x, posY);
		}
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
		// L�gica para borrar el men� mostrado en la pantalla

		// Obt�n las dimensiones del �rea del men�
		int x = 0; // Coordenada x del �rea del men�
		int y = 100; // Coordenada y del �rea del men�
		int width = getWidth(); // Ancho del �rea del men� (ancho de la ventana)
		int height = y + 250 + 35 * 3; // Alto del �rea del men� (incluye las cuatro opciones)

		// Utiliza el m�todo clearRect() para limpiar el �rea del men� con el color de
		// fondo
		g.clearRect(x, y, width, height);

		// Tambi�n puedes utilizar otros m�todos o t�cnicas seg�n tus necesidades y c�mo
		// hayas implementado el men� en el m�todo mostrarMenu().

		// Adem�s de borrar los elementos del men� en la pantalla, puedes realizar otras
		// acciones necesarias,
		// como cambiar el estado del juego o reiniciar variables seg�n tus necesidades.
	}

	public void keyPressed(KeyEvent e) { // queda por poner las teclas para el selector. q co�o hace keypressed?
		if (Estadoactual == EstadoJuego.MenuPrincipal) {
			if (e.getKeyCode() == KeyEvent.VK_1) {
				selector.seleccionarOpcionActual();
			} else if (e.getKeyCode() == KeyEvent.VK_2) {
				selector.seleccionarSiguiente();
			} else if (e.getKeyCode() == KeyEvent.VK_3) {
				selector.seleccionarAnterior();
			} else if (e.getKeyCode() == KeyEvent.VK_4) {
				selector.deseleccionarOpcionActual();
			}
		} else if (Estadoactual == EstadoJuego.Jugando) {
			// Manejar eventos de teclado en el juego
		} else if (Estadoactual == EstadoJuego.MenuOpciones) {
			// Manejar eventos de teclado en las opciones
		}
	}

	private void iniciarNuevoJuego() {
		// L�gica para iniciar un nuevo juego
		// Puede incluir reiniciar variables, cargar niveles, etc.
	}

	private void continuarJuego() {
		// L�gica para continuar el juego guardado
		// Puede incluir cargar el estado guardado, restaurar posici�n del jugador, etc.
	}

	private void mostrarOpciones() {
		// L�gica para mostrar el men� de opciones
		// Puede incluir cambiar el estado del juego a OPCIONES, mostrar
		// configuraciones, etc.
	}
}
