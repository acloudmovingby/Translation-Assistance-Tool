/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.TranslationFile;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import Database.DatabaseOperations;
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

    Dispatcher emptyDispatcher;
    Dispatcher oneSegDispatcher;
    /**
     * 5 segs in three files. None are committed
     */
    Dispatcher simpleDispatcher;
    Dispatcher committedDispatcher;
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

        // sets up 4 dispatchers where the states are defined as follows:
        
        // emptyState (1 file with zero segments)
        State emptyState = TestObjectBuilder.getEmptyState();
        
        emptyDispatcher = new Dispatcher(emptyState);

        // 1 file with 1 seg. This seg is not committed
        State oneSegState = TestObjectBuilder.getOneSegState();
        oneSegDispatcher = new Dispatcher(oneSegState);

        // 5 segs in three files. None are committed
        State simpleState = TestObjectBuilder.getTestState();
        simpleDispatcher = new Dispatcher(simpleState);
        lastSegIndex = simpleState.getMainFile().getActiveSegs().size() - 1;

        // same as simple state except all files are committed
        State simpleCommittedState = TestObjectBuilder.getCommittedTestState();
        committedDispatcher = new Dispatcher(simpleCommittedState);
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
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));

        // one segment state
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        oneSegDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));

        // generic, simple state
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));

        // simple state where all segs are committted
        StateCopierForUndoManagerTest simpleCommittedCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        committedDispatcher.undo();
        assertEquals(true, simpleCommittedCopy.compare(committedDispatcher.getState()));
    }

    /**
     * Tests running undo on states after a Commit action was performed NOT
     * READY
     */
    @Test
    public void testUndoCommit() {

        // empty state
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.acceptAction(new Commit(TestObjectBuilder.getTestSeg()));
        emptyDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));
        TranslationFile mf = emptyDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // main file with one segment
        // commit the only segment in the main file
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        Segment onlyExistantSeg = oneSegDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegDispatcher.acceptAction(new Commit(onlyExistantSeg));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        oneSegDispatcher.undo();
        oneSegStateCopy.compare(oneSegDispatcher.getState());
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));
        mf = oneSegDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        Segment firstSeg = simpleDispatcher.getUIState().getMainFileSegs().get(0);
        simpleDispatcher.acceptAction(new Commit(firstSeg));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        Segment lastSeg = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDispatcher.acceptAction(new Commit(lastSeg));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        StateCopierForUndoManagerTest committedStateCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new Commit(firstSeg));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDispatcher.acceptAction(new Commit(lastSeg));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after an EditEnglish action was performed
     * NOT READY
     */
    @Test
    public void testUndoEditEnglish() {
        // empty state
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.acceptAction(new EditEnglish(TestObjectBuilder.getTestSeg(), "test"));
        emptyDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));
        TranslationFile mf = emptyDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // edit the english in the only segment in the main file
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        Segment onlyExistantSeg = oneSegDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegDispatcher.acceptAction(new EditEnglish(onlyExistantSeg, "test"));
        oneSegDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));
        mf = oneSegDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // editEnglish the first seg then undo
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        Segment firstSeg = simpleDispatcher.getUIState().getMainFileSegs().get(0);
        simpleDispatcher.acceptAction(new EditEnglish(firstSeg, "test"));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editEnglish the last seg then undo
        Segment lastSeg = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDispatcher.acceptAction(new EditEnglish(lastSeg, "test"));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        StateCopierForUndoManagerTest committedStateCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new EditEnglish(firstSeg, "test"));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDispatcher.acceptAction(new EditEnglish(lastSeg, "test"));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after an EditThai action was performed
     */
    @Test
    public void testUndoEditThai() {
        // empty state
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.acceptAction(new EditThai(TestObjectBuilder.getTestSeg(), "test"));
        emptyDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));
        TranslationFile mf = emptyDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // editThai the only segment in the main file
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        Segment onlyExistantSeg = oneSegDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegDispatcher.acceptAction(new EditThai(onlyExistantSeg, "test"));
        oneSegDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));
        mf = oneSegDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // editThai the first seg then undo
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        Segment firstSeg = simpleDispatcher.getUIState().getMainFileSegs().get(0);
        simpleDispatcher.acceptAction(new EditThai(firstSeg, "test"));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        Segment lastSeg = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDispatcher.acceptAction(new EditThai(lastSeg, "test"));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        mf = simpleDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopierForUndoManagerTest committedStateCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new EditThai(firstSeg, "test"));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDispatcher.acceptAction(new EditThai(lastSeg, "test"));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after a Split action was performed
     */
    @Test
    public void testUndoSplit() {
        // empty state
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.acceptAction(new Split(TestObjectBuilder.getTestSeg(), 1));
        emptyDispatcher.undo();
        TranslationFile mf = emptyDispatcher.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // split the only segment in the main file
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        Segment onlyExistantSeg = oneSegDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegDispatcher.acceptAction(new Split(onlyExistantSeg, 1));
        oneSegDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // split the first seg then undo
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        Segment firstSeg = simpleDispatcher.getUIState().getMainFileSegs().get(0);
        simpleDispatcher.acceptAction(new Split(firstSeg, 1));
        simpleDispatcher.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // split the last seg then undo
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        Segment lastSeg = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleDispatcher.acceptAction(new Split(lastSeg, 1));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopierForUndoManagerTest committedStateCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new Split(firstSeg, 1));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        committedDispatcher.acceptAction(new Split(lastSeg, 1));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after a Merge action was performed
     */
    @Test
    public void testUndoMerge() {
        // empty state
        StateCopierForUndoManagerTest emptyStateCopy = new StateCopierForUndoManagerTest(emptyDispatcher.getState());
        emptyDispatcher.acceptAction(new Merge(new ArrayList<Segment>()));
        emptyDispatcher.undo();
        TranslationFile mf = emptyDispatcher.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // "merge" the only segment in the main file
        StateCopierForUndoManagerTest oneSegStateCopy = new StateCopierForUndoManagerTest(oneSegDispatcher.getState());
        Segment onlyExistantSeg = oneSegDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegDispatcher.acceptAction(new Merge(Arrays.asList(onlyExistantSeg)));
        oneSegDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // merge first two segs, then undo
        StateCopierForUndoManagerTest simpleStateCopy = new StateCopierForUndoManagerTest(simpleDispatcher.getState());
        Segment firstSeg = simpleDispatcher.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleDispatcher.getUIState().getMainFileSegs().get(1);
        simpleDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleDispatcher.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // merge the last three segs, then undo
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        Segment seg1 = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        Segment seg2 = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex - 1);
        Segment seg3 = simpleDispatcher.getUIState().getMainFileSegs().get(lastSegIndex - 2);
        simpleDispatcher.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopierForUndoManagerTest committedStateCopy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        secondSeg = committedDispatcher.getUIState().getMainFileSegs().get(1);
        committedDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        seg1 = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        seg2 = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex - 1);
        seg3 = committedDispatcher.getUIState().getMainFileSegs().get(lastSegIndex - 2);
        committedDispatcher.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        committedDispatcher.undo();
        assertEquals(true, committedStateCopy.compare(committedDispatcher.getState()));
        mf = committedDispatcher.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo after both a merge and then a split were performed.
     */
    @Test
    public void testUndoMergeAndSplit() {

        // "save" the committed state
        StateCopierForUndoManagerTest state1Copy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        TranslationFile mf = committedDispatcher.getState().getMainFile();
        System.out.println("State1Copy = " + state1Copy.mainFileCopy);
        assertEquals(true, state1Copy.compare(committedDispatcher.getState()));

        // merge first two segments
        Segment firstSeg = committedDispatcher.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = committedDispatcher.getUIState().getMainFileSegs().get(1);
        committedDispatcher.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        StateCopierForUndoManagerTest state2Copy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        System.out.println("State2Copy = " + state2Copy.mainFileCopy);
        assertEquals(true, state2Copy.compare(committedDispatcher.getState()));

        // commit this newly merged segment 
        Segment mergeResult = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new Commit(mergeResult));
        StateCopierForUndoManagerTest state3Copy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        System.out.println("State3Copy = " + state3Copy.mainFileCopy);
        assertEquals(true, state3Copy.compare(committedDispatcher.getState()));

        // split the newly merged segment
        Segment commitResult = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new Split(commitResult, 1));
        StateCopierForUndoManagerTest state4Copy = new StateCopierForUndoManagerTest(committedDispatcher.getState());
        System.out.println("State4Copy = " + state4Copy.mainFileCopy);
        assertEquals(true, state4Copy.compare(committedDispatcher.getState()));

        // commit the newly split segment
        Segment splitResult = committedDispatcher.getUIState().getMainFileSegs().get(0);
        committedDispatcher.acceptAction(new Commit(splitResult));
        System.out.println("Final = " + committedDispatcher.getState().getMainFile());

        // undo three times, comparing each time with the saved states and the database
        committedDispatcher.undo();
        assertEquals(true, state4Copy.compare(committedDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedDispatcher.undo();
        assertEquals(true, state3Copy.compare(committedDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedDispatcher.undo();
        assertEquals(true, state2Copy.compare(committedDispatcher.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests the method restorePriorMainFile() without using Actions/Dispatcher,
     * and tests more fundamental base cases like just adding/removing segments
     * from various lists (regardless of whether any Actions could do this) and
     * ensuring the UndoManager updates the main file properly, regardless of
     * what the differences are.
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
