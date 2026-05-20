package org.example.iotgreenhouse.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "actuators")
public class Actuator implements GreenHouseElement{
    @Id
    String id;

    @Enumerated(EnumType.STRING)
    ActuatorType type;

    boolean isActive;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    @JsonIgnore
    private Zone zone;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActuatorType getType() {
        return type;
    }

    public void setType(ActuatorType type) {
        this.type = type;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public void accept(GreenHouseVisitor v) {
        v.visit(this);
    }


}
