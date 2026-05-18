package service;

import model.Sensors;
import org.springframework.transaction.annotation.Transactional;
import repository.ActuatorRepository;
import repository.SensorsRepository;

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
