package server;

import client.Client;
import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import modelli.EmailDaInviare;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public interface Server extends Remote{
    /*
    *   Restituisce le email inviate di un determinato utente a partire da quella con 
    *   idEmail uguale a ultimaInviata
    */
    ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException;
    /*
    *   Restituisce le email ricevute di un determinato utente a partire da quella con 
    *   idEmail uguale a ultimaRicevuta
    */
    ArrayList<Email> getRicevute(int ultimaRicevuta, Utente utente) throws RemoteException;
    /*
    *   Elimina l'email dal database
    */
    boolean eliminaEmail(Email emailDaEliminare, Utente utente) throws RemoteException;
    /*
    *   A partire da un istanza di EmailDaInviare crea un'istanza di tipo Email che viene
    *   inserita all'interno del database e poi la restituisce al chiamante
    */
    Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException;
    /*
    *   Genera una connessione remota sull'oggetto chiamante
    */
    void connettiAlClient(String emailClient) throws RemoteException;
}
