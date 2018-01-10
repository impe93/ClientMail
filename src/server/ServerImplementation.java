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

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerImplementation extends UnicastRemoteObject implements Server{
    
    
    private Map<String, Client> clientConnessi = new HashMap<>();
    private final ReadWriteLock rwHM;
    private final Lock rHM;
    private final Lock wHM;
    private final int NUM_THREAD = 3;
    CasellaServer casella;
    Executor exec;
    
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
        exec = Executors.newFixedThreadPool(NUM_THREAD);
        rwHM = new ReentrantReadWriteLock();
        rHM = rwHM.readLock();
        wHM = rwHM.writeLock();
    }
    
    public void aggiungiObserver(ServerGUI serverGui){
        this.casella.addObserver(serverGui);
    }
    
    @Override
    public ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException {
        final int ultimaInviataFinal = ultimaInviata;
        final Utente utenteFinal = utente;
        FutureTask<ArrayList<Email>> ft = new FutureTask<>(new Callable<ArrayList<Email>>(){
            @Override
            public ArrayList<Email> call() {
                return casella.recuperaEmailInviateUtente(ultimaInviataFinal, utenteFinal);
            }
        });
        
        exec.execute(ft);
        ArrayList<Email> inviate = null;
        try{
            inviate = ft.get();
        }catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        
        return inviate;
    }
    
    @Override
    public ArrayList<Email> getRicevute(int ultimaRicevuta, Utente utente) throws RemoteException {
        final int ultimaRicevutaFinal = ultimaRicevuta;
        final Utente utenteFinal = utente;
        
        FutureTask<ArrayList<Email>> ft = new FutureTask<>(new Callable<ArrayList<Email>>(){
            @Override
            public ArrayList<Email> call() {
                return casella.recuperaEmailRicevuteUtente(ultimaRicevutaFinal, utenteFinal);
            }
        });
        
        exec.execute(ft);
        ArrayList<Email> ricevute = null;
        try{
            ricevute = ft.get();
        }catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        
        return ricevute;
    }
    
    /*il metodo riceviEmail sul client viene chiamato all'interno del metodo inviaEmail di CasellaServer*/
    @Override
    public Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException {
        final EmailDaInviare emailDaInviareFinal = emailDaInviare;
        
        FutureTask<Email> ft = new FutureTask<>(new Callable<Email>(){
            @Override
            public Email call() throws RemoteException {
                return casella.inviaEmail(emailDaInviareFinal, clientConnessi);
            }
        });
        Email emailRitorno = null;
        ArrayList<String> destinatariInesistenti = new ArrayList<>();
        
        exec.execute(ft);
        try{
            emailRitorno = ft.get();
        }
        catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        if(emailRitorno != null){
            rHM.lock();
            try{
                try{
                    clientConnessi.get(emailRitorno.getMittente().getEmail()).riceviMessaggio("Email inviata correttamente!");
                }
                catch(RemoteException e){
                    System.out.println(e.getMessage());
                }
            } finally {
                rHM.unlock();
            }
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
                            clientRicevente.riceviMessaggio("Hai ricevuto una nuova email da "
                                    + emailRitorno.getMittente().getEmail()+"!");
                        }
                        catch(RemoteException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else if(casella.recuperaDatiUtente(destinatario.getEmail())==null){
                        destinatariInesistenti.add(destinatario.getEmail());
                    }
                } finally {
                    rHM.unlock();
                }
            }
            if(destinatariInesistenti.isEmpty()==false){
                String messaggio = "Non è stato possibile inviare l'email "
                        + "ai seguenti destinatari:\n";
                for(String utente : destinatariInesistenti){
                    messaggio = messaggio + utente + "\n";
                }
                rHM.lock();
                try{
                    try{
                        clientConnessi.get(emailRitorno.getMittente().getEmail()).riceviMessaggio(messaggio);
                    }
                    catch(RemoteException e){
                        System.out.println(e.getMessage());
                    }
                } finally {
                    rHM.unlock();
                }
            }
            
            return emailRitorno;
        }
        else{
            System.out.println("Si è verificato un problema con l'invio dell'email (server)");
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
        wHM.lock();
        try {
            try {
                clientConnessi.put(emailClient, (Client)Naming.lookup("//localhost/Client/" + emailClient));
                
                casella.setOperazioneEseguita("* [NUOVO CLIENT CONNESSO: " + emailClient + " - " +
                        new Date().toString() + "]");
                casella.logUltimaOperazione();
            } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally {
                wHM.unlock();
        }
    }
    
    @Override
    public boolean disconnettiClient(String emailClient) throws RemoteException {
        wHM.lock();
        try {
            clientConnessi.remove(emailClient);
        } finally {
            wHM.unlock();
        }
        casella.setOperazioneEseguita("* [CLIENT DISCONNESSO: " + emailClient + " - " +
                new Date().toString() + "]");
        casella.logUltimaOperazione();
        //valore di ritorno??
        return true;
        
    }
    
    @Override
    public boolean segnaEmailComeLetta(String emailClient, Email emailLetta) throws RemoteException {
        casella.setLetta(emailClient, emailLetta);
        return true;
    }
    
    @Override
    public boolean eliminaEmailPerMittente(Email emailDaEliminare, Utente utente) throws RemoteException {
        boolean eliminata = casella.eliminaEmailDaMittente(emailDaEliminare, utente.getEmail());
        if(eliminata){
            rHM.lock();
            try {
                clientConnessi.get(utente.getEmail()).riceviMessaggio("Email eliminata con successo!");
            } finally {
                rHM.unlock();
            }
        }
        return eliminata;
    }
    
    @Override
    public boolean eliminaEmailPerDestinatario(Email emailDaEliminare, Utente utente) throws RemoteException {
        boolean eliminata = casella.eliminaEmailDaDestinatario(emailDaEliminare, utente.getEmail());
        if(eliminata){
            rHM.lock();
            try {
                clientConnessi.get(utente.getEmail()).riceviMessaggio("Email eliminata con successo!");
            } finally {
                rHM.unlock();
            }
        }
        return eliminata;
    }
    
}
