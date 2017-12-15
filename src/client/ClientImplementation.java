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
    
    private final Utente utente;
    private Server server;
    private final CasellaPostaElettronicaClient casellaPostaleClient;
    
    public ClientImplementation(String emailUtente) throws RemoteException {
        this.casellaPostaleClient = new CasellaPostaElettronicaClient(emailUtente);
        this.utente = this.casellaPostaleClient.recuperaDatiUtente(emailUtente);
        this.server = null;
        connettiAlServer();
    }
    
    /**
     * Registra ClientImplementation presso un rmiregistry e ottiene un 
     * riferimento all'oggetto remoto Server conoscendo il nome dell'oggetto
     * remoto e la posizione del rmiregistry presso il quale è registrato 
     */
    private void connettiAlServer(){
        /* 
        registrazione dell'oggetto ClientImplementation presso il registro di 
        bootstrap (rmiregistry) e il numero di porta (localhost)
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
    
    /**
     * Metodo per lanciare l'rmiregistry tramite codice Java
     */
    public static void lanciaRMIRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry creato");
        } catch (RemoteException e) {
            //non fa niente: RMI registry esiste già
            System.out.println("java RMI registry già esistente!");
        }
    }
    
    /**
     * Registra la ClientGUI contenuta nel parametro osservatore come Observer 
     * della casella postale dell'utente proprietario e aggiorna le email 
     * inviate e ricevute contenute nella casella postale
     * @param osservatore: ClientGUI che viene registrata come Observer del 
     *      modello
     */
    public void registraOsservatoreEAggiornaEmail(ClientGUI osservatore){
        this.casellaPostaleClient.recuperaEmailInviate();
        this.casellaPostaleClient.addObserver(osservatore);
        this.casellaPostaleClient.recuperaEmailRicevute();
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
    
    /**
     * Ordina le email inviate nella casella postale dell'utente proprietario
     * per priorità decrescente
     */
    public void ordinaInviatePerPriorità(){
        this.casellaPostaleClient.ordinaInviatePerPriorita();
    }
    
    /**
     * Ordina le email ricevute nella casella postale dell'utente proprietario
     * per priorità decrescente
     */
    public void ordinaRicevutePerPriorita(){
        this.casellaPostaleClient.ordinaRicevutePerPriorita();
    }
    
    /**
     * Ordina le email inviate nella casella postale dell'utente proprietario 
     * per data decrescente
     */
    public void ordinaInviatePerData(){
        this.casellaPostaleClient.ordinaInviatePerData();
    }
    
    /**
     * Ordina le email ricevute nella casella postale dell'utente proprietario
     * per data decrescemte
     */
    public void ordinaRicevutePerData(){
        this.casellaPostaleClient.ordinaRicevutePerData();
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email ricevuta dal
    utente proprietario della casella di posta elettronica
    */
    private int getUltimaRicevuta(){
        return this.casellaPostaleClient.getUltimaRicevuta();
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email inviata dal
    utente proprietario della casella di posta elettronica
    */
    private int getUltimaInviata(){
        return this.casellaPostaleClient.getUltimaInviata();
    }
    
    /**
     * Inoltra un email
     * @param emailDaInoltrare: email che si desidera inoltrare
     */
    public void inoltraEmail(Email emailDaInoltrare){
        inviaEmail(emailDaInoltrare);
    }
    
    /**
     * Invia una nuova email
     * @param emailDaInviare: email che si desidera inviare
     */
    public void inviaEmail(Email emailDaInviare){
        try {
            int idEmailInviata = this.server.inviaEmail(emailDaInviare);
            if(idEmailInviata == -1){
                System.out.println("Invio dell'email non riuscito!");
            } else{
                System.out.println("Email inviata con successo!");
                emailDaInviare.setId(idEmailInviata);
                this.casellaPostaleClient.inserisciInInviati(emailDaInviare);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Elimina un email dal database del server e poi da quello del client se 
     * non si sono verificati errori
     * @param emailDaEliminare: email che si desidera eliminare
     */
    public void eliminaEmail(Email emailDaEliminare){
        try {
            if(this.server.eliminaEmail(emailDaEliminare, this.utente)){
                System.out.println("Email eliminata con successo!");
                this.casellaPostaleClient.elimina(emailDaEliminare);
            } else{
                System.out.println("Si è verificato un errore durante l'eliminazione dell'email, riprovare più tardi!");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void riceviEmail(Email emailRicevuta) throws RemoteException{
        if(emailRicevuta != null){
            this.casellaPostaleClient.inserisciInRicevuti(emailRicevuta);
        }
    }
  
}
