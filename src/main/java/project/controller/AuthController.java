package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.CaptchaDto;
import project.dto._auth.*;
import project.service.CaptchaService;
import project.service._AuthService;

import java.io.IOException;

/** Контроллер авторизации */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final _AuthService authService;
    private final CaptchaService captchaService;

    public AuthController(_AuthService authService,
                          CaptchaService captchaService) {
        this.authService = authService;
        this.captchaService = captchaService;
    }

    /**
     * Метод возвращает давнные о пользователе, если он авторизован
     *
     * @return {@link UserResponse}
     */
    @GetMapping("/check")
    public ResponseEntity<UserResponse> checkUserAuthorization() {
        return authService.checkUserAuthorization();
    }

    /**
     * КАПЧА
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {
        return captchaService.getCaptcha();
//        GCage gCage = new GCage();
//        String token = appService.randomString(5);
//        String image = "data:image/" + gCage.getFormat() + ";base64,";
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ImageIO.write(
//                resizeForCaptcha(gCage.drawImage(token)),
//                gCage.getFormat(),
//                outputStream);
//        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());
//
//        String secret = appService.randomString(25);
//        CaptchaCode captchaCode = new CaptchaCode(new Date(), token, secret);
//
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            Timestamp limit = new Timestamp(System.currentTimeMillis() - AppConstant.minuteToMillis(captchaTimeout));
//            String hql = "delete from CaptchaCode where time < :limit";
//            session.createQuery(hql).setParameter("limit", limit).executeUpdate();
//
//            session.save(captchaCode);
//
//            transaction.commit();
//        }
//
//        return new ResponseEntity<>(new CaptchaDto(secret, image), HttpStatus.OK);
    }

    /**
     * Запрос на регистрацию пользователя
     * <br> если данные не прошли, проверку возврашется список ошибок
     *
     * @param request {@link RegistrationRequest}
     * @return {@link RegistrationResponse}
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registration(
            @RequestBody RegistrationRequest request
    ) {
        return authService.registration(request);
    }

    /**
     * Метод возвращает данные полязователя если данные для входа верны
     *
     * @param request {@link LoginRequest}
     * @return {@link UserResponse}
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

}
