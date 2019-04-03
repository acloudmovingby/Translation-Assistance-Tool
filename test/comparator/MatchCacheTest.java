/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
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
public class MatchCacheTest {
    
    /**
     * 
     * These represent what is stored in the cache. They are injected using the MatchCache constructor to make for easy testing. 
     * IMPORTANT: These do not actually represent what is a valid match. They are just various combinations formed to make testing easier with a minimum of test objects (in case the implementation ever changes). These "matches" are nonsensical and do not represent an actual match made by the program's matching algorithm. They are only for testing MatchCache behavior.
     * AandB means the key=segA and the value is a MatchList whose contents are a MatchSegment pointing to SegB.
     */
    
    Segment segA; 
    Segment segB;
    MatchSegment matchSegA;
    MatchSegment matchSegB;
    HashMap<Segment, MatchList> emptyHash; // no matches cached
    HashMap<Segment, MatchList> singlePairingEmptyList; // Contains a seg/matchlist pairing but the matchlist is empty (as if all the matchSegments had been removed by uncommitting them).
    HashMap<Segment, MatchList> singlePairingMatchA; // Contains a single Segment/MatchList pairing and the MatchList contains matchSegA
    HashMap<Segment, MatchList> singlePairingMatchB; // Contains a single Segment/MatchList pairing but the MatchList contains matchSegB
    HashMap<Segment, MatchList> singlePairingMatchAB; // Contains a single Segment/Matchlist pairing and the MatchList contains both matchSegA and matchSegB
    MatchCache cache;
    
    
    /**
     * This Function, regardless of input, will return a "match", i.e. an Optional containing MatchSegment matchSegA.
     */
    Function<Segment, Optional<MatchSegment>> alwaysMatchesSegA;
    
    /**
     * This Function, regardless of input, will return an empty Optional (i.e. no "match").
     */
    Function<Segment, Optional<MatchSegment>> neverMatches;
    
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
        
        
        // Create Segments
        SegmentBuilder sb = new SegmentBuilder();
        sb.setThai("AAAA");
        segA = sb.createSegment();
        sb = new SegmentBuilder();
        sb.setThai("BBBB");
        segB = sb.createSegmentNewID();
        
        // Create associated MatchSegments
        matchSegA = new MatchSegment(segA);
        matchSegB = new MatchSegment(segB);
        
        // Create MatchLists
        MatchList listWithA = new MatchList();
        listWithA.addEntry(matchSegA);
        MatchList listWithB = new MatchList();
        listWithB.addEntry(matchSegB);
        MatchList listWithAB = new MatchList();
        listWithAB.addEntry(matchSegA);
        listWithAB.addEntry(matchSegB);
        
        // Create all HashMaps to inject into MatchCache
        emptyHash = new HashMap();
        singlePairingEmptyList = new HashMap();
        singlePairingEmptyList.put(segA, new MatchList());
        singlePairingMatchA = new HashMap();
        singlePairingMatchA.put(segA, listWithA);
        singlePairingMatchB = new HashMap();
        singlePairingMatchB.put(segA, listWithB);
        singlePairingMatchAB = new HashMap();
        singlePairingMatchAB.put(segA, listWithAB);
        
        cache = new MatchCache();
        
        alwaysMatchesSegA = ((a) -> {
            return Optional.of(new MatchSegment(segA));
        });
        
        neverMatches = ((a) -> Optional.empty());
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * 
     */
    @Test
    public void testEquality() {
        cache = new MatchCache();
        MatchCache testCache = new MatchCache();
        assertEquals(cache, testCache);
        
        cache = new MatchCache(singlePairingEmptyList);
        testCache = new MatchCache(singlePairingEmptyList);
        assertEquals(cache, testCache);
        
        cache = new MatchCache(singlePairingMatchAB);
        testCache = new MatchCache(singlePairingMatchAB);
        assertEquals(cache, testCache);
        
        cache = new MatchCache(singlePairingMatchAB);
        testCache = new MatchCache(singlePairingMatchAB);
        assertEquals(cache, testCache);
        
        cache = new MatchCache(singlePairingEmptyList);
        testCache = new MatchCache(singlePairingMatchAB);
        assertEquals(false, cache.equals(testCache));
    }
    
    /**
     * 
     */
    @Test
    public void testNewlyConstructedMatchCache() {
        assertEquals(Optional.empty(), cache.getMatchList(segA));
        cache.removeSegment(segA);
        assertEquals(cache, new MatchCache());
        
        // If there are no matchlists cached, then committing a new seg can't change any MatchLists
        cache.updateMatchLists(neverMatches);
        assertEquals(cache, new MatchCache());
        cache.updateMatchLists(alwaysMatchesSegA);
        assertEquals(cache, new MatchCache());
    }

    /**
     * Tests a MatchCache where an empty hashmap was injected. Same behavior as testNewlyConstructedMatchCache()
     */
    @Test
    public void testEmptyCache() {
        cache = new MatchCache(emptyHash);
        assertEquals(Optional.empty(), cache.getMatchList(segA));
        cache.removeSegment(segA);
        assertEquals(cache, new MatchCache());
        
        // If there are no matchlists cached, then committing a new seg can't change any MatchLists
        cache.updateMatchLists(neverMatches);
        assertEquals(cache, new MatchCache());
        cache.updateMatchLists(alwaysMatchesSegA);
        assertEquals(cache, new MatchCache());
    }
    
    /**
     * Tests when a match is successful but the MatchList is currently empty. 
     * 
     */
    @Test
    public void testMatchAndEmpty() {
        cache = new MatchCache(singlePairingEmptyList);
        // ensures that when a match is found, the matchsegment is correctly added to the MatchList.
        cache.updateMatchLists(alwaysMatchesSegA);
        assertEquals(true, cache.getMatchList(segA).get().getMatchSegments().contains(matchSegA));
        assertEquals(1, cache.getMatchList(segA).get().getMatchSegments().size());
    }

    /**
     * When a match is found but the MatchList already contains MatchSegA, and thus nothing changes.
     */
     @Test
    public void testMatchContainsA() {
        cache = new MatchCache(singlePairingMatchA);
        cache.updateMatchLists(alwaysMatchesSegA);
        assertEquals(true, cache.getMatchList(segA).get().getMatchSegments().contains(matchSegA));
        assertEquals(1, cache.getMatchList(segA).get().getMatchSegments().size());
    }
    
    /**
     * When a match is found but the MatchList doesn't yet contain MatchSegA, it then adds it to the MatchList.
     */
     @Test
    public void testMatchDoesntContainA() {
        cache = new MatchCache(singlePairingMatchB);
        cache.updateMatchLists(alwaysMatchesSegA);
        assertEquals(true, cache.getMatchList(segA).get().getMatchSegments().contains(matchSegA));
        assertEquals(2, cache.getMatchList(segA).get().getMatchSegments().size());
    }
   
    
    
    
    
    
}
