/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.Segment;
import State.State;
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
public class MatchManagerTest {
    
    public MatchManagerTest() {
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
     * Test of basicMatch method, of class MatchManager.
     */
    @Test
    public void testBasicMatch() {
        System.out.println("basicMatch");
        Segment seg = null;
        State state = null;
        MatchManager instance = null;
        MatchList expResult = null;
        MatchList result = instance.basicMatch(seg, state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of minMatchLengthChanged method, of class MatchManager.
     *  Makes sure that when the match length is changed, that the MatchLists change accordingly.
     * Test if the min length is shortened (MatchLists should grow bigger) as well as if it's lengthened (MatchLists become smaller).
     */
    @Test
    public void testMinMatchLengthChanged() {
        System.out.println("minMatchLengthChanged");
        int minMatchLength = 0;
        MatchManager instance = null;
        instance.minMatchLengthChanged(minMatchLength);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of includeSegmentInMatches method, of class MatchManager.
     * It tests this in 4 ways: first, by adding a segment that doesn't affect matchlists (one case where matches exist but the length is not sufficient, and the second case where no match exists). Second, by adding a segment that does affect the matchlists. Third, by re-adding that segment to make sure no duplicates are created. And lastly, by adding an "empty" segment to make sure no bugs are created by this.
     */
    @Test
    public void testIncludeSegmentInMatches() {
        System.out.println("includeSegmentInMatches");
        Segment seg = null;
        MatchManager instance = null;
        instance.includeSegmentInMatches(seg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeSegmentFromMatches method, of class MatchManager.
     */
    @Test
    public void testRemoveSegmentFromMatches() {
        System.out.println("removeSegmentFromMatches");
        Segment seg = null;
        MatchManager instance = null;
        instance.removeSegmentFromMatches(seg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
