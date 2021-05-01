package project.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageSuccess.class)
    public ResponseEntity<String> handleImage(ImageSuccess e) {
        return ResponseEntity.ok(e.getPath());
    }
}
