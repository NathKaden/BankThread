package banque;

import banque.model.Compte;
import banque.monitoring.AlerteDecouvert;

import java.util.Arrays;
import java.util.List;

public class TestUs6 {

    public static void main(String[] args) {

        Compte clientJuliano = new Compte(1, 100);
        Compte clientJulien = new Compte(2, 50);

        List<Compte> comptes = Arrays.asList(clientJuliano, clientJulien);

        // lancer surveillance
        AlerteDecouvert alerte = new AlerteDecouvert(comptes);
        alerte.demarrer();

        // simulation clients
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                clientJuliano.retirer(120); // passe en négatif
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                clientJulien.retirer(100); // passe en négatif
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(4000);
                clientJulien.deposer(1000); // passe en négatif
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        alerte.arreter();
        System.out.println("Fin du test");
    }
}