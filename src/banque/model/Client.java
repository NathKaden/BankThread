package banque.model;

public class Client {
    private final int id;
    private final Runnable operation;

    public Client(int id, Runnable operation) {
        this.id = id;
        this.operation = operation;
    }

    public int getId() {
        return id;
    }

    public void executerOperation() {
        operation.run();
    }
}