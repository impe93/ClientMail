package server;

import client.Client;
import client.ClientImplementation;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelli.Email;
import modelli.Utente;
import modelli.EmailDaInviare;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerImplementation extends UnicastRemoteObject implements Server{
    
    Client[] client = new Client[3];
    CasellaServer casella;
    int clientConnessi;
    
    public ServerImplementation() throws RemoteException{
        /* 
        registrazione dell'oggetto ServerImplementation presso il registro di 
        bootstrap (rmiregistry)
        */
        lanciaRMIRegistry();
        try {
            Naming.rebind("//localhost/Server", this);
            //chiamare metodo di server perchè lui mi registri come oggetto remoto
        }
        catch(MalformedURLException | RemoteException e) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, e);
        }
        
        casella = new CasellaServer();
        clientConnessi = 0;
    
    }

    @Override
    public ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException {
        return casella.recuperaEmailInviateUtente(ultimaInviata, utente);
    }

    @Override
    public ArrayList<Email> getRicevute(int ultimaRicevuta, Utente utente) throws RemoteException {
        return casella.recuperaEmailRicevuteUtente(ultimaRicevuta, utente);
    }

    @Override
    public boolean eliminaEmail(Email emailDaEliminare, Utente utente) throws RemoteException {
    return false;
    }
    
    /* TODO: Controllare che il email ritorno non sia null e chiamare il metodo
    ricevi email sui giusti destinatari utilizzando l'hash map (vedere todo in connettiAlClient per info) */
    @Override
    public Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException {
        return casella.inviaEmail(emailDaInviare);
    }
    
    public static void lanciaRMIRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry creato");
        } catch (RemoteException e) {
            //non fa niente: RMI registry esiste già
            System.out.println("java RMI registry già esistente!");
        }
    }
    
    /* TODO: Mettere la registrazione dei client come HashMap e non come array se no il client
    è irrintracciabile all'interno dell'array, non avendo alcun nome, utilizzare come chiave dell'hash map
    emailClient */
    @Override
    public void connettiAlClient(String emailClient) throws RemoteException {
        try {
            this.client[clientConnessi] = (Client)Naming.lookup("//localhost/Client/" + emailClient);
            clientConnessi++;
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
}
