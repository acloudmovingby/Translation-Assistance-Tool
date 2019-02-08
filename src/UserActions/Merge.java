/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.MainFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import State.State;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Chris
 */
public class Merge implements Action {
    
    private final List<Segment> segsToMerge;

    public Merge(List<Segment> segsToMerge) {
       this.segsToMerge = segsToMerge;
    }

    @Override
    public void execute(State state) {
        // if there are fewer than 2 segs to merge, nothing can be merged
        if (segsToMerge.size()<2) {
            return;
        }
        ObservableList<Segment> mfActiveSegs = state.getMainFile().getActiveSegs();
        
        // checks that the segsToMerge exist in the main file and that they are contiguous
       
        int firstIndex = mfActiveSegs.indexOf(segsToMerge.get(0));
        if (firstIndex == -1) {
            // if that segment doesn't exist in the file, then return;
            return;
        }
        
        int k =0; // counter for segsToMerge list
        for (int i=firstIndex; i<mfActiveSegs.size(); i++) {
            // if we don't e
            if (k<segsToMerge.size() &&
                    !segsToMerge.get(k).equals(mfActiveSegs.get(i))) {
                return;
            }
        }
       
        
        // ACTUAL MERGE (ABOVE IS JUST CONFIRMING VALID INPUT)
        StringBuilder thaiSB = new StringBuilder();
        StringBuilder engSB = new StringBuilder();
        for (Segment s : segsToMerge) {
            thaiSB.append(s.getThai());
            engSB.append(s.getEnglish());
        }
        SegmentBuilder sb = new SegmentBuilder(segsToMerge.get(0));
        sb.setThai(thaiSB.toString());
        sb.setEnglish(engSB.toString());
        sb.setCommitted(false);
        state.addSeg(firstIndex, sb.createSegmentNewID());
        
        for (Segment s : segsToMerge) {
            state.removeSeg(s);
        }
        
    }
    
}
