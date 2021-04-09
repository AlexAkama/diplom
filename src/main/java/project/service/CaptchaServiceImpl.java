package project.service;

import org.springframework.stereotype.Service;
import project.model.CaptchaCode;
import project.repository.CaptchaRepository;

import java.util.Optional;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaRepository captchaRepository;

    public CaptchaServiceImpl(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public boolean isCodeCorrect(String code, String secret) {
        Optional<CaptchaCode> result = captchaRepository.findCaptchaCodeBySecretCode(secret);
        return result.isPresent() && result.get().getCode().equals(code);
    }

}
