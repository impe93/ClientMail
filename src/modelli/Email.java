package modelli;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class Email {
    int id;
    Utente mittente;
    ArrayList<Utente> destinatari;
    String oggetto;
    String corpo;
    Date data;
    int priorita;
    boolean letto;

    
    public Email(int id, Utente mittente, ArrayList destinatari, String oggetto, String corpo){
        this.id = id;
        this.mittente = mittente;
        this.destinatari = destinatari;
        this.oggetto = oggetto;
        this.corpo = corpo;
        this.data = new Date();
        this.priorita = 3;
        this.letto = false;
                
    }
    
    public Email(){
        this.id = 0;
        this.mittente = null;
        this.destinatari = null;
        this.oggetto = null;
        this.corpo = null;
        this.data = null;
        this.priorita = 0;
        this.letto = false;
                
    }
    
    /*GETTER*/
    
    public int getId(){
        return this.id;
    }
    
    public Utente getMittente(){
        return this.mittente;
    }
    
    public ArrayList<Utente> getDestinatari(){
        return this.destinatari;
    }
    
    public String getOggetto(){
        return this.oggetto;
    }
    
    public String getCorpo(){
        return this.corpo;
    }
    
    public Date getData(){
        return this.data;
    }
    
    public int getPriorita(){
        return this.priorita;
    }
    
    public boolean getLetto(){
        return this.letto;
    }
    
    /*SETTER*/

    public void setId(int id) {
        this.id = id;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public void setDestinatari(ArrayList<Utente> destinatari) {
        this.destinatari = destinatari;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    public void setLetto(boolean letto) {
        this.letto = letto;
    }
}