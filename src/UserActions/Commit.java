/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import State.State;

/**
 *
 * @author Chris
 */
public class Commit implements Action {
    
    private final Segment seg; 
    
    Commit(Segment seg) {
        this.seg = seg;
    }

    @Override
    public void execute(State state) {
        //state.commit(seg);
        
        // if the seg is already committed, then do nothing. 
        if (seg.isCommitted()) {
            return;
        } else {
            SegmentBuilder sb = new SegmentBuilder(seg);
            sb.setCommitted(true);
            state.replaceSeg(seg, sb.createSegment());
        }
    }
    
}
