package banque;

import banque.core.Banque;
import banque.model.Compte;
import banque.model.Client;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Compte compteUnique = new Compte(101, 1000.0);

        Banque banque = new Banque(3, 5);
        banque.demarrer();

        for (int i = 1; i <= 7; i++) {
            final int id = i;
            Client client = new Client(id, () -> {
                boolean transaction = compteUnique.retirer(200);
                System.out.println("Transaction Client " + id + " : Retrait 200 " +
                        (transaction ? "RÉUSSI" : "REFUSÉ (solde insuffisant)") +
                        " | Solde restant = " + compteUnique.getSolde());
            });
            banque.accueillirClient(client);
            Thread.sleep(100);
        }

        Thread.sleep(2000);
        banque.fermerProprement();
    }
}