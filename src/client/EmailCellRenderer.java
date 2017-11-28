/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BoxLayout;
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
    
    EmailCellRenderer() {
        this.pannelloEmail = new JPanel();
        this.pannelloEmail.setLayout(new BorderLayout());
        
        this.pannelloHeader = new JPanel();
        this.pannelloHeader.setLayout(new BorderLayout());
        
        this.labelData = new JLabel();
        labelData.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        
        this.labelTitolo = new JLabel();
        labelTitolo.setFont(new Font(labelTitolo.getFont().getName(), Font.BOLD, 12));
        labelTitolo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        pannelloHeader.add(labelTitolo, BorderLayout.WEST);
        pannelloHeader.add(labelData, BorderLayout.EAST);
        
        this.centralePanel = new JPanel();
        this.centralePanel.setLayout(new BoxLayout(this.centralePanel, BoxLayout.Y_AXIS));
        this.corpoLabel = new JLabel();
        this.oggettoLabel = new JLabel();
        this.centralePanel.add(this.oggettoLabel);
        this.centralePanel.add(this.corpoLabel);
        
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
        corpoLabel.setText("<html>" + formattaCorpo(email.getCorpo()) + "</html>");
        
        
        if (selected) {
            // Background e foreground di selezionato
        } else {
            // Background e foreground di selezionato
        }
        
        return this.pannelloEmail;
    }
    
    private String formattaCorpo (String corpo) {
        String stringaFormattata = "";
        for (int i = 0; i < 50 && i < corpo.length(); i++) {
            stringaFormattata += corpo.charAt(i);
        }
        stringaFormattata = stringaFormattata.trim();
        stringaFormattata += "...";
        return stringaFormattata;
    }
    
    private String formattaOggetto (String oggetto) {
        String stringaFormattata = "";
        for (int i = 0; i < 17 && i < oggetto.length(); i++) {
            stringaFormattata += oggetto.charAt(i);
        }
        stringaFormattata = stringaFormattata.trim();
        stringaFormattata += "...";
        return stringaFormattata;
    }
    
}
