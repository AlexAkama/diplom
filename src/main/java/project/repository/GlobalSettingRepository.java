package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.GlobalSetting;

import java.util.Optional;

public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, Long> {

    Optional<GlobalSetting> findByCode(String code);

}
