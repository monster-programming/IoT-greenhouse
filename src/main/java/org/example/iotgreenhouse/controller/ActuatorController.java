package org.example.iotgreenhouse.controller;

import org.example.iotgreenhouse.model.Actuator;
import org.example.iotgreenhouse.model.ActuatorType;
import org.example.iotgreenhouse.repository.ActuatorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actuators")
public class ActuatorController {
    private final ActuatorRepository actuatorRepository;

    public ActuatorController(ActuatorRepository actuatorRepository) {
        this.actuatorRepository = actuatorRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerActuator(@RequestBody ActuatorRegistrationRequest request) {
        if (actuatorRepository.existsById(request.getId())) {
            return ResponseEntity.badRequest().body("Устройство с Id " + request.getId() + " уже зарегистрировано");
        }

        ActuatorType type;
        try {
            type = ActuatorType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неизвестное устройство " + request.getId());
        }

        Actuator actuator = new Actuator(request.getId(), type, false);

        actuatorRepository.save(actuator);

        return ResponseEntity.badRequest().body("Устройство " + request.getId() + " [ " + request.getType() + " ]" + " успешно загружено");
    }
}
