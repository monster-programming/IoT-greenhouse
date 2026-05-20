package org.example.iotgreenhouse.controller;

import org.example.iotgreenhouse.model.HumiditySensor;
import org.example.iotgreenhouse.model.LightSensor;
import org.example.iotgreenhouse.model.Sensors;
import org.example.iotgreenhouse.model.TemperatureSensor;
import org.example.iotgreenhouse.repository.SensorsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.iotgreenhouse.service.GreenHouseManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/greenhouse")
public class GreenhouseController {
    private final GreenHouseManagementService managementService;
    private final SensorsRepository sensorsRepository;

    public GreenhouseController(GreenHouseManagementService managementService, SensorsRepository sensorsRepository) {
        this.managementService = managementService;
        this.sensorsRepository = sensorsRepository;
    }

    @PostMapping("/sensor")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataRequest request) {

        if ("TEMPERATURE".equalsIgnoreCase(request.getType())) {
            TemperatureSensor temperatureSensor = (TemperatureSensor) sensorsRepository.findById(request.getId()).orElse(null);
            if (temperatureSensor == null) return ResponseEntity.badRequest().body("Датчик не найден!");
            temperatureSensor.setVal(request.getVal());
            managementService.handleIncomingSensorData(temperatureSensor);
            sensorsRepository.save(temperatureSensor);
            return ResponseEntity.ok("Данные температуры загружены");
        }
        else if ("HUMIDITY".equalsIgnoreCase(request.getType())) {
            HumiditySensor humiditySensor = (HumiditySensor) sensorsRepository.findById(request.getId()).orElse(null);
            if (humiditySensor == null) return ResponseEntity.badRequest().body("Датчик не найден!");
            humiditySensor.setVal(request.getVal());
            managementService.handleIncomingSensorData(humiditySensor);
            sensorsRepository.save(humiditySensor);
            return ResponseEntity.ok("Данные влажности загружены");
        }
        else if ("LIGHT".equalsIgnoreCase(request.getType())) {
            LightSensor lightSensor = (LightSensor) sensorsRepository.findById(request.getId()).orElse(null);
            if (lightSensor == null) return ResponseEntity.badRequest().body("Датчик не найден!");
            lightSensor.setVal(request.getVal());
            managementService.handleIncomingSensorData(lightSensor);
            sensorsRepository.save(lightSensor);
            return ResponseEntity.ok("Данные света загружены");
        }
        else {
            return ResponseEntity.badRequest().body("Неизвестный тип датчика: " + request.getType());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<Iterable<Sensors>> getCurrentData() {
        return ResponseEntity.ok(sensorsRepository.findAll());
    }
}