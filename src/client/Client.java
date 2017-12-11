package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import modelli.Email;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public interface Client extends Remote{
    
    public void riceviEmail(Email emailRicevuta) throws RemoteException;
    
}
