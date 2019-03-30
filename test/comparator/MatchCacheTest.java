/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import State.State;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Chris
 */
public class MatchCacheTest {
    
    State committedState;
    
    public MatchCacheTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        committedState = TestObjectBuilder.getCommittedTestState();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test to make sure nothing changes if the minimum length is "changed" to the same length;
     * 
     */
    @Test
    public void testMinLengthChangedEquals() {
        
    }
    
    /**
     * Test to of what happens when you increase the minimum length.
     * 
     */
    @Test
    public void testMinLengthChangedIncrease() {
        
    }

    /**
     * Test of what happens when you decrease the minimum length.
     */
    @Test
    public void testMinLengthDecreased() {
    }

    /**
     * Tests adding a segment that has no common substrings with any of the segments whose matchlists are stored in the cache.
     * 
     * In other words, no changes should happen to any of the match lists.
     */
    @Test
    public void testAddSegmentNoMatch() {
    }

    /**
     * Tests adding a segment that has common substrings with some segments in the main file, but none of which reach the current minimum match length.
     * This should end up not changing any of the match lists (assuming the min match length is also not changed).
     */
    @Test
    public void testAddSegmentNotLongEnough() {
    }
    
    /**
     * Tests adding a segment that has common substrings with some segments in the main file, some of which are at least the current minimum match length.
     * This will change some of those match lists, affecting those where the match length exceeds the current minimum.
     */
    @Test
    public void testAddSegmentLongEnough() {
    }
    
    /**
     * Tests adding a segment that makes no change initially (because the matches are too short), but then after the min match length is reduced, now that segment appears on the MatchLists.
     */
    @Test
    public void testAddSegThenChangeLength() {
    }

    /**
     * Tests removing a segment from the committed corpus that shouldn't affect any of the MatchLists because it isn't currently contained in any of them.
     */
    @Test
    public void testRemoveSegmentNoChange() {
    }
    
    /**
     * Tests removing a segment from the committed corpus that does affect some of the MatchLists because some do currently contain that segment.
     */
    @Test
    public void testRemoveSegmentCausesChange() {
    }
    
    
    
}
