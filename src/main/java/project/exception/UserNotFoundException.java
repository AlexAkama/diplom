package project.exception;

public class UserNotFoundException extends AppException{

    public UserNotFoundException(String description) {
        super(description);
    }

}
