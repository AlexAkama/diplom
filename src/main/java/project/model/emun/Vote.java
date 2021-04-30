package project.model.emun;

public enum Vote {
    LIKE(1),
    DISLIKE(-1);

    private final int value;

    Vote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
