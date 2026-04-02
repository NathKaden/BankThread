package banque;

import banque.model.Client;
import banque.model.Compte;
import banque.queue.FileClients;
import banque.service.DAB;
import banque.service.Guichet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUs7 {

    public static void main(String[] args) {

        FileClients file = new FileClients(10);

        Compte compte = new Compte(1, 500);

        // 2 distributeur de disponible
        DAB dab = new DAB(2);

        // les guichets
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(new Guichet(1, file));
        pool.execute(new Guichet(2, file));

        // ajouts des clients qui utilisent DAB
        for (int i = 1; i <= 6; i++) {

            int id = i;

            Client client = new Client(id, () -> {
                if (id % 2 == 0) {
                    System.out.println("Client " + id + " fait un dépôt");
                    compte.deposer(50);
                } else {
                    System.out.println("Client " + id + " fait un retrait");
                    compte.retirer(30);
                }
            });

            file.ajouterClient(client);
        }

        //simulation
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pool.shutdownNow();

        System.out.println("Solde final = " + compte.getSolde() + " €");
    }
}