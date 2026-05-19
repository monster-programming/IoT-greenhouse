package org.example.iotgreenhouse.controller;

import org.example.iotgreenhouse.model.Sensors;
import org.example.iotgreenhouse.model.TemperatureSensor;
import org.example.iotgreenhouse.repository.SensorsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.iotgreenhouse.service.GreenHouseManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            if (temperatureSensor == null) {
                return ResponseEntity.badRequest().body("Датчик с ID " + request.getId() + " не зарегистрирован в системе!");
            }

            temperatureSensor.setVal(request.getVal());
            managementService.handleIncomingSensorData(temperatureSensor);
            sensorsRepository.save((temperatureSensor));
            return ResponseEntity.ok("Данные датчика температуры успешно загружены");
        }
        else {
            return ResponseEntity.badRequest().body("Неизвестный тип датчика: " + request.getType());
        }
    }

}
