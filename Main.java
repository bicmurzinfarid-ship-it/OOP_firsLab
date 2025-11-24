import functions.*;

import functions.*;
import functions.basic.*;
import functions.meta.*;

import java.io.*;

import static functions.Functions.power;
import static functions.Functions.sum;
import static functions.TabulatedFunctions.*;

public class Main {
    public static void main(String[] args) {
        try {
            Cos cs = new Cos();
            Sin sn = new Sin();

            System.out.print("Cos: ");
            for(double i = 0;i<Math.PI*2; i+=0.1 ){
                System.out.print("("+i+","+cs.getFunctionValue(i)+"), ");
            }

            System.out.print("\nSin: ");
            for(double i = 0;i<Math.PI*2; i+=0.1 ){
                System.out.print("("+i+","+sn.getFunctionValue(i)+"), ");
            }

            TabulatedFunction TabCs = tabulate(cs,0,Math.PI*2,10);
            TabulatedFunction TabSn = tabulate(sn,0, Math.PI*2,10);

            System.out.print("\nTabCos: ");
            for (double i = 0; i<2*Math.PI;i+=0.1 ){
                System.out.print("("+i+","+TabCs.getFunctionValue(i)+"), ");
            }

            System.out.print("\nTabSin: ");
            for (double i = 0; i<2*Math.PI;i+=0.1 ){
                System.out.print("("+i+","+TabSn.getFunctionValue(i)+"), ");
            }
            Function sinSquared = power(TabSn, 2);
            Function cosSquared = power(TabCs, 2);
            Function sumSquards = sum(sinSquared,cosSquared);

            System.out.print("\nsin^2+cos^2: ");
            for (double i = 0; i<2*Math.PI;i+=0.1 ){
                System.out.print("("+i+","+sumSquards.getFunctionValue(i)+"), ");
            }

            Exp E = new Exp();
            TabulatedFunction TabE = tabulate(E,0,10,11);
            System.out.print("\nTabOldE: ");
            for (double i = 0; i<2*Math.PI;i+=0.1 ){
                System.out.print("("+i+","+TabE.getFunctionValue(i)+"), ");
            }
            try(Writer fout = new FileWriter("testE.txt")){
                writeTabulatedFunction(TabE,fout);
            }
            catch (IOException e){
                System.err.print(e.getMessage());
            }
            try(Reader fin = new FileReader("testE.txt")){
                TabulatedFunction newE = readTabulatedFunction(fin);

                System.out.print("\nTabNewE: ");
                for (double i = 0; i<2*Math.PI;i+=0.1 ){
                    System.out.print("("+i+","+ newE.getFunctionValue(i)+"), ");
                }
            }
            catch (IOException e){
                System.err.print(e.getMessage());
            }
            Log lg = new Log(Math.E);
            TabulatedFunction TabLn = tabulate(lg,0,10,11);

            System.out.print("\nOldLn: ");
            for (double i = 0; i<2*Math.PI;i+=0.1 ){
                System.out.print("("+i+","+ TabLn.getFunctionValue(i)+"), ");
            }

            try(OutputStream fout = new FileOutputStream("testLog.txt")){
                outputTabulatedFunction(TabLn,fout);
            } catch (IOException e){
                System.err.print(e.getMessage());
            }

            System.out.print("\nNewLn: ");
            try(InputStream fin = new FileInputStream("testLog.txt")){
                TabulatedFunction newTabE = inputTabulatedFunction(fin);
                for (double i = 0; i<2*Math.PI;i+=0.1 ){
                    System.out.print("("+i+","+ newTabE.getFunctionValue(i)+"), ");
                }
            }

            System.out.print("\nOldLn: ");

            for (double i = 0; i<11;i++ ){
                System.out.print("("+i+","+ lg.getFunctionValue(i)+"), ");
            }

            try(FileOutputStream out = new FileOutputStream("serializate.txt")){
                ObjectOutputStream oout = new ObjectOutputStream(out);
                oout.writeObject(TabLn);
            }
            catch (IOException e){
                System.err.print(e.getMessage());
            }

            try(FileInputStream in = new FileInputStream("serializate.txt")){
                ObjectInputStream oin = new ObjectInputStream(in);
                TabulatedFunction newLg= (TabulatedFunction)oin.readObject();
                System.out.print("\nNewLn: ");

                for (double i = 0; i<11;i++ ){
                    System.out.print("("+i+","+ newLg.getFunctionValue(i)+"), ");
                }
            }
            catch (IOException e){
                System.err.print(e.getMessage());
            }

        }catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Illegal arg"+e.getMessage());

        }



    }
}