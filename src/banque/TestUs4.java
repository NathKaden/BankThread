package banque;

import banque.model.Compte;
import banque.monitoring.Historique;
import banque.service.Virement;

public class TestUs4 {

    public static void main(String[] args) {

        Compte clientJuliano = new Compte(1, 100, null);
        Compte clientNadia = new Compte(2, 100, null);

        //historique partagé
        Historique historique = new Historique();

        // Virement juliano vers nadia
        // ajout de l'historique pour l'us8
        Thread t1 = new Thread(() -> {
            Virement.transferer(clientJuliano, clientNadia, 50,historique);
        }, "Juliano");

        //l'inverse
        Thread t2 = new Thread(() -> {
            Virement.transferer(clientNadia, clientJuliano, 30,historique);
        }, "Nadia");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Solde final du client Juliano est de " + clientJuliano.getSolde() + "€");
        System.out.println("Solde final de la cliante Nadia est de " + clientNadia.getSolde()+ "€");
    }
}