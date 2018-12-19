/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Files.CompareFile;
import java.util.ArrayList;
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
public class ComparatorTest {
    
    public ComparatorTest() {
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
     * Test of findStringMatches method, of class OrigComparator.
     */
    @Test
    public void testFindMatches1() {
        System.out.println("findMatches1");
        String t1 = "xxabcdxx";
        String t2 = "yyyabcdyabcdy";
        OrigComparator instance = new OrigComparator(t1, t2, 4);
        Matches result = instance.findStringMatches(t1, t2);
        
        Matches expResult = new Matches();
        expResult.addMatch("abcd", 2, 3);
        expResult.addMatch("abcd", 2, 8);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of findStringMatches method, of class OrigComparator.
     */
    @Test
    public void testFindMatches2() {
        System.out.println("findMatches2");
        String t1 = "abcdxx";
        String t2 = "yyyabcdyabcdy";
        OrigComparator instance = new OrigComparator(t1, t2, 4);
        Matches result = instance.findStringMatches(t1, t2);
        
        Matches expResult = new Matches();
        expResult.addMatch("abcd", 0, 3);
        expResult.addMatch("abcd", 0, 8);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findStringMatches method, of class OrigComparator.
     */
    @Test
    public void testFindMatches3() {
        System.out.println("findMatches3");
        String t1 = "abcdxx";
        String t2 = "yyyabcdyabcdy";
        OrigComparator instance = new OrigComparator(t1, t2, 5);
        Matches result = instance.findStringMatches(t1, t2);
        
        Matches expResult = new Matches();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findStringMatches method, of class OrigComparator.
     */
    @Test
    public void testFindMatches4() {
        System.out.println("findMatches4");
        String t1 = "abcabc";
        String t2 = "abcabcabcabc";
        OrigComparator instance = new OrigComparator(t1, t2, 3);
        Matches result = instance.findStringMatches(t1, t2);
        
        Matches expResult = new Matches();
        expResult.addMatch("abcd", 0, 0);
        expResult.addMatch("abcd", 0, 6);
        
        assertEquals(expResult, result);
    }
    
    
    
    
}
