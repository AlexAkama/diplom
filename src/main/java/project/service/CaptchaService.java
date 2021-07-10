package project.service;

import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.auth.registration.CaptchaDto;
import project.model.CaptchaCode;
import project.repository.CaptchaCodeRepository;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final Random random = new Random();

    private final CaptchaCodeRepository captchaCodeRepository;
    private final ImageService imageService;

    /**
     * Срок действия кода капчи в минутах
     */
    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {

        var gCage = new GCage();
        var token = randomString(5);
        String image = "data:image/".concat(gCage.getFormat()).concat(";base64,");
        var outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                imageService.resizeCaptchaImage(gCage.drawImage(token)),
                gCage.getFormat(),
                outputStream);
        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());

        var secret = randomString(25);
        var captchaCode = new CaptchaCode(new Date(), token, secret);
        captchaCodeRepository.save(captchaCode);

        return ResponseEntity.ok(new CaptchaDto(secret, image));
    }

    public boolean codeIsCorrect(String code, String secret) {
        var limit = new Date(System.currentTimeMillis() - AppConstant.minuteToMillis(captchaTimeout));
        captchaCodeRepository.deleteAllByTimeBefore(limit);
        Optional<CaptchaCode> result = captchaCodeRepository.findCaptchaCodeBySecretCode(secret);
        return result.isPresent() && result.get().getCode().equals(code.toUpperCase());
    }

    /**
     * Генерация строки из случайныйх символов заданной длины
     * <br></br> Символы выбираются из указанного массива
     *
     * @param length длина результирующей строки
     * @return строка случайных символов заданной длинны
     */
    private String randomString(int length) {
        char[] chars = "ACEFGHJKLMNPQRUVWXY1234567890".toCharArray();
        var stringBuilder = new StringBuilder();
        for (var i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

}
