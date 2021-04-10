package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.ConfigParameter;

import java.util.Optional;

@Repository
public interface ConfigParameterRepository extends JpaRepository<ConfigParameter, Long> {

    Optional<ConfigParameter> findConfigParameterByName (String name);

}
