package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HojaSprites {
	public final int[] pixeles;
	private final int ancho, alto;

	public HojaSprites(final String ruta, final int ancho, final int alto) {
		this.alto = alto;
		this.ancho = ancho;

		pixeles = new int[ancho * alto];

		BufferedImage imagen;
		try {
			imagen = ImageIO.read(HojaSprites.class.getResource(ruta));
			imagen.getRGB(0, 0, ancho, alto, pixeles, 0, ancho);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}
}
