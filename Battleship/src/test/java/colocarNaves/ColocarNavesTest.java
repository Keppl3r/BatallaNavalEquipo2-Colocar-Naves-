/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package colocarNaves;

import itson.battleship.controller.ControladorJuego;
import itson.battleship.enums.ColorJugador;
import itson.battleship.enums.EstadoJugador;
import itson.battleship.enums.OrientacionNave;
import itson.battleship.enums.ResultadoColocacion;
import itson.battleship.model.Coordenadas;
import itson.battleship.model.Jugador;
import itson.battleship.model.Partida;
import itson.battleship.model.Tablero;
import itson.battleship.red.ProxyPeer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author Kevin Adrian Mendoza Moreno - 215752
 */
public class ColocarNavesTest {

    private Jugador jugador1 = new Jugador("Jugador 1", ColorJugador.AZUL, new Tablero(10, 10));
    private Jugador jugador2 = new Jugador("Jugador 2", ColorJugador.ROJO, new Tablero(10, 10));
    private Partida partida = new Partida("Batalla Naval", jugador1, jugador2, 30);
    private ControladorJuego controlador = new ControladorJuego(partida, new ProxyPeer());

    @Test
    public void colocarNaveValida() {
        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.VERTICAL, new Coordenadas(0, 2));

        assertEquals(ResultadoColocacion.COLOCADA, resultado);
        assertEquals(1, partida.obtenerNavesPendientes(jugador1, "PortaAviones"));
        assertTrue(partida.obtenerCasillasOcupadas(jugador1)[2][0]);
        assertTrue(partida.obtenerCasillasOcupadas(jugador1)[5][0]);
    }

    @Test
    public void naveFueraDelTablero() {
        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(7, 0));

        assertEquals(ResultadoColocacion.FUERA_DEL_TABLERO, resultado);
        assertEquals(2, partida.obtenerNavesPendientes(jugador1, "PortaAviones"));
    }

    @Test
    public void navePegadaAOtra() {
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));

        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "Crucero", OrientacionNave.HORIZONTAL, new Coordenadas(0, 1));

        assertEquals(ResultadoColocacion.NAVE_CERCA, resultado);
    }

    @Test
    public void naveEncimada() {
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));

        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "Crucero", OrientacionNave.VERTICAL, new Coordenadas(1, 0));

        assertEquals(ResultadoColocacion.NAVE_CERCA, resultado);
    }

    @Test
    public void tipoDeNaveAgotado() {
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 2));

        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 4));

        assertEquals(ResultadoColocacion.TIPO_AGOTADO, resultado);
        assertEquals(0, partida.obtenerNavesPendientes(jugador1, "PortaAviones"));
    }

    @Test
    public void limpiarTablero() {
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));

        controlador.limpiarTablero(jugador1);

        assertEquals(2, partida.obtenerNavesPendientes(jugador1, "PortaAviones"));
        assertFalse(partida.obtenerCasillasOcupadas(jugador1)[0][0]);
    }

    @Test
    public void confirmarConNavesPendientes() {
        controlador.colocarNave(jugador1, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));

        controlador.confirmarTablero(jugador1);

        assertFalse(partida.estaFlotaCompleta(jugador1));
        assertEquals(EstadoJugador.CONFIGURANDO, partida.obtenerEstadoJugador(jugador1));
    }

    @Test
    public void confirmarTableroCompleto() {
        colocarFlota(jugador1);

        controlador.confirmarTablero(jugador1);

        assertTrue(partida.estaFlotaCompleta(jugador1));
        assertEquals(EstadoJugador.LISTO, partida.obtenerEstadoJugador(jugador1));
        assertFalse(partida.ambosConfirmaron());
    }

    @Test
    public void tableroConfirmadoNoSeModifica() {
        colocarFlota(jugador1);
        controlador.confirmarTablero(jugador1);

        ResultadoColocacion resultado = controlador.colocarNave(jugador1, "Barco", OrientacionNave.HORIZONTAL, new Coordenadas(9, 9));
        controlador.limpiarTablero(jugador1);

        assertEquals(ResultadoColocacion.TABLERO_CONFIRMADO, resultado);
        assertTrue(partida.estaFlotaCompleta(jugador1));
    }

    @Test
    public void ambosJugadoresConfirman() {
        colocarFlota(jugador1);
        colocarFlota(jugador2);

        controlador.confirmarTablero(jugador1);
        controlador.confirmarTablero(jugador2);

        assertTrue(partida.ambosConfirmaron());
    }

    private void colocarFlota(Jugador jugador) {
        controlador.colocarNave(jugador, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 0));
        controlador.colocarNave(jugador, "PortaAviones", OrientacionNave.HORIZONTAL, new Coordenadas(0, 2));
        controlador.colocarNave(jugador, "Crucero", OrientacionNave.HORIZONTAL, new Coordenadas(0, 4));
        controlador.colocarNave(jugador, "Crucero", OrientacionNave.HORIZONTAL, new Coordenadas(0, 6));
        controlador.colocarNave(jugador, "Submarino", OrientacionNave.HORIZONTAL, new Coordenadas(0, 8));
        controlador.colocarNave(jugador, "Submarino", OrientacionNave.HORIZONTAL, new Coordenadas(5, 0));
        controlador.colocarNave(jugador, "Submarino", OrientacionNave.HORIZONTAL, new Coordenadas(5, 2));
        controlador.colocarNave(jugador, "Submarino", OrientacionNave.HORIZONTAL, new Coordenadas(5, 4));
        controlador.colocarNave(jugador, "Barco", OrientacionNave.HORIZONTAL, new Coordenadas(5, 6));
        controlador.colocarNave(jugador, "Barco", OrientacionNave.HORIZONTAL, new Coordenadas(5, 8));
        controlador.colocarNave(jugador, "Barco", OrientacionNave.HORIZONTAL, new Coordenadas(7, 6));
    }
}