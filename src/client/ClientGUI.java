/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelli.Email;

/**
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ClientGUI extends JFrame implements Observer{
    
    public static final String RICEVUTI = "ricevuti";
    public static final String INVIATI = "inviati";
    public static final String INVIATE_PER_PRIORITA ="inviatePerPriorita";
    public static final String INVIATE_PER_DATA ="inviatePerData";
    public static final String RICEVUTE_PER_PRIORITA ="ricevutePerPriorita";
    public static final String RICEVUTE_PER_DATA ="ricevutePerData";
    public static final String EMAIL_INVIATA = "emailInviata";
    public static final String EMAIL_RICEVUTA = "emailRicevuta";
    public static final String EMAIL_INVIATA_ELIMINATA = "emailInviataEliminata";
    public static final String EMAIL_RICEVUTA_ELIMINATA = "emailRicevutaEliminata";
    public static final String NUOVO_MESSAGGIO = "nuovoMessaggio";
    public static final String LETTA_EMAIL = "lettaEmail";
    public static final String NOTIFICA = "notifica";
    
    private Email emailDaVisualizzare;
    private final ClientImplementation modello;
    private final ClientController controller;
    private final ArrayList<Email> listaEmail;
    private String inVisualizzazione;
    
    /* Variabili per la GUI */
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
    private EliminaInoltraButton eliminaSelezionata;
    private EliminaInoltraButton inoltraSelezionata;
    
    private JPanel panelSinistro;
    
    private JPanel ordinePanel;
    private JButton prioritaOrdineButton;
    private JButton dataOrdineButton;
    
    
    public ClientGUI(String email, ClientImplementation modello) {
        super(email);
        this.emailDaVisualizzare = null;
        this.listaEmail = new ArrayList<>();
        this.inVisualizzazione = ClientGUI.RICEVUTI;
        this.modello = modello;
        this.controller = new ClientController(this.modello);
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
        this.mittenteEmailLabel = new JLabel();
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
        this.oggettoEmailLabel = new JLabel();
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
        this.corpoEmailPanel = new JScrollPane(this.corpoTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.emailPanel.add(this.corpoEmailPanel, BorderLayout.CENTER);
 /* ------ Fine Corpo Email Panel ------ */

/* ------ Fine Email Panel ------ */
 
/* ------ Toolbar Panel ------ */
        this.toolbarPanel = new JPanel();
        this.toolbarPanel.setLayout(new BorderLayout());
 /* ------ Toolbar Destra Panel ------ */
        this.toolbarDestraPanel = new JPanel();
        this.toolbarDestraPanel.setLayout(new FlowLayout());
        
        this.creaEmail = new JButton("Nuova Email");
        this.creaEmail.setName("nuova");
        this.creaEmail.addActionListener(this.controller);
        this.toolbarDestraPanel.add(this.creaEmail);
        
        this.eliminaSelezionata = new EliminaInoltraButton("Elimina selezionata");
        this.eliminaSelezionata.setName("elimina");
        this.eliminaSelezionata.addActionListener(this.controller);
        this.toolbarDestraPanel.add(this.eliminaSelezionata);
        
        this.inoltraSelezionata = new EliminaInoltraButton("Inoltra selezionata");
        this.inoltraSelezionata.setName("inoltra");
        this.inoltraSelezionata.addActionListener(this.controller);
        this.toolbarDestraPanel.add(this.inoltraSelezionata);
 /* ------ Fine Toolbar Destra Panel ------ */
 
 /* ------ Toolbar Sinistra Panel ------ */
        this.toolbarSinistraPanel = new JPanel();
        this.toolbarSinistraPanel.setLayout(new FlowLayout());
        
        this.ricevuteButton = new JButton("Ricevute");
        this.ricevuteButton.setName("emailRicevute");
        this.ricevuteButton.addActionListener(this.controller);
        this.toolbarSinistraPanel.add(this.ricevuteButton);
        
        this.inviateButton = new JButton("Inviate");
        this.inviateButton.setName("emailInviate");
        this.inviateButton.addActionListener(new ClientController(modello));
        this.toolbarSinistraPanel.add(this.inviateButton);
 /* ------ Fine Toolbar Sinistra Panel ------ */
 
        this.toolbarPanel.add(this.toolbarSinistraPanel, BorderLayout.WEST);
        this.toolbarPanel.add(this.toolbarDestraPanel, BorderLayout.EAST);
/* ------ Fine Toolbar Panel ------ */

/* ------ Lista Email Panel ------ */
        this.listaEmailList = new ListaInviateRicevute();
        this.listaEmailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.modelListaEmail = new DefaultListModel<>();
        this.listaEmailList.setModel(this.modelListaEmail);
        this.listaEmailList.setCellRenderer(new EmailCellRenderer());
        this.listaEmailList.addListSelectionListener(controller);
        this.listaEmailList.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (((JList<Email>)e.getSource()).getSelectedValue() != null) {
                        emailDaVisualizzare = ((JList<Email>)e.getSource()).getSelectedValue();
                        eliminaSelezionata.setEmailDaInoltrareEliminare(emailDaVisualizzare);
                        inoltraSelezionata.setEmailDaInoltrareEliminare(emailDaVisualizzare);
                        mittenteEmailLabel.setText(emailDaVisualizzare.getMittente().getEmail());
                        String destinatari = "";
                        for(int i = 0; i < emailDaVisualizzare.getDestinatari().size(); i++) {
                            if (i == 0) {
                                destinatari = emailDaVisualizzare.getDestinatari().get(0).getEmail();
                            } else {
                                destinatari += ", " + emailDaVisualizzare.getDestinatari().get(i).getEmail();
                            }
                        }
                        destinatariEmailLabel.setText(destinatari);
                        oggettoEmailLabel.setText(emailDaVisualizzare.getOggetto());
                        corpoTextArea.setText(emailDaVisualizzare.getCorpo());
                    }
                }
            }
        });
        this.listaEmailScrollPane = new JScrollPane(this.listaEmailList);
/* ------ Fine Lista Email Panel ------ */
        
        this.panelSinistro = new JPanel();
        this.panelSinistro.setLayout(new BorderLayout());
        
        this.ordinePanel = new JPanel();
        this.ordinePanel.setLayout(new BorderLayout());
        
        this.prioritaOrdineButton = new JButton("Per priorità");
        this.prioritaOrdineButton.setName("perPrioritaRicevuti");
        this.prioritaOrdineButton.addActionListener(controller);
        this.ordinePanel.add(this.prioritaOrdineButton, BorderLayout.WEST);
        
        this.dataOrdineButton = new JButton("Per data");
        this.dataOrdineButton.setName("perDataRicevuti");
        this.dataOrdineButton.addActionListener(controller);
        this.ordinePanel.add(this.dataOrdineButton, BorderLayout.EAST);
        
        this.panelSinistro.add(this.ordinePanel, BorderLayout.NORTH);
        this.panelSinistro.add(this.listaEmailScrollPane, BorderLayout.CENTER);

/* ------ Split Pane ------ */
        this.clientSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.panelSinistro, this.emailPanel);
        this.clientSplitPane.setOneTouchExpandable(true);
        this.clientSplitPane.setDividerLocation(250);
/* ------ Fine Split Pane ------ */
        
/* ------ Frame ------ */
        Dimension minimumSize = new Dimension(250, 50);
        this.listaEmailScrollPane.setMinimumSize(minimumSize);
        this.emailPanel.setMinimumSize(minimumSize);
        this.setPreferredSize(new Dimension(700, 500));
        this.setLayout(new BorderLayout());        
        this.add(this.toolbarPanel, BorderLayout.NORTH);
        this.add(this.clientSplitPane, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
/* ------ Fine Frame ------ */
    }
    
    @Override
    public void update(Observable o, Object arg) {
        final Observable oFinal = o;
        switch((String)arg) {
            case ClientGUI.RICEVUTI: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailRicevute());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                            dataOrdineButton.setName("perDataRicevuti");
                            prioritaOrdineButton.setName("perPrioritaRicevuti");
                            eliminaSelezionata.setName("eliminaRicevuta");
                            inVisualizzazione = ClientGUI.RICEVUTI;
                            ((ListaInviateRicevute)listaEmailList).setTipoLista(ListaInviateRicevute.LISTA_RICEVUTE);
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.INVIATI: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailInviate());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                            dataOrdineButton.setName("perDataInviati");
                            prioritaOrdineButton.setName("perPrioritaInviati");
                            eliminaSelezionata.setName("eliminaInviata");
                            inVisualizzazione = ClientGUI.INVIATI;
                            ((ListaInviateRicevute)listaEmailList).setTipoLista(ListaInviateRicevute.LISTA_INVIATE);
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.INVIATE_PER_DATA: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailInviate());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.INVIATE_PER_PRIORITA: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailInviate());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                                
                            }
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.RICEVUTE_PER_PRIORITA: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailRicevute());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.RICEVUTE_PER_DATA: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailRicevute());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            if (listaEmail.size() > 0) {
                                emailDaVisualizzare = listaEmail.get(0);
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.LETTA_EMAIL: {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            listaEmail.clear();
                            listaEmail.addAll(((CasellaPostaElettronicaClient)oFinal).getEmailRicevute());
                            modelListaEmail.clear();
                            for (Email email : listaEmail) {
                                modelListaEmail.addElement(email);
                            }
                            if (listaEmail.size() > 0) {
                                int indiceSelezionata = 0;
                                for (Email email : listaEmail){
                                    if (email.getId() == emailDaVisualizzare.getId()) {
                                        System.out.println(indiceSelezionata);
                                        break;
                                    }
                                    indiceSelezionata++;
                                }
                                System.out.println(indiceSelezionata);
                                listaEmailList.setSelectedIndex(indiceSelezionata);
                                eliminaSelezionata.setEmailDaInoltrareEliminare(emailDaVisualizzare);
                                inoltraSelezionata.setEmailDaInoltrareEliminare(emailDaVisualizzare);
                                mittenteEmailLabel.setText(emailDaVisualizzare.getMittente().getEmail());
                                String destinatari = "";
                                for(int i = 0; i < emailDaVisualizzare.getDestinatari().size(); i++) {
                                    if (i == 0) {
                                        destinatari = emailDaVisualizzare.getDestinatari().get(0).getEmail();
                                    } else {
                                        destinatari += ", " + emailDaVisualizzare.getDestinatari().get(i).getEmail();
                                    }
                                }
                                destinatariEmailLabel.setText(destinatari);
                                oggettoEmailLabel.setText(emailDaVisualizzare.getOggetto());
                                corpoTextArea.setText(emailDaVisualizzare.getCorpo());
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case ClientGUI.NOTIFICA: {
                
                break;
            }
            
            default: {
                break;
            }
        }
    }
    
    public class EliminaInoltraButton extends JButton {
        
        private Email emailDaInoltrareEliminare;
        
        public EliminaInoltraButton (String nome) {
            super(nome);
            this.emailDaInoltrareEliminare = null;
        }
        
        public void setEmailDaInoltrareEliminare(Email email) {
            this.emailDaInoltrareEliminare = email;
        }
        
        public Email getEmailDaInoltrareEliminare() {
            return this.emailDaInoltrareEliminare;
        }
        
    }
    
    public class ListaInviateRicevute extends JList {
        public static final String LISTA_INVIATE = "listaInviate";
        public static final String LISTA_RICEVUTE = "listaRicevute";
        
        private String tipoLista;
        
        public ListaInviateRicevute () {
            super();
            this.tipoLista = ListaInviateRicevute.LISTA_RICEVUTE;
        }

        public String getTipoLista() {
            return tipoLista;
        }

        public void setTipoLista(String tipoLista) {
            this.tipoLista = tipoLista;
        }
        
    }
    
    public static void main (String[] args) {
        ClientImplementation modello;
        ClientGUI client;
        if (args.length == 1) {
            try {
                modello = new ClientImplementation(args[0]);
                client = new ClientGUI(args[0], modello);
                modello.registraOsservatoreEAggiornaEmail(client);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
