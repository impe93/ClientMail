/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import modelli.Email;

/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class EmailCellRenderer extends DefaultListCellRenderer {
    
    /* TODO: Mettere tutte i component final ed inizializzarli per impedire il repainting*/
    
    private final JPanel pannelloEmail;
    private final JPanel pannelloHeader;
    private final JLabel labelTitolo;
    private final JLabel labelData;
    private final JPanel centralePanel;
    private final JLabel corpoLabel;
    private final JLabel oggettoLabel;
    private final JLabel prioritaLabel;
    
    EmailCellRenderer() {
        this.pannelloEmail = new JPanel();
        this.pannelloEmail.setLayout(new BorderLayout());
        
        this.pannelloHeader = new JPanel();
        this.pannelloHeader.setLayout(new BorderLayout());
        
        this.labelData = new JLabel();
        labelData.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        
        this.labelTitolo = new JLabel();
        labelTitolo.setFont(new Font(labelTitolo.getFont().getName(), Font.BOLD, 14));
        labelTitolo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        pannelloHeader.add(labelTitolo, BorderLayout.WEST);
        pannelloHeader.add(labelData, BorderLayout.EAST);
        
        this.centralePanel = new JPanel();
        this.centralePanel.setLayout(new BorderLayout());
        this.corpoLabel = new JLabel();
        this.corpoLabel.setForeground(new Color(124, 124, 124));
        this.oggettoLabel = new JLabel();
        this.prioritaLabel = new JLabel();
        this.centralePanel.add(this.oggettoLabel, BorderLayout.WEST);
        this.centralePanel.add(this.prioritaLabel, BorderLayout.EAST);
        this.centralePanel.add(this.corpoLabel, BorderLayout.SOUTH);
        
        this.pannelloEmail.add(this.pannelloHeader, BorderLayout.PAGE_START);
        this.pannelloEmail.add(this.centralePanel, BorderLayout.CENTER);
        
        this.oggettoLabel.setOpaque(true);
        this.corpoLabel.setOpaque(true);
        this.centralePanel.setOpaque(true);
        this.labelTitolo.setOpaque(true);
        this.labelData.setOpaque(true);
        this.pannelloHeader.setOpaque(true);
        this.pannelloEmail.setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {
        
        Email email = (Email)value;
        
        this.labelTitolo.setText(email.getMittente().getNome() + " " + email.getMittente().getCognome());
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.labelData.setText(formatter.format(email.getData()));
        
        oggettoLabel.setText(formattaOggetto(email.getOggetto()));
        this.prioritaLabel.setText("Priorità: " + email.getPriorita());
        corpoLabel.setText("<html>" + formattaCorpo(email.getCorpo()) + "</html>");
        
        if (list instanceof ClientGUI.ListaInviateRicevute) {
            if (((ClientGUI.ListaInviateRicevute)list).getTipoLista().equals(ClientGUI.ListaInviateRicevute.LISTA_INVIATE)) {
                if (selected) {
                    this.pannelloEmail.setBackground(Color.lightGray);
                    this.pannelloHeader.setBackground(Color.lightGray);
                    this.centralePanel.setBackground(Color.lightGray);
                    this.labelTitolo.setBackground(Color.lightGray);
                    this.labelData.setBackground(Color.lightGray);
                    this.oggettoLabel.setBackground(Color.lightGray);
                    this.corpoLabel.setBackground(Color.lightGray);
                } else {
                    this.pannelloEmail.setBackground(Color.white);
                    this.pannelloHeader.setBackground(Color.white);
                    this.centralePanel.setBackground(Color.white);
                    this.labelTitolo.setBackground(Color.white);
                    this.labelData.setBackground(Color.white);
                    this.oggettoLabel.setBackground(Color.white);
                    this.corpoLabel.setBackground(Color.white);
                }
            } else {
                if (selected) {
                    this.pannelloEmail.setBackground(Color.lightGray);
                    this.pannelloHeader.setBackground(Color.lightGray);
                    this.centralePanel.setBackground(Color.lightGray);
                    this.labelTitolo.setBackground(Color.lightGray);
                    this.labelData.setBackground(Color.lightGray);
                    this.oggettoLabel.setBackground(Color.lightGray);
                    this.corpoLabel.setBackground(Color.lightGray);
                } else if(email.getLetto() == 1) {
                    this.pannelloEmail.setBackground(Color.white);
                    this.pannelloHeader.setBackground(Color.white);
                    this.centralePanel.setBackground(Color.white);
                    this.labelTitolo.setBackground(Color.white);
                    this.labelData.setBackground(Color.white);
                    this.oggettoLabel.setBackground(Color.white);
                    this.corpoLabel.setBackground(Color.white);
                } else if (email.getLetto() == 0) {
                    this.pannelloEmail.setBackground(Color.yellow);
                    this.pannelloHeader.setBackground(Color.yellow);
                    this.centralePanel.setBackground(Color.yellow);
                    this.labelTitolo.setBackground(Color.yellow);
                    this.labelData.setBackground(Color.yellow);
                    this.oggettoLabel.setBackground(Color.yellow);
                    this.corpoLabel.setBackground(Color.yellow);
                }
            }
        }
        
        return this.pannelloEmail;
    }
    
    private String formattaCorpo (String corpo) {
        String stringaFormattata = "";
        for (int i = 0; i < 30 && i < corpo.length(); i++) {
            stringaFormattata += corpo.charAt(i);
        }
        stringaFormattata = stringaFormattata.trim();
        if (corpo.length() > 30) {
            stringaFormattata += "...";
        }
        return stringaFormattata;
    }
    
    private String formattaOggetto (String oggetto) {
        String stringaFormattata = "";
        for (int i = 0; i < 17 && i < oggetto.length(); i++) {
            stringaFormattata += oggetto.charAt(i);
        }
        stringaFormattata = stringaFormattata.trim();
        if(oggetto.length() > 17) {
            stringaFormattata += "...";
        }
        return stringaFormattata;
    }
    
}
