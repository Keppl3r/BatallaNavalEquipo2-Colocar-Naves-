/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.battleship.red;

import itson.battleship.model.Jugador;

/**
 *
 * @author Kevin Adrian Mendoza Moreno - 215752 
 * Está clase es una simulación de la conexión local
 */
public class ProxyPeer {
    
    public void enviarConfirmacion(Jugador jugador) {
        System.out.println("red" + jugador.getNombre() + " confirmo su tablero");
    }
    
}
