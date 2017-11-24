package client;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class InizializzazioneDBClient {
    
    private static final String[] nomiDB = {"DB_alessio.berger@edu.unito.it", "DB_lorenzo.imperatrice@edu.unito.it", "DB_francesca.riddone@edu.unito.it"};
    private static final String url = "jdbc:sqlite:";
    
    public static void main(String[] args) {
        try {
            String SDriverName = "org.sqlite.JDBC";
            Class.forName(SDriverName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InizializzazioneDBClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            creaDBClient();
            for(int i = 0; i <= 2; i++){
                String nomeDB = nomiDB[i];
                creaTabellaUtente(nomeDB);
                creaTabellaEmailInviate(nomeDB);
                creaTabellaEmailRicevute(nomeDB);
                inserisciUtenteInTabellaUtente(nomeDB);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void creaDBClient() throws SQLException {
        for(int i = 0; i <= 2; i++){
            String urlDB = url + nomiDB[i] + ".db";
            
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
    }
    
    private static void creaTabellaUtente(String nomeDB) throws SQLException{
        String urlDB = url + nomeDB + ".db";
        String creazioneTabella 
                = "create table utente ("
                + "email text primary key,"
                + "nome text not null,"
                + "cognome text not null"
                + ");";
        String eliminazioneTabella = "DROP TABLE if exists 'utente'";
        eseguiQueryDB(urlDB, eliminazioneTabella, creazioneTabella);
    }
    
    private static void creaTabellaEmailInviate(String nomeDB) throws SQLException{
        String urlDB = url + nomeDB + ".db";
        String creazioneTabella
                = "create table email_inviate ("
                + "id_email integer not null,"
                + "mittente text not null references UTENTE(email),"
                + "destinatario text not null,"
                + "oggetto text,"
                + "corpo text,"
                + "data date not null,"
                + "priorita integer not null check(priorita >= 1 and priorita <= 10),"
                + "letto integer not null check(letto = 0 or letto = 1),"
                + "primary key (id_email, destinatario)"
                + ")";
        String eliminazioneTabella = "DROP TABLE if exists 'email_inviate'";
        eseguiQueryDB(urlDB, eliminazioneTabella, creazioneTabella);
    }
    
    private static void creaTabellaEmailRicevute(String nomeDB) throws SQLException{
        String urlDB = url + nomeDB + ".db";
        String creazioneTabella
                = "create table email_ricevute ("
                + "id_email integer not null,"
                + "mittente text not null,"
                + "destinatario text not null references UTENTE(email),"
                + "oggetto text,"
                + "corpo text,"
                + "data date not null,"
                + "priorita integer not null check(priorita >= 1 and priorita <= 10),"
                + "letto integer not null check(letto = 0 or letto = 1),"
                + "primary key (id_email, destinatario)"
                + ")";
        String eliminazioneTabella = "DROP TABLE if exists 'email_ricevute'";
        eseguiQueryDB(urlDB, eliminazioneTabella, creazioneTabella);
    }
    
    private static void inserisciUtenteInTabellaUtente(String nomeDB) throws SQLException{
        String urlDB = url + nomeDB + ".db";
        String emailUtente = nomeDB.replace("DB_","");
        String nomeCognome = emailUtente.replace("@edu.unito.it", "");
        String nome = nomeCognome.substring(0, nomeCognome.indexOf("."));
        String cognome = nomeCognome.substring(nomeCognome.indexOf(".")+1);
        
        String inserimentoUtente = "INSERT INTO utente (email, nome, cognome) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(urlDB);
            pst = conn.prepareStatement(inserimentoUtente);
            pst.setString(1, emailUtente);
            pst.setString(2, nome);
            pst.setString(3, cognome);
            pst.executeUpdate();
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
    
    private static void eseguiQueryDB(String urlDB, String eliminazioneTabella, String creazioneTabella){
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(eliminazioneTabella);
            st.executeUpdate(creazioneTabella);
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
