package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.ConfigParameter;

import java.util.Optional;

public interface ConfigParameterRepository extends JpaRepository<ConfigParameter, Long> {

    Optional<ConfigParameter> findConfigParameterByName (String name);

}
