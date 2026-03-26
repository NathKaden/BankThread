package banque.model;

public class Compte {
    private final int numero;
    private double solde;

    public Compte(int numero, double soldeInitial) {
        this.numero = numero;
        this.solde = soldeInitial;
    }

    public synchronized void deposit(double montant) {
        // Vérification du montant de dépôt
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant de dépôt invalide : " + montant + "€");
        }
        solde += montant;
        System.out.println("Le client : " + Thread.currentThread().getName() + " a dépôser " + montant + " € sur le compte n°" + numero + " réussi (nouveau solde : " + solde + ")");
    }

    public synchronized boolean retirer(double montant) {
        // Vérification du montant de retrait
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant de retrait invalide : " + montant + "€");
        }
        if (solde >= montant) {
            solde -= montant;
            System.out.println("Le client : " + Thread.currentThread().getName() + " a retiré " + montant + " € sur le compte n°" + numero + ", réussi (nouveau solde : " + solde + " €)");
            return true; 
            } else {
                System.out.println("Le client : " + Thread.currentThread().getName() + " a essayé retirer " + montant + " €, échoué sur le compte n°" + numero + " (solde insuffisant) :" + solde + " €)");
                return false;
            }
    }

    public synchronized double getNumero() { return numero; }

    public synchronized double getSolde() { return solde; }
}