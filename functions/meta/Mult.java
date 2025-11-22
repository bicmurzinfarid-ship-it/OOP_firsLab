package functions.meta;

import functions.Function;

public class Mult implements Function {

    final Function first, second;

    public Mult(Function first, Function second){
        this.first = first;
        this.second = second;
    }
    public double getFunctionValue(double x) {
        return this.first.getFunctionValue(x)*this.second.getFunctionValue(x);
    }

    @Override
    public double getRightDomainBorder() {
        return Math.max(this.first.getRightDomainBorder(),this.second.getRightDomainBorder());
    }

    @Override
    public double getLeftDomainBorder() {
        return Math.min(this.first.getLeftDomainBorder(),this.second.getLeftDomainBorder());
    }
}


