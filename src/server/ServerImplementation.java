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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerImplementation extends UnicastRemoteObject implements Server{
    
    
    private Map<String, Client> clientConnessi = new HashMap<>();
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
    
    }
    
    public void aggiungiObserver(ServerGUI serverGui){
        this.casella.addObserver(serverGui);
    }

    @Override
    public ArrayList<Email> getInviate(int ultimaInviata, Utente utente) throws RemoteException {
       FutureTask<ArrayList<Email>> ft = new FutureTask<>(() -> casella.recuperaEmailInviateUtente(ultimaInviata, utente));
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
         FutureTask<ArrayList<Email>> ft = new FutureTask<>(() -> casella.recuperaEmailRicevuteUtente(ultimaRicevuta, utente));
        exec.execute(ft);
        ArrayList<Email> ricevute = null;
        try{
            ricevute = ft.get();
        }catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        
        return ricevute;
    }

    @Override
    public boolean eliminaEmail(Email emailDaEliminare, Utente utente) throws RemoteException {
    return casella.eliminaEmail(emailDaEliminare, utente.getEmail());
    }
    
    /*il metodo riceviEmail sul client viene chiamato all'interno del metodo inviaEmail di CasellaServer*/
    @Override
    public Email inviaEmail(EmailDaInviare emailDaInviare) throws RemoteException {
        FutureTask<Email> ft = new FutureTask<>(() -> casella.inviaEmail(emailDaInviare, clientConnessi));
        Email emailRitorno = null;
        
        exec.execute(ft);
        try{
            emailRitorno = ft.get();
        }
        catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        if(emailRitorno != null){
            ArrayList<Utente> destinatari = new ArrayList<>();
            destinatari.addAll(emailRitorno.getDestinatari());
            for(Utente destinatario : destinatari){
                Client clientRicevente = clientConnessi.get(destinatario.getEmail());
                if(clientRicevente != null){
                    try{
                        clientRicevente.riceviEmail(emailRitorno);
                    }
                    catch(RemoteException e){
                        System.out.println(e.getMessage());
                    }
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
        try {
            clientConnessi.put(emailClient, (Client)Naming.lookup("//localhost/Client/" + emailClient));
            
            casella.setOperazioneEseguita("* [NUOVO CLIENT CONNESSO: " + emailClient + " - " + 
                        new Date().toString() + "]");
            casella.logUltimaOperazione();
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    @Override
    public boolean disconnettiClient(String emailClient) throws RemoteException {
        clientConnessi.remove(emailClient);
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
    
}
