package model;

import jakarta.persistence.*;

@Entity
@Table(name = "actuators")
public class Actuator implements GreenHouseElement{
    @Id
    String id;

    @Enumerated(EnumType.STRING)
    ActuatorType type;

    boolean isActive;

    protected Actuator() {}

    public Actuator(String id, ActuatorType type, boolean isActive) {
        this.id = id;
        this.type = type;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }
}
