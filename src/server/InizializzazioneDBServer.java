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
    
    private static final String[] nomiDB = {"DB_alessio.berger@edu.unito.it", "DB_lorenzo.imperatrice@edu.unito.it", "DB_francesca.riddone@edu.unito.it"};
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
            creaTabellaUtente(urlDB);
            creaTabellaEmail(urlDB);
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
    
    private static void creaTabellaUtente(String nomeDB) throws SQLException{
        String query 
                = "create table utente ("
                + "email text primary key,"
                + "nome text not null,"
                + "cognome text not null"
                + ");";
        
        queryDB(urlDB, query);
    }
    
    private static void creaTabellaEmail(String nomeDB) throws SQLException{
        String query
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
        queryDB(urlDB, query);
        
    }
    
    private static void queryDB(String urlDB, String query){
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(query);
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
    
}
