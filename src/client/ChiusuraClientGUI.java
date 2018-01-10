/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class ChiusuraClientGUI extends JFrame{
    
    private ClientController controller;
    
    private JLabel messaggioChiusura;
    
    private JPanel panelBottoni;
    private JButton bottoneSi;
    private JButton bottoneNo;
    
    public ChiusuraClientGUI(ClientController controller) {
        this.controller = controller;
        initGUI();
    }
    
    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.messaggioChiusura = new JLabel("Sei sicuro di voler chiudere l'applicazione?", SwingConstants.CENTER);
        
        this.panelBottoni = new JPanel();
        this.bottoneSi = new JButton("Si");
        this.bottoneNo = new JButton("No");
        this.panelBottoni.add(this.bottoneSi);
        this.panelBottoni.add(this.bottoneNo);
        
        this.add(this.messaggioChiusura, BorderLayout.CENTER);
        this.add(this.panelBottoni, BorderLayout.SOUTH);
    }
}
