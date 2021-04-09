package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.ConfigParameter;

public interface ConfigParameterRepository extends JpaRepository<ConfigParameter, Long> {

}
