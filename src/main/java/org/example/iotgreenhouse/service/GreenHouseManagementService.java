package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.model.Sensors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.iotgreenhouse.repository.ActuatorRepository;
import org.example.iotgreenhouse.repository.SensorsRepository;

@Service
public class GreenHouseManagementService {
    private final SensorsRepository sensorsRepository;
    private final ActuatorRepository actuatorRepository;

    public GreenHouseManagementService(SensorsRepository sensorsRepository, ActuatorRepository actuatorRepository) {
        this.sensorsRepository = sensorsRepository;
        this.actuatorRepository = actuatorRepository;
    }

    @Transactional
    public void handleIncomingSensorData(Sensors updateSensor) {
        Sensors saveSensor = sensorsRepository.save(updateSensor);

        AutomationControllerVisitor visitor = new AutomationControllerVisitor(actuatorRepository);

        saveSensor.accept(visitor);
    }
}
