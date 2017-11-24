/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class ClientModel {
    
    private String url = "jdbc:mysql://localhost:3306/";
    private static String user = "root";
    private static String password = "root";
    
    public ClientModel(String emailUtente){
        this.url = url + emailUtente;
        
    }
    
}
