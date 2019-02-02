/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

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

    Corpus corpus;
    List<PostingsList> plList;

    PostingsListManager(Corpus c) {
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
}
