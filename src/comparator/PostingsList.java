/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.Segment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Takes Segments and parses the Thai text in into ngrams (length of ngram specified in
 * PostingsList constructor).
 *
 * @author Chris
 */
public class PostingsList {

    private final int nGramLength;
    private final HashMap<String, List<Segment>> map;

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
    
    /**
     * Runs addSegment(seg) on all Segments in the given HashSet.
     * @param segs 
     */
    public void addMultipleSegments(HashSet<Segment> segs) {
        segs.forEach((seg) -> this.addSegment(seg));
    }

    /**
     * Returns a list of ids of Segments that contain the specified ngram. If no such
     * Segment exists, then an empty list is returned.
     *
     * @param ngram
     * @return A (possibly empty) list of Segment ids.
     */
    public List<Segment> getMatchingID(String ngram) {
        List<Segment> ret = map.get(ngram);
        return (ret == null ? new ArrayList() : ret);

    }

    /**
     * Takes a String and returns a contiguous sequence of substrings from it, each with the specified length. 
     * 
     * For example: ("hello", 2) would return ["he", "el", "ll", "lo"]. If the length is longer than the string's length, then the String is returned wrapped in a list (one element). This is the only circumstance in which the strings in the list will not have the specified length.
     * 
     * @param text
     * @param ngramLength
     * @return The sequence of n-grams
     */
    public static List<String> makeNGrams(String text, int ngramLength) {
        List<String> l = new ArrayList();
        if (text.length() < ngramLength) {
            l.add(text);
        } else {
            for (int i = 0; i <= text.length() - ngramLength; i++) {
                String str;
                str = text.substring(i, i + ngramLength);
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

        // CURRENTLY THIS DOESN'T SEEM TO WORK...probably because it's not doing a deep comparison of the HashMaps
        return (this.getNGramLength() == pl.getNGramLength() && this.map.equals(pl.getMap()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.map);
        return hash;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        map.entrySet().forEach((e) -> {
            sb.append(e.getKey()).append(": ").append(e.getValue());
        });
        return sb.toString();
    }
}
