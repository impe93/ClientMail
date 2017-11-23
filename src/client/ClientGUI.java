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
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import modelli.Email;
import modelli.Utente;

/**
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ClientGUI extends JFrame{
    
    private Email emailDaVisualizzare;
    
    private JSplitPane clientSplitPane;
    private JPanel listaEmailPanel;
    private JPanel emailPanel;
    private JPanel intestazioneEmailPanel;
    
    private JScrollPane corpoEmailPanel;
    private JTextArea corpoTextArea;
    
    private JPanel conclusioneEmail;
    
    private JPanel mittentePanel;
    private JLabel mittenteScrittaLabel;
    private JLabel mittenteEmailLabel;
    
    private JPanel destinatariPanel;
    private JLabel destinatariScrittaLabel;
    private JLabel destinatariEmailLabel;
    
    private JPanel oggettoPanel;
    private JLabel oggettoScrittaLabel;
    private JLabel oggettoEmailLabel;
    
    
    public ClientGUI(Email emailDaVisualizzare) {
        this.emailDaVisualizzare = emailDaVisualizzare;
        
        initGUI();
    }
    
    private void initGUI() {
        
/* ------ Email Panel ------ */
        this.emailPanel = new JPanel();
        BorderLayout emailPanelLayout = new BorderLayout();
        emailPanelLayout.setHgap(20);
        this.emailPanel.setLayout(emailPanelLayout);
        Border empty = new EmptyBorder(10, 10, 10, 10);
        if (this.emailPanel.getBorder() == null) {
            this.emailPanel.setBorder(empty);
        } else {
            this.emailPanel.setBorder(new CompoundBorder(empty, this.emailPanel.getBorder()));
        }
        
 /* ------ Intestazione Email Panel ------ */
        this.intestazioneEmailPanel = new JPanel();
        BoxLayout intestazioneEmailLayout = new BoxLayout(this.intestazioneEmailPanel, BoxLayout.Y_AXIS);
        this.intestazioneEmailPanel.setLayout(intestazioneEmailLayout);
        
  /* ------ Mittente Email Panel ------ */
        this.mittentePanel = new JPanel();
        FlowLayout mittentePanelLayout = new FlowLayout();
        mittentePanelLayout.setAlignment(FlowLayout.LEFT);
        this.mittentePanel.setLayout(mittentePanelLayout);
        this.mittenteScrittaLabel = new JLabel("Mittente: ");
        this.mittentePanel.add(this.mittenteScrittaLabel);
        this.mittenteEmailLabel = new JLabel(this.emailDaVisualizzare.getMittente().getEmail());
        this.mittentePanel.add(this.mittenteEmailLabel);
        this.intestazioneEmailPanel.add(this.mittentePanel);
  /* ------ Fine Mittente Email Panel ------ */
        
  /* ------ Destinatari Email Panel ------ */
        this.destinatariPanel = new JPanel();
        FlowLayout destinatariPanelLayout = new FlowLayout();
        destinatariPanelLayout.setAlignment(FlowLayout.LEFT);
        this.destinatariPanel.setLayout(destinatariPanelLayout);
        this.destinatariScrittaLabel = new JLabel("Destinatari/o: ");
        this.destinatariPanel.add(this.destinatariScrittaLabel);
        this.destinatariEmailLabel = new JLabel();
        this.emailDaVisualizzare.getDestinatari().forEach(destinatario -> {
            if (!this.destinatariEmailLabel.getText().equals("")) {
                this.destinatariEmailLabel.setText(this.destinatariEmailLabel.getText() + ", " + destinatario.getEmail());
            } else {
                this.destinatariEmailLabel.setText(destinatario.getEmail());
            }
        });
        this.destinatariPanel.add(this.destinatariEmailLabel);
        this.intestazioneEmailPanel.add(this.destinatariPanel);
  /* ------ Fine Destinatari Email Panel ------ */
        
  /* ------ Oggetto Email Panel ------ */
        this.oggettoPanel = new JPanel();
        FlowLayout oggettoPanelLayout = new FlowLayout();
        oggettoPanelLayout.setAlignment(FlowLayout.LEFT);
        this.oggettoPanel.setLayout(oggettoPanelLayout);
        this.oggettoScrittaLabel = new JLabel("Oggetto: ");
        this.oggettoPanel.add(this.oggettoScrittaLabel);
        this.oggettoEmailLabel = new JLabel(this.emailDaVisualizzare.getOggetto());
        this.oggettoPanel.add(oggettoEmailLabel);
        this.intestazioneEmailPanel.add(this.oggettoPanel);
  /* ------ Fine Oggetto Email Panel ------ */
        
        this.emailPanel.add(this.intestazioneEmailPanel, BorderLayout.PAGE_START);
 /* ------ Fine Intestazione Email Panel ------ */
        
        
 /* ------ Corpo Email Panel ------ */
        this.corpoTextArea = new JTextArea();
        this.corpoTextArea.setEditable(false);
        this.corpoTextArea.setLineWrap(true);
        this.corpoTextArea.setWrapStyleWord(true);
        this.corpoTextArea.setText(this.emailDaVisualizzare.getCorpo());
        this.corpoEmailPanel = new JScrollPane(this.corpoTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.emailPanel.add(this.corpoEmailPanel, BorderLayout.CENTER);
 /* ------ Fine Corpo Email Panel ------ */

 
 
/* ------ Lista Email Panel ------ */
        this.listaEmailPanel = new JPanel();
        /* ------ Fine Lista Email Panel ------ */
        
        /* ------ Fine Email Panel ------ */
        
        /* ------ Split Pane ------ */
        this.clientSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.listaEmailPanel, this.emailPanel);
        this.clientSplitPane.setOneTouchExpandable(true);
        this.clientSplitPane.setDividerLocation(150);
        
        Dimension minimumSize = new Dimension(100, 50);
        this.listaEmailPanel.setMinimumSize(minimumSize);
        this.emailPanel.setMinimumSize(minimumSize);
        
        /* Frame */
        this.setPreferredSize(new Dimension(500, 500));
        this.add(this.clientSplitPane);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    public static void main (String[] args) {
        ArrayList<Utente> destinatari = new ArrayList<>();
        destinatari.add(new Utente("Francesca", "Riddone", "francesca.riddone@edu.unito.it"));
        ClientGUI client = new ClientGUI(new Email(2, new Utente("Lorenzo", "Imperatrice", "lorenzo.imperatrice@gmail.com"), destinatari, "Prova", "Prova"));
    }
    
}
