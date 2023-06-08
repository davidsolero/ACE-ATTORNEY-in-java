package graficos;

import java.util.Iterator;

public final class Sprite {
	private final int lado;
	private int x, y;

	public int[] pixeles;
	private final HojaSprites hoja;

	public Sprite(final int lado, final int columna, final int fila, final HojaSprites hoja) {
		this.hoja = hoja;
		this.lado = lado;

		pixeles = new int[lado * lado];

		this.x = columna * lado;
		this.y = fila * lado;

		for (int i = 0; i < lado; i++) {
			for (int j = 0; j < lado; j++) {
				pixeles[(x + y) * lado] = hoja.pixeles[(x + j) + (y + i) * hoja.getAncho()]; // más eficiente que array
																								// bidimensional en
																								// ejecucion
			}
		}
	}
}
