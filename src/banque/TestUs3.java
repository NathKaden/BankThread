package banque;

import banque.model.Client;
import banque.model.Compte;
import banque.queue.FileClients;
import banque.service.Guichet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUs3 {

    public static void main(String[] args) {

        // File
        FileClients file = new FileClients(5);

        Compte compte = new Compte(123456, 200, null);

        //Pool de 3 guichets
        int nbGuichets = 3;
        ExecutorService pool = Executors.newFixedThreadPool(nbGuichets);


        for (int i = 1; i <= nbGuichets; i++) {
            pool.execute(new Guichet(i, file));
        }

        //Ajout de clients
        for (int i = 1; i <= 10; i++) {

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

            if (file.ajouterClient(client)) {
                System.out.println("Client " + id + " ajouté à la file");
            } else {
                System.out.println("Client " + id + " refusé (file pleine)");
            }
        }

        // simulation
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Fermeture de la banque...");
        pool.shutdownNow();
        System.out.println("Solde final = " + compte.getSolde() + " €");
    }
}