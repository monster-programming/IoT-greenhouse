package org.example.iotgreenhouse.repository;

import org.example.iotgreenhouse.model.Sensors;
import org.example.iotgreenhouse.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {
}
