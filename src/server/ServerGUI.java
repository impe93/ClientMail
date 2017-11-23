package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerGUI {
    
    public ServerGUI(){
    
        JFrame frame = new JFrame("Server");
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(800, 400));
        Border bordoTrenta = BorderFactory.createEmptyBorder(30, 30, 30, 30);
        panel.setBorder(bordoTrenta);
        frame.add(panel);
     
        JLabel lblIntestazione = new JLabel("Log delle operazioni:");
        Border bordoDieci = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        lblIntestazione.setBorder(bordoDieci);
        panel.add(lblIntestazione, BorderLayout.PAGE_START);
        lblIntestazione.setHorizontalAlignment( JLabel.CENTER );
        
        JTextArea log = new JTextArea();
        log.setLineWrap(true);
        log.setBorder(bordoDieci);
        JScrollPane sp = new JScrollPane(log);
        
        panel.add(sp, BorderLayout.CENTER);
        
        JPanel footer = new JPanel();
        footer.setBorder(bordoDieci);
        JButton chiudi = new JButton("Chiudi il server");
        footer.add(chiudi);
        frame.add(footer, BorderLayout.PAGE_END);
        
        
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args){
        
        ServerGUI gui = new ServerGUI();
    
    }
}
