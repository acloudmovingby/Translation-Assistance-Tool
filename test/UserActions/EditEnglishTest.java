/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.PostingsList;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import State.Dispatcher;
import State.State;
import java.util.ArrayList;
import javafx.collections.ObservableList;
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
public class EditEnglishTest {
    
    public EditEnglishTest() {
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
     * Test of execute method, of class EditEnglish.
     */
    @Test
    public void testExecute() {
       
        // create test objects
        Corpus c = TestObjectBuilder.getCommittedTestCorpus();
        BasicFile mainFile = c.getFiles().get(0);
        Dispatcher d = TestObjectBuilder.getDispatcher(c, mainFile);
       
        
        
        // get first segment of mainfile
        Segment firstSegment = d.getState().getMainFile().getActiveSegs().get(0);
        // run the action
        d.acceptAction(new EditEnglish(firstSegment, "new English"));
        // check that the UI result is correct, that the English of that first segment has in fact changed
        assertEquals("new English", d.getUIState().getMainFileSegs().get(0).getEnglish());
        
        
        // try it with the last segment
        ObservableList<Segment> mainFileSegs = d.getUIState().getMainFileSegs();
        Segment lastSegment = mainFileSegs.get(mainFileSegs.size()-1);
        // change the english for the last segment
        d.acceptAction(new EditEnglish(lastSegment, "new English 2"));
        // check that the last segment has in fact changed
        assertEquals("new English 2", mainFileSegs.get(mainFileSegs.size()-1).getEnglish());
        
        
    }
    
}
