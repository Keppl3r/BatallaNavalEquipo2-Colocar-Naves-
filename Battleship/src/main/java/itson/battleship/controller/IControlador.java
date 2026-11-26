package itson.battleship.controller;

import itson.battleship.enums.OrientacionNave;
import itson.battleship.enums.ResultadoColocacion;
import itson.battleship.model.Coordenadas;
import itson.battleship.model.Jugador;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public interface IControlador {

    void seleccionarDisparo(Coordenadas coord);
    
    ResultadoColocacion colocarNave(Jugador jugador, String tipo, OrientacionNave orientacion, Coordenadas coord);

    void limpiarTablero(Jugador jugador);

    void confirmarTablero(Jugador jugador);
    
}
