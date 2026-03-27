package banque.model;

public class Compte {
    private final int numero;
    private double solde;

    public Compte(int numero, double soldeInitial) {
        if (soldeInitial < 0) throw new IllegalArgumentException("Solde initial négatif");
        this.numero = numero;
        this.solde = soldeInitial;
    }

    public synchronized void deposer(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide : " + montant);
        solde += montant;
    }

    public synchronized boolean retirer(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide : " + montant);
        if (solde >= montant) {
            solde -= montant;
            return true;
        }
        return false;
    }

    public synchronized double getSolde() {
        return solde;
    }

    public int getNumero() {
        return numero;
    }
}
