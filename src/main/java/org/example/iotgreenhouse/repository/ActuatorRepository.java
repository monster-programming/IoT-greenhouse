package org.example.iotgreenhouse.repository;

import org.example.iotgreenhouse.model.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActuatorRepository extends JpaRepository<Actuator, String> {
}
