/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.FileBuilder;
import Database.DatabaseOperations;
import State.DatabaseManager;
import State.Dispatcher;
import State.State;
import State.UIState;
import State.UndoManager;

/**
 *
 * @author Chris
 */
public class Initializer {
    
    private final Dispatcher d;
    private final State state;
    private final DatabaseManager dm;
    
    public Initializer(BasicFile mainFile, Corpus corpus) {
        state = new State(mainFile, corpus);
        dm = new DatabaseManager(state);
        d = new Dispatcher(dm, state, new UndoManager());
    }
    
    public UIState getUIState() {
        return state.getUIState();
    }
    
    public Dispatcher getDispatcher() {
        return d;
    }
        
    public State getState() {
        return state;
    }
    
}
