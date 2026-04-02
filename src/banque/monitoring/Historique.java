package banque.monitoring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Historique {

    private final CopyOnWriteArrayList<Operation> operations = new CopyOnWriteArrayList<>();

    public void ajouter(Operation op) {
        operations.add(op);
    }

    public List<Operation> getToutes() {
        return operations;
    }

    //filtre par compte
    public List<Operation> filtrerParCompte(int numeroCompte) {
        return operations.stream()
                .filter(op -> op.getCompteSource() == numeroCompte ||
                        (op.getCompteDestination() != null &&
                                op.getCompteDestination() == numeroCompte))
                .collect(Collectors.toList());
    }

    //filtre par type
    public List<Operation> filtrerParType(String type) {
        return operations.stream()
                .filter(op -> op.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public void afficher() {
        System.out.println("\n===== HISTORIQUE =====");
        for (Operation op : operations) {
            System.out.println(op);
        }
        System.out.println("==========");
    }
}