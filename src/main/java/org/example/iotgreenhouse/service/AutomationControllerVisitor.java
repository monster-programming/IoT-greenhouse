package org.example.iotgreenhouse.service;

import org.example.iotgreenhouse.config.MqttGateway;
import org.example.iotgreenhouse.model.*;
import org.example.iotgreenhouse.repository.ActuatorRepository;

import java.util.List;

public class AutomationControllerVisitor implements GreenHouseVisitor {

    private final ActuatorRepository repository;
    private final MqttGateway mqttGateway;
    private static final String BASE_ACTUATOR_TOPIC = "greenhouse/actuators/";

    public AutomationControllerVisitor(ActuatorRepository repository, MqttGateway mqttGateway) {
        this.repository = repository;
        this.mqttGateway = mqttGateway;
    }

    @Override
    public void visit(TemperatureSensor sensors) {
        System.out.println("Проверка температуры для " + sensors.getId() + ": " + sensors.getVal() + "°C");

        Zone currZone = sensors.getZone();

        List<Actuator> actuators = currZone.getActuators();

        for(Actuator actuator : actuators) {
            String topic = BASE_ACTUATOR_TOPIC + actuator.getId();

            if (actuator.getType() == ActuatorType.WINDOW) {
                if (sensors.getVal() > 30 && !actuator.isActive()) {
                    actuator.setActive(true);
                    repository.save(actuator);
                    System.out.println("[Action] Повышенная температура в зоне " + currZone.getName() + ". " + "Открыто окно " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"OPEN\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда OPEN в топик: " + topic);
                }
                else if (sensors.getVal() < 22 && actuator.isActive()) {
                    actuator.setActive(false);
                    repository.save(actuator);
                    System.out.println("[Action] Низкая температура в зоне " + currZone.getName() + "." + " Закрыто окно " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"CLOSE\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда CLOSE в топик: " + topic);
                }
            }
        }
    }

    @Override
    public void visit(HumiditySensor sensors) {
        // Предполагаем, что у HumiditySensor метод получения значения называется getVal()
        System.out.println("Проверка влажности для " + sensors.getId() + ": " + sensors.getVal() + "%");
        Zone currZone = sensors.getZone();
        List<Actuator> actuators = currZone.getActuators();

        for(Actuator actuator : actuators) {
            String topic = BASE_ACTUATOR_TOPIC + actuator.getId();

            if (actuator.getType() == ActuatorType.WATER_PUMP) {
                if (sensors.getVal() < 40 && !actuator.isActive()) {
                    actuator.setActive(true);
                    repository.save(actuator);
                    System.out.println("[Action] Сухая почва в зоне " + currZone.getName() + ". Включен полив " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"TURN_ON\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда TURN_ON в топик: " + topic);
                }
                else if (sensors.getVal() > 60 && actuator.isActive()) {
                    actuator.setActive(false);
                    repository.save(actuator);
                    System.out.println("[Action] Оптимальная влажность в зоне " + currZone.getName() + ". Выключен полив " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"TURN_OFF\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда TURN_OFF в топик: " + topic);
                }
            }
        }
    }

    // --- 3. ЛОГИКА ДЛЯ СВЕТА (ФИТОЛАМПЫ) ---
    @Override
    public void visit(LightSensor sensors) {
        System.out.println("Проверка освещенности для " + sensors.getId() + ": " + sensors.getVal() + "%");
        Zone currZone = sensors.getZone();
        List<Actuator> actuators = currZone.getActuators();

        for(Actuator actuator : actuators) {
            String topic = BASE_ACTUATOR_TOPIC + actuator.getId();

            if (actuator.getType() == ActuatorType.LIGHT) {
                if (sensors.getVal() < 30 && !actuator.isActive()) {
                    actuator.setActive(true);
                    repository.save(actuator);
                    System.out.println("[Action] Темно в зоне " + currZone.getName() + ". Включена лампа " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"TURN_ON\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда TURN_ON в топик: " + topic);
                }
                else if (sensors.getVal() > 70 && actuator.isActive()) {
                    actuator.setActive(false);
                    repository.save(actuator);
                    System.out.println("[Action] Светло в зоне " + currZone.getName() + ". Выключена лампа " + actuator.getId() + ".");

                    String payload = String.format("{\"action\":\"TURN_OFF\",\"zone\":\"%s\"}", currZone.getName());
                    mqttGateway.sendToMqtt(payload, topic);
                    System.out.println("[MQTT] Отправлена команда TURN_OFF в топик: " + topic);
                }
            }
        }
    }
    @Override
    public void visit(Actuator actuator) {

    }
}
