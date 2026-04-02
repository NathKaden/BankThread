package banque.monitoring;

import banque.model.Compte;

import java.util.List;

public class AlerteDecouvert {

    private final List<Compte> comptes;
    private volatile boolean running = true;

    public AlerteDecouvert(List<Compte> comptes) {
        this.comptes = comptes;
    }

    public void demarrer() {
        for (Compte compte : comptes) {
            Thread t = new Thread(() -> surveiller(compte));
            t.setDaemon(true);
            t.start();
        }
    }

    public void arreter() {
        running = false;
    }

    private void surveiller(Compte compte) {

        System.out.println("Surveillance active pour compte n°" + compte.getNumero());

        while (running) {
            synchronized (compte) {
                try {
                    while (compte.getSolde() >= 0) {
                        compte.wait();
                    }

                    System.out.println("Alerte le compte n°" + compte.getNumero() + " à découvert, le solde est de " + compte.getSolde() + " €");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}