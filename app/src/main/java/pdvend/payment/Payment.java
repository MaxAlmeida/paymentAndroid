package pdvend.payment;

/**
 * Created by max on 02/11/16.
 */

public class Payment {
    private String type;
    private double value;

    public Payment(String type, double value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {

        return type;
    }

    public double getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

