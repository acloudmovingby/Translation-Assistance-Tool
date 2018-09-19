/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class CompareFileTest {
    
    public CompareFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of toArray method, of class CompareFile.
     *//*
    @Test
    public void testToArray() {
        System.out.println("toArray");
        CompareFile instance = new CompareFile();
        
        System.out.println("The first entry (การเมือง)");
        TMCompareEntry t = new TMCompareEntry();
        t.setThai("การเมือง");
        t.setEnglish("politics");
        t.setFileName("file1");
        t.addMatchInterval(0, 3);
        instance.addEntry(t);
         
        
        System.out.println("The second entry (ข้อที่สอง)");
        TMCompareEntry t2 = new TMCompareEntry();
        t2.setThai("ข้อที่สอง");
        t2.setEnglish("second section");
        t2.setFileName("file2");
        t2.addMatchInterval(1, 6);
        instance.addEntry(t2);
        
        System.out.println("ARE MY ENTRIES THE SAME? : " + t.equals(t2));
        
       
        
        Object[][] expResult = new Object[][] {
            {0, "ข้อที่สอง", "second section", "file2"},
            {1, "การเมือง", "politics", "file1"},
        };
        
        Object[][] result = instance.toArray();
        
        System.out.println("result is: \n\t" + Arrays.deepToString(result));
        System.out.println("expResult is: \n\t" + Arrays.deepToString(expResult));
        
        
        assertArrayEquals(expResult, result);
    }
*/

    
    /**
     * Tests to make sure two TMEntries can be added to CompareFile even if they have same MatchSize
     */
    @Test
    public void testAddEntry1() {
        System.out.println("addEntry1");
        
        CompareFile instance = new CompareFile();
        
        System.out.println("The first entry (การเมือง)");
        TMCompareEntry t = new TMCompareEntry();
        t.setThai("การเมือง");
        t.setEnglish("politics");
        t.setFileName("file1");
        t.addMatchInterval(0, 3);
        instance.addEntry(t);
         
        
        System.out.println("The second entry");
        TMCompareEntry t2 = new TMCompareEntry();
        t2.setThai("การเมืองAA");
        t2.setEnglish("politics");
        t2.setFileName("file1");
        t2.addMatchInterval(0, 4);
        instance.addEntry(t2);
        
        int expResult = 2;
        int result = instance.getTMs().size();
        
        assertEquals(expResult, result);
    }

    /**
     * Tests to make sure the list reorders properly according to match size
     */
    @Test
    public void testAddEntry2() {
        System.out.println("addEntry2");
        
        CompareFile result = new CompareFile();
        
        System.out.println("The first entry (การเมือง)");
        TMCompareEntry t = new TMCompareEntry();
        t.setThai("การเมืองCCC");
        t.setEnglish("politics");
        t.setFileName("file1");
        t.addMatchInterval(4, 5);
        t.addMatchInterval(6,7);
        result.addEntry(t);
         
        
        System.out.println("The second entry");
        TMCompareEntry t2 = new TMCompareEntry();
        t2.setThai("การเมืองAA");
        t2.setEnglish("politics");
        t2.setFileName("file1");
        t2.addMatchInterval(0, 7);
        result.addEntry(t2);
        
        System.out.println("The third entry");
        t2 = new TMCompareEntry();
        t2.setThai("การเมืองB");
        t2.setEnglish("politics");
        t2.setFileName("file1");
        t2.addMatchInterval(1, 4);
        result.addEntry(t2);
        
      //**********************
        CompareFile expResult = new CompareFile();
        
        System.out.println("The first entry (การเมือง)");
        t = new TMCompareEntry();
        t.setThai("การเมืองAA");
        t.setEnglish("politics");
        t.setFileName("file1");
        t.addMatchInterval(0, 7);
        expResult.addEntry(t);
         
        
        System.out.println("The second entry");
        t2 = new TMCompareEntry();
        t2.setThai("การเมืองB");
        t2.setEnglish("politics");
        t2.setFileName("file1");
        t2.addMatchInterval(1, 4);
        expResult.addEntry(t2);
        
        System.out.println("The third entry");
        t2 = new TMCompareEntry();
        t2.setThai("การเมืองCCC");
        t2.setEnglish("politics");
        t2.setFileName("file1");
        t2.addMatchInterval(4, 5);
        t2.addMatchInterval(6,7);
        expResult.addEntry(t2);
       
        System.out.println("result : \n" + result);
        System.out.println("expResult : \n" + expResult);
        assertEquals(expResult, result);
    }
    

    
}
