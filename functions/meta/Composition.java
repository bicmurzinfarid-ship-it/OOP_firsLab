package functions.meta;
import functions.Function;

public class Composition implements Function {
    private Function first, second;

    public Composition(Function f1, Function f2) {
        this.first = f1;
        this.second = f2;
    }

    public double getLeftDomainBorder() {
        return this.first.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return this.first.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return this.second.getFunctionValue(this.first.getFunctionValue(x));
    }
}