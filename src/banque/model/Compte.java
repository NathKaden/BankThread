package banque.model;

public class Compte {
    private final int numero;
    private double solde;

    public Compte(int numero, double soldeInitial) {
        this.numero = numero;
        this.solde = soldeInitial;
    }

    public synchronized void deposer(double montant) {
        solde += montant;   // TODO compléter
    }

    public synchronized boolean retirer(double montant) {
        if (solde >= montant) { solde -= montant; return true; }
        return false;
    }

    public synchronized double getSolde() { return solde; }
}