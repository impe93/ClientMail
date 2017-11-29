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
import java.util.ArrayList;
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
        this.casellaPostaleClient = new CasellaPostaElettronicaClient(emailUtente);
        this.utente = this.casellaPostaleClient.recuperaDatiUtente(emailUtente);
        this.server = null;
        connettiAlServer();
    }
    
    private void connettiAlServer(){
        /* 
        registrazione dell'oggetto ClientImplementation presso il registro di 
        bootstrap (rmiregistry)
        */
        lanciaRMIRegistry();
        try {
            Naming.rebind("//localhost/Client/" + this.utente.getEmail(), this);
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
            this.server.connettiAlClient(this.utente.getEmail());
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
    
    /*
    public ArrayList<Email> ordinaInviatePerData(){
        return this.casellaPostaleClient.ordinaInviatePerData();
    }
    
    public ArrayList<Email> ordinaRicevutePerData(){
        return this.casellaPostaleClient.ordinaRicevutePerData();
    }
    
    */
    
    public void registraOsservatoreEAggiornaEmail(ClientGUI osservatore){
        this.casellaPostaleClient.addObserver(osservatore);
        this.casellaPostaleClient.recuperaTutteEmail();
        ArrayList<Email> nuoveEmailInviate = null;
        ArrayList<Email> nuoveEmailRicevute = null;
        if(this.server != null){
            try {
                nuoveEmailInviate = this.server.getInviate(getUltimaInviata(), this.utente);
                nuoveEmailRicevute = this.server.getRicevute(getUltimaRicevuta(), this.utente);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(nuoveEmailInviate != null && nuoveEmailInviate.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailInviate(nuoveEmailInviate);
            }
            if(nuoveEmailRicevute != null && nuoveEmailRicevute.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailRicevute(nuoveEmailRicevute);
            }
        }
    }
    
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
