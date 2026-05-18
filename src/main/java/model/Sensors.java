package model;

public abstract class Sensors implements GreenHouseElement {
    private final String id;

    public Sensors(String id) {
        this.id = id;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
