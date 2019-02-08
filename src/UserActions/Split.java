/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import State.PostingsListManager;
import State.State;
import java.util.List;

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
        // splits the main file segment
        List<Segment> newSegs = state.getMainFile().splitSeg(seg, index);

        // if the split didn't fail, (newSegs isn't null), the postingslists are updated
        if (newSegs != null) {
            // updates the postings lists
            PostingsListManager plm = state.getPostingsListManager();
            //plm.removeSegment(seg); // remove old segment
            for (Segment s : newSegs) {
                plm.addSegment(s); // add new segment
            }
        }
    }

}
