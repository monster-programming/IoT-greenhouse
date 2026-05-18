package model;

public class HumiditySensor extends Sensors {
    private int humidityVal;
    public HumiditySensor(String id) {
        super(id);
    }

    public int getVal() {
        return humidityVal;
    }

    public void setVal(int val) {
        humidityVal = val;
    }

    @Override
    public void accept(GreenHouseVisitor v) {

    }
}
