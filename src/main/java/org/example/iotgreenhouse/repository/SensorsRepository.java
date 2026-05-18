package org.example.iotgreenhouse.repository;

import org.example.iotgreenhouse.model.Sensors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorsRepository extends JpaRepository<Sensors, String> {

}
