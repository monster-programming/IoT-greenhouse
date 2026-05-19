package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.model.Actuator;
import org.example.iotgreenhouse.model.ActuatorType;
import org.example.iotgreenhouse.repository.ActuatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ActuatorRepository actuatorRepository;

    public DataInitializer(ActuatorRepository actuatorRepository) {
        this.actuatorRepository = actuatorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[База данных] Инициализация ...");

        if (!actuatorRepository.existsById("window_1")) {
            Actuator window = new Actuator("window_1", ActuatorType.WINDOW, false);

            actuatorRepository.save(window);
            System.out.println("[База данных] Добавлено устройство window_1");
        }
        else {
            System.out.println("[База данных] Неизвестное устройство");
        }

        System.out.println("[База данных] Инициализация завершена");
    }
}
