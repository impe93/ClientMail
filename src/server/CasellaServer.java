package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import modelli.Email;
import modelli.EmailDaInviare;
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
    
    
    //OK
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
    
    
    //OK
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
    
    
    //OK
    private synchronized ArrayList<Utente> recuperaUtentiDestinatari(int idEmail){
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
    
    private int recuperaIdMax() {
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int idMax = -1;
        
        String queryIdMax = 
                    "SELECT MAX(id_email) FROM email)";
         try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryIdMax);
            idMax = rs.getInt("id_email");
            
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
            
            return idMax;
        
    }
    
    
    //OK
    public Email inviaEmail(EmailDaInviare emailDaInviare){
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Utente> destinatari = new ArrayList<>();
        
      
        String emailDestinatario;
       
        int nuovoId = recuperaIdMax()+1;
        
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
                + emailDaInviare.getCorpo() + "','" + emailDaInviare.getData() +"'," + emailDaInviare.getPriorita() + ","
                + "0);";
        
        
                conn = DriverManager.getConnection(urlDB);
                st = conn.createStatement();
                st.executeUpdate(inserisciEmail);
        
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
