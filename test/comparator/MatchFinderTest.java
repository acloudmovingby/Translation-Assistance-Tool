/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.BasicFile;
import DataStructures.MatchList;
import DataStructures.Corpus;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import State.State;
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
     * Test of basicMatch method, of class MatchFinderCoreAlgorithm.
     */
    @Test
    public void testBasicMatch() {
        System.out.println("basicMatch");
        
        // makes a corpus with 1 file
        // makes that 1 file have 1 segment called corpusSeg1
        // the Thai text in corpusSeg1 is "test"
        Corpus corpus = new Corpus();
        BasicFile bf1 = new BasicFile();
        
        SegmentBuilder sb = new SegmentBuilder(bf1);
        sb.setThai("test");
        sb.setCommitted(true);
        Segment corpusSeg1 = sb.createSegment();
        bf1.addSeg(corpusSeg1);
        corpus.addFile(bf1);
        State state = new State(bf1, corpus);
        
        // makes a second file with 1 segment: mainFileSeg
        // mainFileSeg has Thai text of "test"
        // this represents a segment that is selected in main file for which the user wants to see matches
        BasicFile bf2 = new BasicFile();
        sb = new SegmentBuilder(bf2);
        sb.setThai("test");
        Segment mainFileSeg = sb.createSegment();
        bf2.addSeg(mainFileSeg);
        
        // We run mainFileSeg through MatchFinderCoreAlgorithm, 
        // minMatchLength is 3
        // it should return 1 matching segment (i.e. corpusSeg1)
        state.setMinLength(3);
        MatchList mList = MatchFinderCoreAlgorithm.basicMatch(mainFileSeg, state);
        MatchSegment ms = (mList.getMatchSegments()).get(0);
        assertEquals("test", ms.getThai());
      
        // give our corpus file another segment: corpusSeg2
        // corpusSeg2 has Thai text of "aaeestcc"
        // running MatchFinderCoreAlgorithm again should now return both corpusSegs
        sb = new SegmentBuilder(bf1);
        sb.setThai("aaestcc");
        sb.setCommitted(true);
        Segment corpusSeg2 = sb.createSegment();
        bf1.addSeg(corpusSeg2);
        state = new State(bf1, corpus);
        
        state.setMinLength(3);
        mList = MatchFinderCoreAlgorithm.basicMatch(mainFileSeg, state);
        assertEquals(2, mList.getMatchSegments().size());
        assertEquals("test", mList.getMatchSegments().get(0).getThai());
        assertEquals("aaestcc", mList.getMatchSegments().get(1).getThai());
        
        // test that seg3 isn't returned
        sb.setThai("tepstflip");
        sb.setCommitted(true);
        Segment seg3 = sb.createSegmentNewID();
        bf1.addSeg(seg3);
        state = new State(bf1, corpus);
        state.setMinLength(3);
        mList = MatchFinderCoreAlgorithm.basicMatch(mainFileSeg, state);
        assertEquals(2, mList.getMatchSegments().size());
        assertEquals("test", mList.getMatchSegments().get(0).getThai());
        assertEquals("aaestcc", mList.getMatchSegments().get(1).getThai());
    }
    

    /**
     * Test of exactMatch method, of class MatchFinderCoreAlgorithm.
     */
    @Test
    public void testExactMatch() {
        System.out.println("exactMatch");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of complexMatch method, of class MatchFinderCoreAlgorithm.
     */
    @Test
    public void testComplexMatch() {
        System.out.println("complexMatch");
        Segment seg = null;
        MatchFinderCoreAlgorithm instance = new MatchFinderCoreAlgorithm();
        MatchList expResult = null;
        MatchList result = instance.complexMatch(seg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
