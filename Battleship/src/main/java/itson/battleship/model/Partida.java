package itson.battleship.model;

import itson.battleship.enums.ResultadoDisparo;
import itson.battleship.enums.EstadoPartida;
import itson.battleship.enums.EstadoJugador;
import itson.battleship.enums.EstadoNave;
import itson.battleship.observer.Observado;
import itson.battleship.observer.Observador;
import java.util.ArrayList;
import java.util.List;
import itson.battleship.enums.ResultadoColocacion;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class Partida implements IPartida, IModel, Observado {

    private EstadoPartida estado;
    private String nombre;
    private boolean posicionInvalida;
    private boolean tiempoAgotado;
    private Disparo ultimoDisparo;
    private List<Observador> observadores;
    private Turno turno;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador ganador;

    public Partida(String nombre, Jugador jugador1, Jugador jugador2, int tiempoLimite) {
        this.nombre = nombre;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estado = EstadoPartida.ESPERANDO_TABLEROS;
        this.observadores = new ArrayList<>();
        this.turno = new Turno(jugador1, jugador2, tiempoLimite);
        this.posicionInvalida = false;
        this.tiempoAgotado = false;
        this.ultimoDisparo = null;
        this.ganador = null;
    }

    public void iniciar() {
        turno.seleccionarPrimerTurno();
        turno.iniciarTiempo();
        jugador1.setEstado(EstadoJugador.JUGANDO);
        jugador2.setEstado(EstadoJugador.JUGANDO);
        estado = EstadoPartida.EN_CURSO;
    }

    @Override
    public void realizarDisparo(Coordenadas coord) {
        posicionInvalida = false;
        tiempoAgotado = false;
        ultimoDisparo = null;

        if (estado != EstadoPartida.EN_CURSO) {
            return;
        }

        Jugador atacante = turno.obtenerJugadorEnTurno();
        Jugador oponente = obtenerOponente(atacante);
        Tablero tableroOponente = oponente.getTablero();

        if (!turno.validarTiempoTurno()) {
            tiempoAgotado = true;
            turno.cambiarTurno();
            notificarObservadores();
            return;
        }

        Disparo disparo = new Disparo(coord, tableroOponente);

        if (!disparo.validarPosicion()) {
            marcarPosicionInvalida();
            notificarObservadores();
            return;
        }

        disparo.evaluarDisparo();
        tableroOponente.marcarCasilla(coord);
        ultimoDisparo = disparo;

        if (disparo.getResultado() == ResultadoDisparo.IMPACTO) {
            if (verificarFinPartida()) {
                finalizarPartida(atacante);
            } else {
                turno.darTurnoExtra();
            }
        } else {
            turno.cambiarTurno();
        }

        notificarObservadores();
    }

    public boolean verificarFinPartida() {
        Jugador oponente = obtenerOponente(turno.obtenerJugadorEnTurno());
        return oponente.getTablero().contarNavesSinHundir() == 0;
    }

    public void finalizarPartida(Jugador ganador) {
        this.ganador = ganador;
        this.estado = EstadoPartida.FINALIZADA;
    }

    private void marcarPosicionInvalida() {
        posicionInvalida = true;
    }

    private Jugador obtenerOponente(Jugador jugador) {
        if (jugador == jugador1) {
            return jugador2;
        }
        return jugador1;
    }

    @Override
    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    @Override
    public void quitarObservador(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        for (Observador observador : observadores) {
            observador.actualizarPantalla();
        }
    }

    @Override
    public EstadoPartida obtenerEstadoPartida() {
        return estado;
    }

    @Override
    public Jugador obtenerGanador() {
        return ganador;
    }

    @Override
    public Jugador obtenerJugadorEnTurno() {
        return turno.obtenerJugadorEnTurno();
    }

    @Override
    public int obtenerTiempoRestante() {
        return turno.obtenerTiempoRestante();
    }

    @Override
    public ResultadoDisparo obtenerResultadoDisparo() {
        if (ultimoDisparo == null) {
            return null;
        }
        return ultimoDisparo.getResultado();
    }
    
    @Override
    public EstadoNave obtenerEstadoNaveImpactada() {
        if (ultimoDisparo == null) {
            return null;
        }
        return ultimoDisparo.getEstadoNaveImpactada();
    }

    @Override
    public String obtenerTipoNaveImpactada() {
        if (ultimoDisparo == null || ultimoDisparo.getNaveImpactada() == null) {
            return null;
        }
        return ultimoDisparo.getNaveImpactada().getTipo();
    }

    @Override
    public boolean hayPosicionInvalida() {
        return posicionInvalida;
    }

    @Override
    public boolean hayTiempoAgotado() {
        return tiempoAgotado;
    }

    @Override
    public boolean esCoordenadaDentroDelTablero(Coordenadas coord) {
        Jugador oponente = obtenerOponente(turno.obtenerJugadorEnTurno());
        return oponente.getTablero().validarCoordenada(coord);
    }
    
    // colocar naves 
    @Override
    public ResultadoColocacion colocarNave(Jugador jugador, Nave nave, Coordenadas coord) {
        ResultadoColocacion resultado = jugador.getTablero().colocarNave(nave, coord);
        if (resultado == ResultadoColocacion.COLOCADA) {
            notificarObservadores();
        }
        return resultado;
    }

    @Override
    public void limpiarTablero(Jugador jugador) {
        jugador.getTablero().limpiar();
        notificarObservadores();
    }

    @Override
    public void confirmarTablero(Jugador jugador) {
        if (jugador.confirmarTablero()) {
            notificarObservadores();
        }
    }
    
      @Override
    public int obtenerNavesPendientes(Jugador jugador, String tipo) {
        return jugador.getTablero().contarNavesPendientes(tipo);
    }

    @Override
    public boolean estaFlotaCompleta(Jugador jugador) {
        return jugador.getTablero().estaFlotaCompleta();
    }

    @Override
    public boolean hayNaveEn(Jugador jugador, Coordenadas coord) {
        return jugador.getTablero().getCasilla(coord).tieneNave();
    }

    @Override
    public boolean ambosConfirmaron() {
        return jugador1.getEstado() == EstadoJugador.LISTO
                && jugador2.getEstado() == EstadoJugador.LISTO;
    }

    @Override
    public EstadoJugador obtenerEstadoJugador(Jugador jugador) {
        return jugador.getEstado();
    }
}
