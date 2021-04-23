package project.controller;

import com.github.cage.GCage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.config.AppConstant;
import project.config.Connection;
import project.dto.*;
import project.dto._auth.*;
import project.model.CaptchaCode;
import project.model.User;
import project.repository.VoteRepository;
import project.service.AppService;
import project.service._AuthService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static project.dto.Dto.resizeForCaptcha;

/** Контроллер авторизации */
@Controller
@RequestMapping("/api/auth")
public class _AuthController {

    /** Срок действия кода капчи в минутах */
    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    private final VoteRepository voteRepository;
    private final _AuthService authService;
    private final AppService appService;

    // CONSTRUCTORS
    public _AuthController(VoteRepository voteRepository,
                           _AuthService authService,
                           AppService appService) {
        this.voteRepository = voteRepository;
        this.authService = authService;
        this.appService = appService;
    }

    // MAPPING

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

        GCage gCage = new GCage();
        String token = appService.randomString(5);
        String image = "data:image/" + gCage.getFormat() + ";base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                resizeForCaptcha(gCage.drawImage(token)),
                gCage.getFormat(),
                outputStream);
        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());

        String secret = appService.randomString(25);
        CaptchaCode captchaCode = new CaptchaCode(new Date(), token, secret);

        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            Timestamp limit = new Timestamp(System.currentTimeMillis() - AppConstant.minuteToMillis(captchaTimeout));
            String hql = "delete from CaptchaCode where time < :limit";
            session.createQuery(hql).setParameter("limit", limit).executeUpdate();

            session.save(captchaCode);

            transaction.commit();
        }

        return new ResponseEntity<>(new CaptchaDto(secret, image), HttpStatus.OK);
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


    //FIXME перенести в соответствующие контроллеры

    @GetMapping("/api/statistics/my")
    public ResponseEntity<Map<String, Long>> getMyStatistics() {
        //Текущий пользователь
        User user = new User();
        user.setId(10);

        Map<String, Long> statistics = new HashMap<>();
        int id = user.getId();

        StatDto statDto = new StatDto().getUserResult(id);
        VoteCounterDto voteCounterDto = voteRepository.getUserResult(id);

        statistics.put("postsCount", statDto.getPostsCount());
        statistics.put("likesCount", voteCounterDto.getLikeCounter());
        statistics.put("dislikesCount", voteCounterDto.getDislikeCounter());
        statistics.put("viewsCount", statDto.getViewsCount());
        statistics.put("firstPublication", statDto.getFirstPublication());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }



}
