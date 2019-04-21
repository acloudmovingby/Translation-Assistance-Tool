/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

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
    
    Dispatcher emptyDisp;
    Dispatcher oneSegDisp;
    /**
     *  5 segs in three files. None are committed
     */
    Dispatcher simpleDisp;
    Dispatcher committedDisp;
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
        
        // sets up 4 dispatchers
        // emptyState (1 file with zero segments)
        State emptyState = TestObjectBuilder.getEmptyState();
        Initializer emptyStateInit = new Initializer(emptyState.getMainFile(), emptyState.getCorpus());
        emptyDisp = emptyStateInit.getDispatcher();
        
        // 1 file with 1 seg. This seg is not committed
        State oneSegState = TestObjectBuilder.getOneSegState();
        Initializer oneSegStateInit = new Initializer(oneSegState.getMainFile(), oneSegState.getCorpus());
        oneSegDisp = oneSegStateInit.getDispatcher();
        
        // 5 segs in three files. None are committed
        State simpleState = TestObjectBuilder.getTestState();
        Initializer simpleStateInit = new Initializer(simpleState.getMainFile(), simpleState.getCorpus());
        simpleDisp = simpleStateInit.getDispatcher();
        lastSegIndex = simpleState.getMainFile().getActiveSegs().size()-1;
        
        // same as simple state except all files are committed
        State simpleCommittedState = TestObjectBuilder.getCommittedTestState();
        Initializer simpleCommittedInit = new Initializer(simpleCommittedState.getMainFile(), simpleCommittedState.getCorpus());
        committedDisp = simpleCommittedInit.getDispatcher();
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
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        
        // one segment state
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        oneSegDisp.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        
        // generic, simple state
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        
        // simple state where all segs are committted
        StateCopier simpleCommittedCopy = new StateCopier(committedDisp.getState());
        committedDisp.undo();
        assertEquals(true, simpleCommittedCopy.compare(committedDisp.getState()));
    }
    
    /**
     * Tests running undo on states after a Commit action was performed
     * NOT READY
     */
    @Test
    public void testUndoCommit() {
        
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.acceptAction(new Commit(TestObjectBuilder.getTestSeg()));
        emptyDisp.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        MainFile mf = emptyDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // main file with one segment
        // commit the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        Segment onlyExistantSeg = oneSegDisp.getUIState().getMainFileSegs().get(0);
        oneSegDisp.acceptAction(new Commit(onlyExistantSeg));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        oneSegDisp.undo();
        oneSegStateCopy.compare(oneSegDisp.getState());
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        mf = oneSegDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        Segment firstSeg = simpleDisp.getUIState().getMainFileSegs().get(0);
        simpleDisp.acceptAction(new Commit(firstSeg));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        Segment lastSeg = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDisp.acceptAction(new Commit(lastSeg));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        
        StateCopier committedStateCopy = new StateCopier(committedDisp.getState());
        firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new Commit(firstSeg));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDisp.acceptAction(new Commit(lastSeg));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after an EditEnglish action was performed
     * NOT READY
     */
    @Test
    public void testUndoEditEnglish() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.acceptAction(new EditEnglish(TestObjectBuilder.getTestSeg(), "test"));
        emptyDisp.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        MainFile mf = emptyDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // edit the english in the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        Segment onlyExistantSeg = oneSegDisp.getUIState().getMainFileSegs().get(0);
        oneSegDisp.acceptAction(new EditEnglish(onlyExistantSeg, "test"));
        oneSegDisp.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        mf = oneSegDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // editEnglish the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        Segment firstSeg = simpleDisp.getUIState().getMainFileSegs().get(0);
        simpleDisp.acceptAction(new EditEnglish(firstSeg, "test"));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editEnglish the last seg then undo
        Segment lastSeg = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDisp.acceptAction(new EditEnglish(lastSeg, "test"));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        StateCopier committedStateCopy = new StateCopier(committedDisp.getState());
        firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new EditEnglish(firstSeg, "test"));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDisp.acceptAction(new EditEnglish(lastSeg, "test"));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after an EditThai action was performed
     */
    @Test
    public void testUndoEditThai() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.acceptAction(new EditThai(TestObjectBuilder.getTestSeg(), "test"));
        emptyDisp.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        MainFile mf = emptyDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // editThai the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        Segment onlyExistantSeg = oneSegDisp.getUIState().getMainFileSegs().get(0);
        oneSegDisp.acceptAction(new EditThai(onlyExistantSeg, "test"));
        oneSegDisp.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        mf = oneSegDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        
        // generic, simple state
        // editThai the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        Segment firstSeg = simpleDisp.getUIState().getMainFileSegs().get(0);
        simpleDisp.acceptAction(new EditThai(firstSeg, "test"));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        Segment lastSeg = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDisp.acceptAction(new EditThai(lastSeg, "test"));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        mf = simpleDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedDisp.getState());
        firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new EditThai(firstSeg, "test"));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDisp.acceptAction(new EditThai(lastSeg, "test"));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after a Split action was performed
     */
    @Test
    public void testUndoSplit() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.acceptAction(new Split(TestObjectBuilder.getTestSeg(), 1));
        emptyDisp.undo();
        MainFile mf = emptyDisp.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // split the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        Segment onlyExistantSeg = oneSegDisp.getUIState().getMainFileSegs().get(0);
        oneSegDisp.acceptAction(new Split(onlyExistantSeg, 1));
        oneSegDisp.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // split the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        Segment firstSeg = simpleDisp.getUIState().getMainFileSegs().get(0);
        simpleDisp.acceptAction(new Split(firstSeg, 1));
        simpleDisp.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // split the last seg then undo
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        Segment lastSeg = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDisp.acceptAction(new Split(lastSeg, 1));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedDisp.getState());
        firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new Split(firstSeg, 1));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDisp.acceptAction(new Split(lastSeg, 1));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo on states after a Merge action was performed
     */
    @Test
    public void testUndoMerge() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyDisp.getState());
        emptyDisp.acceptAction(new Merge(new ArrayList<Segment>()));
        emptyDisp.undo();
        MainFile mf = emptyDisp.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // one segment state
        // "merge" the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegDisp.getState());
        Segment onlyExistantSeg = oneSegDisp.getUIState().getMainFileSegs().get(0);
        oneSegDisp.acceptAction(new Merge(Arrays.asList(onlyExistantSeg)));
        oneSegDisp.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // generic, simple state
        // merge first two segs, then undo
        StateCopier simpleStateCopy = new StateCopier(simpleDisp.getState());
        Segment firstSeg = simpleDisp.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleDisp.getUIState().getMainFileSegs().get(1);
        simpleDisp.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleDisp.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // merge the last three segs, then undo
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        Segment seg1 = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        Segment seg2 = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex-1);
        Segment seg3 = simpleDisp.getUIState().getMainFileSegs().get(lastSegIndex-2);
        simpleDisp.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleDisp.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDisp.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // committed state
         // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedDisp.getState());
        firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        secondSeg = committedDisp.getUIState().getMainFileSegs().get(1);
        committedDisp.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        seg1 = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex);
        seg2 = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex-1);
        seg3 = committedDisp.getUIState().getMainFileSegs().get(lastSegIndex-2);
        committedDisp.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        committedDisp.undo();
        assertEquals(true, committedStateCopy.compare(committedDisp.getState())); 
        mf = committedDisp.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
    /**
     * Tests running undo after both a merge and then a split were performed.
     */
    @Test
    public void testUndoMergeAndSplit() {
        
        // "save" the committed state
        StateCopier state1Copy = new StateCopier(committedDisp.getState());
        MainFile mf = committedDisp.getState().getMainFile();
        System.out.println("State1Copy = " + state1Copy.mainFileCopy);
        assertEquals(true, state1Copy.compare(committedDisp.getState()));
        
        // merge first two segments
        Segment firstSeg = committedDisp.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = committedDisp.getUIState().getMainFileSegs().get(1);
        committedDisp.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        StateCopier state2Copy = new StateCopier(committedDisp.getState());
        System.out.println("State2Copy = " + state2Copy.mainFileCopy);
        assertEquals(true, state2Copy.compare(committedDisp.getState()));
        
        // commit this newly merged segment 
        Segment mergeResult = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new Commit(mergeResult));
        StateCopier state3Copy = new StateCopier(committedDisp.getState());
        System.out.println("State3Copy = " + state3Copy.mainFileCopy);
        assertEquals(true, state3Copy.compare(committedDisp.getState()));
        
        // split the newly merged segment
        Segment commitResult = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new Split(commitResult, 1));
        StateCopier state4Copy = new StateCopier(committedDisp.getState());
        System.out.println("State4Copy = " + state4Copy.mainFileCopy);
        assertEquals(true, state4Copy.compare(committedDisp.getState()));
        
        // commit the newly split segment
        Segment splitResult = committedDisp.getUIState().getMainFileSegs().get(0);
        committedDisp.acceptAction(new Commit(splitResult));
        System.out.println("Final = " + committedDisp.getState().getMainFile());
        
        
        // undo three times, comparing each time with the saved states and the database
        committedDisp.undo();
        assertEquals(true, state4Copy.compare(committedDisp.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedDisp.undo();
        assertEquals(true, state3Copy.compare(committedDisp.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedDisp.undo();
        assertEquals(true, state2Copy.compare(committedDisp.getState())); 
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }
    
     /**
     * Tests the method restorePriorMainFile() without using Actions/Dispatcher, and tests more fundamental base cases like just adding/removing segments from various lists (regardless of whether any Actions could do this) and ensuring the UndoManager updates the main file properly, regardless of what the differences are.
     */
    @Test
    public void testRestorePriorBasicFunctionality() {
        
        /*
        TESTING STRATEGY:
            - build prior main file
            - build current main file
            - save copy of prior main file
            - 
        */
        
        /*
        ALTERNATE STRATEGY
            - make a variety of main files (some empty, some have 1 active, some have 1 hidden seg, 
        */
        
        /* 
        ONE MORE ACTIVE (prior has 1 MORE ACTIVE seg than current)
        test cases: 
            prior = 
                (1) empty 
                (2) one seg
                (3) >1 seg
        */ 
        
        /*
        TWO MORE ACTIVE (prior has 2 MORE ACTIVE segs than current)
        test cases: 
            prior = 
                (1) empty 
                (2) one seg
                (3) >1 seg
        */
        
        /* 
        ONE LESS ACTIVE (prior has 1 LESS ACTIVE seg than current)
        test cases: 
            current = 
                (1) empty
                (2) one seg
                (3) >1 seg
        */
        
        /* 
        TWO LESS ACTIVE (prior has 1 LESS ACTIVE seg than current)
        test cases: 
            current = 
                (1) empty
                (2) one seg
                (3) >1 seg
        */
        
        /* 
        ONE MORE HIDDEN (prior has 1 MORE HIDDEN seg than current)
        test cases: 
            current = 
                (1) empty 
                (2) one seg
                (3) >1 seg
        */
        
        /* 
        TWO MORE HIDDEN (prior has 1 MORE HIDDEN seg than current)
        test cases: 
            current = 
                (1) empty 
                (2) one seg
                (3) >1 seg
        */
        
        /*
        ONE LESS HIDDEN (prior has 1 LESS HIDDEN seg than current)
        
        */
        
        /*
        TWO LESS HIDDEN (prior has 1 LESS HIDDEN seg than current)
        
        */
        
        /*
        ONE ACTIVE SEG REPLACED (prior / current have same number of segs, but one seg is different)
        */
        
        /*
        TWO ACTIVE SEGS REPLACED (prior / current have same number of segs, but two segs are different)
        */
        
        /*
        MISC CASE 1 (prior has many actives, no hidden; current has many hidden but no active)
        */
        
        /*
        MISC CASE 2 (reverse of misc case 1; prior has many hidden, no active; current has many active but no hidden)
        */
    }
}


