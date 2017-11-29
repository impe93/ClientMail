package server;

import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaServer {
    private final String urlDB;
    
    public CasellaServer(){
        this.urlDB = "jdbc:sqlite:Server.db";
    }
    
    public ArrayList<Email> recuperaEmailUtente(Utente utente){
        ArrayList<Email> emailUtente = new ArrayList<Email>();
        
        return emailUtente;
        
    }
    
}
