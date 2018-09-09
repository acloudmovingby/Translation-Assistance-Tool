/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

/**
 *
 * @author Chris
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String t1 = "ขขข\ntการเมือง\nคคคค\nลังเลตลอด";
        String t2 = "ฟหกดกดก\nการเมืองA\nกกกกกกกก\nการเมืองB\nการเมืองC\nไำไพไพ\nไำพำไำะไำะ\nลังเลตลอด";
        Comparator c = new Comparator(t1, t2, 4);
        
      
        
        /*DisplayMatches d = new DisplayMatches(c);
        System.out.println("\n****************\n");
        System.out.println(d.upperCase());*/
        
        System.out.println("\n****************\n");
        System.out.println(c.matches);
    }
    
}
