/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.Segment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Manages the postings lists that store the corpus as ngrams of various lengths.
 *
 * This object makes it easier for other parts of the program to retrieve or
 * update these postingslists as needed.
 *
 * @author Chris
 */
public class PostingsListManager {

    private final HashSet<Segment> committedSegments;
    private final List<PostingsList> plList;

    public PostingsListManager(HashSet<Segment> committedSegments) {

        this.committedSegments = committedSegments;

        plList = new ArrayList();
        for (int i = 4; i < 9; i++) {
            plList.add(new PostingsList(i));
        }

        plList.forEach((pl) -> {
            pl.addMultipleSegments(committedSegments);
        });
    }

    /**
     * Adds the segment to the postings lists.
     *
     * @param s
     */
    public void addSegment(Segment s) {
        for (PostingsList pl : plList) {
            pl.addSegment(s);
        }
    }

    public void removeSegment(Segment s) {
        for (PostingsList pl : plList) {
            pl.removeSegment(s);
        }
    }

    public PostingsList getPostingsList(int ngramLength) {
        for (PostingsList pl : plList) {
            if (ngramLength == pl.getNGramLength()) {
                return pl;
            }
        }
        PostingsList pl = new PostingsList(ngramLength);
        pl.addMultipleSegments(committedSegments);
        return pl;
    }

    /**
     * Returns the size of all postings lists stored in this
     * PostingsListManager. The size of a postings list is equivalent to the
     * number of distinct ngrams it has stored in its hash table (where key is
     * the ngram and value is the list of segs with that ngram).
     *
     * @return
     */
    public int size() {
        int ret = 0;
        for (PostingsList pl : plList) {
            ret = ret + pl.getMap().size();
        }
        return ret;
    }

}
