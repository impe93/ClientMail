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
    int letto;

    
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
        return "Email{" + "id=" + id + ", mittente=" 
                + mittente.getEmail() + ", destinatari=" + destinatari.get(0).getEmail() + ", oggetto=" 
                + oggetto + ", corpo=" + corpo + ", data=" + data.toString() + ", priorita=" 
                + priorita + ", letto=" + letto + '}';
    }
}
