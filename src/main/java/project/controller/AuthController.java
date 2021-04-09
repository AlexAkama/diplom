package project.controller;

import com.github.cage.GCage;
import project.config.Connection;
import project.dto.CaptchaDto;
import project.dto.LikesDto;
import project.dto.PostListDto;
import project.dto.StatDto;
import project.dto.auth.LoginRequest;
import project.dto.auth.RegistrationRequest;
import project.dto.auth.RegistrationResponse;
import project.dto.auth.UserResponse;
import project.model.CaptchaCode;
import project.model.User;
import project.model.emun.PostState;
import project.service.AuthService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static project.dto.Dto.randomString;
import static project.dto.Dto.resizeForCaptcha;

/** Контроллер авторизации */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    /** Срок действия кода капчи в минутах */
    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    /**
     * Сервис авторизации
     */
    private final AuthService authService;


    // CONSTRUCTORS
    public AuthController(AuthService authService) {
        this.authService = authService;
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
        String token = randomString(5);
        String image = "data:image/" + gCage.getFormat() + ";base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                resizeForCaptcha(gCage.drawImage(token)),
                gCage.getFormat(),
                outputStream);
        image += Base64.getEncoder().encodeToString(outputStream.toByteArray());

        String secret = randomString(25);
        CaptchaCode captchaCode = new CaptchaCode(new Date(), token, secret);

        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            Timestamp limit = new Timestamp(System.currentTimeMillis() - (long) captchaTimeout * 60 * 1000);
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
        LikesDto likesDto = new LikesDto().getUserResult(id);

        statistics.put("postsCount", statDto.getPostsCount());
        statistics.put("likesCount", likesDto.getLikeCount());
        statistics.put("dislikesCount", likesDto.getDislikeCount());
        statistics.put("viewsCount", statDto.getViewsCount());
        statistics.put("firstPublication", statDto.getFirstPublication());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/api/post/my")
    public ResponseEntity<PostListDto> getMy(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status
    ) {

        //FIXME --- ИМИТАЦИЯ АВТОРИЗАЦИИ ---
        int id = 10;
        String byId = "p.user.id= " + id + " and ";

        String condition = "";
        //ASK Надо ли тут обрабаатывать возможную ошибку? вдруг фронт пришлет какуюто хрень в status?
        // http://127.0.0.1:8080/api/post/my?offset=0&limit=10&status=inactive
        PostState postState = PostState.valueOf(status.toUpperCase());
        switch (postState) {
            case PENDING:
                condition = "p.isActive=1 and p.moderationStatus='NEW'";
                break;
            case DECLINED:
                condition = "p.isActive=1 and p.moderationStatus='DECLINED'";
                break;
            case PUBLISHED:
                condition = "p.isActive=1 and p.moderationStatus='ACCEPTED'";
                break;
            case INACTIVE:
                condition = " p.isActive=0";
                break;
        }

        return new ResponseEntity<>(
                new PostListDto().makeAnnounces(byId + condition, offset, limit),
                HttpStatus.OK);
    }

}
