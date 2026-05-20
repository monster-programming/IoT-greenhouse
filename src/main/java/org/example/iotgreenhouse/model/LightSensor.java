package org.example.iotgreenhouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "light_sensors")
public class LightSensor extends Sensors {
    private double lightVal;

    protected LightSensor() {
        super();
    }

    public LightSensor(String id) {
        super(id);
    }

    public LightSensor(String id, double val) {
        super(id);
        lightVal = val;
    }

    public double getVal() {
        return lightVal;
    }

    public void setVal(double val) {
        lightVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }
}