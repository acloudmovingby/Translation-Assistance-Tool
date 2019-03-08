/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.PostingsList;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import DataStructures.TestObjectBuilder;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *TESTING STRATEGY
        For each of the following tests, an operation is performed on:
            - empty file (nothing in active or hidden)
            - single seg file (1 seg in active, nothing in hidden)
            - non-trivial file (file with both active and hidden segs)
        General pattern:
            - make assertion about (1) main file (2) postings lists
            - perform operation (addSeg, addSegToHidden, removeSeg)
            - make assertion about result (1) main file (2) postings lists
 * 
 * @author Chris
 */
public class StateTest {
    
    /*
    Used these for the majority of interesting tests. 
    */
    State emptyState;
    State oneSegState;
    State simpleState;
    State simpleCommittedState;
    
    /*
    These are from earlier tests that are neither interesting nor very effective at testing anything of importance, but left here anyways.
    */
    State state1;
    State state2;
    int numFiles;

    public StateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // emptyState (1 file with zero segments)
        emptyState = TestObjectBuilder.getEmptyState();
        
        
        // 1 file with 1 seg. This seg is not committed
        oneSegState = TestObjectBuilder.getOneSegState();
        
        
        // 5 segs in three files. None are committed
        simpleState = TestObjectBuilder.getTestState();
        
        
        // same as simple state except all files are committed
        simpleCommittedState = TestObjectBuilder.getCommittedTestState();
        
        
        
        
        // state where the "mainFile" is already in corpus
        Corpus c1 = TestObjectBuilder.getCommittedTestCorpus();
        System.out.println(c1);
        numFiles = c1.getFiles().size();
        state1 = new State(c1.getFiles().get(0), c1);
      

        // state where the mainfile was created outside of corpus
        Corpus c2 = TestObjectBuilder.getCommittedTestCorpus();
        BasicFile bf = TestObjectBuilder.getTestFile();
        bf.commitAllSegs();
        state2 = new State(bf, c2);
    }

    @After
    public void tearDown() {
    }


    /**
     * This test makes sure that regardless of whether the mainFile originated in the corpus or came from elsewhere, the mainFile ends up in the corpus and no extra copies are added.
     */
    @Test
    public void testGetCorpus() {
        System.out.println("getCorpus");
        
        System.out.println(state1.getCorpus());
        
        assertEquals(numFiles, state1.getCorpus().getFiles().size());
        
        assertEquals(numFiles+1, state2.getCorpus().getFiles().size());
    }
    
     /**
      * ADD 1 SEG AT INDEX = 0
     */
    @Test
    public void testAddSegAtZero() {
        
        int insertIndex;
        State testState;
        
        // EMPTY STATE
        insertIndex = 0;
        testState = emptyState;
        SegmentBuilder sb = new SegmentBuilder(testState.getMainFile());
        Segment seg1 = sb.createSegment();
        testState.addSeg(insertIndex, seg1);
        // assert total number of segs
        assertEquals(1, testState.getMainFile().getAllSegs().size());
        // assert location of inserted seg
        assertEquals(seg1, testState.getMainFile().getActiveSegs().get(insertIndex));
        
        // ONE SEG STATE
        insertIndex = 0;
        testState = oneSegState;
        sb = new SegmentBuilder(testState.getMainFile());
        seg1 = sb.createSegment();
        testState.addSeg(insertIndex, seg1);
        // assert total number of segs
        assertEquals(2, testState.getMainFile().getAllSegs().size());
        // assert location of inserted seg
        assertEquals(seg1, testState.getMainFile().getActiveSegs().get(insertIndex));
        
        // SIMPLE STATE (NOTHING COMMITTED)
        insertIndex = 0;
        testState = simpleState;
        sb = new SegmentBuilder(testState.getMainFile());
        seg1 = sb.createSegment();
        testState.addSeg(insertIndex, seg1);
        // assert total number of segs
        assertEquals(6, testState.getMainFile().getAllSegs().size());
        // assert location of inserted seg
        assertEquals(seg1, testState.getMainFile().getActiveSegs().get(insertIndex));
        
        // SIMPLE STATE (EVERYTHING COMMITTED)
        insertIndex = 0; 
        testState = simpleCommittedState;
        sb = new SegmentBuilder(testState.getMainFile());
        seg1 = sb.createSegment();
        testState.addSeg(insertIndex, seg1);
        // assert total number of segs
        assertEquals(6, testState.getMainFile().getAllSegs().size());
        // assert location of inserted seg
        assertEquals(seg1, testState.getMainFile().getActiveSegs().get(insertIndex));
        
    }
    
    /*
    ADD 1 SEG AT INDEX = END
    */
    @Test
    public void testAddSegAtEnd() {
        
    }
    
    /*
    ADD 1 SEG AT INDEX = MIDDLE
    */
    @Test
    public void testAddSegAtMiddle() {
        
    }
    
    /*
    ADD 1 SEG AT INDEX = NON-EXISTENT
    */
    @Test
    public void testAddSegAtNonExistentIndex() {
        
    }
    
    /*
    Add seg whose id is already contained by another seg in the file. 
    I don't know how to test exception handling with JUnit tests, so I just assert a false statement at end of a block of code that shouldn't ever be reached...
    */
    @Test
    public void testAddAlreadyExistentSeg() {
        
    }
}