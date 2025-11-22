package functions.basic;

import functions.meta.Composition;

public class Log implements functions.Function {
    private double base;

    public Log(double base) {
        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return Math.log(x) / Math.log(base);
    }

}