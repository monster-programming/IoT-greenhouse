package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.config.MqttGateway;
import org.example.iotgreenhouse.model.Sensors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.iotgreenhouse.repository.ActuatorRepository;
import org.example.iotgreenhouse.repository.SensorsRepository;

@Service
public class GreenHouseManagementService {
    private final SensorsRepository sensorsRepository;
    private final ActuatorRepository actuatorRepository;
    private final MqttGateway mqttGateway;

    public GreenHouseManagementService(SensorsRepository sensorsRepository, ActuatorRepository actuatorRepository,
                                       MqttGateway mqttGateway) {
        this.sensorsRepository = sensorsRepository;
        this.actuatorRepository = actuatorRepository;
        this.mqttGateway = mqttGateway;
    }

    @Transactional
    public void handleIncomingSensorData(Sensors updateSensor) {
        Sensors saveSensor = sensorsRepository.save(updateSensor);

        AutomationControllerVisitor visitor = new AutomationControllerVisitor(actuatorRepository, mqttGateway);

        saveSensor.accept(visitor);
    }
}
