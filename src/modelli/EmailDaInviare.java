/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelli;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe da utilizzare quando si sta effettuando l'invio
 * @author Lorenzo Imperatrice, Francesca Riddone, Berger Alessio
 */
public class EmailDaInviare {
    
    private ArrayList<String> destinatari;
    private String mittente;
    private String oggetto;
    private Date data;
    private int priorita;
    private String corpo;
    private final int letto = 0;

    public EmailDaInviare() {
        this.priorita = 3;
        this.data = new Date();
    }
    
    public EmailDaInviare(ArrayList<String> destinatari, String mittente, String oggetto, Date data, int priorita, String corpo) {
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

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
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
    
}
