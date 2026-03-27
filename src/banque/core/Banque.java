package banque.core;

import banque.queue.FileClients;
import banque.service.Guichet;
import banque.model.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Banque {
    private final FileClients fileClients;
    private final ExecutorService poolGuichets;
    private final int nombreGuichets;

    public Banque(int nombreGuichets, int capaciteMaxFile) {
        this.nombreGuichets = nombreGuichets;
        this.fileClients = new FileClients(capaciteMaxFile);

        this.poolGuichets = Executors.newFixedThreadPool(nombreGuichets);
    }

    public void demarrer() {
        System.out.println("Banque démarrée avec " + nombreGuichets + " guichets.");
        for (int i = 1; i <= nombreGuichets; i++) {
            poolGuichets.submit(new Guichet(i, fileClients));
        }
    }

    public void accueillirClient(Client client) {
        boolean accepte = fileClients.ajouterClient(client);
        if (!accepte) {
            System.err.println("File pleine ! Client " + client.getId() + " refusé à l'entrée.");
        } else {
            System.out.println("Client " + client.getId() + " est entré dans la file d'attente (Taille actuelle : " + fileClients.getTaille() + ").");
        }
    }

    public void fermerProprement() {
        System.out.println("Fermeture de la banque...");
        poolGuichets.shutdownNow();
        try {
            if (!poolGuichets.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Fermeture forcée");
            }
        } catch (InterruptedException e) {
            poolGuichets.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("La banque est fermée");
    }
}