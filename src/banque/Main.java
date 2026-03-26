package banque;

import banque.core.Banque;


import banque.model.Compte;
import banque.core.Banque;

public class Main {

    public static void main(String[] args) {

        Compte c1 = new Compte(1, 10);
        Compte c2 = new Compte(2, 1000);

        //  A → B
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Banque.virer(c1, c2, 10);
            }
        });

        //  B → A
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Banque.virer(c2, c1, 10);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Solde final c1 = " + c1.getSolde());
        System.out.println("Solde final c2 = " + c2.getSolde());
    }
}