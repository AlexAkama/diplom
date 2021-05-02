package project;

import org.springframework.http.ResponseEntity;
import project.dto.main.AppResponse;
import project.exception.InternalServerException;
import project.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface RestoreService {

    ResponseEntity<AppResponse> restorePassword(RestoreRequest request, HttpServletRequest httpServletRequest)
            throws NotFoundException, InternalServerException;

}
