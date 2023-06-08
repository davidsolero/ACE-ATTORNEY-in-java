package juego;

public class OpcionMenu {
    private String texto;
    private boolean seleccionada;

    public OpcionMenu(String texto) {
        this.texto = texto;
        this.seleccionada = false;
    }

    public String getTexto() {
        return texto;
    }

    public boolean estaSeleccionada() {
        return seleccionada;
    }

    public void seleccionar() {
        seleccionada = true;
    }

    public void deseleccionar() {
        seleccionada = false;
    }
}
