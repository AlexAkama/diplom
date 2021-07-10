package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.main.AppResponse;
import project.dto.main.AppResponseWithErrors;
import project.dto.password.*;
import project.exception.InternalServerException;
import project.exception.NotFoundException;
import project.model.*;
import project.repository.CaptchaCodeRepository;
import project.repository.RestoreCodeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private final UserService userService;
    private final EmailService emailService;
    private final RestoreCodeRepository restoreCodeRepository;
    private final CaptchaCodeRepository captchaCodeRepository;

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
        var restoreCode = new RestoreCode(code);
        restoreCodeRepository.save(restoreCode);
        user.setCode(code);
        userService.save(user);
        String link = emailService.getHostAndPort(httpServletRequest) + "/login/change-password/" + code;
        emailService.sendRestorePasswordEmail(email, link);
        return ResponseEntity.ok(new AppResponse().ok());
    }

    public ResponseEntity<AppResponseWithErrors> changePassword(PasswordChangeRequest request)
            throws NotFoundException {
        restoreCodeRepository.deleteExpiredCode(restoreTimeout);
        Optional<RestoreCode> optionalCode = restoreCodeRepository.findByCode(request.getCode());
        Optional<CaptchaCode> optionalCaptcha = captchaCodeRepository.findCaptchaCodeBySecretCode(request.getSecret());
        var errors = new PasswordChangeErrorMap();
        if (optionalCode.isEmpty()) errors.addTimeError();
        if (request.getPassword().length() < passwordMinLength) errors.addPasswordError();
        if (optionalCaptcha.isEmpty() || !optionalCaptcha.get().getCode().equals(request.getCaptcha())) {
            errors.addCaptchaError();
        }
        var response = new AppResponseWithErrors();
        if (!errors.getErrors().isEmpty()) {
            response.setErrors(errors.getErrors());
        } else {
            RestoreCode code = optionalCode
                    .orElseThrow(() -> new NotFoundException("Код восстановления не найден"));
            var user = userService.findByCode(code.getCode());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.setCode(null);
            userService.save(user);
            restoreCodeRepository.delete(code);
        }
        return ResponseEntity.ok(response);
    }

}
