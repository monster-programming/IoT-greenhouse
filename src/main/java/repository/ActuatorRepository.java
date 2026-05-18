package repository;

import model.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActuatorRepository extends JpaRepository<Actuator, String> {
}
