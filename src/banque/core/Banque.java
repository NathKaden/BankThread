package banque.core;

import banque.model.Compte;

public class Banque {
    private static final Object tieLock = new Object();

    public static boolean virer(Compte accountSource, Compte accountDestination, double amount) {

        int hashSource = System.identityHashCode(accountSource);
        int hashDestination = System.identityHashCode(accountSource);

        if (hashSource < hashDestination) {
            synchronized (accountDestination) {
                synchronized (accountSource) {
                    return executerVirement(accountSource, accountDestination, amount);
                }

            }


        } else if (hashSource > hashDestination) {
            synchronized (accountDestination) {
                synchronized (accountSource) {

                    return executerVirement(accountSource, accountDestination, amount);
                }
            }

        } else {
            synchronized (tieLock) {

                synchronized (accountSource) {
                    synchronized (accountDestination) {

                        return executerVirement(accountSource, accountDestination, amount);
                    }
                }
            }
        }
    }


    private static boolean executerVirement(Compte accountSource, Compte accountDestination, double amount) {
        if (accountSource.retirer(amount)) {
            accountDestination.deposer(amount);


            System.out.println(Thread.currentThread().getName() +
                    " VIREMENT OK : " + amount +
                    " de " + accountSource.getNumero() +
                    " vers " + accountDestination.getNumero());
            return true;


        }

        System.out.println(Thread.currentThread().getName() +
                " VIREMENT REFUSÉ (solde insuffisant)");
        return false;

    }


    public void demarrer() {
        System.out.println("Banque démarrée");
    }
}
