package project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.dto.main.Message;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageSuccess.class)
    public ResponseEntity<String> handleImage(ImageSuccess e) {
        return ResponseEntity.ok(e.getPath());
    }

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Message> handleContentBadRequest(BadRequestException e) {
        return getHandlerResponse(e, BAD_REQUEST);
    }

    //401
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Message> handleContentUnauthorized(UnauthorizedException e) {
        return getHandlerResponse(e, UNAUTHORIZED);
    }

    //403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Message> handleContentForbidden(ForbiddenException e) {
        return getHandlerResponse(e, FORBIDDEN);
    }

    //404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Message> handleContentNotFound(NotFoundException e) {
        return getHandlerResponse(e, NOT_FOUND);
    }

    //500
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Message> handleContentInternalServerError(InternalServerException e) {
        return getHandlerResponse(e, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Message> getHandlerResponse(AppException e, HttpStatus status) {
        return new ResponseEntity<>(new Message(e.getDescription()), status);
    }
}
