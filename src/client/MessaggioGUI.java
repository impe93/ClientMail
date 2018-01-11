package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author Lorenzo Imperatrice, Francesca Riddone, Alessio Berger
 */
public class MessaggioGUI extends JFrame {
    
    private String messaggio;
    
    private final JFrame questoFrame;
    
    private JLabel messaggioCentrale;
    private JButton bottoneChiusura;
    
    public MessaggioGUI(String nome, String messaggio) {
        super(nome);
        this.messaggio = messaggio;
        questoFrame = this;
        initGUI();
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
    
    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.messaggioCentrale = new JLabel(this.messaggio, JLabel.CENTER);
        
        this.bottoneChiusura = new JButton("Ok");
        this.bottoneChiusura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questoFrame.setVisible(false);
                dispose();
            }
        });
        
        this.add(this.messaggioCentrale, BorderLayout.CENTER);
        this.add(this.bottoneChiusura, BorderLayout.SOUTH);
        
        this.setPreferredSize(new Dimension(500, 130));
        this.pack();
        this.setVisible(true);
    }
    
}
