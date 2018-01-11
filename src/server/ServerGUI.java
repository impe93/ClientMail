package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.Border;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerGUI implements Observer{
    
    ServerImplementation model;
    ServerController controller = new ServerController(model);
    
    JFrame frame = new JFrame("Server");
    JPanel panel = new JPanel();
    JLabel lblIntestazione = new JLabel("Log delle operazioni:");
    JPanel footer = new JPanel();
    JButton chiudi = new JButton("Chiudi il server");
    JTextArea logArea = new JTextArea("* [SERVER START - " + (new Date()).toString() + " ]");
    
    /*
    *   Costruttore dell'interfaccia del server
    */
    public ServerGUI(ServerImplementation server){
        this.model = server;
        
        
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(1200, 400));
        Border bordoTrenta = BorderFactory.createEmptyBorder(30, 30, 30, 30);
        panel.setBorder(bordoTrenta);
        frame.add(panel);
        
        
        Border bordoDieci = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        lblIntestazione.setBorder(bordoDieci);
        panel.add(lblIntestazione, BorderLayout.PAGE_START);
        lblIntestazione.setHorizontalAlignment( JLabel.CENTER );
        
        
        logArea.setLineWrap(true);
        logArea.setBorder(bordoDieci);
        logArea.setEditable(false);
        JScrollPane sp = new JScrollPane(logArea);
        
        panel.add(sp, BorderLayout.CENTER);
        
        
        footer.setBorder(bordoDieci);
        chiudi.setName("chiudi");
        chiudi.addActionListener(controller);
        footer.add(chiudi);
        frame.add(footer, BorderLayout.PAGE_END);
        
        
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvt) {
                int risposta = JOptionPane.showConfirmDialog(null,"Confermi la "
                        + "chiusura del server?","Conferma",
                        JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                
                if(risposta == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        String logOperazione = (String) arg;
        logArea.setText(logArea.getText() + "\n" + logOperazione);
    }
    
    public static void main(String[] args) throws RemoteException{
        ServerImplementation server = new ServerImplementation();
        ServerGUI gui = new ServerGUI(server);
        server.aggiungiObserver(gui);
        
    }
}
