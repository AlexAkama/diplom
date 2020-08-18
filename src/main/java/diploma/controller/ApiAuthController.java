package diploma.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

    @GetMapping("/api/auth/check")
    public ResultDto isAuthorized() {
        return new ResultDto(false);
    }

}
