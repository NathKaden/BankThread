
package banque.service;

import banque.model.Compte;
import banque.monitoring.Historique;
import banque.monitoring.Operation;

public class Virement {

    public static boolean transferer(Compte source, Compte destination, double montant, Historique historique) {

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

                // operation auto
                source.retirer(montant);
                destination.deposer(montant);

                // ajouter dans l'historique
                if (historique != null) {
                    historique.ajouter(new Operation(
                            "VIREMENT",
                            (int) source.getNumero(),
                            (int) destination.getNumero(),
                            montant
                    ));
                }

                System.out.println("Virement de " + montant + "€ du compte n°" + source.getNumero() + " vers compte n°" + destination.getNumero());

                return true;
            }
        }
    }
}