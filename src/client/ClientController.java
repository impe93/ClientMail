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
        final Component fonte = (Component)e.getSource();
        switch (fonte.getName()) {
            case "perPrioritaRicevuti": {
                new Thread() {
                    @Override
                    public void run() {
                        model.ordinaRicevutePerPriorita();
                    }
                }.start();
                break;
            }
            case "perPrioritaInviati": {
                Thread thread1 = new Thread() {
                    @Override
                    public void run() {
                        model.ordinaInviatePerPriorità();
                    }
                };
                thread1.start();
                break;
            }
            case "perDataInviati": {
                new Thread() {
                    @Override
                    public void run() {
                        model.ordinaInviatePerData();
                    }
                }.start();
                break;
            }
            case "perDataRicevuti": {
                new Thread() {
                    @Override
                    public void run() {
                        model.ordinaRicevutePerData();
                    }
                }.start();
                break;
            }
            case "emailInviate": {
                new Thread() {
                    @Override
                    public void run() {
                        model.getInviate();
                    }
                }.start();
                break;
            }
            case "emailRicevute": {
                new Thread() {
                    @Override
                    public void run() {
                        model.getRicevute();
                    }
                }.start();
                break;
            }
            case "nuova": {
                schermateNuoveEmail.add(new NuovaEmailGUI(this, schermateNuoveEmail.size()));
                break;
            }
            case "eliminaInviata": {
                new Thread() {
                    @Override
                    public void run() {
                        if (fonte instanceof ClientGUI.EliminaInoltraButton) {
                            Email emailDaEliminare = ((ClientGUI.EliminaInoltraButton)fonte).getEmailDaInoltrareEliminare();
                            model.eliminaEmailInviata(emailDaEliminare);
                        }
                    }
                }.start();
                break;
            }
            case "eliminaRicevuta": {
                new Thread() {
                    @Override
                    public void run() {
                        if (fonte instanceof ClientGUI.EliminaInoltraButton) {
                            Email emailDaEliminare = ((ClientGUI.EliminaInoltraButton)fonte).getEmailDaInoltrareEliminare();
                            model.eliminaEmailRicevuta(emailDaEliminare);
                        }
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
                        if (fonte instanceof NuovaEmailGUI.BottoneInviaCancella) {
                            NuovaEmailGUI schermataEvento = schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)fonte).getPosizione());
                            EmailDaInviare emailDaInviare = schermateNuoveEmail.get(((NuovaEmailGUI.BottoneInviaCancella)fonte).getPosizione()).getEmail();
                            if (emailDaInviare != null) {
                                model.inviaEmail(emailDaInviare);
                                schermataEvento.setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(schermataEvento, "Si è verificato un problema con l'email, verificare che tutti i campi siano stati compilati correttamente", "Errore email", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }.start();
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
        if (e.getValueIsAdjusting()) {
            final Component fonte = (Component)e.getSource();
            new Thread() {
                @Override
                public void run() {
                    Email emailSelezionata = ((JList<Email>)fonte).getSelectedValue();
                    if (emailSelezionata != null && emailSelezionata.getLetto() == 0 && ((ClientGUI.ListaInviateRicevute)fonte).getTipoLista().equals(ClientGUI.ListaInviateRicevute.LISTA_RICEVUTE)) {
                        model.segnaLetturaEmail(emailSelezionata);
                    }
                }
            }.start();
        }
    }
    
}
