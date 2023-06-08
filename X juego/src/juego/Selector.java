package juego;

public class Selector {
    private int opcionActual;
    private OpcionMenu[] opcionesMenu;

    public Selector(OpcionMenu[] opcionesMenu) {
        this.opcionesMenu = opcionesMenu;
        this.opcionActual = 0;
    }

    public void seleccionarSiguiente() {
        opcionActual++;
        if (opcionActual >= opcionesMenu.length) {
            opcionActual = 0;
        }
    }

    public void seleccionarAnterior() {
        opcionActual--;
        if (opcionActual < 0) {
            opcionActual = opcionesMenu.length - 1;
        }
    }

    public void seleccionarOpcionActual() {
        opcionesMenu[opcionActual].seleccionar();
    }

    public void deseleccionarOpcionActual() {
        opcionesMenu[opcionActual].deseleccionar();
    }

    public int getOpcionActual() {
        return opcionActual;
    }

    public OpcionMenu getOpcionActualMenu() {
        return opcionesMenu[opcionActual];
    }
}
