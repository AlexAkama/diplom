package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.model.CaptchaCode;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Long> {

    Optional<CaptchaCode> findCaptchaCodeBySecretCode(String secret);

    @Transactional
    @Modifying
    void deleteAllByTimeBefore(Date limit);

}
