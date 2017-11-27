/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelli.Email;
import modelli.Utente;
import server.Server;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ClientImplementation extends UnicastRemoteObject implements Client{
    
    private Utente utente;
    private Server server;
    private CasellaPostaElettronicaClient casellaPostaleClient;
    
    public ClientImplementation(String emailUtente) throws RemoteException{
        connettiAlServer();
        this.casellaPostaleClient = new CasellaPostaElettronicaClient(emailUtente);
        this.utente = this.casellaPostaleClient.recuperaDatiUtente(emailUtente);
        
    }
    
    private void connettiAlServer(){
        /* 
        registrazione dell'oggetto ClientImplementation presso il registro di 
        bootstrap (rmiregistry)
        */
        lanciaRMIRegistry();
        try {
            Naming.rebind("//localhost/Client" + this.utente.getEmail(), this);
            //chiamare metodo di server perchè lui mi registri come oggetto remoto
        }
        catch(MalformedURLException | RemoteException e) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, e);
        }
        /*
        utilizzando il metodo lookup con il nome dell'oggetto remoto Server 
        viene creato nel computer del Client uno stub dell'oggetto remoto
        */
        try {
            this.server = (Server)Naming.lookup("//localhost/Server");
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
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
    
    //public boolean inoltraEmail(Email emailDaInoltrare){}
    
    //public ArrayList<Email> ordinaPerPriorità(){}
    
    //public ArrayList<Email> ordinaPerData(){}
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email ricevuta dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaRicevuta(){
        return this.casellaPostaleClient.getUltimaRicevuta();
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email inviata dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaInviata(){
        return this.casellaPostaleClient.getUltimaInviata();
    }
    
    //fare metodi per prendere mail inviate e ricevute
    
    @Override
    public boolean riceviEmail(Email emailRicevuta) throws RemoteException{
        //azioni da intraprendere quando viene ricevuta una mail
        System.out.println("RMI FUNZIONA!!!!!");
        return true;
    }
    
    
    
}
