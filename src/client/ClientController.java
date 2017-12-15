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
    NuovaEmailGUI nuovaEmailGUI;
    
    public ClientController(ClientImplementation model) {
        this.model = model;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Component fonte = (Component)e.getSource();
        switch (fonte.getName()) {
            case "emailInviate": {
                
                break;
            }
            case "emailRicevute": {
                System.out.println("Sono entrato in emailRicevute!");
                break;
            }
            case "nuova": {
                this.nuovaEmailGUI = new NuovaEmailGUI(this);
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
            case "invia": {
                System.out.println("Sono entrato in invia!");
                this.nuovaEmailGUI.setVisible(false);
                break;
            }
            case "cancella": {
                System.out.println("Sono entrato in cancella!");
                this.nuovaEmailGUI.setVisible(false);
                break;
            }
            default: {
                break;
            }
        }
    }
    
}
