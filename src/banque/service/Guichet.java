package banque.service;

import banque.queue.FileClients;
import banque.model.Client;

public class Guichet implements Runnable {

    private final int id;
    private final FileClients fileClients;

    public Guichet(int id, FileClients fileClients) {
        this.id = id;
        this.fileClients = fileClients;
    }

    @Override
    public void run() {

        System.out.println("Le guichet " + id + " est ouvert.");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Client client = fileClients.prendreClient();

                System.out.println("Le guichet " + id + " sert le client " + client.getId());

                try {
                    client.executerOperation();
                } catch (RuntimeException e) {
                    System.err.println("Erreur au guichet " + id + " avec le client " + client.getId() + " : " + e.getMessage());
                }

            } catch (InterruptedException e) {
                System.out.println("Le guichet " + id + " ferme ses portes.");
                Thread.currentThread().interrupt();
            }
        }
    }
}