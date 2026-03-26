package banque;

import banque.core.Banque;

public class Main {
    public static void main(String[] args) {
        System.out.println("Projet OK");

        Banque banque = new Banque();
        banque.demarrer();
    }
}