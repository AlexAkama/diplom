package diploma.controller;

import com.github.cage.GCage;
import diploma.main.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static diploma.main.Service.*;

@RestController
public class ApiAuthController {

    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    @GetMapping("/api/auth/check")
    public ResponseEntity<ResultDto> isAuthorized() {
        return new ResponseEntity<>(new ResultDto(false), HttpStatus.OK);
    }

    @GetMapping("/api/auth/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() {
        final String header = "data: image/png; base64, ";
        String secret = randomString(25);

        GCage gCage = new GCage();
        String token = gCage.getTokenGenerator().next();
        String encoding = header +
                Arrays.toString(Base64.getEncoder()
                        .encode(gCage.draw(token)));

        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String sql;

            sql = "delete from captcha_codes"
                    + " where time < (NOW() - INTERVAL " + captchaTimeout + " MINUTE)";
            session.createSQLQuery(sql).executeUpdate();

            sql = String.format("insert into captcha_codes (time, code, secret_code)"
                            + " values('%s', '%s', '%s')",
                    dateToSqlDate(new Date()),
                    token,
                    secret);
            session.createSQLQuery(sql).executeUpdate();

            transaction.commit();
        }

        return new ResponseEntity<>(new CaptchaDto(secret, encoding), HttpStatus.OK);
    }

    @GetMapping("api/auth/login")
    public ResponseEntity<HttpStatus> login() {
        return new ResponseEntity<>(HttpStatus.OK);
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

    private class CaptchaDto {
        private String secret;
        private String image;

        public CaptchaDto(String secret, String image) {
            this.secret = secret;
            this.image = image;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
