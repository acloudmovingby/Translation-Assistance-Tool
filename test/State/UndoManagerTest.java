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
    Dispatcher simpleCommittedStateDispatcher;
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
        
        // one seg, this seg is not committed
        State oneSegState = TestObjectBuilder.getOneSegState();
        Initializer oneSegStateInit = new Initializer(oneSegState.getMainFile(), oneSegState.getCorpus());
        oneSegStateDispatcher = oneSegStateInit.getDispatcher();
        
        // 5 segs in three files. none are committed
        State simpleState = TestObjectBuilder.getTestState();
        Initializer simpleStateInit = new Initializer(simpleState.getMainFile(), simpleState.getCorpus());
        simpleStateDispatcher = simpleStateInit.getDispatcher();
        lastSegIndex = simpleState.getMainFile().getActiveSegs().size()-1;
        
        // same as simple state except all files are committed
        State simpleCommittedState = TestObjectBuilder.getCommittedTestState();
        Initializer simpleCommittedInit = new Initializer(simpleCommittedState.getMainFile(), simpleCommittedState.getCorpus());
        simpleCommittedStateDispatcher = simpleCommittedInit.getDispatcher();
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
        
        // simple state where all segs are committted
        StateCopier simpleCommittedCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, simpleCommittedCopy.compare(simpleCommittedStateDispatcher.getState()));
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
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        oneSegStateDispatcher.undo();
        oneSegStateCopy.compare(oneSegStateDispatcher.getState());
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
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
        
        
        StateCopier committedStateCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new Commit(firstSeg));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleCommittedStateDispatcher.acceptAction(new Commit(lastSeg));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
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
        
        StateCopier committedStateCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new EditEnglish(firstSeg, "test"));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleCommittedStateDispatcher.acceptAction(new EditEnglish(lastSeg, "test"));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
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
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new EditThai(firstSeg, "test"));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleCommittedStateDispatcher.acceptAction(new EditThai(lastSeg, "test"));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
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
        MainFile mf = emptyStateDispatcher.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // split the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Split(onlyExistantSeg, 1));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // split the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleStateDispatcher.acceptAction(new Split(firstSeg, 1));
        simpleStateDispatcher.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // split the last seg then undo
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment lastSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleStateDispatcher.acceptAction(new Split(lastSeg, 1));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new Split(firstSeg, 1));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleCommittedStateDispatcher.acceptAction(new Split(lastSeg, 1));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
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
        MainFile mf = emptyStateDispatcher.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // "merge" the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Merge(Arrays.asList(onlyExistantSeg)));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // merge first two segs, then undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleStateDispatcher.getUIState().getMainFileSegs().get(1);
        simpleStateDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleStateDispatcher.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // merge the last three segs, then undo
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment seg1 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        Segment seg2 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-1);
        Segment seg3 = simpleStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-2);
        simpleStateDispatcher.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(simpleCommittedStateDispatcher.getState());
        firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        secondSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(1);
        simpleCommittedStateDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        seg1 = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        seg2 = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-1);
        seg3 = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex-2);
        simpleCommittedStateDispatcher.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(simpleCommittedStateDispatcher.getState())); 
        mf = simpleCommittedStateDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo after both a merge and then a split were performed.
     */
    @Test
    public void testUndoMergeAndSplit() {
        
        // "save" the committed state
        StateCopier state1Copy = new StateCopier(simpleCommittedStateDispatcher.getState());
        MainFile mf = simpleCommittedStateDispatcher.getState().getMainFile();
        
        // merge first two segments
        Segment firstSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(1);
        simpleCommittedStateDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        StateCopier state2Copy = new StateCopier(simpleCommittedStateDispatcher.getState());
        
        // commit this newly merged segment
        Segment mergeResult = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new Commit(mergeResult));
        StateCopier state3Copy = new StateCopier(simpleCommittedStateDispatcher.getState());
        
        // split the newly merged segment
        Segment commitResult = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new Split(commitResult, 1));
        StateCopier state4Copy = new StateCopier(simpleCommittedStateDispatcher.getState());
        
        // commit the newly split segment
        Segment splitResult = simpleCommittedStateDispatcher.getUIState().getMainFileSegs().get(0);
        simpleCommittedStateDispatcher.acceptAction(new Split(splitResult, 1));
        
        // undo three times, comparing each time with the saved states and the database
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, state4Copy.compare(simpleCommittedStateDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, state1Copy.compare(simpleCommittedStateDispatcher.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        simpleCommittedStateDispatcher.undo();
        assertEquals(true, state1Copy.compare(simpleCommittedStateDispatcher.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    /*
    OTHER TESTS TO MAKE
    - obviously, chains of undo
    - make sure that at some point you're testing things that affect the postings lists. The above tests work on files where no segments are committed
    */
}


