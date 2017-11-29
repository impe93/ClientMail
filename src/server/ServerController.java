/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author alessio
 */
public class ServerController implements ActionListener{
    private final ServerImplementation model;
    
    public ServerController(ServerImplementation model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component button = (Component)e.getSource();
        switch (button.getName()) {
            case "chiudi": {
                MyQuitDialog chiudi = new MyQuitDialog();
                break;
            }
            default: {
                break;
            }
        }
    }
    
}
