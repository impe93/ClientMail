package modelli;

/**
 *
 * @author Alessio Berger, Lorenzo Imperatrice, Francesca Riddone
 */
public interface CasellaPostaElettronica {
    void inserisciInInviati(Email email);
    void inserisciInRicevuti(Email email);
    void eliminaPerMittente(Email email);
    void eliminaPerDestinatario(Email email);
}
