package org.example.iotgreenhouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "humidity_sensors")
public class HumiditySensor extends Sensors {
    private int humidityVal;

    protected HumiditySensor() {}
    public HumiditySensor(String id) {
        super(id);
    }

    public int getVal() {
        return humidityVal;
    }

    public void setVal(int val) {
        humidityVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }
}
