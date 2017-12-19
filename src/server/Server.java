package server;

import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import modelli.EmailDaInviare;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public interface Server extends Remote{
    ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException;
    ArrayList<Email> getRicevute(int ultimaRicevuta, Utente utente) throws RemoteException;
    boolean eliminaEmail(Email emailDaEliminare, Utente utente) throws RemoteException;
    Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException;
    void connettiAlClient(String emailClient) throws RemoteException;
}
