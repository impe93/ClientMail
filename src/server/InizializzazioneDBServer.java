package server;

import client.InizializzazioneDBClient;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
        String urlDB = "jdbc:sqlite:Server.db";
       
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
        String urlDB = "jdbc:sqlite:Server.db";
       
        String inserimentoEmail 
                = "INSERT INTO email (id_email, mittente, destinatario, oggetto, corpo,"
                + "data, priorita,letto) "
                + "VALUES "
                + "(1,'alessio.berger@edu.unito.it','francesca.riddone@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-11-02',3,0),"
                + ""
                + "(2,'alessio.berger@edu.unito.it','lorenzo.imperatrice@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-12-02',3,0),"
                +""
                + "(3,'alessio.berger@edu.unito.it','lorenzo.imperatrice@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-09-02',3,0),"
                +""
                + "(4,'lorenzo.imperatrice@edu.unito.it','alessio.berger@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-09-09',3,0),"
                +""
                + "(5,'lorenzo.imperatrice@edu.unito.it','francesca.riddone@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-05-09',3,0),"
                +""
                + "(6,'lorenzo.imperatrice@edu.unito.it','francesca.riddone@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-09-08',3,0),"
                +""
                + "(7,'francesca.riddone@edu.unito.it','alessio.berger@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-04-07',3,0),"
                +""
                + "(8,'francesca.riddone@edu.unito.it','alessio.berger@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-04-06',3,0),"
                +""
                + "(9,'francesca.riddone@edu.unito.it','lorenzo.imperatrice@edu.unito.it','Lorem ipsum',"
                + "'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in nulla tincidunt,"
                + " rutrum mauris sodales, luctus arcu. Sed felis leo, imperdiet ac finibus nec, "
                + "laoreet vitae urna.','2017-07-19',3,0);";
        
        Connection conn = null;
        Statement st = null;

        try{
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(inserimentoEmail);
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

}
