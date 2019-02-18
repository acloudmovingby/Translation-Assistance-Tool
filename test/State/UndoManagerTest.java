/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import Database.DatabaseOperations;
import JavaFX_1.Initializer;
import UserActions.Commit;
import UserActions.EditEnglish;
import UserActions.EditThai;
import UserActions.Merge;
import UserActions.Split;
import java.util.ArrayList;
import java.util.Arrays;
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
public class UndoManagerTest {
    
    Dispatcher emptyStateDispatcher;
    Dispatcher oneSegStateDispatcher;
    Dispatcher simpleStateDispatcher;
    int lastSegIndex;
    
    public UndoManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        State emptyState = TestObjectBuilder.getEmptyState();
        Initializer emptyStateInit = new Initializer(emptyState.getMainFile(), emptyState.getCorpus());
        emptyStateDispatcher = emptyStateInit.getDispatcher();
        
        State oneSegState = TestObjectBuilder.getOneSegState();
        Initializer oneSegStateInit = new Initializer(oneSegState.getMainFile(), oneSegState.getCorpus());
        oneSegStateDispatcher = oneSegStateInit.getDispatcher();
        
        State simpleState = TestObjectBuilder.getTestState();
        Initializer simpleStateInit = new Initializer(simpleState.getMainFile(), simpleState.getCorpus());
        simpleStateDispatcher = simpleStateInit.getDispatcher();
        lastSegIndex = simpleState.getMainFile().getActiveSegs().size()-1;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Tests running undo on states where no action has ever been performed.
     */
    @Test
    public void testNoActionUndo() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        
        // one segment state
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        
        // generic, simple state
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        
    }
    
    /**
     * Tests running undo on states after a Commit action was performed
     * NOT READY
     */
    @Test
    public void testUndoCommit() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new Commit(TestObjectBuilder.getTestSeg()));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        MainFile mf = emptyStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // commit the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Commit(onlyExistantSeg));
        oneSegStateDispatcher.undo();
        oneSegStateCopy.compare(oneSegStateDispatcher.getState());
        //assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        mf = oneSegStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new Commit(firstSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        Segment lastSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleStateDispatcher.acceptAction(new Commit(lastSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after an EditEnglish action was performed
     * NOT READY
     */
    @Test
    public void testUndoEditEnglish() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new EditEnglish(TestObjectBuilder.getTestSeg(), "test"));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        MainFile mf = emptyStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // edit the english in the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new EditEnglish(onlyExistantSeg, "test"));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        mf = oneSegStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // editEnglish the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new EditEnglish(firstSeg, "test"));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editEnglish the last seg then undo
        Segment lastSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleStateDispatcher.acceptAction(new EditEnglish(lastSeg, "test"));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after an EditThai action was performed
     */
    @Test
    public void testUndoEditThai() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new EditThai(TestObjectBuilder.getTestSeg(), "test"));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        MainFile mf = emptyStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // editThai the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new EditThai(onlyExistantSeg, "test"));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        mf = oneSegStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        
        // generic, simple state
        // editThai the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new EditThai(firstSeg, "test"));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        Segment lastSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleStateDispatcher.acceptAction(new EditThai(lastSeg, "test"));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        mf = simpleStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after a Split action was performed
     */
    @Test
    public void testUndoSplit() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new Split(TestObjectBuilder.getTestSeg(), 1));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        
        // one segment state
        // split the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Split(onlyExistantSeg, 1));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        
        // generic, simple state
        // split the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new Split(firstSeg, 1));
        simpleStateDispatcher.undo();
        // split the last seg then undo
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment lastSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleStateDispatcher.acceptAction(new Split(lastSeg, 1));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
    }
    
    /**
     * Tests running undo on states after a Merge action was performed
     */
    @Test
    public void testUndoMerge() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new Merge(new ArrayList<Segment>()));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        
        // one segment state
        // merge the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Merge(Arrays.asList(onlyExistantSeg)));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        
        // generic, simple state
        // merge first two segs, then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleStateDispatcher.undo();
        // merge the last three segs, then undo
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment seg1 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        Segment seg2 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-1);
        Segment seg3 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-2);
        simpleStateDispatcher.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
    }
    
    /*
    OTHER TESTS TO MAKE
    - obviously, chains of undo
    - make sure that at some point you're testing things that affect the postings lists. The above tests work on files where no segments are committed
    */
}


