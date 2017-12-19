/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelli.Email;
/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ClientController implements ActionListener {
    
    private final ClientImplementation model;
    private final ArrayList<NuovaEmailGUI> schermateNuoveEmail;
    
    public ClientController(ClientImplementation model) {
        this.model = model;
        this.schermateNuoveEmail = new ArrayList<>();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Component fonte = (Component)e.getSource();
        switch (fonte.getName()) {
            case "perPrioritaRicevuti": {
                model.ordinaRicevutePerPriorita();
                break;
            }
            case "perPrioritaInviati": {
                model.ordinaInviatePerPriorità();
                break;
            }
            case "perDataInviati": {
                model.ordinaInviatePerData();
                break;
            }
            case "perDataRicevuti": {
                model.ordinaRicevutePerData();
                break;
            }
            case "emailInviate": {
                
                break;
            }
            case "emailRicevute": {
                
                break;
            }
            case "nuova": {
                this.schermateNuoveEmail.add(new NuovaEmailGUI(this, this.schermateNuoveEmail.size()));
                break;
            }
            case "elimina": {
                break;
            }
            case "inoltra": {
                break;
            }
            case "invia": {
                if (e.getSource() instanceof NuovaEmailGUI.BottoneInviaCancella) {
                    NuovaEmailGUI schermataEvento = this.schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)e.getSource()).getPosizione());
                    Email emailDaInviare = this.schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)e.getSource()).getPosizione()).getEmail();
                    if (emailDaInviare != null) {
                        this.model.inviaEmail(emailDaInviare);
                        schermataEvento.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(schermataEvento, "Si è verificato un problema con l'email, verificare che tutti i campi siano stati compilati correttamente", "Errore email", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            }
            case "cancella": {
                if (e.getSource() instanceof NuovaEmailGUI.BottoneInviaCancella) {
                    this.schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)e.getSource()).getPosizione()).setVisible(false);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
    
}
