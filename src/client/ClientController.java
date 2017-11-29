/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author lorenzo
 */
public class ClientController implements ActionListener {
    
    private ClientImplementation model;
    
    public ClientController(ClientImplementation model) {
        this.model = model;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Component fonte = (Component)e.getSource();
        switch (fonte.getName()) {
            case "emailInviate": {
                System.out.println("Sono entrato in emailInviate!");
                break;
            }
            case "emailRicevute": {
                System.out.println("Sono entrato in emailRicevute!");
                break;
            }
            case "nuova": {
                System.out.println("Sono entrato in nuova!");
                break;
            }
            case "elimina": {
                System.out.println("Sono entrato in elimina!");
                break;
            }
            case "inoltra": {
                System.out.println("Sono entrato in inoltra!");
                break;
            }
            default: {
                break;
            }
        }
    }
    
}
