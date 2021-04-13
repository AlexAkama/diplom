package project.exception;

abstract class AppException extends RuntimeException {

    protected final String description;

    public AppException(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
