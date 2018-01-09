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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelli.Email;
import modelli.EmailDaInviare;
/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ClientController implements ActionListener, ListSelectionListener {
    
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
                model.getInviate();
                break;
            }
            case "emailRicevute": {
                model.getRicevute();
                break;
            }
            case "nuova": {
                this.schermateNuoveEmail.add(new NuovaEmailGUI(this, this.schermateNuoveEmail.size()));
                break;
            }
            case "eliminaInviata": {
                if (e.getSource() instanceof ClientGUI.EliminaInoltraButton) {
                    Email emailDaEliminare = ((ClientGUI.EliminaInoltraButton)e.getSource()).getEmailDaInoltrareEliminare();
                    this.model.eliminaEmail(emailDaEliminare);
                }
                break;
            }
            case "eliminaRicevuta": {
                //Mettere elimina ricevuta uguale ad inviata ma chiamaqndo due metodi differenti del model
                break;
            }
            case "inoltra": {
                if (e.getSource() instanceof ClientGUI.EliminaInoltraButton) {
                    Email emailDaInoltrare = ((ClientGUI.EliminaInoltraButton)e.getSource()).getEmailDaInoltrareEliminare();
                    this.schermateNuoveEmail.add(new NuovaEmailGUI(this, this.schermateNuoveEmail.size(), emailDaInoltrare));
                }
                break;
            }
            case "invia": {
                if (e.getSource() instanceof NuovaEmailGUI.BottoneInviaCancella) {
                    NuovaEmailGUI schermataEvento = this.schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)e.getSource()).getPosizione());
                    EmailDaInviare emailDaInviare = this.schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)e.getSource()).getPosizione()).getEmail();
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Email emailSelezionata = ((JList<Email>)e.getSource()).getSelectedValue();
        if (emailSelezionata != null && emailSelezionata.getLetto() == 0) {
            model.segnaLetturaEmail(emailSelezionata);
        }
    }
    
}
