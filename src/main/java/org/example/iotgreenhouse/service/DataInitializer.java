package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.model.*;
import org.example.iotgreenhouse.repository.ActuatorRepository;
import org.example.iotgreenhouse.repository.SensorsRepository;
import org.example.iotgreenhouse.repository.ZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ActuatorRepository actuatorRepository;
    private final ZoneRepository zoneRepository;
    private final SensorsRepository sensorsRepository;

    public DataInitializer(SensorsRepository sensorsRepository, ActuatorRepository actuatorRepository, ZoneRepository zoneRepository) {
        this.sensorsRepository = sensorsRepository;
        this.actuatorRepository = actuatorRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[База данных] Инициализация ...");

        Zone zoneAlpha;

        if (!zoneRepository.existsById("zone_alpha")) {
            zoneAlpha = new Zone("zone_alpha", "Томаты");
            zoneRepository.save(zoneAlpha);
            System.out.println("[База данных] Зона " + zoneAlpha.getName() + " создана");
        } else {
            zoneAlpha = zoneRepository.findById("zone_alpha").get();
        }

        // --- ИНИЦИАЛИЗАЦИЯ АКТУАТОРОВ ---
        if (!actuatorRepository.existsById("window_1")) {
            Actuator window = new Actuator("window_1", ActuatorType.WINDOW, false);
            window.setZone(zoneAlpha);
            actuatorRepository.save(window);
            System.out.println("[База данных] Добавлено устройство " + window.getId() + " в зону " + zoneAlpha.getName());
        }

        if (!actuatorRepository.existsById("pump_1")) {
            Actuator pump = new Actuator("pump_1", ActuatorType.WATER_PUMP, false);
            pump.setZone(zoneAlpha);
            actuatorRepository.save(pump);
            System.out.println("[База данных] Добавлено устройство " + pump.getId() + " в зону " + zoneAlpha.getName());
        }

        if (!actuatorRepository.existsById("lamp_1")) {
            Actuator lamp = new Actuator("lamp_1", ActuatorType.LIGHT, false);
            lamp.setZone(zoneAlpha);
            actuatorRepository.save(lamp);
            System.out.println("[База данных] Добавлено устройство " + lamp.getId() + " в зону " + zoneAlpha.getName());
        }

        // --- ИНИЦИАЛИЗАЦИЯ ДАТЧИКОВ ---
        if (!sensorsRepository.existsById("temp_sensor_1")) {
            TemperatureSensor temperatureSensor = new TemperatureSensor("temp_sensor_1", 25.0);
            temperatureSensor.setZone(zoneAlpha);
            sensorsRepository.save(temperatureSensor);
            System.out.println("[База данных] Добавлен датчик " + temperatureSensor.getId() + " в зону " + zoneAlpha.getName());
        }

        if (!sensorsRepository.existsById("hum_sensor_1")) {
            HumiditySensor humiditySensor = new HumiditySensor("hum_sensor_1", 50.0);
            humiditySensor.setZone(zoneAlpha);
            sensorsRepository.save(humiditySensor);
            System.out.println("[База данных] Добавлен датчик " + humiditySensor.getId() + " в зону " + zoneAlpha.getName());
        }

        if (!sensorsRepository.existsById("light_sensor_1")) {
            LightSensor lightSensor = new LightSensor("light_sensor_1", 50); // Стартовая яркость 50%
            lightSensor.setZone(zoneAlpha);
            sensorsRepository.save(lightSensor);
            System.out.println("[База данных] Добавлен датчик " + lightSensor.getId() + " в зону " + zoneAlpha.getName());
        }

        System.out.println("[База данных] Инициализация завершена");
    }
}