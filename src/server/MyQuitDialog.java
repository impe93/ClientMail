/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javax.swing.JOptionPane;

/**
 *
 * @author alessio
 */
public class MyQuitDialog extends JOptionPane{
    int risposta;
    
    public void MyQuitDialog(){
    
    this.risposta = JOptionPane.showOptionDialog(null,
                    "Sei sicuro di voler chiudere il server?",
                    "Attenzione",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,null,null);

   if (risposta == JOptionPane.YES_OPTION) {
                    // option 1
    } else if (risposta == JOptionPane.NO_OPTION) {
                    // option 2
    }
   }
}
