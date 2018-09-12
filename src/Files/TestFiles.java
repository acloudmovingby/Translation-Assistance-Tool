/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.Arrays;

/**
 *
 * @author Chris
 */
public class TestFiles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    // first comparefile
        CompareFile cf = new CompareFile();
        TMCompareEntry a;
        // third entry in comparefile
        a = new TMCompareEntry();
        a.setThai("การเมืองไม่ได้");
        a.setEnglish("politicsnot");
        a.addMatchInterval(0, 7);
        a.setFileName("POLITICS2");
        System.out.println(a);
        cf.addEntry(a);   
            System.out.println(a);
        // first entry in compare file
        a = new TMCompareEntry();
        a.setThai("การเมือง");
        a.setEnglish("politics");
        a.addMatchInterval(0, 3);
        a.setFileName("POLITICS");
        cf.addEntry(a);
            System.out.println(a);
        // second entry in compare file
        a = new TMCompareEntry();
        a.setThai("อย่างอื่น");
        a.setEnglish("something else");
        a.addMatchInterval(1, 5);
        a.setFileName("WOAHDADDY");
        cf.addEntry(a);
            System.out.println(a);
        
        
    // second comparefile
        CompareFile cf2 = new CompareFile();
        //first entry in comparefile
        TMCompareEntry a2 = new TMCompareEntry();
        a2.setThai("อย่างอื่น");
        a2.setEnglish("something else");
        a2.addMatchInterval(1, 5);
        a2.setFileName("WOAHDADDY");
        cf2.addEntry(a2);
            System.out.println(a2);
        // second entry in comparefile
        a2 = new TMCompareEntry();
        a2.setThai("การเมือง");
        a2.setEnglish("politics");
        a2.addMatchInterval(0, 3);
        a2.setFileName("POLITICS");
        cf2.addEntry(a2);
            System.out.println(a2);
        // third entry in comparefile
        a2 = new TMCompareEntry();
        a2.setThai("การเมืองไม่ได้");
        a2.setEnglish("politicsnot");
        a2.addMatchInterval(0, 7);
        a2.setFileName("POLITICS2");
        cf2.addEntry(a2);
            System.out.println(a2);
        
      
        
        System.out.println("***** THE COMPARE FILES *****");
        System.out.println(cf);
        System.out.println(cf2);
        
        System.out.println("***** LOOKING WITHIN *****");
        System.out.println("Are they equal? "+cf.equals(cf2));
        System.out.println("The toArray method on cf: \n\t" + Arrays.deepToString(cf.toArray()));
        System.out.println("The toArray method on cf2: \n\t" + Arrays.deepToString(cf2.toArray()));
        
          BasicFile bf = new BasicFile();
        TMEntryBasic x = new TMEntryBasic();
        x.setThai("การเมือง");
        x.setEnglish("politics");
        bf.addEntry(x);
        
        FileFactory ff = new FileFactory();
        BasicFile bf2 = ff.buildBasicParse("การเมือง", "politics");
    }
    
}
