package project.service.impementation;

import org.springframework.stereotype.Service;
import project.model.CaptchaCode;
import project.repository._CaptchaRepository;
import project.service._CaptchaService;

import java.util.Optional;

@Service
public class CaptchaServiceImpl implements _CaptchaService {

    private final _CaptchaRepository captchaRepository;

    public CaptchaServiceImpl(_CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public boolean isCodeCorrect(String code, String secret) {
        Optional<CaptchaCode> result = captchaRepository.findCaptchaCodeBySecretCode(secret);
        return result.isPresent() && result.get().getCode().equals(code);
    }

}
