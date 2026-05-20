package org.example.iotgreenhouse.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sensors implements GreenHouseElement {
    @Id
    private String id;

    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    @JsonIgnore
    private Zone zone;

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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
