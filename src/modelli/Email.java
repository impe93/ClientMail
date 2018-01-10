package modelli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class Email implements Serializable{
    private int id;
    private Utente mittente;
    private ArrayList<Utente> destinatari;
    private String oggetto;
    private String corpo;
    private Date data;
    private int priorita;
    private int letto;

    
    public Email(int id, Utente mittente, ArrayList destinatari, String oggetto, String corpo){
        this.id = id;
        this.mittente = mittente;
        this.destinatari = destinatari;
        this.oggetto = oggetto;
        this.corpo = corpo;
        this.data = new Date();
        this.priorita = 3;
        this.letto = 0;
                
    }
    public Email(int id, Utente mittente, ArrayList destinatari, String oggetto, String corpo, int priorita){
        this.id = id;
        this.mittente = mittente;
        this.destinatari = destinatari;
        this.oggetto = oggetto;
        this.corpo = corpo;
        this.data = new Date();
        this.priorita = priorita;
        this.letto = 0;
                
    }
    
    public Email(){
        this.id = 0;
        this.mittente = null;
        this.destinatari = null;
        this.oggetto = null;
        this.corpo = null;
        this.data = null;
        this.priorita = 3;
        this.letto = 0;
                
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
    
    public int getLetto(){
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

    public void setLetto(int letto) {
        this.letto = letto;
    }
    
     @Override
    public String toString() {
        String destinatariEmail = "";
        for(Utente dest: this.destinatari){
            destinatariEmail += " "+dest.getEmail();
        }
        return "Email{" + "id=" + id + ", mittente=" 
                + mittente.getEmail() + ", destinatari=" + destinatariEmail + ", oggetto=" 
                + oggetto + ", corpo=" + corpo + ", data=" + data.toString() + ", priorita=" 
                + priorita + ", letto=" + letto + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Email other = (Email) obj;
        return this.id == other.id;
    }
    
    
}
