package functions;

import java.io.*;


public class TabulatedFunctions {
    private TabulatedFunctions() {
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("The tabulation boundaries extend beyond the function's definition area");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        DataOutputStream dout = new DataOutputStream(out);
        try {
            dout.writeInt(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); i++) {
                dout.writeDouble(function.getPointX(i));
                dout.writeDouble(function.getPointY(i));
            }
            dout.flush();
        } catch (IOException e) {
            throw new RuntimeException("IO error in outputTabulatedFunction", e);
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in){
        DataInputStream din  =new DataInputStream(in);
        try{
            int pointsCount = din.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i<pointsCount;i++){
                double x = din.readDouble();
                double y = din.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        PrintWriter prt = new PrintWriter(out);
        try {
            prt.print(function.getPointsCount()+"\n");
            for (int i = 0; i < function.getPointsCount(); i++) {
                prt.print(function.getPointX(i)+" ");
                prt.print(function.getPointY(i)+"\n");
            }
            prt.flush();
        } catch (RuntimeException e) {
            throw new RuntimeException("error in writeTabulatedFunction", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in){
        StreamTokenizer sin = new StreamTokenizer(in);
        sin.resetSyntax();
        sin.wordChars('0','9');
        sin.wordChars('.','.');
        sin.wordChars('-','-');
        sin.whitespaceChars(' ', ' ');
        sin.whitespaceChars('\n','\n');
        sin.whitespaceChars('\r','\r');
        sin.parseNumbers();

        try{
            sin.nextToken();
            int pointsCount = (int)sin.nval;
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i<pointsCount;i++){
                sin.nextToken();
                double x = sin.nval;
                sin.nextToken();
                double y = sin.nval;
                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}