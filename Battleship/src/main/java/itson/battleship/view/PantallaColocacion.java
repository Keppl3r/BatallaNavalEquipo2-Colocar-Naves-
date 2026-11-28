/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.battleship.view;

import itson.battleship.controller.IControlador;
import itson.battleship.enums.OrientacionNave;
import itson.battleship.enums.ResultadoColocacion;
import itson.battleship.model.Coordenadas;
import itson.battleship.model.IModel;
import itson.battleship.model.Jugador;
import itson.battleship.observer.Observador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


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
    
    public void iniciar() {
        frame = new JFrame("Coloca tus naves - " + jugador.getNombre());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // tablero
        JPanel panelTablero = new JPanel(new GridLayout(11, 11));
        panelTablero.setBorder(BorderFactory.createTitledBorder("Tu tablero"));
        panelTablero.add(new JLabel(""));
        for (int x = 0; x < 10; x++) {
            panelTablero.add(new JLabel(String.valueOf((char) ('A' + x)), SwingConstants.CENTER));
        }
        casillas = new JButton[10][10];
        for (int y = 0; y < 10; y++) {
            panelTablero.add(new JLabel(String.valueOf(y + 1), SwingConstants.CENTER));
            for (int x = 0; x < 10; x++) {
                casillas[y][x] = new JButton();
                casillas[y][x].addActionListener(new CasillaListener(x, y));
                casillas[y][x].addMouseListener(new GirarListener());
                panelTablero.add(casillas[y][x]);
            }
        }

        // naves disponibles
        JPanel panelNaves = new JPanel();
        panelNaves.setLayout(new BoxLayout(panelNaves, BoxLayout.Y_AXIS));
        panelNaves.setBorder(BorderFactory.createTitledBorder("Naves disponibles"));
        botonesNave = new JButton[TIPOS.length];
        for (int i = 0; i < TIPOS.length; i++) {
            botonesNave[i] = new JButton(TIPOS[i]);
            botonesNave[i].addActionListener(new NaveListener(TIPOS[i]));
            panelNaves.add(botonesNave[i]);
        }
        lblOrientacion = new JLabel("Orientación: horizontal");
        panelNaves.add(lblOrientacion);
        panelNaves.add(new JLabel("Clic derecho para girarla"));

        // mensajes y botones
        lblMensaje = new JLabel(" ");
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new LimpiarListener());
        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener(new ConfirmarListener());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.add(btnLimpiar);
        panelInferior.add(btnConfirmar);
        panelInferior.add(lblMensaje);

        frame.getContentPane().add(BorderLayout.NORTH, new JLabel(" Elige una nave y da clic en la casilla donde empieza"));
        frame.getContentPane().add(BorderLayout.CENTER, panelTablero);
        frame.getContentPane().add(BorderLayout.EAST, panelNaves);
        frame.getContentPane().add(BorderLayout.SOUTH, panelInferior);

        mostrarTablero();

        frame.setSize(800, 560);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    
    // eventos
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
        int respuesta = JOptionPane.showConfirmDialog(frame, "Una vez confirmado, ya no podrás modificar tu tablero",
                "Confirmar tablero", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            aceptarConfirmacion();
        } else {
            mostrarMensaje("Confirmación cancelada");
        }
    }

    public void aceptarConfirmacion() {
        controlador.confirmarTablero(jugador);
    }

    @Override
    public void actualizarPantalla() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}