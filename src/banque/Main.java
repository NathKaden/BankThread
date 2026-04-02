package banque;

import banque.model.Compte;
import banque.monitoring.Historique;
import banque.service.Virement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    //C'est l'us9

    public static void main(String[] args) throws InterruptedException {


        int NB_CLIENTS = 50;
        int NB_COMPTES = 5;

        Random random = new Random();

        //historique partagé entre tous les threads
        Historique historique = new Historique();

        //Création des comptes avec un solde init
        List<Compte> comptes = new ArrayList<>();

        for (int i = 0; i < NB_COMPTES; i++) {
            comptes.add(new Compte(i, 1000));
        }

        //la somme totale du debut
        double sommeInitiale = comptes.stream()
                .mapToDouble(Compte::getSolde)
                .sum();

        System.out.println("Somme initiale = " + sommeInitiale);

        // Pool de thread : limite le nombre de threads actif
        ExecutorService pool = Executors.newFixedThreadPool(NB_CLIENTS);

        long start = System.currentTimeMillis();

        //simulation
        for (int i = 0; i < NB_CLIENTS; i++) {

            pool.execute(() -> {

                //Chaque client fait entre 1 et 10 operations
                int nbOperations = random.nextInt(10) + 1;

                for (int j = 0; j < nbOperations; j++) {

                    //choix aléatoire
                    Compte c1 = comptes.get(random.nextInt(NB_COMPTES));
                    Compte c2 = comptes.get(random.nextInt(NB_COMPTES));

                    //montant aléatoire
                    double montant = random.nextInt(100) + 1;

                    //check pour ne pas faire le virement sur le même compte
                    if (c1 != c2) {
                        Virement.transferer(c1, c2, montant, historique);
                    }
                }
            });
        }

        //Arre^t du pool
        pool.shutdown();

        //attente que tout les threads ce termine
        pool.awaitTermination(1, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();

        double sommeFinale = comptes.stream()
                .mapToDouble(Compte::getSolde)
                .sum();

        //temps total d el'execution
        long duree = end - start;

        System.out.println("\nRESULTATS");
        System.out.println("Somme initiale est de " + sommeInitiale);
        System.out.println("Somme finale est de " + sommeFinale);

        //verification de l'invariation
        if (sommeInitiale == sommeFinale) {
            System.out.println("INVARIANT OK");
        } else {
            System.out.println("INVARIANT KO");
        }

        System.out.println("Temps total = " + duree + " ms");
    }
}