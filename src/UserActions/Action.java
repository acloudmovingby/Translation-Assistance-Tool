/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import State.State;

/**
 *
 * @author Chris
 */
public interface Action {
    void execute(State state);
}
