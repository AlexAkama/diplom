package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import project.model.RestoreCode;

import java.util.Date;
import java.util.Optional;

public interface RestoreCodeRepository extends JpaRepository<RestoreCode, Long> {

    Optional<RestoreCode> findByCode(String code);

    @Transactional
    @Modifying
    void deleteAllByTimeBefore(Date date);
}
