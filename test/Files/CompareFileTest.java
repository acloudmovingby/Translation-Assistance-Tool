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
     * Test of getTMs method, of class CompareFile.
     */
    @Test
    public void testGetTMs() {
        System.out.println("getTMs");
        CompareFile instance = new CompareFile();
        ArrayList<TMEntry> expResult = null;
        ArrayList<TMEntry> result = instance.getTMs();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



    /**
     * Test of toArray method, of class CompareFile.
     */
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
            {"ข้อที่สอง", "second section", "file2"},
            {"การเมือง", "politics", "file1"},
        };
        
        Object[][] result = instance.toArray();
        
        System.out.println("result is: \n\t" + Arrays.deepToString(result));
        System.out.println("expResult is: \n\t" + Arrays.deepToString(expResult));
        
        
        assertArrayEquals(expResult, result);
    }



    
}
