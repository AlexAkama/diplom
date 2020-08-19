package diploma.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

    @GetMapping("/api/auth/check")
    public ResponseEntity isAuthorized() {
        return new ResponseEntity(new ResultDto(false), HttpStatus.OK);
    }


    private class ResultDto {
        private final boolean result;

        public ResultDto(boolean result) {
            this.result = result;
        }

        public boolean isResult() {
            return result;
        }
    }
}
