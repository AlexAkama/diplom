package project.exception;

public class AppException extends RuntimeException {

    private final String description;

    public AppException(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
