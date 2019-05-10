/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import State.State;

/**
 * Represents a user action that directly affects segments in a file, and therefore the postings lists.
 * They are sent by the {@link Controller} to the Dispatcher.
 *
 * @author Chris
 * 
 */
public interface MainFileAction {

    void execute(State state);
    
    /*
    DELETE TEMP UNNECESSARY
    - if we change the main file, what needs to change? Possibilities:
        - wrap UM, DM and UIState together around a mainfile
        - just reset UM/DM/UIState
        - have Dispatcher control UIState, not State.
        - have reset method for UIState/DM/UM (how do I initialize these so far?):
            - UM: totally fine, just re-initialize
            - DM: constructor takes state. After state is set, then you can re-initialize
            - UIState: seems to be hooked up elsewhere. Maybe just set all values in constructor?
        - can easily move up to Dispatcher:
            - export committed segs
        - can UIState be moved to Dispatcher? Let's assume that MainFile stays in State because it's hard to move.
            - declaration: not a problem cuz it doesn't take args
            - setting mainFileSegs isn't hard to change, can be done in Disp
            - two cases where matchList is reset: newSelection / minLength chngd, both are easy to move up
            - 
    */
}
