package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.CaptchaCode;

import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Long> {

    Optional<CaptchaCode> findCaptchaCodeBySecretCode(String secret);

}
