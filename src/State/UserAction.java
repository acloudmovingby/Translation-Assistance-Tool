/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

/**
 *
 * @author Chris
 */
public interface UserAction {

    /**
     * Makes the appropriate change to the state.
     * @param state
     */
    public void executeStateChange(State state);
}
