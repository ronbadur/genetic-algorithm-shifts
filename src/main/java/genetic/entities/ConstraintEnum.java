package genetic.entities;

public enum ConstraintEnum {
    Available(1000),
    NotInterested(100),
    NotAvailable(10),
    NotAvailableAtAll(1);

    private int value;
    ConstraintEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
