/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Files.BasicFile;
import Files.MatchFile;
import Files.FileList;
import Files.MatchSegment;
import Files.Segment;
import java.util.List;
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
     *
    @Test
    public void testBasicMatch() {
        System.out.println("basicMatch");
        FileList fl = new FileList();
        BasicFile bf1 = new BasicFile();
        Segment newSeg = bf1.newSeg();
        newSeg.setThai("test");
        newSeg.setCommitted(true);
        fl.addFile(bf1);
        
        BasicFile bf2 = new BasicFile();
        Segment testSeg = bf2.newSeg();
        testSeg.setThai("test");
        
        // test that seg1 is returned
        MatchFile mFile = MatchFinder.basicMatch(testSeg, 3, fl);
        MatchSegment me = (mFile.getObservableList()).get(0);
        
        assertEquals("test", me.getThai());
      
        // test that both seg1 and seg2 are returned
        Segment seg2 = bf1.newSeg();
        seg2.setThai("aaestcc");
        seg2.setCommitted(true);
        mFile = MatchFinder.basicMatch(testSeg, 3, fl);
        assertEquals(2, mFile.getObservableList().size());
        assertEquals("test", mFile.getObservableList().get(0).getThai());
        assertEquals("aaestcc", mFile.getObservableList().get(1).getThai());
        
        // test that seg3 isn't returned
        Segment seg3 = bf1.newSeg();
        seg3.setThai("tepstflip");
        seg3.setCommitted(true);
        mFile = MatchFinder.basicMatch(testSeg, 3, fl);
        assertEquals(2, mFile.getObservableList().size());
        assertEquals("test", mFile.getObservableList().get(0).getThai());
        assertEquals("aaestcc", mFile.getObservableList().get(1).getThai());
        
        
        
    }
    */

    /**
     * Test of exactMatch method, of class MatchFinder.
     */
    @Test
    public void testExactMatch() {
        System.out.println("exactMatch");
        String text = "";
        MatchFinder instance = new MatchFinder();
        MatchFile expResult = null;
        MatchFile result = instance.exactMatch(text);
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
        Segment seg = null;
        MatchFinder instance = new MatchFinder();
        MatchFile expResult = null;
        MatchFile result = instance.complexMatch(seg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
