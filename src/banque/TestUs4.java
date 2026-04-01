package banque;

import banque.model.Compte;
import banque.service.Virement;

public class TestUs4 {

    public static void main(String[] args) {

        Compte clientJuliano = new Compte(1, 100);
        Compte clientNadia = new Compte(2, 100);

        // Virement juliano vers nadia
        Thread t1 = new Thread(() -> {
            Virement.transferer(clientJuliano, clientNadia, 50);
        }, "Juliano");

        //l'inverse
        Thread t2 = new Thread(() -> {
            Virement.transferer(clientNadia, clientJuliano, 30);
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