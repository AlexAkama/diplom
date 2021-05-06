package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.auth.registration.CaptchaDto;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface CaptchaService {

    ResponseEntity<CaptchaDto> getCaptcha() throws IOException;

    BufferedImage resizeImage(BufferedImage image);

    boolean codeIsCorrect(String code, String secret);

}
