/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
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
    
    private final Email emailDaVisualizzare;
    private final ArrayList<Email> listaEmailRicevute;
    
    private JSplitPane clientSplitPane;
    private JScrollPane listaEmailScrollPane;
    private JList<Email> listaEmailList;
    private DefaultListModel<Email> modelListaEmail;
    
    
    private JPanel emailPanel;
    private JPanel intestazioneEmailPanel;
    
    private JScrollPane corpoEmailPanel;
    private JTextArea corpoTextArea;
    
    private JPanel mittentePanel;
    private JLabel mittenteScrittaLabel;
    private JLabel mittenteEmailLabel;
    
    private JPanel destinatariPanel;
    private JLabel destinatariScrittaLabel;
    private JLabel destinatariEmailLabel;
    
    private JPanel oggettoPanel;
    private JLabel oggettoScrittaLabel;
    private JLabel oggettoEmailLabel;
    
    private JPanel toolbarPanel;
    private JPanel toolbarSinistraPanel;
    private JButton inviateButton;
    private JButton ricevuteButton;
    private JPanel toolbarDestraPanel;
    private JButton creaEmail;
    private JButton eliminaSelezionata;
    private JButton inoltraSelezionata;
    
    
    public ClientGUI(Utente utenteUtilizzatore, Email emailDaVisualizzare, ArrayList<Email> listaEmailRicevute) {
        super(utenteUtilizzatore.getNome() + " " + utenteUtilizzatore.getCognome() + " - " + utenteUtilizzatore.getEmail());
        this.emailDaVisualizzare = emailDaVisualizzare;
        this.listaEmailRicevute = listaEmailRicevute;
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

/* ------ Fine Email Panel ------ */
 
/* ------ Lista Email Panel ------ */
        this.listaEmailList = new JList();
        this.listaEmailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.modelListaEmail = new DefaultListModel<>();
        this.listaEmailList.setModel(this.modelListaEmail);
        this.listaEmailList.setCellRenderer(new EmailCellRenderer());
        this.listaEmailRicevute.forEach(email -> {
            this.modelListaEmail.addElement(email);
        });
        this.listaEmailScrollPane = new JScrollPane(this.listaEmailList);
/* ------ Fine Lista Email Panel ------ */

/* ------ Toolbar Panel ------ */
        this.toolbarPanel = new JPanel();
        this.toolbarPanel.setLayout(new BorderLayout());
 /* ------ Toolbar Destra Panel ------ */
        this.toolbarDestraPanel = new JPanel();
        this.toolbarDestraPanel.setLayout(new FlowLayout());
        this.creaEmail = new JButton("Nuova Email");
        this.toolbarDestraPanel.add(this.creaEmail);
        this.eliminaSelezionata = new JButton("Elimina selezionata");
        this.toolbarDestraPanel.add(this.eliminaSelezionata);
        this.inoltraSelezionata = new JButton("Inoltra selezionata");
        this.toolbarDestraPanel.add(this.inoltraSelezionata);
 /* ------ Fine Toolbar Destra Panel ------ */
 /* ------ Toolbar Sinistra Panel ------ */
        this.toolbarSinistraPanel = new JPanel();
        this.toolbarSinistraPanel.setLayout(new FlowLayout());
        this.ricevuteButton = new JButton("Ricevute");
        this.toolbarSinistraPanel.add(this.ricevuteButton);
        this.inviateButton = new JButton("Inviate");
        this.toolbarSinistraPanel.add(this.inviateButton);
 /* ------ Fine Toolbar Destra Panel ------ */
        this.toolbarPanel.add(this.toolbarSinistraPanel, BorderLayout.WEST);
        this.toolbarPanel.add(this.toolbarDestraPanel, BorderLayout.EAST);
/* ------ Fine Toolbar Panel ------ */
        
        /* ------ Split Pane ------ */
        this.clientSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.listaEmailScrollPane, this.emailPanel);
        this.clientSplitPane.setOneTouchExpandable(true);
        this.clientSplitPane.setDividerLocation(250);
        
        Dimension minimumSize = new Dimension(250, 50);
        this.listaEmailScrollPane.setMinimumSize(minimumSize);
        this.emailPanel.setMinimumSize(minimumSize);
        
        /* ------ Frame ------ */
        this.setPreferredSize(new Dimension(700, 500));
        this.setLayout(new BorderLayout());
        
        this.add(this.toolbarPanel, BorderLayout.NORTH);
        this.add(this.clientSplitPane, BorderLayout.CENTER);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    public static void main (String[] args) {
        ArrayList<Utente> destinatari = new ArrayList<>();
        destinatari.add(new Utente("Francesca", "Riddone", "francesca.riddone@edu.unito.it"));
        String corpo = "Ciao mi chiamo Lorenzo Imperatrice, le ho mandato questo messaggio per proporle una collaborazione con Jesu";
        String oggetto = "Collaborazione con Jesu";
        Email emailVisualizzata = new Email(2, new Utente("Lorenzo", "Imperatrice", "lorenzo.imperatrice@gmail.com"), destinatari, oggetto, corpo);
        ArrayList<Email> listaEmail = new ArrayList<>();
        listaEmail.add(emailVisualizzata);
        listaEmail.add(emailVisualizzata);
        listaEmail.add(emailVisualizzata);
        listaEmail.add(emailVisualizzata);
        ClientGUI client = new ClientGUI(new Utente("Francesca", "Riddone", "francesca.riddone@edu.unito.it"), emailVisualizzata, listaEmail);
        
        
        try {
            ClientImplementation modelloClient = new ClientImplementation("francesca.riddone@edu.unito.it");
            modelloClient.chiamaMetodoProvaServer();
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
