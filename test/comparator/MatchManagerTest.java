/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;
    
import DataStructures.MatchSegment;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
public class MatchManagerTest {

    MatchManager matchManager;

    Segment segA;
    Segment segB;
    Segment segAB;

    // The pool of segs that can be searched in matches.
    HashSet<Segment> emptySet;
    HashSet<Segment> hasSegA;
    HashSet<Segment> hasSegB;
    HashSet<Segment> hasSegAandB;

    // possible results for match searches
    List<MatchSegment> emptyML;
    List<MatchSegment> matchWithA;
    List<MatchSegment> matchWithB;
    List<MatchSegment> matchWithAandB;

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
        SegmentBuilder sb = new SegmentBuilder();
        sb.setThai("AAAA");
        sb.setCommitted(true);
        segA = sb.createSegment();

        sb = new SegmentBuilder();
        sb.setThai("BBBB");
        sb.setCommitted(true);
        segB = sb.createSegmentNewID();

        sb = new SegmentBuilder();
        sb.setThai("AAAABBBB");
        sb.setCommitted(true);
        segAB = sb.createSegmentNewID();

        emptySet = new HashSet();
        hasSegA = new HashSet(Arrays.asList(segA));
        hasSegB = new HashSet(Arrays.asList(segB));
        hasSegAandB = new HashSet(Arrays.asList(segA, segB));

        // make MatchSegments, the matches array shows that all characters in the target Segment are a match to the source
        boolean[] matches = new boolean[4];
        for (int i = 0; i < matches.length; i++) {
            matches[i] = true;
        }
        MatchSegment matchSegA = new MatchSegment(segA);
        matchSegA.setMatches(matches);
        MatchSegment matchSegB = new MatchSegment(segB);
        matchSegB.setMatches(matches);

        //make MatchLists
        emptyML = new ArrayList();
        matchWithA = new ArrayList();
        matchWithA.add(matchSegA);
        matchWithB = new ArrayList();
        matchWithB.add(matchSegB);
        matchWithAandB = new ArrayList();
        matchWithAandB.add(matchSegA);
        matchWithAandB.add(matchSegB);
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests basicMatch in a MatchManager holding ZERO committed segments
     */
    @Test
    public void testGetWithEmptyMatchManager() {
        matchManager = new MatchManager(emptySet);
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));
    }

    /**
     * Tests basicMatch in a MatchManager holding ONE committed segment
     */
    @Test
    public void testGetOneCommittedSeg() {
        matchManager = new MatchManager(hasSegA);
        // test if no match should be expected
        assertEquals(new ArrayList(), matchManager.basicMatch(segB, 3));
        // test if match should be expected
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
    }

    /**
     * Tests basicMatch in a MatchManager holding TWO committed segments
     */
    @Test
    public void testGetTwoCommittedSegs() {
        matchManager = new MatchManager(hasSegAandB);
        // segA should match with segA
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
        // segB will match with segB
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
        // segAB will match with both
        assertEquals(matchWithAandB, matchManager.basicMatch(segAB, 3));
        assertEquals(2, matchManager.basicMatch(segAB, 3).size());
    }

    /**
     * Tests adding a segments to the pool of searchable segments where the
     * MatchManager currently holds ZERO committed segments.
     */
    @Test
    public void testAddSegToEmptyMatchManager() {
        matchManager = new MatchManager(emptySet);
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));

        matchManager.includeSegmentInMatches(segA);
        assertEquals(emptyML, matchManager.basicMatch(segB, 3));
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
    }

    /**
     * Tests adding a segments to the pool of searchable segments where the
     * MatchManager currently holds ONE committed segments.
     */
    @Test
    public void testAddSegWithOneCommittedSegment() {
        matchManager = new MatchManager(hasSegA);
        // test if no match should be expected
        assertEquals(new ArrayList(), matchManager.basicMatch(segB, 3));
        // test if match should be expected
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));

        matchManager.includeSegmentInMatches(segB);
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
        // segB will match with segB
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));
        // segAB will match with both
        assertEquals(matchWithAandB, matchManager.basicMatch(segAB, 3));
        assertEquals(2, matchManager.basicMatch(segAB, 3).size());
    }

    /**
     * Tests removing a segment from the pool of searchable segments when the
     * MatchManager currently holds ZERO committed segments. Essentially makes
     * sure no errors are thrown.
     */
    @Test
    public void testRemoveSegWhenMatchManagerEmpty() {
        matchManager = new MatchManager(emptySet);
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));

        matchManager.removeSegmentFromMatches(segA);
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));
    }

    /**
     * Tests removing a segment from the pool of searchable segments when the
     * MatchManager currently holds ONE committed segment.
     */
    @Test
    public void testRemoveSegWhenMatchManagerHasOneSeg() {
        matchManager = new MatchManager(hasSegA);

        // try "removing" segB. Nothing changes.
        matchManager.removeSegmentFromMatches(segB);
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));

        // remove segA. Now the MatchManager has now committed segs to search for matches
        matchManager.removeSegmentFromMatches(segA);
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));
    }

    /**
     * Tests removing a segment from the pool of searchable segments when the
     * MatchManager currently holds TWO committed segment.
     */
    @Test
    public void testRemoveSegWhenMatchManagerHasTwoSegs() {
        matchManager = new MatchManager(hasSegAandB);

        // try "removing" segAB. Nothing changes.
        matchManager.removeSegmentFromMatches(segAB);
        assertEquals(matchWithA, matchManager.basicMatch(segA, 3));

        // remove segA. Searching for matches with segA will return nothing, but with segB will still return a match
        matchManager.removeSegmentFromMatches(segA);
        assertEquals(emptyML, matchManager.basicMatch(segA, 3));
        assertEquals(matchWithB, matchManager.basicMatch(segB, 3));
        assertEquals(matchWithB, matchManager.basicMatch(segAB, 3));
    }

    /**
     * Tests removing several segments from the pool of searchable segments.
     */
    @Test
    public void testConsecutiveRemovals() {
        matchManager = new MatchManager(hasSegAandB);

        // remove segA. Searching for matches with segA will return nothing, but with segB will still return a match
        matchManager.removeSegmentFromMatches(segA);
        assertEquals(emptyML, matchManager.basicMatch(segA, 3));
        assertEquals(matchWithB, matchManager.basicMatch(segB, 3));
        assertEquals(matchWithB, matchManager.basicMatch(segAB, 3));

        // remove segB. Now all basicMatch calls will return an empty match list
        matchManager.removeSegmentFromMatches(segB);
        assertEquals(emptyML, matchManager.basicMatch(segA, 3));
        assertEquals(emptyML, matchManager.basicMatch(segB, 3));
        assertEquals(emptyML, matchManager.basicMatch(segAB, 3));
    }

}
