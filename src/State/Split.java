/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Files.Segment;

/**
 * Causes the segment to be split at the specified index.
 * @author Chris
 */
public class Split implements UserAction {
    
    Segment seg;
    int index;
    
    public Split(Segment seg, int index) {
        this.seg = seg;
        this.index = index;
    }

    @Override
    public void executeStateChange(State state) {
        state.getMainFile().splitTU(seg, index);
    }
    
}
