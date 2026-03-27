package banque.queue;

import banque.model.Client;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileClients {
    private final BlockingQueue<Client> queue;

    public FileClients(int capaciteMax) {
        this.queue = new LinkedBlockingQueue<>(capaciteMax);
    }

    public boolean ajouterClient(Client client) {
        return queue.offer(client);
    }

    public Client prendreClient() throws InterruptedException {
        return queue.take();
    }

    public int getTaille() {
        return queue.size();
    }
}