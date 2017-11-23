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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import modelli.Email;
import modelli.Utente;
import server.Server;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ClientImplementation extends UnicastRemoteObject implements Client{
    
    private ArrayList<Email> emailRicevute;
    private ArrayList<Email> emailInviate;
    private Utente utente;
    private Server server;
    private ClientModel modelloClient;
    
    public ClientImplementation(String emailUtente) throws RemoteException{
        /* 
        registrazione dell'oggetto ClientImplementation presso il registro di 
        bootstrap (rmiregistry)
        */
        lanciaRMIRegistry();
        try {
            Naming.rebind("//localhost/Client"+emailUtente, this);
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
        } catch (NotBoundException | MalformedURLException ex) {
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
    
    @Override
    public boolean riceviEmail(Email emailRicevuta) throws RemoteException{
        System.out.println("RMI FUNZIONA!!!!!");
        return true;
    }
    
    
    
}
