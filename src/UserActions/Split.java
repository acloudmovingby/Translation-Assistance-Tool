/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import State.State;

/**
 * 
 * Represents when a user chooses to split a segment into two.
 * 
 * @author Chris
 */
public class Split implements Action {
    
    private final Segment seg;
    private final int index;
    
    public Split(Segment seg, int index) {
        this.seg = seg;
        this.index = index;
    }

    @Override
    public void execute(State state) {
         state.split(seg, index);
    }
    
}
