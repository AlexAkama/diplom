package project.exception;

public class ImageSuccess extends Exception {

    private final String path;

    public ImageSuccess(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
