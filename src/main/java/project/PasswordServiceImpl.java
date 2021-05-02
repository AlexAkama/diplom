package project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.main.AppResponse;
import project.dto.main.AppResponseWithErrors;
import project.exception.InternalServerException;
import project.exception.NotFoundException;
import project.model.*;
import project.repository.*;
import project.service.EmailService;
import project.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserService userService;
    private final EmailService emailService;
    private final RestoreCodeRepository restoreCodeRepository;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    /**
     * Срок действия кода востановления пароля в минутах
     */
    @Value("${config.restore.timeout}")
    private int restoreTimeout;

    /**
     * Минимальная длинна пароля
     */
    @Value("${config.password.minlength}")
    private int passwordMinLength;

    public PasswordServiceImpl(UserService userService,
                               EmailService emailService,
                               RestoreCodeRepository restoreCodeRepository,
                               CaptchaCodeRepository captchaCodeRepository,
                               UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.restoreCodeRepository = restoreCodeRepository;
        this.captchaCodeRepository = captchaCodeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<AppResponse> restorePassword(
            PasswordRestoreRequest request,
            HttpServletRequest httpServletRequest
    ) throws InternalServerException {
        String email = request.getEmail();
        User user;
        try {
            user = userService.findByEmail(email);
        } catch (NotFoundException e) {
            return ResponseEntity.ok(new AppResponse().bad());
        }
        String code = UUID.randomUUID().toString().replace("-", "");
        RestoreCode restoreCode = new RestoreCode(code);
        restoreCodeRepository.save(restoreCode);
        user.setCode(code);
        userService.save(user);
        String link = emailService.getHostAndPort(httpServletRequest) + "/login/change-password/" + code;
        emailService.sendRestorePasswordEmail(email, link);
        return ResponseEntity.ok(new AppResponse().ok());
    }

    @Override
    public ResponseEntity<AppResponseWithErrors> changePassword(PasswordChangeRequest request)
            throws NotFoundException {
        Date limit = new Date(System.currentTimeMillis() - AppConstant.minuteToMillis(restoreTimeout));
        restoreCodeRepository.deleteAllByTimeBefore(limit);
        Optional<RestoreCode> optionalCode = restoreCodeRepository.findByCode(request.getCode());
        Optional<CaptchaCode> optionalCaptcha = captchaCodeRepository.findCaptchaCodeBySecretCode(request.getSecret());
        PasswordChangeErrorMap errors = new PasswordChangeErrorMap();
        if (optionalCode.isEmpty()) errors.addTimeError();
        if (request.getPassword().length() < passwordMinLength) errors.addPasswordError();
        if (optionalCaptcha.isEmpty() || !optionalCaptcha.get().getCode().equals(request.getCaptcha())) {
            errors.addCaptchaError();
        }
        AppResponseWithErrors response = new AppResponseWithErrors();
        if (!errors.getErrors().isEmpty()) {
            response.setErrors(errors.getErrors());
        } else {
            RestoreCode code = optionalCode
                    .orElseThrow(() -> new NotFoundException("Код востановления не найден"));
            User user = userRepository.findByCode(optionalCode.get().getCode())
                    .orElseThrow(() -> new NotFoundException("Пользователь для смены пароля не найден"));
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.setCode(null);
            userRepository.save(user);
            restoreCodeRepository.delete(code);
        }
        return ResponseEntity.ok(response);
    }

}
