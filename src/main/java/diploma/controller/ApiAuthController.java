package diploma.controller;

import diploma.main.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

    @GetMapping("/api/auth/check")
    public ResponseResult isAuthorized() {
        return new ResponseResult(false);
    }

}
