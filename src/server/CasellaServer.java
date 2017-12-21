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

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaServer extends Observable {
    private final String urlDB;
    private String operazioneEseguita;

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
    }
   
    /*
    *   Recupera le Email ricevute di un utente a partire dall'Email con id ultimaRicevuta
    *   che viene passato come parametro, e le resttuisce in un ArrayList
    */
    
    public ArrayList<Email> recuperaEmailRicevuteUtente(int ultimaRicevuta, Utente utente)
   {
        ArrayList<Email> emailRicevuteUtente = new ArrayList<>();
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String cercaEmailRicevuteUtente =
                "SELECT * " + 
                "FROM email " +
                "WHERE destinatario = '" + utente.getEmail() 
                + "' AND id_email >" + ultimaRicevuta ;
        
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
                + "' AND id_email >" + ultimaInviata;
        
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
    *   Accede al database e recupera l'idEmail più alto e lo restituisce
    */
    private int recuperaIdMax() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idMax = -1;
        
        String queryIdMax = 
                    "SELECT MAX(id_email) FROM email";
         try {
            conn = DriverManager.getConnection(urlDB);
            ps = conn.prepareStatement(queryIdMax);
            rs = ps.executeQuery();
            if(rs.next())
                idMax = rs.getInt("MAX(id_email)");
            else
                idMax=0;
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
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
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            
         }
            
            return idMax;
        
    }
    
    /*
    *   A partire da un istanza di EmailDaInviare crea un'istanza di tipo Email che viene
    *   inserita all'interno del database e poi la restituisce al chiamante
    */
    public Email inviaEmail(EmailDaInviare emailDaInviare){   
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Utente> destinatari = new ArrayList<>();
        Date data = new Date();
        data = emailDaInviare.getData();
        java.sql.Date dataSql = new java.sql.Date(data.getTime());
        String emailDestinatario;
        int nuovoId = recuperaIdMax()+1;
        
        setOperazioneEseguita("* [RICEVUTA RICHIESTA DI INVIO EMAIL DA " + emailDaInviare.getMittente().getEmail() + ""
                        + " A " + emailDaInviare.getDestinatari().toString() + " - " + 
                        new Date().toString() + "]");
        logUltimaOperazione();
        
        if(nuovoId == 0){
            System.err.println("Errore nel recuperare l ID massimo");
            return null;
        }
        try {   
            for(int i = 0; i<emailDaInviare.getDestinatari().size();i++){
                emailDestinatario = emailDaInviare.getDestinatari().get(i);
                Utente destinatario = new Utente();
                destinatario = recuperaDatiUtente(emailDestinatario);
                destinatari.add(destinatario);
                
                String inserisciEmail =
                "INSERT INTO email (id_email,mittente,destinatario,oggetto,"
                + "corpo,data,priorita,letto)"
                + "VALUES"
                + "(" + nuovoId + ",'" + emailDaInviare.getMittente().getEmail()
                + "','" + emailDestinatario+ "','" + emailDaInviare.getOggetto() + "','"
                + emailDaInviare.getCorpo() + "','" + dataSql +"'," + emailDaInviare.getPriorita() + ","
                + "0);";
        
                conn = DriverManager.getConnection(urlDB);
                st = conn.createStatement();
                st.executeUpdate(inserisciEmail);
                
                setOperazioneEseguita("* [INVIATA EMAIL A " + emailDestinatario + ""
                        + " DA " + emailDaInviare.getMittente().getEmail() + " - " + 
                        new Date().toString() + "]");
                logUltimaOperazione();
            }
        } catch(SQLException e) {
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
        }
        
        Email email = new Email(nuovoId, emailDaInviare.getMittente(), 
                destinatari,emailDaInviare.getOggetto(),emailDaInviare.getCorpo());
        
        return email;
    }

}
