package org.example.iotgreenhouse.model;

public interface GreenHouseVisitor {
    void visit(TemperatureSensor sensors);
    void visit(HumiditySensor sensors);

    void visit(Actuator actuator);
}
