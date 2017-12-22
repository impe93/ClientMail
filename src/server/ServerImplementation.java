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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelli.Email;
import modelli.Utente;
import modelli.EmailDaInviare;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerImplementation extends UnicastRemoteObject implements Server{
    
    
    private Map<String, Client> clientConnessi = new HashMap<>();
    CasellaServer casella;

    public Map<String, Client> getClientConnessi() {
        return clientConnessi;
    }
    
    
    /*
    *  Costruttore di ServerImplementation
    */
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
    
    public void aggiungiObserver(ServerGUI serverGui){
        this.casella.addObserver(serverGui);
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
    
    /*il metodo riceviEmail sul client viene chiamato all'interno del metodo inviaEmail di CasellaServer*/
    @Override
    public Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException {
        Email emailRitorno = casella.inviaEmail(emailDaInviare, clientConnessi);
        if(emailRitorno != null){
            return emailRitorno;
        }
        else{
            System.out.println("Si è verificato un problema con l'invio dell'email");
            return null;
        }
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
            clientConnessi.put(emailClient, (Client)Naming.lookup("//localhost/Client/" + emailClient));
            
            casella.setOperazioneEseguita("* [NUOVO CLIENT CONNESSO: " + emailClient + " - " + 
                        new Date().toString() + "]");
            casella.logUltimaOperazione();
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
}
