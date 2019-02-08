/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.Segment;
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
public class SplitTest {
    
    public SplitTest() {
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
     * Test of execute method, of class Split.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        // create test objects
        Corpus c = TestObjectBuilder.getCommittedTestCorpus();
        BasicFile mainFile = c.getFiles().get(0);
        Dispatcher d = TestObjectBuilder.getDispatcher(c, mainFile);
       
        
        
        // get first segment of mainfile
        Segment firstSegment = d.getState().getMainFile().getActiveSegs().get(0);
        
        
        // Split with 0 index
        d.acceptAction(new Split(firstSegment, 0));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("th1", d.getUIState().getMainFileSegs().get(0).getThai());
        assertEquals(5, d.getUIState().getMainFileSegs().size());
        // confirm postings lists are unchanged
        assertEquals(3, d.getState().getPostingsList(3).getMatchingID("th1").size());
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
        
        
        
        // Split with middle index
        d.acceptAction(new Split(firstSegment, 1));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("t", d.getUIState().getMainFileSegs().get(0).getThai());
        assertEquals(6, d.getUIState().getMainFileSegs().size());
        System.out.println(c);
        
        // confirm postings lists 
        assertEquals(3, d.getState().getPostingsList(3).getMatchingID("th1").size()); // even though the segment was removed, it was committed so it should stay in pl
        assertEquals(3, d.getState().getPostingsList(2).getMatchingID("h1").size());
        assertEquals(0, d.getState().getPostingsList(4).getMatchingID("th11").size());
        
        // assert changes have been made to database correctly
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
    }
    
}
