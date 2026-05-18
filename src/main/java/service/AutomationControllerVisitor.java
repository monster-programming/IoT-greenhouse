package service;

import model.Actuator;
import model.GreenHouseVisitor;
import model.HumiditySensor;
import model.TemperatureSensor;
import repository.ActuatorRepository;

public class AutomationControllerVisitor implements GreenHouseVisitor {

    private final ActuatorRepository repository;

    public AutomationControllerVisitor(ActuatorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void visit(TemperatureSensor sensors) {
        System.out.println("Проверка температуры для " + sensors.getId() + ": " + sensors.getVal() + " C");

        repository.findById("window_1").ifPresent(window -> {
            if (sensors.getVal() > 30 && !window.isActive()) {
                window.setActive(true);
                repository.save(window);
                System.out.println("[Action] Повышенная температура. Окна открыты.");
            }
            else if (sensors.getVal() < 22 && window.isActive()) {
                window.setActive(false);
                repository.save(window);
                System.out.println("[Action] Низкая температура. Окна закрыты.");
            }

        });
    }

    @Override
    public void visit(HumiditySensor sensors) {

    }
    @Override
    public void visit(Actuator actuator) {

    }
}
