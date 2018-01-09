package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelli.CasellaPostaElettronica;
import modelli.Email;
import modelli.Utente;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class CasellaPostaElettronicaClient extends Observable implements CasellaPostaElettronica{
    
    private final String urlDB;
    private final Utente utenteProprietario;
    private ArrayList<Email> emailInviate;
    private ArrayList<Email> emailRicevute;
    private ArrayList<String> messaggi;
    
    public CasellaPostaElettronicaClient(String emailUtente){
        registraDriver();
        this.urlDB = "jdbc:sqlite:" + "DB_" + emailUtente + ".db";   
        this.utenteProprietario = recuperaDatiUtente(emailUtente);
        this.emailInviate = new ArrayList<>();
        this.emailRicevute = new ArrayList<>();
        this.messaggi = new ArrayList<>();
    }

    public Utente getUtenteProprietario() {
        return utenteProprietario;
    }

    public ArrayList<Email> getEmailInviate() {
        return emailInviate;
    }
    
    public ArrayList<Email> getEmailRicevute() {
        return emailRicevute;
    }
    
    /**
     * Mostra all'utente le sue email inviate
     */
    public void mostraEmailInviate(){
        setChanged();
        notifyObservers(ClientGUI.INVIATI);
    }
    
    /**
     * Mostra all'utente le sue email ricevute
     */
    public void mostraEmailRicevute(){
        setChanged();
        notifyObservers(ClientGUI.RICEVUTI);
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
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email inviata dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaInviata(){
        return getUltimaEmail("email_inviate");
    }
    
    /*
    Ritorna l'intero corrispondente all'id dell'ultima email ricevuta dal
    utente proprietario della casella di posta elettronica
    */
    public int getUltimaRicevuta(){
        return getUltimaEmail("email_ricevute");
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
     * Ordina le email inviate per priorità decrescente
     */
    public void ordinaInviatePerPriorita(){
        ordinaPerPriorita(true);
        setChanged();
        notifyObservers(ClientGUI.INVIATE_PER_PRIORITA);
    }
    
    /**
     * Ordina le email ricevute per priorità decrescente
     */
    public void ordinaRicevutePerPriorita(){
        ordinaPerPriorita(false);
        setChanged();
        notifyObservers(ClientGUI.RICEVUTE_PER_PRIORITA);
    }
    
    /**
     * Esegue una query di ordinamento per priorità decrescente nella tabella
     * del DB desiderata (email_inviate o email_ricevute a seconda del valore
     * del parametro isInviate)
     * @param isInviate: valore booleano che determina in quale tabella va 
     *      effettuato un ordinamento delle tuple
     */
    private void ordinaPerPriorita(boolean isInviate){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryOrdinamentoPerPriorita = "";
        if(isInviate){
            queryOrdinamentoPerPriorita = "SELECT * FROM email_inviate ORDER BY priorita DESC";
            this.emailInviate.clear();
        } else{
            queryOrdinamentoPerPriorita = "SELECT * FROM email_ricevute ORDER BY priorita DESC";
            this.emailRicevute.clear();
        }
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryOrdinamentoPerPriorita);
            while(rs.next()){
                Email email = new Email();
                email.setId(rs.getInt("id_email"));
                email.setMittente(recuperaDatiUtente(rs.getString("mittente")));
                ArrayList<Utente> destinatariEmail = recuperaUtentiDestinatari(rs.getInt("id_email"), isInviate);
                email.setDestinatari(destinatariEmail);
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getInt("letto"));
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
     * Ordina le email inviate per data decrescente
     */
    public void ordinaInviatePerData(){
        ordinaPerData(true);
        setChanged();
        notifyObservers(ClientGUI.INVIATE_PER_DATA);
    }
    
    /**
     * Ordina le email ricevute per data decrescente
     */
    public void ordinaRicevutePerData(){
        ordinaPerData(false);
        setChanged();
        notifyObservers(ClientGUI.RICEVUTE_PER_DATA);
    }
    
    /**
     * Esegue una query di ordinamento per data decrescente nella tabella
     * del DB desiderata (email_inviate o email_ricevute a seconda del valore
     * del parametro isInviate)
     * @param isInviate: valore booleano che determina in quale tabella va 
     *      effettuato un ordinamento delle tuple
     */
    private void ordinaPerData(boolean isInviate){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryOrdinamentoPerPriorita = "";
        if(isInviate){
            queryOrdinamentoPerPriorita = "SELECT * FROM email_inviate ORDER BY data DESC";
            this.emailInviate.clear();
        } else{
            queryOrdinamentoPerPriorita = "SELECT * FROM email_ricevute ORDER BY data DESC";
            this.emailRicevute.clear();
        }
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            rs = st.executeQuery(queryOrdinamentoPerPriorita);
            while(rs.next()){
                Email email = new Email();
                email.setId(rs.getInt("id_email"));
                email.setMittente(recuperaDatiUtente(rs.getString("mittente")));
                ArrayList<Utente> destinatariEmail = recuperaUtentiDestinatari(rs.getInt("id_email"), isInviate);
                email.setDestinatari(destinatariEmail);
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getInt("letto"));
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
     * Recupera tutte le email inviate dall'utente proprietario della casella
     * di posta elettronica
     */
    public void recuperaEmailInviate(){
        this.emailInviate = new ArrayList<>();
        String queryEmailRicevute = 
                "SELECT * " + 
                "FROM email_inviate " +
                "WHERE mittente = '" + this.utenteProprietario.getEmail() + "'";
        recuperaEmailUtente(queryEmailRicevute, true);
    }
    
    /**
     * Recupera tutte le email ricevute dall'utente proprietario della casella
     * di posta elettronica
     */
    public void recuperaEmailRicevute(){
        this.emailRicevute = new ArrayList<>();
        String queryEmailRicevute = 
                "SELECT * " + 
                "FROM email_ricevute " +
                "WHERE destinatario = '" + this.utenteProprietario.getEmail() + "'";
        recuperaEmailUtente(queryEmailRicevute, false);
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
                ArrayList<Utente> destinatariEmail = recuperaUtentiDestinatari(rs.getInt("id_email"), isInviate);
                email.setDestinatari(destinatariEmail);
                email.setOggetto(rs.getString("oggetto"));
                email.setCorpo(rs.getString("corpo"));
                email.setData(new Date(rs.getDate("data").getTime()));
                email.setPriorita(rs.getInt("priorita"));
                email.setLetto(rs.getInt("letto"));
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
    private ArrayList<Utente> recuperaUtentiDestinatari(int idEmail, boolean inInviate){
        ArrayList<Utente> utentiDestinatari = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String queryUtentiDestinatari;
        if(inInviate){
            queryUtentiDestinatari =
                    "SELECT * " + 
                    "FROM utente " +
                    "WHERE email = (SELECT destinatario FROM email_inviate WHERE id_email= " + idEmail+ ")";
        } else{
            queryUtentiDestinatari =
                    "SELECT * " + 
                    "FROM utente " +
                    "WHERE email = (SELECT destinatario FROM email_ricevute WHERE id_email= " + idEmail+ ")";
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
    
    /**
     * Aggiorna le email inviate al momento della connessione al server
     * @param nuoveEmailInviate: ArrayList di nuove Email da inserire in table 
     *      email_inviate
     */
    public void inserisciNuoveEmailInviate(ArrayList<Email> nuoveEmailInviate){
        for (Email email : nuoveEmailInviate){
            inserisciNuovaEmail(email, "email_inviate");
        }
        this.emailInviate.addAll(nuoveEmailInviate);
        
        setChanged();
        notifyObservers(ClientGUI.INVIATI);
    }
    
    /**
     * Aggiorna le email ricevute al momento della connessione al server
     * @param nuoveEmailRicevute: ArrayList di nuove Email da inserire in table 
     *      email_ricevute
     */
    public void inserisciNuoveEmailRicevute(ArrayList<Email> nuoveEmailRicevute){
        for(Email email: nuoveEmailRicevute) {
            inserisciNuovaEmail(email, "email_ricevute");
        }
        this.emailRicevute.addAll(nuoveEmailRicevute);
        
        setChanged();
        notifyObservers(ClientGUI.RICEVUTI);
    }

    /**
     * Inserisce una nuova email in email_ricevute o in email_inviate a seconda
     * del contenuto del paramentro nomeTabella
     * @param email: email da aggiungere in DB
     * @param nomeTabella: stringa contenente il nome della table in cui 
     *      effettuare l'inserimento
     */
    private void inserisciNuovaEmail(Email email, String nomeTabella){
        ArrayList<Utente> utentiPerControllo = utentiInEmail(email);
        controllaPresenzaUtentiInDB(utentiPerControllo);
        String inserimentoNuovaEmail = 
                "INSERT INTO " + nomeTabella + " (id_email, mittente, destinatario, oggetto, corpo, data, priorita, letto) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn;
        PreparedStatement ps;
        
        try {
            conn = DriverManager.getConnection(urlDB);
            ps = conn.prepareStatement(inserimentoNuovaEmail);
            if(email.getDestinatari() != null){
                for(Utente utente : email.getDestinatari()){
                    inserisciValoriInPS(
                            ps, email.getId(),
                            email.getMittente().getEmail(),
                            utente.getEmail(),
                            email.getOggetto(),
                            email.getCorpo(),
                            email.getData(),
                            email.getPriorita(),
                            email.getLetto());
                    ps.executeUpdate();
                }
            }    
        } catch (SQLException ex) {
            Logger.getLogger(CasellaPostaElettronicaClient.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * Trova tutti gli utenti che compaiono nell'email e li restituisce 
     * all'interno di un ArrayList
     * @param email: email da cui estrapolare oggetti Utente
     * @return ArrayList contenente tutti gli utenti che compaiono nell'email
     */
    private ArrayList<Utente> utentiInEmail(Email email){
        ArrayList<Utente> tuttiUtenti = new ArrayList<>();
        if(email.getDestinatari() != null){
            tuttiUtenti.addAll(email.getDestinatari());
        }
        if(email.getMittente() != null){
            tuttiUtenti.add(email.getMittente());
        }
        return tuttiUtenti;
    }
    
    /**
     * Inserisce in table utente gli eventuali utenti non ancora presenti in DB
     * contenuti nel parametro utenti
     * @param utenti: ArrayList contenente oggetti di tipo Utente dei quali 
     *      vogliamo verificare la presenza in DB
     */
    private void controllaPresenzaUtentiInDB(ArrayList<Utente> utenti){
        if(utenti != null){
            String controlloPresenzaUtente;
            String inserimentoUtente = "INSERT INTO utente (email, nome, cognome) VALUES (?, ?, ?)";
            Connection conn = null;
            Statement st = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            try{
                conn = DriverManager.getConnection(urlDB);
                
                for(Utente utente: utenti){
                    boolean utentePresente = false;
                    controlloPresenzaUtente = "SELECT * FROM utente WHERE email = '" + utente.getEmail() + "'";
                    st = conn.createStatement();
                    rs = st.executeQuery(controlloPresenzaUtente);
                    if(rs.next()){
                        utentePresente = true;
                    }
                    if(!utentePresente){
                        pst = conn.prepareStatement(inserimentoUtente);
                        pst.setString(1, utente.getEmail());
                        pst.setString(2, utente.getNome());
                        pst.setString(3, utente.getCognome());
                        pst.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if(rs != null){
                        rs.close();
                    }
                    if(st != null){
                        st.close();
                    }
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
    
    /**
     * Inserisce i valori presenti nei parametri all'intero del PrepareStatement
     * passato nel parametro ps
     * @param ps: oggetto di tipo PreparedStatement
     * @param par1: intero contenente id dell'email
     * @param par2: stringa contenente email del mittente
     * @param par3: stringa contenente email del destinatario
     * @param par4: stringa contenente oggetto dell'email
     * @param par5: stringa contenente il corpo dell'email
     * @param par6: oggetto di tipo Date rappresentante la data dell'email
     * @param par7: intero compreso tra 1 e 10 rappresentante la priorità
     * @param par8: intero di valore 0 o 1
     */
    private void inserisciValoriInPS(PreparedStatement ps, int par1, String par2, String par3, String par4, String par5, Date par6, int par7, int par8){
        try{
            ps.setInt(1, par1);
            ps.setString(2, par2);
            ps.setString(3, par3);
            ps.setString(4, par4);
            ps.setString(5, par5);
            java.sql.Date par6sql = new java.sql.Date(par6.getTime());
            ps.setDate(6, par6sql);
            ps.setInt(7, par7);
            ps.setInt(8, par8);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Elimina l'email passata come parametro dalle email inviate oppure da 
     * quelle ricevute dell'utenteProprietario a seconda che questo sia il 
     * mittente o il distinatario dell'email da eliminare
     * @param email: oggetto Email che si desidera rimuove da DB
     */
    private void eliminaEmailDaDB(Email email){
        // il mio utenteProprietario è il mittente dell'email
        if(this.utenteProprietario.getEmail().equals(email.getMittente().getEmail())){
            eliminaEmail(email, "email_inviate");
        } else{ // il mio utenteProprietario è il destinatario dell'email
            eliminaEmail(email, "email_ricevute");
        }
    }
    
    /**
     * Rimuove l'email passata come parametro dalla tabella in cui è situata 
     * (indicata dal parametro nomeTabellaDB)
     * @param emailDaEliminare: email che si desidera rimuovere
     * @param nomeTabellaDB: nome della tabella dalla quale cancellare l'email
     */
    private void eliminaEmail(Email emailDaEliminare, String nomeTabellaDB){
        String queryEliminazioneEmailInviata = "DELETE FROM "+ nomeTabellaDB +" WHERE id_email = " + emailDaEliminare.getId();
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(queryEliminazioneEmailInviata);
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
    
    /**
     * Segna l'email passata come parametro come già letta
     * @param emailLetta : email che si desidera contrassegnare come già letta
     */
    public void segnaLetturaEmail(Email emailLetta){
        String queryLetturaEmail = "UPDATE email_ricevute SET letto = 1 WHERE id_email = " + emailLetta.getId();
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            st = conn.createStatement();
            st.executeUpdate(queryLetturaEmail);
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
        
        setChanged();
        notifyObservers(ClientGUI.LETTA_EMAIL);
    }
    
    /**
     * Inserisce il nuovo messaggio nella lista di messaggi ricevuti dal server
     * ma non ancora letti
     * @param messaggio: stringa contenente un messaggio proveniente dal server
     */
    public void inserisciMessaggio(String messaggio){
        this.messaggi.add(messaggio);
        setChanged();
        notifyObservers(ClientGUI.NUOVO_MESSAGGIO);
    }
    
    /**
     * Recupera e ritorna i messaggi ricevuti dal server se sono presenti, 
     * altrimenti ritorna null
     * @return un ArrayList di messaggi ricevuti se presenti, null altrimenti
     */
    public ArrayList<String> leggiMessaggi(){
        if(!this.messaggi.isEmpty()){
            ArrayList<String> messaggiRicevuti = new ArrayList<>();
            messaggiRicevuti.addAll(this.messaggi);
            this.messaggi.clear();
            return messaggiRicevuti;
        } else{
            return null;
        }
    }
    
    @Override
    public void inserisciInInviati(Email email) {
        inserisciNuovaEmail(email, "email_inviate");
        this.emailInviate.add(email);
        setChanged();
        notifyObservers(ClientGUI.EMAIL_INVIATA);
    }

    @Override
    public void inserisciInRicevuti(Email email) {
        inserisciNuovaEmail(email, "email_ricevute");
        this.emailRicevute.add(email);
        setChanged();
        notifyObservers(ClientGUI.EMAIL_RICEVUTA);
    }

    @Override
    public void elimina(Email email) {
        eliminaEmailDaDB(email);
        setChanged();
        notifyObservers(ClientGUI.EMAIL_ELIMINATA);
    }
    
}
