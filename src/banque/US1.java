package banque;

import banque.model.Compte;

public class US1 {
    public static void main(String[] args) {
        System.out.println("Projet OK");

        Compte Compte_Principal = new Compte(1, 1000);
        //Compte Test2 = new Compte(2, 500);

        Thread enfant = new Thread(() -> {
            Compte_Principal.deposer(200);
        }, "Enfant");

        Thread maman = new Thread(() -> {
            Compte_Principal.retirer(2000);
        }, "Maman");

        Thread papa = new Thread(() -> {
            Compte_Principal.retirer(100);
            Compte_Principal.deposer(50);
        }, "Papa");

        enfant.start();
        maman.start();
        papa.start();

        try {
            enfant.join();
            maman.join();
            papa.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }   

        System.out.println("Solde final du compte 1 : " + Compte_Principal.getSolde() + "€");
        //System.out.println("Solde final du compte 2 : " + Test2.getSolde() + "€");
    }
}