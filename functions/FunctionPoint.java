package functions;

public class FunctionPoint {
    private double x;
    private double y;

    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }

    public void setX(double newX){this.x = newX;}
    public void setY(double newY){this.y = newY;}
}
