/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import State.State;
import java.util.ArrayList;
import java.util.List;

/**
 * Uncommits one or more segments in the MainFile.
 *
 * @author Chris
 */
public class Uncommit implements MainFileAction {

    //private final Segment seg; 
    private final List<Segment> segList;

    public Uncommit(Segment seg) {
        segList = new ArrayList();
        segList.add(Segment.getDeepCopy(seg));
    }

    public Uncommit(List<Segment> segList) {
        // NEED TO DEFENSIVELY COPY because JavaFX always has seglist list point back to activeSegsList in main file (thus causing concurrent modification issues). 
        List<Segment> listCopy = new ArrayList();
        segList.forEach((s) -> {
            listCopy.add(Segment.getDeepCopy(s));
        });
        this.segList = listCopy;
    }

    @Override
    public void execute(State state) {
        for (Segment seg : segList) {
            // if the seg isn't committed, then do nothing (no need to uncommit).
            if (seg.isCommitted()) {
                SegmentBuilder sb = new SegmentBuilder(seg);
                sb.setCommitted(false);
                state.replaceSegInFile(seg, sb.createSegmentNewID(), state.getMainFile());
            }
        }
    }

}
