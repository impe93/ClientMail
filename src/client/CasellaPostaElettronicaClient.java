package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaPostaElettronicaClient {
    private final String urlDB;
    private final Utente utenteProprietario;
    private ArrayList<Email> emailRicevute;
    private ArrayList<Email> emailInviate;
    
    public CasellaPostaElettronicaClient(String emailUtente){
        registraDriver();
        this.urlDB = "jdbc:sqlite:" + "DB_" + emailUtente + ".db";   
        this.utenteProprietario = recuperaDatiUtente(emailUtente);
        recuperaEmailRicevute();
        recuperaEmailInviate();
    }

    public Utente getUtenteProprietario() {
        return utenteProprietario;
    }

    public ArrayList<Email> getEmailRicevute() {
        return emailRicevute;
    }

    public ArrayList<Email> getEmailInviate() {
        return emailInviate;
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email ricevuta dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaRicevuta(){
        return getUltimaEmail("email_ricevute");
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email inviata dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaInviata(){
        return getUltimaEmail("email_inviate");
    }
    
    /**
     * A seconda del parametro tabellaEmail effettua una ricerca nella table
     * email_ricevute oppure nella table email_inviate e restituisce l'id
     * della email più recente contenuta nella tabella
     * @param tabellaEmail: stringa corrispondente al nome della tabella del DB 
     *      dove effettuare la ricerca
     * @return intero corrispondente all'id dell'email più recente in 
     *      tabellaEmail
     */
    private int getUltimaEmail(String tabellaEmail){
        int idUltimaEmail = -1;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String trovaIdUltimaRicevuta =
                "SELECT id_email " + 
                "FROM " + tabellaEmail + " " +
                "WHERE data = " +
                "(SELECT MAX(data) FROM " + tabellaEmail + ")";
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(trovaIdUltimaRicevuta);
            if(rs.next()){
                idUltimaEmail = rs.getInt("id_email");
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
        }
        return idUltimaEmail;
    }
    
    /**
     * Registrazione del driver
     */
    private void registraDriver(){
        try {
            String SDriverName = "org.sqlite.JDBC";
            Class.forName(SDriverName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InizializzazioneDBClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Recupera i dati personali di un utente e li restituisce all'interno di 
     * un oggetto Utente
     * @param emailUtente: stringa email dell'utente che stiamo cercando
     * @return oggetto utente contenente i dati dell'utente se esiste, null 
     * altrimenti 
     */
    public Utente recuperaDatiUtente(String emailUtente){
        Utente utente = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtente = 
                "SELECT * " + 
                "FROM utente " +
                "WHERE email = '" + emailUtente + "'";
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryUtente);
            if(rs.next()){
                utente = new Utente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"));
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
        }
        return utente;
    }
    
    /**
     * Recupera tutte le email ricevute dall'utente proprietario della casella
     * di posta elettronica
     */
    private synchronized void recuperaEmailRicevute(){
        this.emailRicevute = new ArrayList<>();
        String queryEmailRicevute = 
                "SELECT * " + 
                "FROM email_ricevute " +
                "WHERE destinatario = '" + this.utenteProprietario.getEmail() + "'";
        recuperaEmailUtente(queryEmailRicevute, false);
    }
    
    /**
     * Recupera tutte le email inviate dall'utente proprietario della casella
     * di posta elettronica
     */
    private synchronized void recuperaEmailInviate(){
        this.emailInviate = new ArrayList<>();
        String queryEmailRicevute = 
                "SELECT * " + 
                "FROM email_inviate " +
                "WHERE mittente = '" + this.utenteProprietario.getEmail() + "'";
        recuperaEmailUtente(queryEmailRicevute, true);
    }
    
    /**
     * Recupera le email contenute nella tabella email_ricevute oppure nella 
     * tabella email_inviate, a seconda della query e del valore di isInviate
     * @param query: query per ricerca email in DB da eseguire
     * @param isInviate: boleano con valore true se la tabella in cui recuperare
     * le email è email_inviate, false se la tabella è email_ricevute
     */
    private void recuperaEmailUtente(String query, boolean isInviate){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                Email email = new Email();
                email.setId(rs.getInt("id_email"));
                email.setMittente(recuperaDatiUtente(rs.getString("mittente")));
                email.setDestinatari(recuperaUtentiDestinatari(rs.getInt("id_email"), isInviate));
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getBoolean("letto"));
                if(isInviate){
                    if(!this.emailInviate.contains(email)){
                        this.emailInviate.add(email);
                    }
                } else{
                    this.emailRicevute.add(email);
                }
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
        }
    }
    
    /**
     * Recupera tutti i destinatari di una email dato il suo id (idEmail) nel
     * caso in cui siano presenti più destinatari
     * @param idEmail: intero che identifica una email
     * @param inInviate: boleano che indica se effettuare la ricerca dei 
     * destinatari in table email_inviate (true) oppure in table email_ricevute
     * (false)
     * @return: un ArrayList di Utente contenente tutti gli utenti destinatari 
     * dell'email
     */
    private synchronized ArrayList<Utente> recuperaUtentiDestinatari(int idEmail, boolean inInviate){
        ArrayList<Utente> utentiDestinatari = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtentiDestinatari = "";
        if(inInviate){
            queryUtentiDestinatari =
                    "SELECT * " + 
                    "FROM utente " +
                    "WHERE email = '(SELECT destinatario FROM email_inviate WHERE id_email= " + idEmail+ ")'";
        } else{
            queryUtentiDestinatari =
                    "SELECT * " + 
                    "FROM utente " +
                    "WHERE email = '(SELECT destinatario FROM email_ricevute WHERE id_email= " + idEmail+ ")'";
        }
         try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryUtentiDestinatari);
            while(rs.next()){
                Utente utente = new Utente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"));
                utentiDestinatari.add(utente);
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
        }
        return utentiDestinatari;        
    }
    
    /*
    inserisciInInviate(Email email)
    inserisciInRicevute(Email email)
    elimina(Email email)
    */
}