package banque.model;

public class Compte {
    private final int numero;
    private double solde;

    public int getNumero() {
        return numero;
    }

    public Compte(int numero, double soldeInitial) {
        this.numero = numero;
        this.solde = soldeInitial;
    }

    public synchronized void deposer(double montant) {
        if ( montant <=0){

            throw new IllegalArgumentException("Montant invalide ");
        }
        solde += montant;   // TODO compléter
        System.out.println("compte"+numero+"+"+montant+"solde:"+solde);
    }

    public synchronized boolean retirer(double montant) {
        if(montant<=0){

            throw new IllegalArgumentException("Montant invalide");
        }


        if (solde >= montant) {
            solde -= montant;
            System.out.println("Compte"+numero+"-"+montant+"=>solde:"+solde);
            return true;
        }
        return false;
    }

    public synchronized double getSolde() {
        return solde; }
}