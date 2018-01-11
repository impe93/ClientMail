package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import modelli.Email;
import modelli.EmailDaInviare;
import modelli.Utente;
import java.util.Observable;
import client.Client;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaServer extends Observable {
    private final String urlDB;
    private String operazioneEseguita;
    private final ReadWriteLock rwDB1;
    private final Lock rDB1;
    private final Lock wDB1;
    
    public String getOperazioneEseguita() {
        return operazioneEseguita;
    }
    
    public void setOperazioneEseguita(String operazioneEseguita) {
        this.operazioneEseguita = operazioneEseguita;
    }
    
    public void logUltimaOperazione(){
        setChanged();
        notifyObservers(getOperazioneEseguita());
    }
    /*
    *   Costruttore CasellaServer
    */
    public CasellaServer(){
        this.urlDB = "jdbc:sqlite:Server.db";
        rwDB1 = new ReentrantReadWriteLock();
        rDB1 = rwDB1.readLock();
        wDB1 = rwDB1.writeLock();
        
    }
    
    /*
    *   Recupera le Email ricevute di un utente a partire dall'Email con id ultimaRicevuta
    *   che viene passato come parametro, e le resttuisce in un ArrayList
    */
    
    public ArrayList<Email> recuperaEmailRicevuteUtente(int ultimaRicevuta, Utente utente){
        ArrayList<Email> emailRicevuteUtente = new ArrayList<>();
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String cercaEmailRicevuteUtente =
                "SELECT * " +
                "FROM email " +
                "WHERE destinatario = '" + utente.getEmail()
                + "' AND id_email >" + ultimaRicevuta +" AND eliminataDaDestinatario=0";
        
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(cercaEmailRicevuteUtente);
            while(rs.next()){
                Email email = new Email();
                
                email.setId(rs.getInt("id_email"));
                email.setMittente(recuperaDatiUtente(rs.getString("mittente")));
                email.setDestinatari(recuperaUtentiDestinatari(rs.getInt("id_email")));
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getInt("letto"));
                emailRicevuteUtente.add(email);
                
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            
            rDB1.unlock();
        }
        
        return emailRicevuteUtente;
        
    }
    
    /*
    *   Recupera le Email inviatee di un utente a partire dall'Email con id ultimaInviata
    *   che viene passato come parametro, e le resttuisce in un ArrayList
    */
    public ArrayList<Email> recuperaEmailInviateUtente(int ultimaInviata, Utente utente){
        ArrayList<Email> emailInviateUtente = new ArrayList<>();
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String cercaEmailInviateUtente =
                "SELECT * " +
                "FROM email " +
                "WHERE mittente = '" + utente.getEmail()
                + "' AND id_email >" + ultimaInviata +" AND eliminataDaMittente=0";
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(cercaEmailInviateUtente);
            while(rs.next()){
                Email email = new Email();
                
                email.setId(rs.getInt("id_email"));
                email.setMittente(recuperaDatiUtente(rs.getString("mittente")));
                email.setDestinatari(recuperaUtentiDestinatari(rs.getInt("id_email")));
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getInt("letto"));
                emailInviateUtente.add(email);
                
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        
        return emailInviateUtente;
    }
    
    /*
    *   A partire dall'email utente accede al database utenti e recupera
    *   tutti i dati del relativo utente: nome, cognome e email e restituisce un istanza
    *   dell'utente.
    */
    public Utente recuperaDatiUtente(String emailUtente){
        Utente utente = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtente =
                "SELECT * " +
                "FROM utenti " +
                "WHERE email = '" + emailUtente + "'";
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryUtente);
            if(rs.next()){
                utente = new Utente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                if(conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        return utente;
    }
    
    /*
    *   A partire dall'idEmail passato come parametro, accede al database e recupera tutti gli
    *   utenti destinatari dell'email in questione e li restituisce sottoforma di ArrayList
    */
    private ArrayList<Utente> recuperaUtentiDestinatari(int idEmail){
        ArrayList<Utente> utentiDestinatari = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtentiDestinatari =
                "SELECT * " +
                "FROM utenti " +
                "WHERE email IN (SELECT destinatario FROM email "
                + "WHERE id_email= " + idEmail+ ")";
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryUtentiDestinatari);
            while(rs.next()){
                Utente utente = new Utente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"));
                utentiDestinatari.add(utente);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        return utentiDestinatari;
    }
    
    /*
    *   Accede al database e recupera l'idEmail più alto e lo restituisce
    */
    private int recuperaIdMax() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idMax = -1;
        
        String queryIdMax =
                "SELECT MAX(id_email) FROM email";
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            ps = conn.prepareStatement(queryIdMax);
            rs = ps.executeQuery();
            if(rs.next())
                idMax = rs.getInt("MAX(id_email)");
            else
                idMax=0;
            
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        
        return idMax;
        
    }
    
    /*
    *   A partire da un istanza di EmailDaInviare crea un'istanza di tipo Email che viene
    *   inserita all'interno del database e poi la restituisce al chiamante
    */
    public Email inviaEmail(EmailDaInviare emailDaInviare, Map<String, Client> clientConnessi) throws RemoteException{
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Utente> destinatari = new ArrayList<>();
        Date data;
        data = emailDaInviare.getData();
        String emailDestinatario;
        int nuovoId = recuperaIdMax()+1;
        Email email = null;
        
        setOperazioneEseguita("* [RICEVUTA RICHIESTA DI INVIO EMAIL DA " + emailDaInviare.getMittente().getEmail() + ""
                + " A " + emailDaInviare.getDestinatari().toString() + " - " +
                new Date().toString() + "]");
        logUltimaOperazione();
        
        if(nuovoId == 0){
            System.err.println("Errore nel recuperare l ID massimo");
            return null;
        }
        ArrayList<String> utentiAccreditati = recuperaEmailUtenti();
        
        wDB1.lock();
        try{
            for(int i = 0; i<emailDaInviare.getDestinatari().size();i++){
                emailDestinatario = emailDaInviare.getDestinatari().get(i);
                if(utentiAccreditati.contains(emailDestinatario)){
                    Utente destinatario;
                    destinatario = recuperaDatiUtente(emailDestinatario);
                    destinatari.add(destinatario);
                    String inserisciEmail = "INSERT INTO email (id_email, mittente, "
                            + "destinatario, oggetto, corpo,"
                            + "data, priorita,letto,eliminataDaMittente,"
                            + "eliminataDaDestinatario) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?)";
                    conn = DriverManager.getConnection(urlDB);
                    pst = conn.prepareStatement(inserisciEmail);
                    
                    pst.setInt(1,nuovoId);
                    pst.setString(2, emailDaInviare.getMittente().getEmail());
                    pst.setString(3,emailDestinatario);
                    pst.setString(4,emailDaInviare.getOggetto());
                    pst.setString(5,emailDaInviare.getCorpo());
                    java.sql.Date dataSql = new java.sql.Date(data.getTime());
                    pst.setDate(6, dataSql);
                    pst.setInt(7,emailDaInviare.getPriorita());
                    pst.setInt(8,0);
                    pst.setInt(9,0);
                    pst.setInt(10,0);
                    pst.executeUpdate();
                    
                    setOperazioneEseguita("* [INVIATA EMAIL A " + emailDestinatario + ""
                            + " DA " + emailDaInviare.getMittente().getEmail() + " - " +
                            new Date().toString() + "]");
                    logUltimaOperazione();
                    email = new Email(nuovoId, emailDaInviare.getMittente(), destinatari, emailDaInviare.getOggetto(), emailDaInviare.getCorpo(), data, emailDaInviare.getPriorita(), 0);
                }
                else{
                    setOperazioneEseguita("* [INVIO EMAIL A " + emailDestinatario
                            + " DA " + emailDaInviare.getMittente().getEmail() + " NON ESEGUITO,"
                            + " DESTINATARIO INESISTENTE - " + new Date().toString() + "]");
                    logUltimaOperazione();
                }
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(pst != null){
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            wDB1.unlock();
        }
        
        ArrayList<String> destinatariControllo = new ArrayList<>();
            destinatariControllo.addAll(emailDaInviare.getDestinatari());
            ArrayList<String> destinatariInesistenti = new ArrayList<>();
            for(String destinatario : destinatariControllo){
                if(recuperaDatiUtente(destinatario)==null)
                    destinatariInesistenti.add(destinatario);
            }
            if(destinatariInesistenti.isEmpty()==false){
                String messaggio = "Non è stato possibile inviare l'email "
                        + "ai seguenti destinatari:\n";
                for(String utente : destinatariInesistenti){
                    messaggio = messaggio + utente + "\n";
                }
                
                    try{
                        clientConnessi.get(emailDaInviare.getMittente().getEmail()).riceviMessaggio(messaggio);
                    }
                    catch(RemoteException e){
                        System.out.println(e.getMessage());
                    }
               
            }
        
        return email;
    }
    
    public boolean setLetta(String emailClient, Email emailLetta){
        Connection conn = null;
        Statement st = null;
        
        String querySetLetta =
                "UPDATE email "
                + "SET letto=1 "
                + "WHERE id_email=" + emailLetta.getId() + " AND destinatario = '" + emailClient +"';";
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(querySetLetta);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            try {
                if(st != null){
                    st.close();
                }
                if(conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        
        return true;
        
    }
    
    public boolean eliminaEmailDaMittente(Email email, String clientRichiedente){
        Connection conn = null;
        Statement st = null;
        String queryEliminaEmail;
        
        //System.out.println("eliminata da mittente");
        
        queryEliminaEmail =
                "UPDATE email "
                + "SET eliminataDaMittente=1 "
                + "WHERE id_email= " + email.getId() + " AND mittente = '"
                + clientRichiedente +"';";
        
        setOperazioneEseguita("* [ELIMINATA EMAIL DA " + clientRichiedente +
                " IN CASELLA INVIATE - " + new Date().toString() + "]");
        logUltimaOperazione();
        
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(queryEliminaEmail);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            try {
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        return true;
        
    }
    
    public boolean eliminaEmailDaDestinatario(Email email, String clientRichiedente){
        Connection conn = null;
        Statement st = null;
        String queryEliminaEmail;
        
        //System.out.println("eliminata da destinatario");
        queryEliminaEmail =
                "UPDATE email "
                + "SET eliminataDaDestinatario=1 "
                + "WHERE id_email= " + email.getId() + " AND destinatario = '" +
                clientRichiedente +"';";
        
        setOperazioneEseguita("* [ELIMINATA EMAIL DA " + clientRichiedente + ""
                + " IN CASELLA RICEVUTE - " + new Date().toString() + "]");
        logUltimaOperazione();
        
        
        
        rDB1.lock();
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(queryEliminaEmail);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            try {
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        return true;
        
    }
    
    private ArrayList<String> recuperaEmailUtenti(){
        ArrayList<String> utenti = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtenti =
                "SELECT email " +
                "FROM utenti";
        rDB1.lock();
        try{
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryUtenti);
            while(rs.next()){
                utenti.add(rs.getString("email"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            rDB1.unlock();
        }
        return utenti;
    }
}
