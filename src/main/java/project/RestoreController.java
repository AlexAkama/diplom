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
public class RestoreController {

    private final RestoreService restoreService;

    public RestoreController(RestoreService restoreService) {
        this.restoreService = restoreService;
    }

    @PostMapping("/restore")
    public ResponseEntity<? extends AppResponse> restorePassword(
            @RequestBody RestoreRequest request,
            HttpServletRequest httpServletRequest
    ) throws InternalServerException, NotFoundException {
        return restoreService.restorePassword(request, httpServletRequest);
    }

}
