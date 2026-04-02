package banque.core;

import banque.model.Client;
import banque.queue.FileClients;
import banque.service.Guichet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class StressGuichetTest {

    @Test
    void stressGuichets_pasDeDoubleConso() throws InterruptedException {
        int NB_GUICHETS = 5;
        int NB_CLIENTS = 50;

        CountDownLatch done = new CountDownLatch(NB_CLIENTS);
        FileClients file = new FileClients(NB_CLIENTS);

        for (int i = 0; i < NB_CLIENTS; i++) {
            file.ajouterClient(new Client(i, done::countDown)); // le Runnable décrémente le latch
        }

        ExecutorService pool = Executors.newFixedThreadPool(NB_GUICHETS);
        for (int i = 0; i < NB_GUICHETS; i++) {
            pool.submit(new Guichet(i, file));
        }

        assertTrue(done.await(30, TimeUnit.SECONDS), "Tous les clients n'ont pas été servis !");
        pool.shutdownNow();

        assertEquals(0, file.taille(), "Des clients sont restés dans la file !");
    }

    @Test
    void stressGuichets_throughput() throws InterruptedException {
        int NB_GUICHETS = 5;
        int NB_CLIENTS = NB_GUICHETS * 2; // 10 clients → 2 rounds × 2s = ~4s par guichet
        AtomicInteger servis = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(NB_CLIENTS);

        FileClients file = new FileClients(NB_CLIENTS);
        for (int i = 0; i < NB_CLIENTS; i++) {
            file.ajouterClient(new Client(i, () -> {
                servis.incrementAndGet();
                done.countDown();
            }));
        }

        long debut = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(NB_GUICHETS);
        for (int i = 0; i < NB_GUICHETS; i++) {
            pool.submit(new Guichet(i, file));
        }

        assertTrue(done.await(15, TimeUnit.SECONDS), "Timeout guichets"); // 15s > 4s
        long duree = System.currentTimeMillis() - debut;
        pool.shutdownNow();

        System.out.println("Clients servis : " + servis.get() + " en " + duree + " ms");
        System.out.println("Throughput : " + (servis.get() * 1000 / duree) + " clients/sec");
        assertEquals(NB_CLIENTS, servis.get());
    }
}