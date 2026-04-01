package banque.monitoring;

import java.time.LocalDateTime;

public class Operation {

    private final LocalDateTime date;
    private final String type;
    private final int compteSource;
    private final Integer compteDestination;
    private final double montant;

    public Operation(String type, int compteSource, Integer compteDestination, double montant) {
        this.date = LocalDateTime.now();
        this.type = type;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.montant = montant;
    }

    public String getType() {
        return type;
    }

    public int getCompteSource() {
        return compteSource;
    }

    public Integer getCompteDestination() {
        return compteDestination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + " | " + type + " | source=" + compteSource + (compteDestination != null ? " | destination =" + compteDestination : "") + " | montant=" + montant + "€";
    }
}