package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaServer {
    private final String urlDB;
    
    public CasellaServer(){
        this.urlDB = "jdbc:sqlite:Server.db";
    }
    
    public ArrayList<Email> recuperaEmailRicevuteUtente(int ultimaRicevuta, Utente utente)
   {
        ArrayList<Email> emailRicevuteUtente = new ArrayList<Email>();
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String cercaEmailRicevuteUtente =
                "SELECT * " + 
                "FROM email " +
                "WHERE destinatario = '" + utente.getEmail() 
                + "' AND id_email >" + ultimaRicevuta;
        
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
                email.setData(new java.sql.Date(rs.getDate("data").getTime()));
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
    
    public ArrayList<Email> recuperaEmailInviateUtente(int ultimaInviata, Utente utente){
        ArrayList<Email> emailInviateUtente = new ArrayList<Email>();
        
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
                email.setData(new java.sql.Date(rs.getDate("data").getTime()));
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
    
    private synchronized ArrayList<Utente> recuperaUtentiDestinatari(int idEmail){
        ArrayList<Utente> utentiDestinatari = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtentiDestinatari = 
                    "SELECT * " + 
                    "FROM utenti " +
                    "WHERE email = '(SELECT destinatario FROM email "
                + "WHERE id_email= " + idEmail+ ")'";
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
    
    public boolean inviaEmail(Email emailDaInviare){
        
        /*FUNZIONA MA METTE LA DATA NON IN MILLISECONDI ---> CORREGGERE*/
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int[] val = new int[emailDaInviare.getDestinatari().size()];
      
        Utente destinatario;
        try {
            
        for(int i = 0; i<emailDaInviare.getDestinatari().size();i++){
            destinatario = emailDaInviare.getDestinatari().get(i);
        
        String inserisciEmail =
                "INSERT INTO email (id_email,mittente,destinatario,oggetto,"
                + "corpo,data,priorita,letto)"
                + "VALUES"
                + "(" + emailDaInviare.getId() + ",'" + emailDaInviare.getMittente().getEmail()
                + "','" + destinatario.getEmail()+ "','" + emailDaInviare.getOggetto() + "','"
                + emailDaInviare.getCorpo() + "','" + emailDaInviare.getData() +"'," + emailDaInviare.getPriorita() + ","
                + emailDaInviare.getLetto() +");";
        
        
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            val[i] = st.executeUpdate(inserisciEmail);
        
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
        
        boolean eseguitaCorrettamente = true;
        int i = 0;
        while(i<val.length && eseguitaCorrettamente==true){
            if(val[i]<0)
                eseguitaCorrettamente = false;
        }
            
        return eseguitaCorrettamente;
    }
}
