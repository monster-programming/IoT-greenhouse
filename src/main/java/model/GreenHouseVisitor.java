package model;

public interface GreenHouseVisitor {
    void visit(TemperatureSensor sensors);
    void visit(HumiditySensor sensors);
}
