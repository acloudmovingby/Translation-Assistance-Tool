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
public class CommitTest {
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class Commit.
     */
    @Test
    public void testExecute() {
        // create test objects
        // corpus has nothing committed 
        Corpus c = TestObjectBuilder.getIdenticalCorpus();
        c.getFiles().get(1).commitAllSegs();
        BasicFile mainFile = TestObjectBuilder.getIdenticalFile();
                //c.getFiles().get(0);
        Dispatcher d = TestObjectBuilder.getDispatcher(c, mainFile);
        
        // initially no segs are committed, so 0 segs have the word "Thai" in them
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
    
}
