/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import State.State;
import javafx.collections.ObservableList;

/**
 *
 * Represents when a user chooses to split a segment into two.
 *
 * @author Chris
 */
public class Split implements MainFileAction {

    private final Segment seg;
    private final int splitIndex;

    public Split(Segment seg, int index) {
        this.seg = seg;
        this.splitIndex = index;
    }

    @Override
    public void execute(State state) {
        
        
        ObservableList<Segment> mfActiveSegs = state.getMainFile().getActiveSegs();
        // if the seg is null or is not in the list of active segs, return
        // if the split index is out of bounds, return
        if (seg == null 
                || splitIndex <= 0 
                || splitIndex >= seg.getThai().length()) {
            return;
        }
        
        // This makes sure that the selected seg is actually in the active segs list.
        // we use == to make sure it's the exact same object (the .equals method for Segments compares the value of the fields, not identity of the object)
        // if two segs have same field values (including id) but are not the same object, this method (splitSeg) will not work correctly
        boolean isInActiveSegs = false;
        for (Segment s : mfActiveSegs) {
            if (s==seg) {
                isInActiveSegs = true;
            }
        }
        if (!isInActiveSegs) {
            return;
        }
            
        // splits the Thai text into two parts, splitting at the splitIndex
        String firstThai = seg.getThai().substring(0, splitIndex);
        String secondThai = seg.getThai().substring(splitIndex);

        // creates first new seg and inserts
        SegmentBuilder sb = new SegmentBuilder(seg);
        sb.setThai(firstThai);
        sb.setEnglish(seg.getEnglish());
        sb.setCommitted(false);
        Segment newSeg1 = sb.createSegmentNewID();
        // if the first seg replaced the old seg correctly, then the second seg is added
        if (state.replaceSeg(seg, newSeg1)) {
            // creates second new seg and inserts
            sb.setThai(secondThai);
            sb.setEnglish("");
            Segment newSeg2 = sb.createSegmentNewID();
            // inserts it after the other new Seg
            int insertIndex = mfActiveSegs.indexOf(newSeg1);
            state.addSeg(insertIndex + 1, newSeg2);
        }
        

        
    }

}
