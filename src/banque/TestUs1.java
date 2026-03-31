package banque;

import banque.model.Compte;

public class TestUs1 {
    public static void main(String[] args) {
        Compte compteBancaire  = new Compte(123456, 100);

        // Client 1 retire 70€
        Thread client1 = new Thread(() -> {
            compteBancaire.retirer(70);
        }, "Client1");

        // Client 2 tente aussi de retirer 70€
        Thread client2 = new Thread(() -> {
            compteBancaire.retirer(70);
        }, "Client2");

        //Client 3 dépose 50€
        Thread client3 = new Thread(() -> {
            compteBancaire.deposer(50);
        }, "Client3");

        //Client 4 essai dépose 0€
        Thread client4 = new Thread(() -> {
            compteBancaire.deposer(0);
        }, "Client4");

        client1.start();
        client2.start();
        client3.start();
        client4.start();

        try {
            client1.join();
            client2.join();
            client3.join();
            client4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrompu");
            e.printStackTrace();
        }

        System.out.println("Le solde final du compte est " + compteBancaire.getSolde() + " €");
    }
}