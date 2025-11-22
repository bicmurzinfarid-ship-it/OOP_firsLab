import functions.*;

import functions.*;
import functions.basic.*;

import java.io.*;

import static functions.Functions.power;
import static functions.Functions.sum;
import static functions.TabulatedFunctions.*;

public class Main {
    public static void main(String[] args) {
        try {
           Cos cs = new Cos();
           TabulatedFunction TabCs = tabulate(cs,0,10,11);
           try(java.io.FileOutputStream out = new  java.io.FileOutputStream( "cosout.txt")){
               FileWriter(out);
            }
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Illegal arg"+e.getMessage());

        }



    }
}