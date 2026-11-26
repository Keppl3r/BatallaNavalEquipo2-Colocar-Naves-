package itson.battleship.controller;

import itson.battleship.enums.OrientacionNave;
import itson.battleship.enums.ResultadoColocacion;
import itson.battleship.model.Barco;
import itson.battleship.model.Coordenadas;
import itson.battleship.model.Crucero;
import itson.battleship.model.IPartida;
import itson.battleship.model.Jugador;
import itson.battleship.model.Nave;
import itson.battleship.model.PortaAviones;
import itson.battleship.model.Submarino;
import itson.battleship.red.ProxyPeer;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 *          Kevin Adrian Mendoza Moreno - 215752
 */
public class ControladorJuego implements IControlador {

    private IPartida partida;
    private ProxyPeer proxyPeer;

    public ControladorJuego(IPartida partida, ProxyPeer proxyPeer) {
        this.partida = partida;
        this.proxyPeer = proxyPeer;
    }

    @Override
    public void seleccionarDisparo(Coordenadas coord) {
        partida.realizarDisparo(coord);
    }

    @Override
    public ResultadoColocacion colocarNave(Jugador jugador, String tipo, OrientacionNave orientacion, Coordenadas coord) {
        return partida.colocarNave(jugador, crearNave(tipo, orientacion), coord);
    }

    @Override
    public void limpiarTablero(Jugador jugador) {
        partida.limpiarTablero(jugador);
    }

    @Override
    public void confirmarTablero(Jugador jugador) {
        partida.confirmarTablero(jugador);
        proxyPeer.enviarConfirmacion(jugador);
    }

    private Nave crearNave(String tipo, OrientacionNave orientacion) {
        switch (tipo) {
            case "PortaAviones":
                return new PortaAviones(orientacion);
            case "Crucero":
                return new Crucero(orientacion);
            case "Submarino":
                return new Submarino(orientacion);
            default:
                return new Barco(orientacion);
        }
    }
}
