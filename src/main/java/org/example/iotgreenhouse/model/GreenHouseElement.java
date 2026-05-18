package org.example.iotgreenhouse.model;

public interface GreenHouseElement {
    void accept(GreenHouseVisitor v);
}
