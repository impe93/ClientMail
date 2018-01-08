package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
        
        
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
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
