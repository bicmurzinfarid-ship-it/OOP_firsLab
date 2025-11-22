package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction,Externalizable {
    private FunctionPoint[] points;
    private int size;

    public ArrayTabulatedFunction() {}

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        size = pointsCount;
        if (size<2){
            throw new  IllegalArgumentException("The number of elements is less than 2");
        }
        if (leftX >= rightX){
            throw new  IllegalArgumentException(leftX+" bigger than "+rightX);
        }
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        this.points = new FunctionPoint[values.length];
        size = values.length;

        if (size<2){
            throw new  IllegalArgumentException("The number of elements is less than 2");
        }
        if (leftX >= rightX){
            throw new  IllegalArgumentException(leftX+"bigger than"+rightX);
        }

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] array) {
        if (array.length < 2) {
            throw new IllegalArgumentException("At least 2 points are required");
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i].getX() <= array[i - 1].getX()) {
                throw new IllegalArgumentException("Points must be strictly increasing by X coordinate");
            }
        }

        this.points = new FunctionPoint[array.length];
        for (int i = 0; i < array.length; i++) {
            this.points[i] = new FunctionPoint(array[i].getX(), array[i].getY());
        }
        this.size = array.length;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException{
        size = in.readInt();
        points = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public double getLeftDomainBorder(){
        return this.points[0].getX();
    }
    public double getRightDomainBorder(){
        return this.points[size - 1].getX();
    }

    public double getFunctionValue(double x){
        if (x<this.points[0].getX() || x>this.points[size - 1].getX()) {
            return Double.NaN;
        }
        int i = 0;
        while (i < size && x > points[i].getX()) i++;
        if (Math.abs(this.points[i].getX() - x) < 1e-9) {
            return this.points[i].getY();
        }
        double x1 = this.points[i-1].getX();
        double y1 = this.points[i-1].getY();
        double x2 = this.points[i].getX();
        double y2 = this.points[i].getY();

        return (y1+((x-x1)*(y2-y1))/(x2-x1));
    }

    public int getPointsCount(){
        return size;
    }
    public FunctionPoint getPoint(int index){
        if (index<0||index> this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range"+index+" bigger than "+ size);
        }
        return (new FunctionPoint(this.points[index].getX(),this.points[index].getY()));
    }

    public void setPoint(int index, FunctionPoint point) {
        double newX = point.getX();

        if (index > 0 && newX <= points[index - 1].getX()) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        if (index < points.length - 1 && newX >= points[index + 1].getX()) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }

        points[index] = new FunctionPoint(newX, point.getY());
    }

    public double getPointX(int index) {
        if (index<0||index> this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index<0||index> this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        if (index > 0 && x <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("X incorrect");
        }
        if (index < points.length - 1 && x >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("X incorrect");
        }

        points[index].setX(x);
    }

    public double getPointY(int index){

        if (index<0||index>this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }

        return points[index].getY();
    }

    public void setPointY(int index, double y){

        if (index<0||index> this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }

        points[index].setY(y);
    }

    public void deletePoint(int index){

        if (index<0||index> this.size - 1){
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        if (this.size<3){
            throw new IllegalArgumentException();
        }

       System.arraycopy(points,index+1,points,index,points.length-index-1);
       points[--size] = null;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        double x = point.getX();
        
        for (int i = 0; i < size; i++) {
            if (Math.abs(points[i].getX() - x) < 1e-9)
                throw new InappropriateFunctionPointException("X incorrect");;
        }
        
        if (size == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, size);
            points = newPoints;
        }
        
        int index = 0;
        while (index < size && points[index].getX() < x) {
            index++;
        }

        System.arraycopy(points, index, points, index + 1, size - index);
        
        points[index] = new FunctionPoint(point.getX(), point.getY());
        size++;
    }
}