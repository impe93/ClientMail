package modelli;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public class Utente {
    private final String nome;
    private final String cognome;
    private final String email;
    
    public Utente(String nome, String cognome, String email){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    public Utente(){
        this.nome = null;
        this.cognome = null;
        this.email = null;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public String getCognome(){
        return this.cognome;
    }
    
    public String getEmail(){
        return this.email;
    }
}
