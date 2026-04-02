package banque.core;

import banque.model.Client;
import banque.queue.FileClients;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class StressFileClientsTest {

    @Test
    void stressAjouterClient_sousCharge() throws InterruptedException {
        int NB_THREADS = 50;
        int CAPACITE = 30;
        FileClients file = new FileClients(CAPACITE);
        AtomicInteger acceptes = new AtomicInteger(0);
        AtomicInteger refuses  = new AtomicInteger(0);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(NB_THREADS);

        ExecutorService pool = Executors.newFixedThreadPool(NB_THREADS);
        for (int i = 0; i < NB_THREADS; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    start.await();
                    boolean ok = file.ajouterClient(new Client(id, () -> {}));
                    if (ok) acceptes.incrementAndGet();
                    else    refuses.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS), "Timeout — possible blocage");
        pool.shutdown();

        assertEquals(NB_THREADS, acceptes.get() + refuses.get());
        assertTrue(file.getTaille() <= CAPACITE);
        System.out.println("Acceptés : " + acceptes.get() + " | Refusés : " + refuses.get());
    }

    @Test
    void stressProducerConsumer_sousCharge() throws InterruptedException {
        int NB_PRODUCERS = 20;
        int NB_CONSUMERS = 10;
        int CLIENTS_PAR_PRODUCER = 50;
        int TOTAL = NB_PRODUCERS * CLIENTS_PAR_PRODUCER; // 1000

        FileClients file = new FileClients(TOTAL); // capacité = TOTAL, plus aucun refus possible
        AtomicInteger produits  = new AtomicInteger(0);
        AtomicInteger consommes = new AtomicInteger(0);

        CountDownLatch start    = new CountDownLatch(1);
        CountDownLatch doneProd = new CountDownLatch(NB_PRODUCERS);
        CountDownLatch doneCons = new CountDownLatch(TOTAL);

        ExecutorService pool = Executors.newCachedThreadPool();

        // Producteurs
        for (int i = 0; i < NB_PRODUCERS; i++) {
            final int base = i * CLIENTS_PAR_PRODUCER;
            pool.submit(() -> {
                try {
                    start.await();
                    for (int j = 0; j < CLIENTS_PAR_PRODUCER; j++) {
                        file.ajouterClient(new Client(base + j, () -> {}));
                        produits.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneProd.countDown();
                }
            });
        }

        // Consommateurs
        for (int i = 0; i < NB_CONSUMERS; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    while (true) {
                        file.prendreClient();
                        consommes.incrementAndGet();
                        doneCons.countDown();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        start.countDown();
        assertTrue(doneCons.await(15, TimeUnit.SECONDS), "Timeout — clients non consommés");
        pool.shutdownNow();

        assertEquals(TOTAL, consommes.get(), "Des clients ont été perdus ou dupliqués !");
        System.out.println("Produits : " + produits.get() + " | Consommés : " + consommes.get());
    }
}