/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parses the thai text in Segments into ngrams (length of ngram specified in
 * PostingsList constructor).
 *
 * @author Chris
 */
public class PostingsList {

    private final int nGramLength;
    private HashMap<String, List<Segment>> map;

    /**
     * Parses Segment into ngrams to be stored/retrieved from database.
     *
     * @param nGramLength
     */
    public PostingsList(int nGramLength) {
        this.nGramLength = nGramLength;
        map = new HashMap<>();
    }

    /**
     * Tokenizes a segment into ngrams and adds these to the postings list. If
     * the segment is not committed, it is NOT added to the postings list.
     *
     * @param seg
     */
    public void addSegment(Segment seg) {
        if (seg != null && seg.isCommitted()) {
            List<String> ngrams = makeNGrams(seg.getThai(), nGramLength);
            ngrams.forEach(ngram -> {
                // retrieves the segments for a given nGram
                List<Segment> segList = map.get(ngram);
                /*
                If this ngram hasn't appeared in any previously seen segments, a new list is created. Otherwise, this segment is appended to the end of the list.
                 */
                if (segList == null) {
                    segList = new ArrayList();
                    segList.add(seg);
                    map.put(ngram, segList);
                } else {
                    if (!segList.contains(seg)) {
                        segList.add(seg);
                        //map.put(s, segList); // replaces old list
                    }
                }
            });
        }
    }

    public boolean addFile(BasicFile bf) {
        if (bf != null) {
            bf.getHiddenSegs().forEach(seg -> {
                if (seg.isCommitted()) {
                    addSegment(seg);
                }
            });
            bf.getActiveSegs().forEach(seg -> {
                if (seg.isCommitted()) {
                    addSegment(seg);
                }
            });
        }
        return false;
    }

    public void addCorpus(Corpus fl) {
        if (fl != null) {
            fl.getFiles().forEach(f -> addFile(f));
        }
    }

    /**
     * Returns a list of ids of TUs that contain the specified ngram. If no such
     * TU exists, an empty list is returned.
     *
     * @param ngram
     * @return A (possibly empty) list of TU ids.
     */
    public List<Segment> getMatchingID(String ngram) {
        List<Segment> ret = map.get(ngram);
        return (ret == null ? new ArrayList() : ret);

    }

    public static List<String> makeNGrams(String text, int ngLength) {
        List<String> l = new ArrayList();
        if (text.length() < ngLength) {
            l.add(text);
        } else {
            for (int i = 0; i <= text.length() - ngLength; i++) {
                String str;
                str = text.substring(i, i + ngLength);
                l.add(str);
            }
        }
        return l;
    }

    public int getNGramLength() {
        return this.nGramLength;
    }

    public HashMap<String, List<Segment>> getMap() {
        return map;
    }

    /**
     * Removes the ngrams associated with the specified Segment from the
     * postings list. If the PostingsList does not contain this segment, nothing
     * changes.
     *
     * @param seg
     */
    public void removeSegment(Segment seg) {
        List<String> ngrams = makeNGrams(seg.getThai(), nGramLength);

        List<String> nonExistentNgrams = new ArrayList();

        ngrams.forEach(ngram -> {
            List<Segment> listOfSegs = getMap().get(ngram);
            if (listOfSegs != null) {
                listOfSegs.remove(seg);

                // if there are no longer any segments matching, the ngram is removed from the pl entirely
                if (listOfSegs.isEmpty()) {
                    getMap().remove(ngram);
                }
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof PostingsList)) {
            return false;
        }

        PostingsList pl = (PostingsList) o;

        if (this.map.equals(pl.getMap())) {
            return false;
        }

        return true;
    }
}
