package itson.battleship.model;

import itson.battleship.enums.ResultadoDisparo;
import itson.battleship.enums.EstadoPartida;
import itson.battleship.enums.EstadoNave;
import itson.battleship.enums.EstadoJugador;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public interface IModel {

    EstadoPartida obtenerEstadoPartida();

    Jugador obtenerGanador();

    Jugador obtenerJugadorEnTurno();

    int obtenerTiempoRestante();

    ResultadoDisparo obtenerResultadoDisparo();

    EstadoNave obtenerEstadoNaveImpactada();

    String obtenerTipoNaveImpactada();

    boolean hayPosicionInvalida();

    boolean hayTiempoAgotado();

    boolean esCoordenadaDentroDelTablero(Coordenadas coord);
    
    int obtenerNavesPendientes(Jugador jugador, String tipo);

    boolean estaFlotaCompleta(Jugador jugador);

    boolean hayNaveEn(Jugador jugador, Coordenadas coord);

    boolean ambosConfirmaron();

    EstadoJugador obtenerEstadoJugador(Jugador jugador);
}
