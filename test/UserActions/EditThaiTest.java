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
public class EditThaiTest {

    public EditThaiTest() {
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
     * Test of execute method, of class EditThai.
     */
    @Test
    public void testExecute() {
        /*
        TEST:
        - seg doesn't exist in file
        - no change to Thai
        - blank Thai text ""
        - change Thai to something (normal)
        - do normal with first, middle, end seg
         */
        // create test objects
        Corpus c = TestObjectBuilder.getCommittedTestCorpus();
        BasicFile mainFile = c.getFiles().get(0);
        Dispatcher d = TestObjectBuilder.getDispatcher(c, mainFile);
        mainFile = d.getState().getMainFile();

        String newThai = "new Thai";

        /* CHANGE NON-EXISTENT SEGMENT */
        // nothing should change
        assertEquals(0, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get first segment of mainfile
        Segment seg = (new SegmentBuilder(d.getState().getMainFile().getActiveSegs().get(0))).createSegmentNewID();
        // run the action
        d.acceptAction(new EditThai(seg, newThai));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("th1", d.getUIState().getMainFileSegs().get(0).getThai());
        assertEquals(0, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get file back from Database and check that it's the same
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        /* CHANGE FIRST SEGMENT */
        // The OLD text will be in the postings list because it was a committed segment
        // the NEW text will NOT be in the pl because this action makes the seg uncommitted (it appears as the same seg to the user)
        assertEquals(0, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get first segment of mainfile
        seg = d.getState().getMainFile().getActiveSegs().get(0);
        // run the action
        d.acceptAction(new EditThai(seg, newThai));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        seg = d.getUIState().getMainFileSegs().get(0);
        assertEquals("new Thai", seg.getThai());
        assertEquals(0, d.getState().getPostingsList(3).getMatchingID("new").size());
        d.acceptAction(new Commit(seg));
        assertEquals(1, d.getState().getPostingsList(3).getMatchingID("new").size());
        assertEquals(3, d.getState().getPostingsList(3).getMatchingID("th1").size());
        // get file back from Database and check that it's the same
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        /* CHANGE FIRST SEGMENT AGAIN */
        assertEquals(1, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get first segment of mainfile
        seg = d.getState().getMainFile().getActiveSegs().get(0);
        // run the action
        d.acceptAction(new EditThai(seg, ""));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("", d.getUIState().getMainFileSegs().get(0).getThai());
        // note that here the postings list will still contain
        assertEquals(1, d.getState().getPostingsList(3).getMatchingID("new").size());
        assertEquals(3, d.getState().getPostingsList(3).getMatchingID("th1").size());
        // get file back from Database and check that it's the same
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        /* CHANGE MIDDLE SEGMENT */
        // get first segment of mainfile
        seg = d.getState().getMainFile().getActiveSegs().get(2);
        // run the action
        d.acceptAction(new EditThai(seg, newThai));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("new Thai", d.getUIState().getMainFileSegs().get(2).getThai());
        assertEquals(1, d.getState().getPostingsList(3).getMatchingID("new").size());
        seg = d.getState().getMainFile().getActiveSegs().get(2);
        d.acceptAction(new Commit(seg));
        assertEquals(2, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get file back from Database and check that it's the same
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        /* CHANGE LAST SEGMENT */
        // get first segment of mainfile
        seg = d.getState().getMainFile().getActiveSegs().get(4);
        // run the action
        d.acceptAction(new EditThai(seg, newThai));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("new Thai", d.getUIState().getMainFileSegs().get(4).getThai());
        assertEquals(2, d.getState().getPostingsList(3).getMatchingID("new").size());
        seg = d.getState().getMainFile().getActiveSegs().get(4);
        d.acceptAction(new Commit(seg));
        assertEquals(3, d.getState().getPostingsList(3).getMatchingID("new").size());
        // get file back from Database and check that it's the same
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
    }

}
