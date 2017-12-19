package server;

import client.InizializzazioneDBClient;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class InizializzazioneDBServer {
    
    private static final String urlDB = "jdbc:sqlite:Server.db";
    
    public static void main(String[] args){
        try {
            String SDriverName = "org.sqlite.JDBC";
            Class.forName(SDriverName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InizializzazioneDBClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            creaDBServer();
            creaTabellaUtenti(urlDB);
            creaTabellaEmail(urlDB);
            inserisciUtentiInTabellaUtenti();
            inserisciEmailInTabellaEmail();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void creaDBServer() throws SQLException {
            try (Connection conn = DriverManager.getConnection(urlDB)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }  
    }
    
    private static void creaTabellaUtenti(String nomeDB) throws SQLException{
        String creazioneTabellaUtenti 
                = "create table utenti ("
                + "email text primary key,"
                + "nome text not null,"
                + "cognome text not null"
                + ");";
        String eliminazioneTabellaUtenti
                = "DROP TABLE if exists 'utenti'";
        
        eseguiQueryDB(urlDB, creazioneTabellaUtenti, eliminazioneTabellaUtenti);
    }
    
    private static void creaTabellaEmail(String nomeDB) throws SQLException{
        String creaTabellaEmail
                = "create table email ("
                + "id_email integer not null,"
                + "mittente text not null references UTENTE(email),"
                + "destinatario text not null references UTENTE(email),"
                + "oggetto text,"
                + "corpo text,"
                + "data date not null,"
                + "priorita integer not null check(priorita >= 1 and priorita <= 10),"
                + "letto integer not null check(letto = 0 or letto = 1),"
                + "primary key (id_email, destinatario)"
                + ")";
        String eliminaTabellaEmail 
                = "DROP TABLE if exists 'email'";
        eseguiQueryDB(urlDB, creaTabellaEmail, eliminaTabellaEmail);
        
    }
    
    private static void eseguiQueryDB(String urlDB, String eliminazioneTabella, String creazioneTabella){
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(creazioneTabella);
            st.executeUpdate(eliminazioneTabella);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
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
    
    private static void inserisciUtentiInTabellaUtenti() throws SQLException{
       
        String inserimentoUtenti 
                = "INSERT INTO utenti (email, nome, cognome)"
                + "VALUES"
                + "('francesca.riddone@edu.unito.it','Francesca','Riddone'),"
                + "('lorenzo.imperatrice@edu.unito.it','Lorenzo','Imperatrice'),"
                + "('alessio.berger@edu.unito.it','Alessio','Berger');";
        
        Connection conn = null;
        Statement st = null;
        
        try{
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(inserimentoUtenti);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
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
    
    private static void inserisciEmailInTabellaEmail() throws SQLException{
        
        String[] mittente = {
            "alessio.berger@edu.unito.it",
            "alessio.berger@edu.unito.it",
            "alessio.berger@edu.unito.it",
            "francesca.riddone@edu.unito.it",
            "francesca.riddone@edu.unito.it",
            "francesca.riddone@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it"
        };
        String[] destinatario = {
            "francesca.riddone@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it",
            "alessio.berger@edu.unito.it",
            "alessio.berger@edu.unito.it",
            "lorenzo.imperatrice@edu.unito.it",
            "francesca.riddone@edu.unito.it",
            "francesca.riddone@edu.unito.it",
            "alessio.berger@edu.unito.it"
        };
        String oggetto="Lorem ipsum";
        String corpo="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.";
        
        
        int priorita = 3;
        int letto = 0;
       
        String inserimentoEmail = "INSERT INTO email (id_email, mittente, destinatario, oggetto, corpo,"
                + "data, priorita,letto) VALUES (?,?,?,?,?,?,?,?)";
        
        Connection conn = null;
        PreparedStatement pst = null;

        try{
            conn = DriverManager.getConnection(urlDB);
            for(int i=0;i<9;i++){
                pst = conn.prepareStatement(inserimentoEmail);
                pst.setInt(1,i+1);
                pst.setString(2,mittente[i]);
                pst.setString(3,destinatario[i]);
                pst.setString(4,oggetto);
                pst.setString(5,corpo);
                Date data = new Date();
                java.sql.Date dataSql = new java.sql.Date(data.getTime());
                pst.setDate(6, dataSql);
                pst.setInt(7,priorita);
                pst.setInt(8,letto);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(pst != null){
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }   
    
    }

}
