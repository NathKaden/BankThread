package banque;

import banque.model.Client;
import banque.model.Compte;
import banque.queue.FileClients;
import banque.service.Guichet;
import banque.monitoring.SurveillanceThread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUs5 {

    public static void main(String[] args) {

        FileClients file = new FileClients(10);

        Compte c1 = new Compte(1, 100);
        Compte c2 = new Compte(2, 200);

        List<Compte> comptes = Arrays.asList(c1, c2);

        // Pool de guichets
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(new Guichet(1, file));
        pool.execute(new Guichet(2, file));

        // Monitoring
        SurveillanceThread monitoring = new SurveillanceThread(comptes, file);
        monitoring.demarrer();

        for (int i = 1; i <= 10; i++) {
            int id = i;

            Client client = new Client(id, () -> {
                if (id % 2 == 0) {
                    c1.deposer(50);
                } else {
                    c2.retirer(30);
                }
            });

            file.ajouterClient(client);
        }

        try {
            //simulation de la banque qui reste ouvert 12s
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Arrêt propre
        monitoring.arreter();
        pool.shutdownNow();

        System.out.println("fin du programme");
    }
}