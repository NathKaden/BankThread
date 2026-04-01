
package banque.service;

import banque.model.Compte;

public class Virement {

    public static boolean transferer(Compte source, Compte destination, double montant) {

        // verif montant
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        //verrous pour éviter deadlock
        Compte premier;
        Compte second;

        if (System.identityHashCode(source) < System.identityHashCode(destination)) {
            premier = source;
            second = destination;
        } else {
            premier = destination;
            second = source;
        }

        synchronized (premier) {
            synchronized (second) {

                // verif solde
                if (source.getSolde() < montant) {
                    System.out.println("Virement refusé : solde insuffisant sur compte " + source.getNumero());
                    return false;
                }

                // virement auto
                source.retirer(montant);
                destination.deposer(montant);

                System.out.println("Virement de " + montant + "€ du compte n°" + source.getNumero() + " vers compte n°" + destination.getNumero());

                return true;
            }
        }
    }
}