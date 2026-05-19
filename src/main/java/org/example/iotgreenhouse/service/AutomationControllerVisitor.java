package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.model.*;
import org.example.iotgreenhouse.repository.ActuatorRepository;

import java.util.List;

public class AutomationControllerVisitor implements GreenHouseVisitor {

    private final ActuatorRepository repository;

    public AutomationControllerVisitor(ActuatorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void visit(TemperatureSensor sensors) {
        System.out.println("Проверка температуры для " + sensors.getId() + ": " + sensors.getVal() + " C");

        Zone currZone = sensors.getZone();

        List<Actuator> actuators = currZone.getActuators();

        for(Actuator actuator : actuators) {
            if (actuator.getType() == ActuatorType.WINDOW) {
                if (sensors.getVal() > 30 && !actuator.isActive()) {
                    actuator.setActive(true);
                    repository.save(actuator);
                    System.out.println("[Action] Повышенная температура в зоне " + currZone.getName() + ". " + "Открыто окно " + actuator.getId() + ".");
                }
                else if (sensors.getVal() < 22 && actuator.isActive()) {
                    actuator.setActive(false);
                    repository.save(actuator);
                    System.out.println("[Action] Низкая температура в зоне " + currZone.getName() + "." + " Закрыто окно " + actuator.getId() + ".");
                }
            }
        }
    }

    @Override
    public void visit(HumiditySensor sensors) {

    }
    @Override
    public void visit(Actuator actuator) {

    }
}
