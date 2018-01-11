/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */

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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public final class ServerImplementation extends UnicastRemoteObject implements 
        Server{
     
    private final Map<String, Client> clientConnessi;
    private final ReadWriteLock rwHM;
    private final Lock rHM;
    private final Lock wHM;
    private final int NUM_THREAD = 3;
    private final CasellaServer casella;
    private final Executor exec;
  
    /**
    *  Costruttore di ServerImplementation
     * @throws java.rmi.RemoteException
    */
    public ServerImplementation() throws RemoteException{
        
        clientConnessi = new HashMap<>();
        casella = new CasellaServer();
        exec = Executors.newFixedThreadPool(NUM_THREAD);
        rwHM = new ReentrantReadWriteLock();
        rHM = rwHM.readLock();
        wHM = rwHM.writeLock();
        
        serverUp();
    }
    
    /**
     * Metodo per lanciare l'RMI Registry.
    */
    public static void lanciaRMIRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            //RMI registry esiste già
        }
    }
    
    /**
     * Metodo per la registrazione dell'oggetto ServerImplementation presso il 
     * registro di bootstrap (rmiregistry)
    */
    public void serverUp(){
       
        lanciaRMIRegistry();
        try {
            Naming.rebind("//localhost/Server", this);
        }
        catch(MalformedURLException | RemoteException e) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Metodo per la registrazione dell'interfaccia grafica (ServerGUI) come 
     * observer del modello (CasellaServer)
     * @param serverGui
    */  
    public void aggiungiObserver(ServerGUI serverGui){
        this.casella.addObserver(serverGui);
    }
    
    /** Metodo che restituisce un ArrayList di Email contenente tutte le email 
     * inviate dall'utente a partire da quella con id uguale a ultimaInviata
     * @param ultimaInviata: identificativo numerico dell'ultima email presente 
     * nella casella di posta inviata
     * @param utente: utente di cui di vuole ricevere le email inviate
     * @return ArrayList di Email contenente tutte le Email inviate da utente a
     * partire da quella con id uguale a ultimaInviata. Null in caso d'errore
     * @throws java.rmi.RemoteException*/
    @Override
    public ArrayList<Email> getInviate(final int ultimaInviata, final Utente utente) throws RemoteException {
        
        FutureTask<ArrayList<Email>> ft = new FutureTask<>(new Callable<ArrayList<Email>>(){
            @Override
            public ArrayList<Email> call() {
                return casella.recuperaEmailInviateUtente(ultimaInviata, utente);
            }
        });
        
        ArrayList<Email> inviate = null;
        
        if(ultimaInviata>=0 && utente!= null){
            exec.execute(ft);
            try{
                inviate = ft.get();
            }catch(InterruptedException | ExecutionException e){
                System.out.println(e.getMessage());
            }
        }
        
        return inviate;
    }
    
    /** Metodo che restituisce un ArrayList di Email contenente tutte le email 
     * ricevute dall'utente a partire da quella con id uguale a ultimaRicevuta
     * @param ultimaRicevuta: identificativo numerico dell'ultima email presente 
     * nella casella di posta ricevuta
     * @param utente: utente di cui di vuole ricevere le email ricevute
     * @return ArrayList di Email contenente tutte le Email ricevute da utente a
     * partire da quella con id uguale a ultimaRicevuta. Null in caso d'errore
     * @throws java.rmi.RemoteException*/
    @Override
    public ArrayList<Email> getRicevute(final int ultimaRicevuta, final Utente utente) throws RemoteException {
        
        FutureTask<ArrayList<Email>> ft = new FutureTask<>(new Callable<ArrayList<Email>>(){
            @Override
            public ArrayList<Email> call() {
                return casella.recuperaEmailRicevuteUtente(ultimaRicevuta, utente);
            }
        });
        
        ArrayList<Email> ricevute = null;
        
        if(ultimaRicevuta >=0 && utente != null){
            exec.execute(ft);
            try{
                ricevute = ft.get();
            }catch(InterruptedException | ExecutionException e){
                System.out.println(e.getMessage());
            }
        }
        
        return ricevute;
    }
    
    /** Metodo che invia un Email nella casella di posta dei destinatari 
     * dell'EmailDaInviare passata come parametro; effettua il controllo sui
     * destinatari (se esisteono o no) e per quelli esistenti e online effettua 
     * la chiamata del metodo riceviEmail() sul rispettivo client
     * @param emailDaInviare: Email che si vuole inviare ricevuta 
     * dall'interfaccia grafica del client mittente
     * @return un oggetto di tipo Email che è una copia dell'EmailDaInviare
     * ricevuta come parametro, con la differenza che in questa Email i destinatari
     * sono oggetti di tipo Utente, mentre in EmailDaInvare sono email testuali,
     * quindi di tipo String (questo perchè l'interfaccia grafica non conosce
     * l'oggetto Utente). Null in caso d'errore
     * @throws java.rmi.RemoteException*/
    @Override
    public Email inviaEmail(final EmailDaInviare emailDaInviare) throws RemoteException {
        
        FutureTask<Email> ft;
        ft = new FutureTask<>(new Callable<Email>(){
            @Override
            public Email call() throws RemoteException {
                rHM.lock();
                try {
                    return casella.inviaEmail(emailDaInviare, clientConnessi);
                } finally {
                    rHM.unlock();
                }
            }
        });
        
        //Email da ritornare al mittente (copia di emailDaInviare) se l'invio
        //va a buon fine
        Email emailRitorno = null;
        
        if(emailDaInviare != null){
            if(emailDaInviare.getDestinatari()!=null && emailDaInviare.getMittente()!=null
                    && emailDaInviare.getOggetto()!=null && emailDaInviare.getPriorita()>0
                    && emailDaInviare.getPriorita()<11){
                exec.execute(ft);
                
                try{
                    emailRitorno = ft.get();
                }
                catch(InterruptedException | ExecutionException e){
                    System.out.println(e.getMessage());
                }
                
                if(emailRitorno != null){
                    /*se emailRitorno è diverso da null vuol dire che l'email è stata
                    *inviata correttamente, quindi notifico il mittente dell'avvenuto
                    *invio.
                    */
                    rHM.lock();
                    try{
                        try{
                            clientConnessi.get(emailRitorno.getMittente().getEmail()).riceviMessaggio("<b>Email inviata</b> correttamente!");
                        }
                        catch(RemoteException e){
                            System.out.println(e.getMessage());
                        }
                    } finally {
                        rHM.unlock();
                    }
                    
                    /*
                    * Invio a ognuno dei destinatari l'email che andrà nella sua
                    * casella di posta in arrivo e notifico ognuno di loro con un
                    * messaggio della ricezione della nuova email
                    */
                    ArrayList<Utente> destinatari = new ArrayList<>();
                    destinatari.addAll(emailRitorno.getDestinatari());
                    
                    for(Utente destinatario : destinatari){
                        rHM.lock();
                        try {
                            Client clientRicevente = clientConnessi.get(destinatario.getEmail());
                            if(clientRicevente != null){
                                try{
                                    clientRicevente.riceviEmail(emailRitorno);
                                }
                                catch(RemoteException e){
                                    System.out.println(e.getMessage());
                                }
                                try{
                                    clientRicevente.riceviMessaggio("Hai ricevuto una <b>nuova email</b> da <b>"
                                            + emailRitorno.getMittente().getEmail()+"</b>!<br>"
                                            + "<b>Oggetto</b>: " + emailRitorno.getOggetto());
                                }
                                catch(RemoteException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                        } finally {
                            rHM.unlock();
                        }
                    }
                    
                    /*Ritorno al mittente l'istanza di Email inviata */
                    return emailRitorno;
                }
                else{
                    System.out.println("Server Error - Si è verificato un problema con l'invio dell'email.");
                    return null;
                }
            }
            else return null;
        }
        
        else
            return null;
    }
    
    /**Metodo che connette il Server tramite Rmi al Client quando quest'ultimo
     * effettua la connessione e inserisce il rispettivo client nell'HashMap
     * dei client connessi.
     * @param emailClient: email del Client a cui ci si vuole connettere
     * @throws java.rmi.RemoteException*/
    @Override
    public void connettiAlClient(String emailClient) throws RemoteException {
        if (emailClient != null){
            wHM.lock();
            try {
                try {
                    clientConnessi.put(emailClient, (Client)Naming.lookup("//localhost/Client/" + emailClient));
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    
                    casella.setOperazioneEseguita("* [NUOVO CLIENT CONNESSO: " + emailClient + " - " +
                            new Date().toString() + "]");
                    casella.logUltimaOperazione();
                }
            }
            finally {
                wHM.unlock();
            }
        }
    }
    
    /**Metodo che viene chiamato quando un client si sconnette e rimuove il 
     *rispettivo client dalla HashMap dei client connessi.
     * @param emailClient: email del client che si sconnette
     * @return true in caso di successo, false altrimenti
     * @throws java.rmi.RemoteException
     */
    @Override
    public boolean disconnettiClient(String emailClient) throws RemoteException {
        Client client = null;
        wHM.lock();
        try {
            client = clientConnessi.remove(emailClient);
        } finally {
            wHM.unlock();
        }
        casella.setOperazioneEseguita("* [CLIENT DISCONNESSO: " + emailClient + " - " +
                new Date().toString() + "]");
        casella.logUltimaOperazione();
        return client != null;
        
    }
    
    /**Metodo che segna l'email passata come parametro come letta nel database.
     * @param emailClient: email del client che ha letto l'email
     * @param emailLetta: email letta
     * @return true in caso di successo, false altrimenti
     * @throws java.rmi.RemoteException*/
    @Override
    public boolean segnaEmailComeLetta(String emailClient, Email emailLetta) throws RemoteException {
        return casella.setLetta(emailClient, emailLetta);  
    }
    
    /**Metodo che segna nel database l'eliminazione di un'email da parte 
     * dell'utente (quando quest'ultimo ne è il mittente).
     * @param emailDaEliminare: email che si desidera eliminare
     * @param utente: utente che effettua l'eliminazione
     * @return true in caso di successo, false atrimenti.
     * @throws java.rmi.RemoteException*/
    @Override
    public boolean eliminaEmailPerMittente(Email emailDaEliminare, Utente utente) throws RemoteException {
        if(emailDaEliminare != null && utente != null){
            boolean eliminata = casella.eliminaEmailDaMittente(emailDaEliminare, utente.getEmail());
            if(eliminata){
                rHM.lock();
                try {
                    clientConnessi.get(utente.getEmail()).riceviMessaggio("<b>Email eliminata</b> con successo!");
                } finally {
                    rHM.unlock();
                }
            }
            return eliminata;
        }
        else return false;
    }
    
    /**Metodo che segna nel database l'eliminazione di un'email da parte 
     * dell'utente (quando quest'ultimo ne è il destinatario).
     * @param emailDaEliminare: email che si desidera eliminare
     * @param utente: utente che effettua l'eliminazione
     * @return true in caso di successo, false atrimenti.
     * @throws java.rmi.RemoteException*/
    @Override
    public boolean eliminaEmailPerDestinatario(Email emailDaEliminare, Utente utente) throws RemoteException {
        if(emailDaEliminare!=null && utente!=null){
            boolean eliminata = casella.eliminaEmailDaDestinatario(emailDaEliminare, utente.getEmail());
            if(eliminata){
                rHM.lock();
                try {
                    clientConnessi.get(utente.getEmail()).riceviMessaggio("<b>Email eliminata</b> con successo!");
                } finally {
                    rHM.unlock();
                }
            }
            return eliminata;
        }
        else
            return false;
    }
    
}
