package banque;

import banque.model.Client;
import banque.model.Compte;
import banque.queue.FileClients;

public class TestUs2 {

    public static void main(String[] args) {

        // File FIFO encapsulée
        FileClients file = new FileClients(3);

        Compte compte = new Compte(123456, 100);

        // Création des clients
        Client cli1 = new Client(1, () -> compte.retirer(30));
        Client cli2 = new Client(2, () -> compte.retirer(30));
        Client cli3 = new Client(3, () -> compte.retirer(30));
        Client cli4 = new Client(4, () -> compte.retirer(30)); // peut être refusé

        //Thread qui ajoute les clients
        Thread arriveeClients = new Thread(() -> {

            ajouterClient(file, cli1);
            ajouterClient(file, cli2);
            ajouterClient(file, cli3);
            // le refus si pleine
            ajouterClient(file, cli4);

        });

        // les guichets
        Thread guichet1 = new Thread(() -> {
            while (true) {
                try {
                    Client client = file.prendreClient();
                    System.out.println("Guichet 1 traite client " + client.getId());
                    client.executerOperation();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread guichet2 = new Thread(() -> {
            while (true) {
                try {
                    Client client = file.prendreClient();
                    System.out.println("Guichet 2 traite client " + client.getId());
                    client.executerOperation();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        arriveeClients.start();
        guichet1.start();
        guichet2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        guichet1.interrupt();
        guichet2.interrupt();

        System.out.println("Test terminé. Solde final = " + compte.getSolde() + " €");
    }

    private static void ajouterClient(FileClients file, Client client) {
        if (file.ajouterClient(client)) {
            System.out.println("Client " + client.getId() + " ajouté");
        } else {
            System.out.println("Client " + client.getId() + " refusé (file pleine)");
        }
    }
}