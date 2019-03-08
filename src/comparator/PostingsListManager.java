/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.Corpus;
import DataStructures.PostingsList;
import DataStructures.Segment;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the postings lists that store the corpus as ngrams of various length.
 * This object makes it easier for other parts of the program to retrieve or
 * update these postingslists as neede.
 *
 * @author Chris
 */
public class PostingsListManager {

    private final Corpus corpus;
    private final List<PostingsList> plList;

    public PostingsListManager(Corpus c) {
        plList = new ArrayList();
        for (int i = 2; i < 9; i++) {
            plList.add(new PostingsList(i));
        }

        this.corpus = c;
        plList.forEach((pl) -> {
            pl.addCorpus(c);
        });
    }
    
    /**
     * Adds the segment to the postings lists.
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
        pl.addCorpus(corpus);
        return pl;
    }
    
    /**
     * Returns the size of all postings lists stored in this PostingsListManager. The size of a postings list is equivalent to the number of distinct ngrams it has stored in its hash table (where key is the ngram and value is the list of segs with that ngram).
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
