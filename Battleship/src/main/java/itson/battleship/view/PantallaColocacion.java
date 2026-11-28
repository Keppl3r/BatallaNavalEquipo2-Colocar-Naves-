/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.battleship.view;

import itson.battleship.controller.IControlador;
import itson.battleship.enums.EstadoJugador;
import itson.battleship.enums.OrientacionNave;
import itson.battleship.enums.ResultadoColocacion;
import itson.battleship.model.Coordenadas;
import itson.battleship.model.IModel;
import itson.battleship.model.Jugador;
import itson.battleship.observer.Observador;
import java.util.Scanner;

/**
 * Simulación de la pantalla de colocar naves
 * @author Kevin Adrian Mendoza Moreno
 */
public class PantallaColocacion implements Observador {
    
    private static final String[] TIPOS = {"PortaAviones", "Crucero", "Submarino", "Barco"};

    private IControlador controlador;
    private IModel modelo;
    private Jugador jugador;
    private Scanner scanner;
    private boolean activa;

    public PantallaColocacion(IControlador controlador, IModel modelo, Jugador jugador, Scanner scanner) {
        this.controlador = controlador;
        this.modelo = modelo;
        this.jugador = jugador;
        this.scanner = scanner;
        this.activa = false;
    }
    
    
    public void iniciar() {
        activa = true;
        mostrarMensaje("Coloca tus naves");
        mostrarTablero();
        while (modelo.obtenerEstadoJugador(jugador) != EstadoJugador.LISTO) {
            String opcion = leer("1 = colocar nave, 2 = limpiar tablero, 3 = confirmar tablero");
            if (opcion.equals("1")) {
                String tipo = convertirTipo(leer("Nave: P = PortaAviones (4), C = Crucero (3), S = Submarino (2), B = Barco (1)"));
                if (tipo == null) {
                    mostrarMensaje("Nave no valida");
                } else {
                    Coordenadas coord = convertirCoordenadas(leer("Casilla donde empieza la nave (ej. A1)"));
                    OrientacionNave orientacion = OrientacionNave.HORIZONTAL;
                    if (leer("Orientacion: H = horizontal, V = vertical").equalsIgnoreCase("V")) {
                        orientacion = OrientacionNave.VERTICAL;
                    }
                    soltarNave(tipo, orientacion, coord);
                }
            } else if (opcion.equals("2")) {
                presionarLimpiar();
            } else if (opcion.equals("3")) {
                presionarConfirmar();
            } else {
                mostrarMensaje("Opcion no valida");
            }
        }
        activa = false;
    }
    
    public void soltarNave(String tipo, OrientacionNave orientacion, Coordenadas coord) {
        ResultadoColocacion resultado = controlador.colocarNave(jugador, tipo, orientacion, coord);
        mostrarResultado(resultado, tipo);
    }
    
    public void presionarLimpiar() {
        controlador.limpiarTablero(jugador);
    }

    public void presionarConfirmar() {
        if (!modelo.estaFlotaCompleta(jugador)) {
            mostrarMensaje("Debes colocar todas las naves para confirmar. Faltan: " + describirFaltantes());
            return;
        }
        mostrarMensaje("Una vez confirmado, ya no podras modificar tu tablero");
        if (leer("Confirmar? S/N").equalsIgnoreCase("S")) {
            aceptarConfirmacion();
        } else {
            mostrarMensaje("Confirmacion cancelada");
        }
    }

    public void aceptarConfirmacion() {
        controlador.confirmarTablero(jugador);
    }

    @Override
    public void actualizarPantalla() {
        boolean listo = modelo.obtenerEstadoJugador(jugador) == EstadoJugador.LISTO;

        if (!listo) {
            if (activa) {
                mostrarTablero();
            }
            return;
        }

        if (modelo.ambosConfirmaron()) {
            mostrarMensaje("Ambos tableros confirmados. Continua en Iniciar partida");
        } else if (activa) {
            mostrarMensaje("Tablero confirmado. Esperando a que tu rival confirme el suyo...");
        }
    }

    private void mostrarTablero() {
        boolean[][] ocupadas = modelo.obtenerCasillasOcupadas(jugador);
        System.out.println();
        System.out.println("   A B C D E F G H I J");
        for (int y = 0; y < ocupadas.length; y++) {
            System.out.printf("%2d ", y + 1);
            for (int x = 0; x < ocupadas[y].length; x++) {
                if (ocupadas[y][x]) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        if (modelo.estaFlotaCompleta(jugador)) {
            mostrarMensaje("Todas las naves colocadas correctamente. Confirma tu tablero");
        } else {
            mostrarMensaje("Pendientes: " + describirFaltantes());
        }
    }
    
     private void mostrarResultado(ResultadoColocacion resultado, String tipo) {
        switch (resultado) {
            case FUERA_DEL_TABLERO:
                mostrarMensaje("La nave no cabe en esa posicion");
                break;
            case NAVE_CERCA:
                mostrarMensaje("Posicion invalida: la nave no puede quedar pegada a otra");
                break;
            case TIPO_AGOTADO:
                mostrarMensaje("Ya colocaste todos los " + tipo + " permitidos");
                break;
            case TABLERO_CONFIRMADO:
                mostrarMensaje("Tu tablero ya esta confirmado");
                break;
        }
    }

    private String describirFaltantes() {
        String texto = "";
        for (String tipo : TIPOS) {
            int pendientes = modelo.obtenerNavesPendientes(jugador, tipo);
            if (pendientes > 0) {
                texto += pendientes + " " + tipo + " ";
            }
        }
        return texto.trim();
    }
    
    private String convertirTipo(String letra) {
        switch (letra.toUpperCase()) {
            case "P":
                return "PortaAviones";
            case "C":
                return "Crucero";
            case "S":
                return "Submarino";
            case "B":
                return "Barco";
            default:
                return null;
        }
    }

    private Coordenadas convertirCoordenadas(String texto) {
        String limpio = texto.trim().toUpperCase();
        if (limpio.length() < 2) {
            return new Coordenadas(-1, -1);
        }
        int x = limpio.charAt(0) - 'A';
        int y;
        try {
            y = Integer.parseInt(limpio.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return new Coordenadas(-1, -1);
        }
        return new Coordenadas(x, y);
    }
    
    private String leer(String pregunta) {
        System.out.print("[" + jugador.getNombre() + "] " + pregunta + ": ");
        if (!scanner.hasNextLine()) {
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private void mostrarMensaje(String mensaje) {
        System.out.println("[" + jugador.getNombre() + "] " + mensaje);
    }
}
