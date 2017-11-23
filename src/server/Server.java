/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public interface  Server {
    ArrayList<Email> getInviate(int ultimaInviata, Utente utente);
    ArrayList<Email> getRicevute(int ultimaRicevuta, Utente utente);
    boolean eliminaEmail(Email emailDaEliminare, Utente utente);
    boolean inviaEmail(Email emailDaInviare);
}
