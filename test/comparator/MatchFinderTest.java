/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Files.CompareFile;
import Files.FileList;
import Files.TUEntryBasic;
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
public class MatchFinderTest {
    
    public MatchFinderTest() {
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
     * Test of basicMatch method, of class MatchFinder.
     */
    @Test
    public void testBasicMatch() {
        System.out.println("basicMatch");
        
        
        TUEntryBasic seg = null;
        int minMatchLength = 0;
        FileList corpus = null;
        MatchFinder instance = new MatchFinder();
        CompareFile expResult = null;
        CompareFile result = instance.basicMatch(seg, minMatchLength, corpus);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exactMatch method, of class MatchFinder.
     */
    @Test
    public void testExactMatch() {
        System.out.println("exactMatch");
        String text = "";
        MatchFinder instance = new MatchFinder();
        CompareFile expResult = null;
        CompareFile result = instance.exactMatch(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of complexMatch method, of class MatchFinder.
     */
    @Test
    public void testComplexMatch() {
        System.out.println("complexMatch");
        TUEntryBasic seg = null;
        MatchFinder instance = new MatchFinder();
        CompareFile expResult = null;
        CompareFile result = instance.complexMatch(seg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
