package model;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sensors implements GreenHouseElement {
    @Id
    private String id;

    protected Sensors() {};
    public Sensors(String id) {
        this.id = id;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
