package banque;

import banque.model.Client;
import banque.model.Compte;
import banque.monitoring.Historique;
import banque.queue.FileClients;
import banque.service.DAB;
import banque.service.Guichet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUs8 {

    public static void main(String[] args) {

        // creer un historique
        Historique historique = new Historique();

        // création du compte avec l'historique
        Compte compte = new Compte(1, 500, historique);

        //file de clients
        FileClients file = new FileClients(20);

        DAB dab = new DAB(2);

        //création des pools de guichets
        ExecutorService poolGuichets = Executors.newFixedThreadPool(2);
        poolGuichets.execute(new Guichet(1, file));
        poolGuichets.execute(new Guichet(2, file));

        // ajout des clients utilisant DAB et dépôts
        for (int i = 1; i <= 6; i++) {
            int id = i;

            // Client retrait via DAB
            Client clientDAB = new Client(id, () -> dab.retirer(compte, 50));
            file.ajouterClient(clientDAB);

            //client depot concurrent
            Client clientDepot = new Client(id + 100, () -> compte.deposer(30));
            file.ajouterClient(clientDepot);
        }

        //simulation
        try {
            Thread.sleep(8000); // Laisser le temps à tous les clients de passer
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // arret des guichets
        poolGuichets.shutdownNow();

        // afficher le solde final
        System.out.println("\n=== Solde final du compte ===");
        System.out.println("Compte n°" + compte.getNumero() + " : " + compte.getSolde() + " €");

        //affichage complet de l'historique
        System.out.println("\n=== Historique complet des opérations ===");
        historique.afficher();

        // exemple de filtre : opérations pour ce compte seulement
        System.out.println("\n=== Filtrage par compte (n°1) ===");
        historique.filtrerParCompte(1).forEach(System.out::println);

        // exemple de filtre : retraits seulement
        System.out.println("\n=== Filtrage par type : RETRAIT ===");
        historique.filtrerParType("RETRAIT").forEach(System.out::println);

        // exemple de filtre : dépôts seulement
        System.out.println("\n=== Filtrage par type : DEPOT ===");
        historique.filtrerParType("DEPOT").forEach(System.out::println);
    }
}