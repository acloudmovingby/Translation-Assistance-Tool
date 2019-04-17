/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import State.State;

/**
 * Represents a user action that directly affects segments in the main file. They are sent by the {@link Controller} to the Dispatcher.
 * @author Chris
 */
public interface MainFileAction {
    void execute(State state);
}
