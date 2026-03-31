package banque.model;

public class Compte {
    private final int numero;
    private double solde;

    public Compte(int numero, double soldeInitial) {
        this.numero = numero;
        this.solde = soldeInitial;
    }

    public synchronized void deposer(double montant) {
        solde += montant;
        // Vérification du montant de dépôt
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant de dépôt invalide : " + montant + " € . Le montant doit être supérieur a 0.");
        }
        solde += montant;
        System.out.println("Le client : " + Thread.currentThread().getName() + " a dépôsé " + montant + " € sur le compte n°" + numero + ", le nouveau solde est " + solde + " €");
    }

    public synchronized boolean retirer(double montant) {
        // Vérification du montant de retrait
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant de retrait invalide : " + montant + " € . Le montant doit être supérieur a 0.");
        }
        if (solde >= montant) {
            solde -= montant;
            System.out.println("Le client : " + Thread.currentThread().getName() + " a retiré " + montant + " € sur le compte n°" + numero + ", le nouveau solde est " + solde + " €)");
            return true;
        } else {
            System.out.println("Le client : " + Thread.currentThread().getName() + " a essayé de retirer " + montant + " €, sur le compte n°" + numero + ", le solde est " + solde + " €)");
            return false;
        }
    }

    public synchronized double getSolde() { return solde; }

    public synchronized double getNumero() { return numero; }
}