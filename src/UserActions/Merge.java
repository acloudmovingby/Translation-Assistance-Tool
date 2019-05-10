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
import javafx.collections.ObservableList;

/**
 *
 * @author Chris
 */
public class Merge implements MainFileAction {

    private final List<Segment> segsToMerge;

    public Merge(List<Segment> segsToMerge) {
        // NEED TO DEFENSIVELY COPY because JavaFX always has segsToMerge point back to activeSegsLists (thus causing concurrent modification issues). 
        this.segsToMerge = new ArrayList();
        segsToMerge.forEach((s) -> {
            this.segsToMerge.add(s);
        });
    }

    @Override
    public void execute(State state) {

        ObservableList<Segment> mfActiveSegs = state.getMainFile().getActiveSegs();

        if (inputIsValid(mfActiveSegs, segsToMerge)) {
            int firstIndex = mfActiveSegs.indexOf(segsToMerge.get(0));

            // create the merged segment
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

            // add the newly created segment to the main file
            state.addSegToFileActiveList(firstIndex, sb.createSegmentNewID(), state.getMainFile());

            // remove the old segments
            for (Segment s : segsToMerge) {
                state.removeSegFromFile(s, state.getMainFile());
                state.addToMainFileHidden(s, state.getMainFile());
            }
        }

        return;

    }

    /**
     * Makes sure that the segments to be merged (1) exist in the main file and
     * (2) are contiguous and in order
     *
     * @param mfActiveSegs
     * @param input
     * @return
     */
    private boolean inputIsValid(ObservableList<Segment> mfActiveSegs, List<Segment> input) {
        // if there are fewer than 2 segs to merge, nothing can be merged
        if (input.size() < 2) {
            return false;
        }

        // checks that the segsToMerge exist in the main file and that they are contiguous
        int firstIndex = mfActiveSegs.indexOf(input.get(0));
        if (firstIndex == -1) {
            // if that segment doesn't exist in the file, then return;
            return false;
        }

        int k = 0; // counter for segsToMerge list
        for (int i = firstIndex; i < mfActiveSegs.size(); i++) {
            // if we don't e
            if (k < input.size() && !input.get(k).equals(mfActiveSegs.get(i))) {
                return false;
            }
            k++;
        }

        return true;
    }

}
