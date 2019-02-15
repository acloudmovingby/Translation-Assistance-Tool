/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.TestObjectBuilder;
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
    
    State emptyState;
    State state1;
    
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
        emptyState = TestObjectBuilder.getEmptyState();
        state1 = TestObjectBuilder.getTestState();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of push method, of class UndoManager.
     */
    @Test
    public void testPush() {
        System.out.println("push");
        State state = null;
        UndoManager instance = new UndoManager();
        instance.push(state);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pop method, of class UndoManager.
     */
    @Test
    public void testPop() {
        System.out.println("pop");
        UndoManager instance = new UndoManager();
        State expResult = null;
        State result = instance.pop();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
