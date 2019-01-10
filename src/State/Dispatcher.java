/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import UserActions.Action;

/**
 *  
 * @author Chris
 */
public class Dispatcher {
    final DatabaseManager db;
    final State state;
    final UndoManager um;
    
    public Dispatcher(DatabaseManager db, State state, UndoManager um) {
        this.db = db;
        this.state = state;
        this.um = um;
    }
    
    public void acceptAction(Action a) {
        um.push(state); // current state is stored for undo functionality
        a.execute(state); //action executes, affecting state
        db.push(state); // new state is pushed to database
    }
    
    public void undo() {
        State priorMainFile = um.pop();
        state.resetMainFile(priorMainFile);
        db.push(state); // new state is pushed to database
    }
    
    public UIState getUIState() {
        return state.getUIState();
    }      
}
