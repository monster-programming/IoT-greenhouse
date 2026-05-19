package org.example.iotgreenhouse.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
public class Zone {
    @Id
    String id;

    private String name;

    @OneToMany(mappedBy = "zone", fetch = FetchType.EAGER)
    List<Sensors> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "zone", fetch = FetchType.EAGER)
    List<Actuator> actuators = new ArrayList<>();

    protected Zone() {}

    public Zone(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Sensors> getSensors() {
        return sensors;
    }

    public List<Actuator> getActuators() {
        return actuators;
    }
}
