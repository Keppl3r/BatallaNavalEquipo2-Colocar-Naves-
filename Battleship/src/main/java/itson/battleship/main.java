package itson.battleship;

import itson.battleship.controller.ControladorJuego;
import itson.battleship.controller.IControlador;
import itson.battleship.enums.ColorJugador;
import itson.battleship.enums.EstadoPartida;
import itson.battleship.model.Jugador;
import itson.battleship.model.Partida;
import itson.battleship.model.Tablero;
import itson.battleship.red.ProxyPeer;
import itson.battleship.view.PantallaColocacion;
import itson.battleship.view.PantallaPrincipal;
import java.util.Scanner;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 *          Kevin Adrian Mendoza Moreno - 215752
 */
public class main {

    public static void main(String[] args) {
        Jugador jugador1 = new Jugador("Jugador 1", ColorJugador.AZUL, new Tablero(10, 10));
        Jugador jugador2 = new Jugador("Jugador 2", ColorJugador.ROJO, new Tablero(10, 10));

        Partida partida = new Partida("Batalla Naval", jugador1, jugador2, 30);
        ProxyPeer proxyPeer = new ProxyPeer();
        IControlador controlador = new ControladorJuego(partida, proxyPeer);
        Scanner scanner = new Scanner(System.in);

        PantallaColocacion colocacion1 = new PantallaColocacion(controlador, partida, jugador1, scanner);
        PantallaColocacion colocacion2 = new PantallaColocacion(controlador, partida, jugador2, scanner);
        partida.agregarObservador(colocacion1);
        partida.agregarObservador(colocacion2);
        colocacion1.iniciar();
        colocacion2.iniciar();
        partida.quitarObservador(colocacion1);
        partida.quitarObservador(colocacion2);

        partida.iniciar();

        PantallaPrincipal pantalla1 = new PantallaPrincipal(controlador, partida, jugador1, scanner);
        PantallaPrincipal pantalla2 = new PantallaPrincipal(controlador, partida, jugador2, scanner);
        partida.agregarObservador(pantalla1);
        partida.agregarObservador(pantalla2);

        while (partida.obtenerEstadoPartida() == EstadoPartida.EN_CURSO) {
            if (partida.obtenerJugadorEnTurno() == jugador1) {
                pantalla1.iniciar();
            } else {
                pantalla2.iniciar();
            }
        }
    }
}