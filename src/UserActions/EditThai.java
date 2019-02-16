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
public class EditThai implements Action {
    
    Segment seg;
    String newThaiText;
    
    public EditThai(Segment seg, String newThaiText) {
        this.seg = seg;
        this.newThaiText = newThaiText;
    }

    @Override
    public void execute(State state) {
        
        if (!state.getMainFile().getActiveSegs().contains(seg)) {
            return; // if the seg doesn't exist in the file, then this Action should do nothing
        } else {
            SegmentBuilder sb = new SegmentBuilder(seg);
            sb.setThai(newThaiText);
            sb.setCommitted(false);
            Segment newSeg = sb.createSegmentNewID();
            
            state.replaceSeg(seg, newSeg);
        }
    }
    
}
