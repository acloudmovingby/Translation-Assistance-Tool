/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

/**
 *
 * @author Chris
 */
public class TestMain {

    public static void main(String[] args) {
       
        String orig = ".a   bcd..abcd..a.b..\n...c..  ...d.!";
        
                // "[.]{2,}"
        System.out.println(orig + "\n\n");
        orig = orig.replaceAll("[ !\t\n\f\r_]", "");
        orig = orig.replaceAll("[.]{2,}", "");
        System.out.println(orig);

    }

}
