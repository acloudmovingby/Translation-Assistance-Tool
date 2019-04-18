/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import DataStructures.TestObjectBuilder;
import Database.DatabaseOperations;
import State.Dispatcher;
import java.util.Arrays;
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
public class CommitTest {
    
    Dispatcher d;
    BasicFile mainFile;
    
    public CommitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Corpus c = TestObjectBuilder.getIdenticalCorpus();
        // commit all the segments in one of the files in corpus (not the main file)
        c.getFiles().get(1).commitAllSegs();
        // the main file has no committed segments
        mainFile = TestObjectBuilder.getIdenticalFile();
        
        d = TestObjectBuilder.getDispatcher(c, mainFile);
        mainFile = d.getState().getMainFile();
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class Commit.
     */
    @Test
    public void testExecute() {
        
        // initially no segs are committed in main file, so 5 segs have the word "Thai" in them (from the corpus)
        assertEquals(5, d.getState().getPostingsList(4).getMatchingID("Thai").size());

        /* COMMIT FIRST SEGMENT */
        // gets first segment of MainFile
        Segment firstSegment = d.getState().getMainFile().getActiveSegs().get(0);
        // run the action
        d.acceptAction(new Commit(firstSegment));
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(4).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        // after 1 seg is committed, now 6 segs have the word "Thai" in them
        assertEquals(6, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        
        /* COMMIT THIRD SEGMENT */
        // gets third segment of mainfile
        Segment thirdSegment = d.getState().getMainFile().getActiveSegs().get(2);
        // run the action
        d.acceptAction(new Commit(thirdSegment));
        // check that only the 3rd segment in the UIState is committed
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(4).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        assertEquals(7, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        /* COMMIT LAST SEGMENT */
        // gets third segment of mainfile
        Segment lastSegment = d.getState().getMainFile().getActiveSegs().get(4);
        // run the action
        d.acceptAction(new Commit(lastSegment));
        // check that only the 3rd segment in the UIState is committed
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(4).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        assertEquals(8, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        /* RE-COMMIT FIRST SEGMENT */
        // gets third segment of mainfile
        Segment firstSegAgain = d.getState().getMainFile().getActiveSegs().get(0);
        // run the action
        d.acceptAction(new Commit(firstSegAgain));
        // check that nothing changed
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(4).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        assertEquals(8, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        /* COMMIT NON-EXISTENT SEGMENT */
        // makes a copy of the 4th segment, but assigns a new id
        Segment fakeFourthSeg = (new SegmentBuilder(d.getState().getMainFile().getActiveSegs().get(3))).createSegmentNewID();
        // run the action
        d.acceptAction(new Commit(fakeFourthSeg));
        // check that 4th segment is not committed
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(4).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        assertEquals(8, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
    }
    
    /**
     * Tests when Commit is used on multiple segments
     */
    @Test
    public void testMultipleCommit() {
        Segment firstSeg = mainFile.getActiveSegs().get(0);
        Segment secondSeg = mainFile.getActiveSegs().get(1);
        Segment thirdSeg = mainFile.getActiveSegs().get(2);
        Segment fourthSeg = mainFile.getActiveSegs().get(3);
        List<Segment> segsToCommit = Arrays.asList(secondSeg, thirdSeg);
        
        // In the main file, no segs are committed, but there are 5 segs in the corpus that have the word "Thai" in them
        assertEquals(5, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        // Commit segments 2 and 3
        d.acceptAction(new Commit(segsToCommit));
        assertEquals(false, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(false, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        // two segs with the words "Thai" were committed in the mainfile, so now 7 segments total are committed that have the word "Thai" in them
        assertEquals(7, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
        
        // Commit segments 1 through 4
        // Segs 2 and 3 will remain committed as before and segs 1 and 4 will now also be committed
        segsToCommit = Arrays.asList(firstSeg, secondSeg, thirdSeg, fourthSeg);
        d.acceptAction(new Commit(segsToCommit));
        assertEquals(true, d.getUIState().getMainFileSegs().get(0).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(1).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(2).isCommitted());
        assertEquals(true, d.getUIState().getMainFileSegs().get(3).isCommitted());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        // two segs with the words "Thai" were committed in the mainfile, so now 9 segments total are committed that have the word "Thai" in them
        assertEquals(9, d.getState().getPostingsList(4).getMatchingID("Thai").size());
        
    }
    
}
