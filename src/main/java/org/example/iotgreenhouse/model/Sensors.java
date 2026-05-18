package org.example.iotgreenhouse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sensors implements GreenHouseElement {
    @Id
    private String id;

    private boolean isActive;

    protected Sensors() {};
    public Sensors(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
