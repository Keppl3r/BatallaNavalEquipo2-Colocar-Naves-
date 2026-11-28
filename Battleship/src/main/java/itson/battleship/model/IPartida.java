package itson.battleship.model;


import itson.battleship.enums.ResultadoColocacion;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 *          Kevin Adrian Mendoza Moreno - 215752
 */
public interface IPartida {

    void realizarDisparo(Coordenadas coord);
    
    ResultadoColocacion colocarNave(Jugador jugador, Nave nave, Coordenadas coord);

    void limpiarTablero(Jugador jugador);

    boolean confirmarTablero(Jugador jugador);
    
}
