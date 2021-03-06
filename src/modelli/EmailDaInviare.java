package modelli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe da utilizzare quando si sta effettuando l'invio
 * @author Lorenzo Imperatrice, Francesca Riddone, Berger Alessio
 */
public class EmailDaInviare implements Serializable{
    
    private ArrayList<String> destinatari;
    private Utente mittente;
    private String oggetto;
    private Date data;
    private int priorita;
    private String corpo;
    private final int letto = 0;

    public EmailDaInviare() {
        this.priorita = 3;
        this.data = new Date();
    }
    
    public EmailDaInviare(ArrayList<String> destinatari, String oggetto, Date data, int priorita, String corpo, Utente mittente) {
        this.destinatari = destinatari;
        this.mittente = mittente;
        this.oggetto = oggetto;
        this.data = data;
        this.priorita = priorita;
        this.corpo = corpo;
    }

    public ArrayList<String> getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(ArrayList<String> destinatari) {
        this.destinatari = destinatari;
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getPriorita() {
        return priorita;
    }

    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    @Override
    public String toString() {
        String stringaDestinatari = "";
        for(String d: destinatari){
            stringaDestinatari = stringaDestinatari.concat(d + " ");
        }
        return "EmailDaInviare{" + "destinatari=" + stringaDestinatari + 
                ", mittente=" + mittente.getEmail() + ", oggetto=" + oggetto + ", data=" + data + ", priorita=" + priorita + ", corpo=" + corpo + ", letto=" + letto + '}';
    }
    
    
    
}
