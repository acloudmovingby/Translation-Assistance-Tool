/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import java.util.List;

/**
 *
 * @author Chris
 */
public class StateBuilder {

    private final Dispatcher d;
    private final State state;
    private final DatabaseManager dm;
    
    public StateBuilder(BasicFile mainFile, List<BasicFile> fileList) {
        state = new State(mainFile, fileList);
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
