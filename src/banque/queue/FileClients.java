package banque.queue;

import banque.model.Client;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileClients {

    private final BlockingQueue<Client> file;

    public FileClients(int capacite) {
        this.file = new LinkedBlockingQueue<>(capacite);
    }

    public boolean ajouterClient(Client client) {
        //refuse si pleine
        return file.offer(client);
    }

    public Client prendreClient() throws InterruptedException {
        //bloque si vide
        return file.take();
    }

    public int getTaille() {
        return file.size();
    }
}