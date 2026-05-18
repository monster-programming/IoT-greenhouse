package org.example.iotgreenhouse.controller;

import org.example.iotgreenhouse.model.TemperatureSensor;
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

    public GreenhouseController(GreenHouseManagementService managementService) {
        this.managementService = managementService;
    }

    @PostMapping("/sensor")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataRequest request) {
        if ("TEMPERATURE".equalsIgnoreCase(request.getType())) {
            TemperatureSensor temperatureSensor = new TemperatureSensor(request.getId(), request.getVal());
            managementService.handleIncomingSensorData(temperatureSensor);
            return ResponseEntity.ok("Данные датчика температуры успешно загружены");
        }
        else {
            return ResponseEntity.badRequest().body("Неизвестный тип датчика: " + request.getType());
        }
    }

}
