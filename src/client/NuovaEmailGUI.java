/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import modelli.Email;
import modelli.EmailDaInviare;

/**
 *
 * @author lorenzo
 */
public class NuovaEmailGUI extends JFrame {
    
    private final ClientController controller;
    private final int posizione;
    
    private JPanel panelSuperiore;
    
    private JPanel destinatariPanel;
    private JLabel destinatariLabel;
    private JTextField destinatariTextField;
    
    private JPanel oggettoPanel;
    private JLabel oggettoLabel;
    private JPanel textFieldPanel;
    private JTextField oggettoTextField;
    
    private JPanel corpoPanel;
    private JLabel corpoLabel;
    private JScrollPane corpoScrollPane;
    private JTextArea corpoTextArea;
    
    private JPanel bottoniPanel;
    private BottoneInviaCancella inviaButton;
    private BottoneInviaCancella cancellaButton;
    
    public NuovaEmailGUI (ClientController controller, int posizione) {
        super("Nuova Email");
        this.controller = controller;
        this.posizione = posizione;
        this.initGUI();
    }
    
    public int getPosizione() {
        return this.posizione;
    }
    
    private void initGUI () {
        Border empty = new EmptyBorder(10, 10, 10, 10);
        this.setLayout(new BorderLayout());
        
        this.panelSuperiore = new JPanel();
        this.panelSuperiore.setLayout(new BorderLayout());
        
        /* ----- Destinatari Panel ----- */
        this.destinatariPanel = new JPanel();
        this.destinatariPanel.setLayout(new BorderLayout());
        if (this.getContentPane() == null) {
            this.destinatariPanel.setBorder(empty);
        } else {
            this.destinatariPanel.setBorder(new CompoundBorder(empty, this.destinatariPanel.getBorder()));
        }
        this.destinatariLabel = new JLabel("Destinatario/i: ");
        this.destinatariPanel.add(this.destinatariLabel, BorderLayout.WEST);
        this.destinatariTextField = new JTextField(20);
        this.destinatariPanel.add(this.destinatariTextField, BorderLayout.EAST);
        this.panelSuperiore.add(this.destinatariPanel, BorderLayout.NORTH);
        /* ----- Fine Destinatari Panel ----- */
        
        /* ----- Oggetto Panel ----- */
        this.oggettoPanel = new JPanel();
        this.oggettoPanel.setLayout(new BorderLayout());
        if (this.getContentPane() == null) {
            this.oggettoPanel.setBorder(empty);
        } else {
            this.oggettoPanel.setBorder(new CompoundBorder(empty, this.oggettoPanel.getBorder()));
        }
        this.oggettoLabel = new JLabel("Oggetto: ");
        this.oggettoPanel.add(this.oggettoLabel, BorderLayout.WEST);
        this.textFieldPanel = new JPanel();
        this.textFieldPanel.setLayout(new BoxLayout(this.textFieldPanel, BoxLayout.X_AXIS));
        this.oggettoTextField = new JTextField(20);
        this.textFieldPanel.add(this.oggettoTextField);
        this.oggettoPanel.add(this.textFieldPanel, BorderLayout.EAST);
        this.panelSuperiore.add(this.oggettoPanel, BorderLayout.CENTER);
        /* ----- Fine Oggetto Panel ----- */
        
        this.add(this.panelSuperiore, BorderLayout.NORTH);
        
        /* ----- Text Area ----- */
        this.corpoPanel = new JPanel();
        this.corpoPanel.setLayout(new BorderLayout());
        if (this.getContentPane() == null) {
            this.corpoPanel.setBorder(empty);
        } else {
            this.corpoPanel.setBorder(new CompoundBorder(empty, this.corpoPanel.getBorder()));
        }
        this.corpoLabel = new JLabel("Messaggio: ");
        this.corpoPanel.add(this.corpoLabel, BorderLayout.NORTH);
        this.corpoTextArea = new JTextArea();
        this.corpoTextArea.setLineWrap(true);
        this.corpoTextArea.setWrapStyleWord(true);
        this.corpoScrollPane = new JScrollPane(this.corpoTextArea);
        this.corpoPanel.add(this.corpoScrollPane, BorderLayout.CENTER);
        this.add(this.corpoPanel, BorderLayout.CENTER);
        /* ----- Fine Text Area ----- */
        
        /* ----- Bottoni Panel ----- */
        this.bottoniPanel = new JPanel();
        this.bottoniPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.inviaButton = new BottoneInviaCancella("Invia", this.posizione);
        this.inviaButton.setName("invia");
        this.inviaButton.addActionListener(controller);
        this.bottoniPanel.add(this.inviaButton);
        this.cancellaButton = new BottoneInviaCancella("Cancel", this.posizione);
        this.cancellaButton.setName("cancella");
        this.cancellaButton.addActionListener(controller);
        this.bottoniPanel.add(this.cancellaButton);
        /* ----- Fine Bottoni Panel ----- */
        
        this.add(this.bottoniPanel, BorderLayout.SOUTH);
        
        this.pack();
        this.setVisible(true);
        
    }
    
    public class BottoneInviaCancella extends JButton {
        
        private final int posizione;
        
        public BottoneInviaCancella(String nome, int posizione) {
            super(nome);
            this.posizione = posizione;
        }
        
        public int getPosizione() {
            return this.posizione;
        }
    }
    
    public EmailDaInviare getEmail() {
        EmailDaInviare emailDaInviare = new EmailDaInviare();
        String destinatari = this.destinatariTextField.getText();
        String oggetto = this.oggettoTextField.getText();
        String corpo = this.corpoTextArea.getText();
        if (!destinatari.equals("")) {
            if (!oggetto.equals("")) {
                if(!corpo.equals("")){
                    emailDaInviare.setCorpo(corpo);
                    emailDaInviare.setOggetto(oggetto);
                    emailDaInviare.setDestinatari(this.prendiDestinatari(destinatari));
                    return emailDaInviare;
                }
            }
        }
        return null;
    }
    
    private ArrayList<String> prendiDestinatari (String destinatari) {
        String destinatario = "";
        ArrayList<String> valoreDaRitornare = new ArrayList<>();
        for (int i = 0; i < destinatari.length(); i++) {
            if(!(destinatari.charAt(i) == ' ')) {
                destinatario += String.valueOf(destinatari.charAt(i));
            } else {
                valoreDaRitornare.add(destinatario);
            }
        }
        valoreDaRitornare.add(destinatario);
        return valoreDaRitornare;
    }
}
