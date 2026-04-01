package banque.monitoring;

import banque.model.Compte;
import banque.queue.FileClients;

import java.util.List;

public class SurveillanceThread {

    private final List<Compte> comptes;
    private final FileClients fileClients;
    private volatile boolean running = true;

    public SurveillanceThread(List<Compte> comptes, FileClients fileClients) {
        this.comptes = comptes;
        this.fileClients = fileClients;
    }

    public void demarrer() {
        Thread t = new Thread(this::afficher);
        t.setDaemon(true);
        t.start();
    }

    public void arreter() {
        running = false;
    }

    private void afficher() {

        System.out.println("Monitoring démarré");

        while (running) {
            try {
                System.out.println("\n===== ETAT =====");

                for (Compte c : comptes) {
                    System.out.println("Compte n°" + c.getNumero() + "  " + c.getSolde() + " €");
                }

                System.out.println("File clients : " + fileClients.taille());

                System.out.println("==========");

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Monitoring arrêté");
    }
}