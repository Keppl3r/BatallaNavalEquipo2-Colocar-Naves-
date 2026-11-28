/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.battleship.view;

import itson.battleship.controller.IControlador;
import itson.battleship.enums.OrientacionNave;
import itson.battleship.model.IModel;
import itson.battleship.model.Jugador;
import itson.battleship.observer.Observador;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * Pantalla para colocar las naves
 * @author Kevin Adrian Mendoza Moreno
 */
public class PantallaColocacion implements Observador {
    
     private static final String[] TIPOS = {"PortaAviones", "Crucero", "Submarino", "Barco"};

    private IControlador controlador;
    private IModel modelo;
    private Jugador jugador;

    private JFrame frame;

    // tablero
    private JButton[][] casillas;

    // naves disponibles
    private JButton[] botonesNave;
    private JLabel lblOrientacion;
    private String tipoSeleccionado;
    private OrientacionNave orientacion;

    // mensajes y botones
    private JLabel lblMensaje;
    private JButton btnLimpiar;
    private JButton btnConfirmar;

    public PantallaColocacion(IControlador controlador, IModel modelo, Jugador jugador) {
        this.controlador = controlador;
        this.modelo = modelo;
        this.jugador = jugador;
        this.orientacion = OrientacionNave.HORIZONTAL;
    }

    @Override
    public void actualizarPantalla() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}