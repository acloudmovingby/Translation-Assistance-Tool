/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import UserActions.MainFileAction;

/**
 * Receives messages from the Controller.
 *
 * These include all user forms of input on a main file: editing, committing,
 * split, merge, etc.
 *
 * When the user performs an action that affects the MainFile, a
 *
 * @author Chris
 */
public class Dispatcher {

    final DatabaseManager dm;
    final State state;
    final UndoManager um;

    public Dispatcher(DatabaseManager db, State state, UndoManager um) {
        this.dm = db;
        this.state = state;
        this.um = um;
    }

    public void acceptAction(MainFileAction a) {
        um.push(state); // current state is stored for undo functionality
        a.execute(state); //action executes, affecting state
        dm.push(state); // new state is pushed to database
    }

    public void undo() {
        um.executeUndo(state); // takes state and replaces the main file with the prior stored version in UndoManager
        dm.push(state); // new state is pushed to database
    }

    public UIState getUIState() {
        return state.getUIState();
    }

    public State getState() {
        return state;
    }

    public DatabaseManager getDM() {
        return dm;
    }
}
