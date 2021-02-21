package diploma.controller;

import com.github.cage.GCage;
import diploma.config.Connection;
import diploma.dto.*;
import diploma.model.CaptchaCode;
import diploma.model.User;
import diploma.model.emun.PostState;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static diploma.dto.Dto.*;

@RestController
public class ApiAuthController {

    @Value("${config.captcha.timeout}")
    private int captchaTimeout;

    @GetMapping("/api/auth/check")
    public ResponseEntity<LoginDto> isAuthorized() {
        //FIXME --- ИМИТАЦИЯ АВТОРИЗАЦИИ ---
        boolean auth = false;

        LoginDto response = new LoginDto();
        if (auth) {

            User user = new User();
            user.setId(123);
            user.setName("Лелик");
            user.setPhoto("/img/avatars/jake.jpg");
            user.setEmail("gremcox@bk.ru");
            user.setModerator(false);

            response.setResult(true);
            response.setUser(new AuthUserDto(user));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/auth/captcha")
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

            Timestamp limit = new Timestamp(System.currentTimeMillis() - captchaTimeout * 60 * 1000);
            String hql = "delete from CaptchaCode where time < :limit" ;
            session.createQuery(hql).setParameter("limit", limit).executeUpdate();

            session.save(captchaCode);

            transaction.commit();
        }

        return new ResponseEntity<>(new CaptchaDto(secret, image), HttpStatus.OK);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<RegisterDto> registration(
            @RequestBody RegistrationRequest request
    ) {
        boolean registration;
        Map<String, String> errors = new HashMap<>();

        //ASK Нужно ли как-то проверять email?
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String code = request.getCode();
        String secret = request.getSecret();

        String hql;
        Object result;
        boolean emailCorrect;
        boolean captchaConfirm;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            hql = "select 1 from User where email='" + email + "'";
            result = session.createQuery(hql).uniqueResult();
            emailCorrect = result == null;

            hql = "select c.code from CaptchaCode c where c.secretCode='" + secret + "'";
            result = session.createQuery(hql).uniqueResult();
            captchaConfirm = result != null && (((String) result).equals(code));

            transaction.commit();
        }

        boolean nameCorrect = name.length() == name.replaceAll("[^A-Za-zА-Яа-яЁё\\s]+", "").length();
        boolean passwordCorrect = password.length() > 5;

        if (!emailCorrect) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!nameCorrect) {
            errors.put("name", "Имя указано не верно");
        }
        if (!passwordCorrect) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaConfirm) {
            errors.put("captcha", "Код с картинки введен не верно");
        }

        registration = emailCorrect && nameCorrect && passwordCorrect && captchaConfirm;

        if (registration) {

            User newUser = new User().makeSimple(name, email, password);

            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                session.save(newUser);

                transaction.commit();
            }
        }

        return new ResponseEntity<>(
                new RegisterDto(registration, errors),
                HttpStatus.OK
        );
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginDto> login(
            @RequestBody LoginRequest request
    ) {

        String email = request.getEmail();
        String password = request.getPassword();

        LoginDto response = new LoginDto();

        //FIXME --- ИМИТАЦИЯ АВТОРИЗАЦИИ ---
        if (email.equals("gremcox@bk.ru") && password.equals("livada")) {

            User user = new User();
            user.setId(123);
            user.setName("Лелик");
            user.setPhoto("/img/avatars/jake.jpg");
            user.setEmail("gremcox@bk.ru");
            user.setModerator(false);

            response.setResult(true);
            response.setUser(new AuthUserDto(user));

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
