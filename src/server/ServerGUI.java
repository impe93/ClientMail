package server;

import java.awt.Dimension;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ServerGUI {
    
    public ServerGUI(){
    
        JFrame frame = new JFrame("Server");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 200));
        frame.add(panel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        
    }
    
    public static void main(String[] args){
        
        ServerGUI gui = new ServerGUI();
    
    }
}
