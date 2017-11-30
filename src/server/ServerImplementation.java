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
    
    Client client;
    CasellaServer casella;
    
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
    
    }

    @Override
    public ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException {
    return null;
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
    public boolean inviaEmail(Email emailDaInviare) throws RemoteException {
    return false;
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
    public boolean ciao() throws RemoteException{
        System.out.println("ciao Impe, suca");
        return true;
    }

    @Override
    public void connettiAlClient(String emailClient) throws RemoteException {
        try {
            this.client = (Client)Naming.lookup("//localhost/Client/" + emailClient);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static void main(String[] args) throws RemoteException{
        
    ArrayList<Email> email = new ArrayList<Email>();
    ServerImplementation server = new ServerImplementation();
        
        
            email = server.getRicevute(1,new Utente("Lorenzo","Imperatrice",
                    "lorenzo.imperatrice@edu.unito.it"));
       
            for (int i=0;i<email.size();i++){
                email.get(i).toString();
            }
    }
    
}
