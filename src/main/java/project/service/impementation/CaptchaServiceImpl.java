package project.service.impementation;

import com.github.cage.GCage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.config.Connection;
import project.dto.CaptchaDto;
import project.model.CaptchaCode;
import project.repository._CaptchaRepository;
import project.service.CaptchaService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final _CaptchaRepository captchaRepository;

    /**
     * Срок действия кода капчи в минутах
     * */
    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    /**
     * Ширина картинки капчи
     */
    @Value("${config.captcha.image.width}")
    private int width;

    /**
     * Высота картинки капчи
     */
    @Value("${config.captcha.image.height}")
    private int height;

    public CaptchaServiceImpl(_CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {

        GCage gCage = new GCage();
        String token = randomString(5);
        String image = "data:image/" + gCage.getFormat() + ";base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                resizeImage(gCage.drawImage(token)),
                gCage.getFormat(),
                outputStream);
        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());

        String secret = randomString(25);
        CaptchaCode captchaCode = new CaptchaCode(new Date(), token, secret);

        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            Timestamp limit = new Timestamp(System.currentTimeMillis() - AppConstant.minuteToMillis(captchaTimeout));
            String hql = "delete from CaptchaCode where time < :limit";
            session.createQuery(hql).setParameter("limit", limit).executeUpdate();

            session.save(captchaCode);

            transaction.commit();
        }

        return ResponseEntity.ok(new CaptchaDto(secret, image));
    }

    @Override
    public BufferedImage resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    @Override
    public boolean isCodeCorrect(String code, String secret) {
        Optional<CaptchaCode> result = captchaRepository.findCaptchaCodeBySecretCode(secret);
        return result.isPresent() && result.get().getCode().equals(code);
    }

    /**
     * Генерация строки из случайныйх символов заданной длины
     * <br></br> Символы выбираются из указанного массива
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
