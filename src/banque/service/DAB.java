package banque.service;

import banque.model.Compte;

import java.util.concurrent.Semaphore;

public class DAB {

    private final Semaphore semaphore;

    public DAB(int nombreDAB) {
        this.semaphore = new Semaphore(nombreDAB);
    }

    public void retirer(Compte compte, double montant) {

        try {
            System.out.println(Thread.currentThread().getName() + " attend un DAB...");

            //prend un DAB (bloque si aucun dispo)
            semaphore.acquire();

            System.out.println(Thread.currentThread().getName() + " utilise un DAB");

            //opération
            compte.retirer(montant);

            //simulation de l'utilisation du DAB
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            //libère le DAB
            semaphore.release();

            System.out.println(Thread.currentThread().getName() + " libère le DAB");
        }
    }
}