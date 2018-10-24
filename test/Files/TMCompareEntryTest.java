/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

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
public class TMCompareEntryTest {
    
    TUCompareEntry e;
    
    public TMCompareEntryTest() {
        e = new TUCompareEntry();
        e.setThai("การเมือง");
        e.setEnglish("politics");
        e.addMatchInterval(0, 2);
        e.addMatchInterval(3, 4);
        e.setFileName("file1");
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
     * Test of getCopy method, of class TUCompareEntry.
     */
    @Test
    public void testGetCopy() {
        System.out.println("getCopy");
        TUCompareEntry instance = new TUCompareEntry();
        
        TUCompareEntry e2 = new TUCompareEntry();
        e2.setThai("การเมือง");
        e2.setEnglish("politics");
        e2.addMatchInterval(0, 2);
        e2.addMatchInterval(3, 4);
        e2.setFileName("file1");
        
        TUCompareEntry e3 = e2.getCopy();
        
        boolean result = 
                e2.equals(e3) &&
                e2 != e3;
        
        boolean expResult = true;
        assertEquals(expResult, result); 
    }

    /**
     * Test of getMatchIntervalsArray method, of class TUCompareEntry.
     */
    @Test
    public void testGetMatchIntervalsArray() {
        System.out.println("getMatchIntervalsArray");
        int[][] expResult = new int[][] {
            {0,2},
            {3,4}
        };
        int[][] result = e.getMatchIntervalsArray();
        System.out.println("expResult array is \t" + Arrays.deepToString(expResult));
        System.out.println("result array is \t" + Arrays.deepToString(result));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getMatchSize method, of class TUCompareEntry.
     */
    @Test
    public void testGetMatchSize() {
        System.out.println("getMatchSize");
        int expResult = 5;
        int result = e.getMatchSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class TUCompareEntry.
     */
    @Test
    public void testToArray() {
        System.out.println("toArray");
        Object[] expResult = new Object[] {
            "การเมือง", "politics", "file1"
        };
        Object[] result = e.toArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of equals method, of class TUCompareEntry.
     */
    @Test
    public void testEquals() {
        
        TUCompareEntry e2 = new TUCompareEntry();
        e2.setThai("การเมือง");
        e2.setEnglish("politics");
        e2.addMatchInterval(0, 2);
        e2.addMatchInterval(3, 4);
        e2.setFileName("file1");
        
        // CHANGED INTERVALS
        TUCompareEntry e3 = new TUCompareEntry();
        e3.setThai("การเมือง");
        e3.setEnglish("politics");
        e3.addMatchInterval(0, 3);
        e3.addMatchInterval(3, 4);
        e3.setFileName("file1");
        
        // CHANGED INTERVALS
        TUCompareEntry e4 = new TUCompareEntry();
        e4.setThai("การเมือง");
        e4.setEnglish("politics");
        e4.addMatchInterval(0, 2);
        e4.addMatchInterval(2, 4);
        e4.setFileName("file1");
        
        //CHANGED THAI
        TUCompareEntry e5 = new TUCompareEntry();
        e5.setThai("CHANGED");
        e5.setEnglish("politics");
        e5.addMatchInterval(0, 2);
        e5.addMatchInterval(3, 4);
        e5.setFileName("file1");
        
        // CHANGED ENGLISH
        TUCompareEntry e6 = new TUCompareEntry();
        e6.setThai("การเมือง");
        e6.setEnglish("CHANGED");
        e6.addMatchInterval(0, 2);
        e6.addMatchInterval(3, 4);
        e6.setFileName("file1");
        
        // CHANGED FILENAME
        TUCompareEntry e7 = new TUCompareEntry();
        e7.setThai("การเมือง");
        e7.setEnglish("politics");
        e7.addMatchInterval(0, 2);
        e7.addMatchInterval(3, 4);
        e7.setFileName("CHANGED");
        
        
        boolean expResult = true;
        boolean result = 
                e2.equals(e) &&
                !e2.equals(e3) &&
                !e2.equals(e4) &&
                !e2.equals(e5) &&
                !e2.equals(e6) &&
                !e2.equals(e7);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class TUCompareEntry.
     */
    @Test
    public void testHashCode() {
        
        // SAME AS e DEFINED ABOVE
        TUCompareEntry e2 = new TUCompareEntry();
        e2.setThai("การเมือง");
        e2.setEnglish("politics");
        e2.addMatchInterval(0, 2);
        e2.addMatchInterval(3, 4);
        e2.setFileName("file1");
        
        // CHANGED INTERVALS
        TUCompareEntry e3 = new TUCompareEntry();
        e3.setThai("การเมือง");
        e3.setEnglish("politics");
        e3.addMatchInterval(0, 3);
        e3.addMatchInterval(3, 4);
        e3.setFileName("file1");
        
        // CHANGED INTERVALS
        TUCompareEntry e4 = new TUCompareEntry();
        e4.setThai("การเมือง");
        e4.setEnglish("politics");
        e4.addMatchInterval(0, 2);
        e4.addMatchInterval(1, 4);
        e4.setFileName("file1");
        
        //CHANGED THAI
        TUCompareEntry e5 = new TUCompareEntry();
        e5.setThai("CHANGED");
        e5.setEnglish("politics");
        e5.addMatchInterval(0, 2);
        e5.addMatchInterval(3, 4);
        e5.setFileName("file1");
        
        // CHANGED ENGLISH
        TUCompareEntry e6 = new TUCompareEntry();
        e6.setThai("การเมือง");
        e6.setEnglish("CHANGED");
        e6.addMatchInterval(0, 2);
        e6.addMatchInterval(3, 4);
        e6.setFileName("file1");
        
        // CHANGED FILENAME
        TUCompareEntry e7 = new TUCompareEntry();
        e7.setThai("การเมือง");
        e7.setEnglish("politics");
        e7.addMatchInterval(0, 2);
        e7.addMatchInterval(3, 4);
        e7.setFileName("CHANGED");
        
        System.out.println(e2.hashCode() == e.hashCode());
        System.out.println(e2.hashCode() != e3.hashCode());
        System.out.println(e2.hashCode() != e4.hashCode());
        System.out.println(e2.hashCode() != e5.hashCode());
        System.out.println(e2.hashCode() != e6.hashCode());
        System.out.println(e2.hashCode() != e7.hashCode());
        
        boolean expResult = true;
        boolean result = 
                e2.hashCode() == e.hashCode() &&
                e2.hashCode() != e3.hashCode() &&
                e2.hashCode() != e4.hashCode() &&
                e2.hashCode() != e5.hashCode() &&
                e2.hashCode() != e6.hashCode() &&
                e2.hashCode() != e7.hashCode();
        
        assertEquals(expResult, result);
    }
    
}
