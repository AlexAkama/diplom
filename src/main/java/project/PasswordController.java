package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.main.AppResponse;
import project.exception.InternalServerException;
import project.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/auth")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/restore")
    public ResponseEntity<AppResponse> restorePassword(
            @RequestBody PasswordRestoreRequest request,
            HttpServletRequest httpServletRequest
    ) throws InternalServerException, NotFoundException {
        return passwordService.restorePassword(request, httpServletRequest);
    }

//    @PostMapping("/password")
//    public

}
