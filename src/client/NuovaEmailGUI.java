/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import modelli.Utente;

/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
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
    private JTextField oggettoTextField;
    
    private JPanel prioritaPanel;
    private JLabel prioritaLabel;
    private JComboBox prioritaComboBox;
    
    private JPanel pannelloLivelloMedio;
    
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
    
    /**
     * Utilizzato quando viene inoltrata una email,
     * @param controller: Il controller di riferimento
     * @param posizione: La posizione all'interno dell'arrayList di nuovaEmailGUI sul controller
     * @param email: email che si vuole inoltrare
     */
    public NuovaEmailGUI(ClientController controller, int posizione, Email email) {
        this.controller = controller;
        this.posizione = posizione;
        this.initGUI();
        this.oggettoTextField.setText("Fwd: " + email.getOggetto());
        String destinatari = "";
        int i = 0;
        for(Utente destinatario : email.getDestinatari()) {
            if (i == 0) {
                destinatari += destinatario.getEmail() + " <" + destinatario.getNome() + " " + destinatario.getCognome() + ">";
            } else {
                destinatari += ", " + destinatario.getEmail() + " <" + destinatario.getNome() + " " +  destinatario.getCognome() + ">";
            }
            i++;
        }
        this.corpoTextArea.setText("Inizio messaggio inoltrato\n\nDa: " + email.getMittente().getEmail() + " <" + email.getMittente().getNome() + " " + email.getMittente().getCognome() + ">\nA: " + destinatari + "\nOggetto: " + email.getOggetto() + "\n\n" + email.getCorpo());
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
        this.destinatariTextField = new JTextField();
        this.destinatariPanel.add(this.destinatariTextField, BorderLayout.CENTER);
        this.panelSuperiore.add(this.destinatariPanel, BorderLayout.NORTH);
        /* ----- Fine Destinatari Panel ----- */
        
        /* ----- Pannello di livello medio ----- */
        this.pannelloLivelloMedio = new JPanel();
        this.pannelloLivelloMedio.setLayout(new BorderLayout());
        /* ----- Fine Pannello di livello medio ----- */
        
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
        this.oggettoTextField = new JTextField();
        this.oggettoPanel.add(this.oggettoTextField, BorderLayout.CENTER);
        this.pannelloLivelloMedio.add(this.oggettoPanel, BorderLayout.NORTH);
        /* ----- Fine Oggetto Panel ----- */
        
        /* ----- Priorità Panel ----- */
        this.prioritaPanel = new JPanel();
        this.prioritaPanel.setLayout(new BorderLayout());
        if (this.getContentPane() == null) {
            this.prioritaPanel.setBorder(empty);
        } else {
            this.prioritaPanel.setBorder(new CompoundBorder(empty, this.prioritaPanel.getBorder()));
        }
        this.prioritaLabel = new JLabel("Priorità");
        this.prioritaPanel.add(this.prioritaLabel, BorderLayout.WEST);
        String [] listaNumeriPriorita = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        this.prioritaComboBox = new JComboBox(listaNumeriPriorita);
        this.prioritaComboBox.setSelectedIndex(2);
        this.prioritaPanel.add(this.prioritaComboBox, BorderLayout.CENTER);
        this.pannelloLivelloMedio.add(this.prioritaPanel, BorderLayout.SOUTH);
        /* ----- Fine Priorità Panel ----- */
        
        this.panelSuperiore.add(this.pannelloLivelloMedio, BorderLayout.CENTER);
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
        Dimension d = new Dimension(600, 600);
        this.setSize(d);
        this.setPreferredSize(d);
        
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
        int priorita = Integer.parseInt((String)this.prioritaComboBox.getSelectedItem());
        if (!destinatari.equals("")) {
            if (!oggetto.equals("")) {
                if(!corpo.equals("")){
                    emailDaInviare.setCorpo(corpo);
                    emailDaInviare.setOggetto(oggetto);
                    emailDaInviare.setDestinatari(this.prendiDestinatari(destinatari));
                    emailDaInviare.setPriorita(priorita);
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