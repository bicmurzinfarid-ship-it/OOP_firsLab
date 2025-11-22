package functions.meta;
import functions.Function;

public class Shift implements Function {
    private Function f;
    private double shiftX, shiftY;

    public Shift(Function f, double shiftX, double shiftY) {
        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {
        return this.f.getLeftDomainBorder() - this.shiftX;
    }

    public double getRightDomainBorder() {
        return this.f.getRightDomainBorder() - this.shiftX;
    }

    public double getFunctionValue(double x) {
        return this.shiftY + this.f.getFunctionValue(x - this.shiftX);
    }
}
