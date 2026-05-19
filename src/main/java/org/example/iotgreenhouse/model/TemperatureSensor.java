package org.example.iotgreenhouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "temperature_sensors")
public class TemperatureSensor extends Sensors {
    private double tempVal;

    protected TemperatureSensor() {
        super();
    }
    public TemperatureSensor(String id) {
        super(id);
    }

    public TemperatureSensor(String id, double val) {
        super(id);
        tempVal = val;
    }

    public double getVal() {
        return tempVal;
    }

    public void setVal(double val) {
        tempVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }
}
