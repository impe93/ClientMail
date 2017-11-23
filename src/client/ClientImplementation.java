/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ClientImplementation implements Client{
    
    private ArrayList<Email> emailRicevute;
    private ArrayList<Email> emailInviate;
    private Utente utente;
    private Server server;
    
    @Override
    public boolean riceviEmail(Email emailRicevuta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
