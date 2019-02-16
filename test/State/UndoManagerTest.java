/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.Corpus;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import JavaFX_1.Initializer;
import UserActions.Commit;
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
     */
    @Test
    public void testUndoCommit() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        emptyStateDispatcher.acceptAction(new Commit(TestObjectBuilder.getTestSeg()));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        
        // one segment state
        // commit the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Commit(onlyExistantSeg));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        
        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Commit(firstSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment lastSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        oneSegStateDispatcher.acceptAction(new Commit(lastSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
    }
    
    /**
     * Tests running undo on states after an EditThai action was performed
     */
    @Test
    public void testUndoEditThai() {
        // empty state
        StateCopier emptyStateCopy = new StateCopier(emptyStateDispatcher.getState());
        //emptyStateDispatcher.acceptAction(new EditThai(TestObjectBuilder.getTestSeg()));
        emptyStateDispatcher.undo();
        assertEquals(true, emptyStateCopy.compare(emptyStateDispatcher.getState()));
        
        // one segment state
        // commit the only segment in the main file
        StateCopier oneSegStateCopy = new StateCopier(oneSegStateDispatcher.getState());
        Segment onlyExistantSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Commit(onlyExistantSeg));
        oneSegStateDispatcher.undo();
        assertEquals(true, oneSegStateCopy.compare(oneSegStateDispatcher.getState()));
        
        // generic, simple state
        // commit the first seg, undo
        // commit the last seg, undo
        StateCopier simpleStateCopy = new StateCopier(simpleStateDispatcher.getState());
        Segment firstSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(0);
        oneSegStateDispatcher.acceptAction(new Commit(firstSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
        Segment lastSeg = oneSegStateDispatcher.getUIState().getMainFileSegs().get(lastSegIndex);
        oneSegStateDispatcher.acceptAction(new Commit(lastSeg));
        simpleStateDispatcher.undo();
        assertEquals(true, simpleStateCopy.compare(simpleStateDispatcher.getState())); 
    }
}
