package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.main.AppResponse;
import project.dto.main.AppResponseWithErrors;
import project.dto.password.PasswordChangeRequest;
import project.dto.password.PasswordRestoreRequest;
import project.exception.InternalServerException;
import project.exception.NotFoundException;
import project.service.PasswordService;

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

    @PostMapping("/password")
    public ResponseEntity<AppResponseWithErrors> changePassword(
            @RequestBody PasswordChangeRequest request
    ) throws NotFoundException {
        return passwordService.changePassword(request);
    }

}
