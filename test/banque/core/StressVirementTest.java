package banque.core;

import banque.model.Compte;
import banque.monitoring.Historique;
import banque.service.Virement;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class StressVirementTest {

    /**
     * Stress test anti-deadlock : virements croisés en sens opposés en même temps.
     * A → B et B → A simultanément, avec 50 threads.
     * Si le verrou ordonné est cassé → deadlock → timeout.
     */
    @Test
    void stressVirement_pasDeDdeadlock() throws InterruptedException {
        Compte compteA = new Compte(1, 100_000, null);
        Compte compteB = new Compte(2, 100_000, null);
        Historique historique = new Historique();
        int NB_THREADS = 50;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(NB_THREADS);

        ExecutorService pool = Executors.newFixedThreadPool(NB_THREADS);
        for (int i = 0; i < NB_THREADS; i++) {
            final boolean sens = (i % 2 == 0);
            pool.submit(() -> {
                try {
                    start.await();
                    // Alternance A→B et B→A pour maximiser la contention
                    if (sens) Virement.transferer(compteA, compteB, 100, historique);
                    else      Virement.transferer(compteB, compteA, 100, historique);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        boolean finished = done.await(5, TimeUnit.SECONDS);
        pool.shutdownNow();

        assertTrue(finished, "DEADLOCK détecté : les threads ne se sont pas terminés !");
    }

    /**
     * Stress test d'intégrité : la somme totale des soldes doit être
     * constante avant et après tous les virements.
     */
    @Test
    void stressVirement_integriteSolde() throws InterruptedException {
        int NB_COMPTES = 5;
        int NB_THREADS = 100;
        int NB_ITERATIONS = 200;
        double SOLDE_INITIAL = 10_000;

        Compte[] comptes = new Compte[NB_COMPTES];
        for (int i = 0; i < NB_COMPTES; i++) comptes[i] = new Compte(i, SOLDE_INITIAL, null);

        double totalAvant = 0;
        for (Compte c : comptes) totalAvant += c.getSolde();

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(NB_THREADS);
        AtomicInteger erreurs = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(NB_THREADS);
        for (int i = 0; i < NB_THREADS; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    ThreadLocalRandom rng = ThreadLocalRandom.current();
                    for (int j = 0; j < NB_ITERATIONS; j++) {
                        int a = rng.nextInt(NB_COMPTES);
                        int b;
                        do { b = rng.nextInt(NB_COMPTES); } while (b == a);
                        try {
                            Virement.transferer(comptes[a], comptes[b], 10, null);
                        } catch (IllegalArgumentException ignored) {}
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(15, TimeUnit.SECONDS), "Timeout — possible deadlock");
        pool.shutdown();

        double totalApres = 0;
        for (Compte c : comptes) totalApres += c.getSolde();

        System.out.println("Total avant : " + totalAvant + " | Total après : " + totalApres);
        assertEquals(totalAvant, totalApres, 0.001,
                "Intégrité violée : de l'argent a été créé ou perdu !");
    }
}