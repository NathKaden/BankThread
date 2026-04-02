package banque.model;

import banque.monitoring.Historique;
import banque.monitoring.Operation;

public class Compte {
    private final int numero;
    private double solde;
    //ajout d'un historique partagée
    private final Historique historique;

    public Compte(int numero, double soldeInitial, Historique historique) {
        this.numero = numero;
        this.solde = soldeInitial;
        this.historique = historique;
    }

    public synchronized void deposer(double montant) {
        // Vérification du montant de dépôt
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant de dépôt invalide : " + montant + " € . Le montant doit être supérieur a 0.");
        }
        solde += montant;

        //ajouter dans l'historique
        if (historique != null) {
            historique.ajouter(new Operation("DEPOT", numero, null, montant));
        }
        System.out.println("Le client : " + Thread.currentThread().getName() + " a dépôsé " + montant + " € sur le compte n°" + numero + ", le nouveau solde est " + solde + " €");
    }

    public synchronized boolean retirer(double montant) {

        if (montant <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        //autorise le découvert retirer la condition
        solde -= montant;
        //ajouter dans l'historique
        if (historique != null){
            historique.ajouter(new Operation("RETRAIT", numero, null, montant));
        }

        System.out.println("Le client : " + Thread.currentThread().getName() + " a retiré " + montant + " € sur le compte n°" + numero + ", nouveau solde = " + solde + " €");

        //notifier si il y a un passage au negatifs
        if (solde < 0) {
            notifyAll();
        }

        return true;
    }

    public synchronized double getSolde() { return solde; }

    public synchronized double getNumero() { return numero; }
}