package model;

public interface GreenHouseElement {
    void accept(GreenHouseVisitor v);
}
