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
    
    public StateBuilder(BasicFile mainFile, List<BasicFile> fileList) {
        state = new State(fileList);
        d = new Dispatcher(state);
        d.setMainFile(mainFile);
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
