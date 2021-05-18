package project.service.implementation;

import com.github.cage.GCage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.auth.registration.CaptchaDto;
import project.model.CaptchaCode;
import project.repository.CaptchaCodeRepository;
import project.service.CaptchaService;
import project.service.ImageService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaCodeRepository captchaCodeRepository;
    private final ImageService imageService;

    /**
     * Срок действия кода капчи в минутах
     */
    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    public CaptchaServiceImpl(CaptchaCodeRepository captchaCodeRepository, ImageService imageService) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.imageService = imageService;
    }

    @Override
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {

        GCage gCage = new GCage();
        String token = randomString(5);
        String image = "data:image/" + gCage.getFormat() + ";base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                imageService.resizeCaptchaImage(gCage.drawImage(token)),
                gCage.getFormat(),
                outputStream);
        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());

        String secret = randomString(25);
        CaptchaCode captchaCode = new CaptchaCode(new Date(), token, secret);
        captchaCodeRepository.save(captchaCode);

        return ResponseEntity.ok(new CaptchaDto(secret, image));
    }

    @Override
    public boolean codeIsCorrect(String code, String secret) {
        Date limit = new Date(System.currentTimeMillis() - AppConstant.minuteToMillis(captchaTimeout));
        captchaCodeRepository.deleteAllByTimeBefore(limit);
        Optional<CaptchaCode> result = captchaCodeRepository.findCaptchaCodeBySecretCode(secret);
        return result.isPresent() && result.get().getCode().equals(code);
    }

    /**
     * Генерация строки из случайныйх символов заданной длины
     * <br></br> Символы выбираются из указанного массива
     *
     * @param length длина результирующей строки
     * @return строка случайных символов заданной длинны
     */
    private String randomString(int length) {
        char[] chars = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx1234567890".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

}
