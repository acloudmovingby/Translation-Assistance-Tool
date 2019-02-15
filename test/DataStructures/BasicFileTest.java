/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

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
public class BasicFileTest {
    
    public BasicFileTest() {
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
     * Test of newSeg method, of class BasicFile.
     */
    @Test
    public void testEquals() {
        System.out.println("Equals");
        BasicFile f1 = new BasicFile();
        BasicFile f2 = new BasicFile(f1.getFileID(), f1.getFileName());
        assertEquals(f1, f2);
        
        // added a seg to f1, so they should no longer be equals
        Segment newSeg = TestObjectBuilder.getTestSeg();
        f1.getActiveSegs().add(newSeg);
        assertEquals(f1.equals(f2), false);
        
        // added the same seg to f2. Now the should be equal
        f2.getActiveSegs().add(newSeg);
        assertEquals(f1, f2);
        
        // Now added a seg to removedSegs in f1. Not equal
        Segment removedSeg = TestObjectBuilder.getTestSeg();
        f1.getRemovedSegs().add(removedSeg);
        assertEquals(f1.equals(f2), false);
        
        // Now added the same seg to f2's removed segs. Equal
        f2.getRemovedSegs().add(removedSeg);
        assertEquals(f1, f2);
    }
    
    /**
     * Tests that constructing a BasicFile based on another BasicFile actually makes two entirely distinct objects (no pointers to the other).
     */
    @Test
    public void testCopyFile() {
        BasicFile testFile = TestObjectBuilder.getTestFile();
        BasicFile testFileCopy = new BasicFile(testFile);
        
        
    }

   
    
}
