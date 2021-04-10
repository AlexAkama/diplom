package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.CaptchaCode;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Long> {

    Optional<CaptchaCode> findCaptchaCodeBySecretCode(String secret);

}
