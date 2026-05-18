package model;

public class TemperatureSensor extends Sensors {
    private int tempVal;
    public TemperatureSensor(String id) {
        super(id);
    }

    public int getVal() {
        return tempVal;
    }

    public void setVal(int val) {
        tempVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
