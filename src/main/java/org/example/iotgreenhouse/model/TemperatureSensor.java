package org.example.iotgreenhouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "temperature_sensors")
public class TemperatureSensor extends Sensors {
    private int tempVal;

    protected TemperatureSensor() {}
    public TemperatureSensor(String id) {
        super(id);
    }

    public TemperatureSensor(String id, int val) {
        super(id);
        tempVal = val;
    }

    public int getVal() {
        return tempVal;
    }

    public void setVal(int val) {
        tempVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }
}
