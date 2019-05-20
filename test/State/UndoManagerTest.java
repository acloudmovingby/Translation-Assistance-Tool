/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
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

    TopLevelBackEnd emptyBackEnd;
    TopLevelBackEnd oneSegBackEnd;
    /**
     * 5 segs in three files. None are committed
     */
    TopLevelBackEnd simpleBackEnd;
    TopLevelBackEnd committedBackEnd;
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
        StateBuilder emptyStateInit = new StateBuilder(emptyState.getMainFile(), emptyState.getCorpusFiles());
        emptyBackEnd = emptyStateInit.getTopLevelBackEnd();

        // 1 file with 1 seg. This seg is not committed
        State oneSegState = TestObjectBuilder.getOneSegState();
        StateBuilder oneSegStateInit = new StateBuilder(oneSegState.getMainFile(), oneSegState.getCorpusFiles());
        oneSegBackEnd = oneSegStateInit.getTopLevelBackEnd();

        // 5 segs in three files. None are committed
        State simpleState = TestObjectBuilder.getTestState();
        StateBuilder simpleStateInit = new StateBuilder(simpleState.getMainFile(), simpleState.getCorpusFiles());
        simpleBackEnd = simpleStateInit.getTopLevelBackEnd();
        lastSegIndex = simpleState.getMainFile().getActiveSegs().size() - 1;

        // same as simple state except all files are committed
        State simpleCommittedState = TestObjectBuilder.getCommittedTestState();
        StateBuilder simpleCommittedInit = new StateBuilder(simpleCommittedState.getMainFile(), simpleCommittedState.getCorpusFiles());
        committedBackEnd = simpleCommittedInit.getTopLevelBackEnd();
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
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.undo();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));

        // one segment state
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        oneSegBackEnd.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));

        // generic, simple state
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));

        // simple state where all segs are committted
        StateCopier simpleCommittedCopy = new StateCopier(committedBackEnd.getState());
        committedBackEnd.undo();
        assertEquals(true, simpleCommittedCopy.compare(committedBackEnd.getState()));
    }

    /**
     * Tests running undo on states after a Commit action was performed NOT
     * READY
     */
    @Test
    public void testUndoCommit() {

        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.acceptAction(new Commit(TestObjectBuilder.getTestSeg()));
        emptyBackEnd.undo();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));
        BasicFile mf = emptyBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // main file with one segment
        // commit the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        Segment onlyExistantSeg = oneSegBackEnd.getUIState().getMainFileSegs().get(0);
        oneSegBackEnd.acceptAction(new Commit(onlyExistantSeg));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        oneSegBackEnd.undo();
        oneSegStateCopy.compare(oneSegBackEnd.getState());
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));
        mf = oneSegBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        Segment firstSeg = simpleBackEnd.getUIState().getMainFileSegs().get(0);
        simpleBackEnd.acceptAction(new Commit(firstSeg));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        Segment lastSeg = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleBackEnd.acceptAction(new Commit(lastSeg));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        StateCopier committedStateCopy = new StateCopier(committedBackEnd.getState());
        firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new Commit(firstSeg));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        committedBackEnd.acceptAction(new Commit(lastSeg));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after an EditEnglish action was performed
     * NOT READY
     */
    @Test
    public void testUndoEditEnglish() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.acceptAction(new EditEnglish(TestObjectBuilder.getTestSeg(), "test"));
        emptyBackEnd.undo();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));
        BasicFile mf = emptyBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // edit the english in the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        Segment onlyExistantSeg = oneSegBackEnd.getUIState().getMainFileSegs().get(0);
        oneSegBackEnd.acceptAction(new EditEnglish(onlyExistantSeg, "test"));
        oneSegBackEnd.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));
        mf = oneSegBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // editEnglish the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        Segment firstSeg = simpleBackEnd.getUIState().getMainFileSegs().get(0);
        simpleBackEnd.acceptAction(new EditEnglish(firstSeg, "test"));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editEnglish the last seg then undo
        Segment lastSeg = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleBackEnd.acceptAction(new EditEnglish(lastSeg, "test"));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        StateCopier committedStateCopy = new StateCopier(committedBackEnd.getState());
        firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new EditEnglish(firstSeg, "test"));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        lastSeg = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        committedBackEnd.acceptAction(new EditEnglish(lastSeg, "test"));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after an EditThai action was performed
     */
    @Test
    public void testUndoEditThai() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.acceptAction(new EditThai(TestObjectBuilder.getTestSeg(), "test"));
        emptyBackEnd.undo();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));
        BasicFile mf = emptyBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // editThai the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        Segment onlyExistantSeg = oneSegBackEnd.getUIState().getMainFileSegs().get(0);
        oneSegBackEnd.acceptAction(new EditThai(onlyExistantSeg, "test"));
        oneSegBackEnd.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));
        mf = oneSegBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // editThai the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        Segment firstSeg = simpleBackEnd.getUIState().getMainFileSegs().get(0);
        simpleBackEnd.acceptAction(new EditThai(firstSeg, "test"));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        Segment lastSeg = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleBackEnd.acceptAction(new EditThai(lastSeg, "test"));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        mf = simpleBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedBackEnd.getState());
        firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new EditThai(firstSeg, "test"));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        committedBackEnd.acceptAction(new EditThai(lastSeg, "test"));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after a Split action was performed
     */
    @Test
    public void testUndoSplit() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.acceptAction(new Split(TestObjectBuilder.getTestSeg(), 1));
        emptyBackEnd.undo();
        BasicFile mf = emptyBackEnd.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // split the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        Segment onlyExistantSeg = oneSegBackEnd.getUIState().getMainFileSegs().get(0);
        oneSegBackEnd.acceptAction(new Split(onlyExistantSeg, 1));
        oneSegBackEnd.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // split the first seg then undo
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        Segment firstSeg = simpleBackEnd.getUIState().getMainFileSegs().get(0);
        simpleBackEnd.acceptAction(new Split(firstSeg, 1));
        simpleBackEnd.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // split the last seg then undo
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        Segment lastSeg = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        simpleBackEnd.acceptAction(new Split(lastSeg, 1));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedBackEnd.getState());
        firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new Split(firstSeg, 1));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        lastSeg = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        committedBackEnd.acceptAction(new Split(lastSeg, 1));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo on states after a Merge action was performed
     */
    @Test
    public void testUndoMerge() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyBackEnd.getState());
        emptyBackEnd.acceptAction(new Merge(new ArrayList<Segment>()));
        emptyBackEnd.undo();
        BasicFile mf = emptyBackEnd.getState().getMainFile();
        assertEquals(true, emptyStateCopy.compare(emptyBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // one segment state
        // "merge" the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegBackEnd.getState());
        Segment onlyExistantSeg = oneSegBackEnd.getUIState().getMainFileSegs().get(0);
        oneSegBackEnd.acceptAction(new Merge(Arrays.asList(onlyExistantSeg)));
        oneSegBackEnd.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // generic, simple state
        // merge first two segs, then undo
        StateCopier simpleStateCopy = new StateCopier(simpleBackEnd.getState());
        Segment firstSeg = simpleBackEnd.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = simpleBackEnd.getUIState().getMainFileSegs().get(1);
        simpleBackEnd.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        simpleBackEnd.undo();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // merge the last three segs, then undo
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        Segment seg1 = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        Segment seg2 = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex - 1);
        Segment seg3 = simpleBackEnd.getUIState().getMainFileSegs().get(lastSegIndex - 2);
        simpleBackEnd.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        simpleBackEnd.undo();
        assertEquals(true, simpleStateCopy.compare(simpleBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // committed state
        // editThai the first seg then undo
        StateCopier committedStateCopy = new StateCopier(committedBackEnd.getState());
        firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        secondSeg = committedBackEnd.getUIState().getMainFileSegs().get(1);
        committedBackEnd.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // editThai the last seg then undo
        seg1 = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex);
        seg2 = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex - 1);
        seg3 = committedBackEnd.getUIState().getMainFileSegs().get(lastSegIndex - 2);
        committedBackEnd.acceptAction(new Merge(Arrays.asList(seg1, seg2, seg3)));
        committedBackEnd.undo();
        assertEquals(true, committedStateCopy.compare(committedBackEnd.getState()));
        mf = committedBackEnd.getState().getMainFile();
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
    }

    /**
     * Tests running undo after both a merge and then a split were performed.
     */
    @Test
    public void testUndoMergeAndSplit() {

        // "save" the committed state
        StateCopier state1Copy = new StateCopier(committedBackEnd.getState());
        BasicFile mf = committedBackEnd.getState().getMainFile();
        System.out.println("State1Copy = " + state1Copy.mainFileCopy);
        assertEquals(true, state1Copy.compare(committedBackEnd.getState()));

        // merge first two segments
        Segment firstSeg = committedBackEnd.getUIState().getMainFileSegs().get(0);
        Segment secondSeg = committedBackEnd.getUIState().getMainFileSegs().get(1);
        committedBackEnd.acceptAction(new Merge(Arrays.asList(firstSeg, secondSeg)));
        StateCopier state2Copy = new StateCopier(committedBackEnd.getState());
        System.out.println("State2Copy = " + state2Copy.mainFileCopy);
        assertEquals(true, state2Copy.compare(committedBackEnd.getState()));

        // commit this newly merged segment 
        Segment mergeResult = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new Commit(mergeResult));
        StateCopier state3Copy = new StateCopier(committedBackEnd.getState());
        System.out.println("State3Copy = " + state3Copy.mainFileCopy);
        assertEquals(true, state3Copy.compare(committedBackEnd.getState()));

        // split the newly merged segment
        Segment commitResult = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new Split(commitResult, 1));
        StateCopier state4Copy = new StateCopier(committedBackEnd.getState());
        System.out.println("State4Copy = " + state4Copy.mainFileCopy);
        assertEquals(true, state4Copy.compare(committedBackEnd.getState()));

        // commit the newly split segment
        Segment splitResult = committedBackEnd.getUIState().getMainFileSegs().get(0);
        committedBackEnd.acceptAction(new Commit(splitResult));
        System.out.println("Final = " + committedBackEnd.getState().getMainFile());

        // undo three times, comparing each time with the saved states and the database
        committedBackEnd.undo();
        assertEquals(true, state4Copy.compare(committedBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedBackEnd.undo();
        assertEquals(true, state3Copy.compare(committedBackEnd.getState()));
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        committedBackEnd.undo();
        assertEquals(true, state2Copy.compare(committedBackEnd.getState()));
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
