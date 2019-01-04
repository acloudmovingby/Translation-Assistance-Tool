/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.Corpus;
import Files.Segment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parses Segment into ngrams to be stored/retrieved from database.
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
     * Tokenizes a segment into ngrams and adds these to the postings list.
     *
     * @param seg
     */
    public void tokenizeSegment(Segment seg) {
        if (seg != null) {
            List<String> l = makeNGrams(seg.getThai(), nGramLength);
            l.forEach(s -> {
                // retrieves the segments for a given nGram (s)
                List<Segment> segList = map.get(s);
                /*
                If this ngram hasn't appeared in any previously seen segments, a new list is created. Otherwise, this segment is appended to the end of the list.
                */
                if (segList == null) {
                    segList = new ArrayList();
                    segList.add(seg);
                    map.put(s, segList);
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
            bf.getRemovedSegs().forEach(tu -> {
                if (tu.isCommitted()) {
                    tokenizeSegment(tu);
                }
            });
            bf.getActiveSegs().forEach(tu -> {
                if (tu.isCommitted()) {
                    tokenizeSegment(tu);
                }
            });
        }
        return false;
    }

    public void addFileList(Corpus fl) {
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

}
