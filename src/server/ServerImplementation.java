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

    @Override
    public Email inviaEmail(Email emailDaInviare) throws RemoteException {
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
    
    @Override
    public void connettiAlClient(String emailClient) throws RemoteException {
        try {
            this.client[clientConnessi] = (Client)Naming.lookup("//localhost/Client/" + emailClient);
            clientConnessi++;
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static void main(String[] args) throws RemoteException{
        
        ServerImplementation server = new ServerImplementation();
        Utente uno = new Utente("alessio","berger","alessio.berger@edu.unito.it");
        Utente due = new Utente("lorenzo","imperatrice","lorenzo.imperatrice@edu.unito.it");
        ArrayList<Utente> destinatari = new ArrayList();
        destinatari.add(uno);
        destinatari.add(due);
        
        
        
    Email emailInviare = new Email(10,uno,destinatari,"ciao","ciao ciao");
        
        server.casella.inviaEmail(emailInviare);
    }
    
}
