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
import modelli.EmailDaInviare;
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
            /*
            utilizzando il metodo lookup con il nome dell'oggetto remoto Server 
            viene creato nel computer del Client uno stub dell'oggetto remoto
            */
            this.server = (Server)Naming.lookup("//localhost/Server");
            this.server.connettiAlClient(this.utente.getEmail());
        }
        catch(NullPointerException | MalformedURLException | NotBoundException | RemoteException e) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, e);
            String messaggio = "Connessione al server non riuscita! Impossibile prelevare eventuali nuove email!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }   
    }
    
    /**
     * Metodo per lanciare l'rmiregistry tramite codice Java
     */
    public static void lanciaRMIRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            //non fa niente: RMI registry esiste già
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
        this.casellaPostaleClient.addObserver(osservatore);
        connettiAlServer();
        this.casellaPostaleClient.recuperaEmailInviate();
        this.casellaPostaleClient.recuperaEmailRicevute();
        ArrayList<Email> nuoveEmailInviate = null;
        ArrayList<Email> nuoveEmailRicevute = null;
        if(this.server != null){
            try {
                final int val1 =getUltimaInviata();
                final int val2 =getUltimaRicevuta();
                nuoveEmailInviate = this.server.getInviate(val1, this.utente);
                nuoveEmailRicevute = this.server.getRicevute(val2, this.utente);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(nuoveEmailInviate == null){
                System.out.println("si è verificato un errore durante il recupero delle email inviate");
            } else if(nuoveEmailInviate.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailInviate(nuoveEmailInviate);
            }
            if(nuoveEmailRicevute == null){
                System.out.println("si è verificato un errore durante il recupero delle email ricevute");
            } else if(nuoveEmailRicevute.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailRicevute(nuoveEmailRicevute);
            }
        }   
        this.casellaPostaleClient.terminaAggiornamentoInizialeEmail();
    }
    
    /**
     * Mostra al Client le sue email inviate
     */
    public void getInviate(){
        ArrayList<Email> nuoveEmailInviate = null;
        if(this.server != null){
            try {
                nuoveEmailInviate = this.server.getInviate(getUltimaInviata(), this.utente);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(nuoveEmailInviate != null && nuoveEmailInviate.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailInviate(nuoveEmailInviate);
            } else if(nuoveEmailInviate == null){
                System.out.println("si è verificato un errore durante il recupero delle email inviate");
            }
        } else{
            String messaggio = "Impossibile prelevare nuove email dal server: connessione assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
        this.casellaPostaleClient.mostraEmailInviate();
    }
    
    /**
     * Mostra al Client le sue email ricevute
     */
    public void getRicevute(){
        ArrayList<Email> nuoveEmailRicevute = null;
        if(this.server != null){
            try {
                nuoveEmailRicevute = this.server.getInviate(getUltimaInviata(), this.utente);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(nuoveEmailRicevute != null && nuoveEmailRicevute.size() > 0){
                this.casellaPostaleClient.inserisciNuoveEmailInviate(nuoveEmailRicevute);
            } else if(nuoveEmailRicevute == null){
                System.out.println("si è verificato un errore durante il recupero delle email ricevute");
            }
        } else{
            String messaggio = "Impossibile prelevare nuove email dal server: connessione assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
        this.casellaPostaleClient.mostraEmailRicevute();
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
    public void inoltraEmail(EmailDaInviare emailDaInoltrare){
        if(!inviaEmail(emailDaInoltrare)){
            String messaggio = "Impossibile inoltrare l'email: connessione al server assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
    }
    
    /**
     * Invia una nuova email
     * @param emailDaInviare: email che si desidera inviare
     * @return true se è avvenuta comunicazione con il server, false altrimenti
     */
    public boolean inviaEmail(EmailDaInviare emailDaInviare){
        try {
            emailDaInviare.setMittente(this.utente);
            Email emailInviata = this.server.inviaEmail(emailDaInviare);
            if(emailInviata == null){
                System.out.println("Invio dell'email non riuscito!");
            } else{
                System.out.println("Email inviata con successo!");
                this.casellaPostaleClient.inserisciInInviati(emailInviata);
            }
            return true;
        } catch (RemoteException | NullPointerException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            String messaggio = "Impossibile inviare l'email: connessione al server assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
            return false;
        }
    }
    
    /**
     * Elimina un email dal database del server e poi da quello del client se 
     * non si sono verificati errori
     * @param emailDaEliminare: email che si desidera eliminare
     */
    public void eliminaEmailInviata(Email emailDaEliminare){
        try {
            if(this.server.eliminaEmailPerMittente(emailDaEliminare, this.utente)){
                this.casellaPostaleClient.eliminaPerMittente(emailDaEliminare);
                System.out.println("Email inviata eliminata con successo!");
            } else{
                System.out.println("Si è verificato un errore durante l'eliminazione dell'email inviata, riprovare più tardi!");
            }
        } catch (RemoteException | NullPointerException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            String messaggio = "Impossibile eliminare email inviata: connessione al server assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
    }
    
    /**
     * Elimina un email dal database del server e poi da quello del client se 
     * non si sono verificati errori
     * @param emailDaEliminare: email che si desidera eliminare
     */
    public void eliminaEmailRicevuta(Email emailDaEliminare){
        try {
            if(this.server.eliminaEmailPerDestinatario(emailDaEliminare, this.utente)){
                this.casellaPostaleClient.eliminaPerDestinatario(emailDaEliminare);
                System.out.println("Email ricevuta eliminata con successo!");
            } else{
                System.out.println("Si è verificato un errore durante l'eliminazione dell'email ricevuta, riprovare più tardi!");
            }
        } catch (RemoteException | NullPointerException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            String messaggio = "Impossibile eliminare email ricevuta: connessione al server assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
    }
    
    /**
     * Notifica al server che il client si vuole disconnettere
     * @return true nel caso in cui la disconnessione sia avvenuta con successo,
     *      false altrimenti
     */
    public boolean disconnettiClientDaServer(){
        try {
            if(this.server != null && this.server.disconnettiClient(this.utente.getEmail())){
                System.out.println("disconnessione del client dal server riuscita");
                return true;
            } else{
                if(this.server != null) {
                    System.out.println("disconnessione del client dal server fallita");
                } else {
                    System.out.println("Non eri connesso a nessun server");
                }
            }
        } catch (RemoteException | NullPointerException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Notifica la lettura di un'email al server e alla casella di posta locale
     * @param emailLetta: email da segnare come già letta
     */
    public void segnaLetturaEmail(Email emailLetta){
        try {
            if(this.server.segnaEmailComeLetta(utente.getEmail(),emailLetta)){
                this.casellaPostaleClient.segnaLetturaEmail(emailLetta);
                System.out.println("lettura email riuscita");
            } else{
                System.out.println("lettura email fallita");
            }
        } catch (RemoteException | NullPointerException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            String messaggio = "Impossibile leggere email: connessione al server assente!";
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
    }
    
    @Override
    public void riceviEmail(Email emailRicevuta) throws RemoteException{
        if(emailRicevuta != null){
            this.casellaPostaleClient.inserisciInRicevuti(emailRicevuta);
        }
    }

    @Override
    public void riceviMessaggio(String messaggio) throws RemoteException {
        if(messaggio != null){
            this.casellaPostaleClient.inserisciMessaggio(messaggio);
        }
    }    
}
