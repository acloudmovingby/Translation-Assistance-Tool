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
import State.State;
import State.StateWithDatabase;
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
        
        // makes a corpus with 1 file
        // makes that 1 file have 1 segment called corpusSeg1
        // the Thai text in corpusSeg1 is "test"
        Corpus corpus = new Corpus();
        BasicFile bf1 = new BasicFile();
        Segment corpusSeg1 = bf1.newSeg();
        corpusSeg1.setThai("test");
        corpusSeg1.setCommitted(true);
        corpus.addFile(bf1);
        State state = new StateWithDatabase(bf1, corpus);
        
        // makes a second file with 1 segment: mainFileSeg
        // mainFileSeg has Thai text of "test"
        // this represents a segment that is selected in main file for which the user wants to see matches
        BasicFile bf2 = new BasicFile();
        Segment mainFileSeg = bf2.newSeg();
        mainFileSeg.setThai("test");
        
        // We run mainFileSeg through MatchFinder, 
        // minMatchLength is 3
        // it should return 1 matching segment (i.e. corpusSeg1)
        MatchList mList = MatchFinder.basicMatch(mainFileSeg, 3, state);
        MatchSegment ms = (mList.getObservableList()).get(0);
        assertEquals("test", ms.getThai());
      
        // give our corpus file another segment: corpusSeg2
        // corpusSeg2 has Thai text of "aaeestcc"
        // running MatchFinder again should now return both corpusSegs
        Segment corpusSeg2 = bf1.newSeg();
        corpusSeg2.setThai("aaestcc");
        corpusSeg2.setCommitted(true);
        state = new StateWithDatabase(bf1, corpus);
        mList = MatchFinder.basicMatch(mainFileSeg, 3, state);
        assertEquals(2, mList.getObservableList().size());
        assertEquals("test", mList.getObservableList().get(0).getThai());
        assertEquals("aaestcc", mList.getObservableList().get(1).getThai());
        
        // test that seg3 isn't returned
        Segment seg3 = bf1.newSeg();
        seg3.setThai("tepstflip");
        seg3.setCommitted(true);
        state = new StateWithDatabase(bf1, corpus);
        mList = MatchFinder.basicMatch(mainFileSeg, 3, state);
        assertEquals(2, mList.getObservableList().size());
        assertEquals("test", mList.getObservableList().get(0).getThai());
        assertEquals("aaestcc", mList.getObservableList().get(1).getThai());
    }
    

    /**
     * Test of exactMatch method, of class MatchFinder.
     */
    @Test
    public void testExactMatch() {
        System.out.println("exactMatch");
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
        MatchList expResult = null;
        MatchList result = instance.complexMatch(seg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
