/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndoManager;

import Files.BasicFile;
import Files.FileBuilder;
import State.State;
import State.StateForTesting;
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
public class UndoManagerTest {
    
    public UndoManagerTest() {
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
     * Test of pushState method, of class UndoManager.
     */
    @Test
    public void testPushState() {
        System.out.println("pushState");
        State state = null;
        UndoManager instance = new UndoManager();
        instance.pushState(state);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of undo method, of class UndoManager.
     */
    @Test
    public void testUndo() {
        State state = new StateForTesting(FileBuilder.getTestFile(), FileBuilder.getTestCorpus());
        
        System.out.println("undo");
        State state = null;
        UndoManager instance = new UndoManager();
        instance.undo(state);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copyBasicFile method, of class UndoManager.
     */
    @Test
    public void testCopyBasicFile() {
        System.out.println("copyBasicFile");
         
        BasicFile original = FileBuilder.getTestFile();
        BasicFile copy = UndoManager.copyBasicFile(original);
        
        assertEquals(original, copy);
        
        original.getActiveSegs().clear();
        
        assert(original.getActiveSegs().isEmpty());
        assert(! copy.getActiveSegs().isEmpty());
    }
    
}
