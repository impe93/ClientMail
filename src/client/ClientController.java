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
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Component fonte = (Component)e.getSource();
        switch (fonte.getName()) {
            case "emailInviate":
                break;
            case "emailRicevute":
                break;
            case "nuova":
                break;
            case "elimina":
                break;
            case "inoltra":
                break;
            default:
                break;
        }
    }
    
}
