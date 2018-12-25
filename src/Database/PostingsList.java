/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.FileList;
import Files.Segment;
import comparator.NGramWrapper;
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
     * If TU is committed, it parses the ngrams and adds to the hash.
     *
     * @param tu
     */
    public void addTU(Segment tu) {
        if (tu != null) {
            List<String> l = makeNGrams(tu.getThai(), nGramLength);
            l.forEach(s -> {
                List<Segment> oldIDs = map.get(s);
                if (oldIDs == null) {
                    oldIDs = new ArrayList();
                    oldIDs.add(tu);
                    map.put(s, oldIDs);
                } else {
                    if (!oldIDs.contains(tu)) {
                        oldIDs.add(tu);
                        map.put(s, oldIDs);
                    }
                }
            });
        }
    }

    public boolean addFile(BasicFile bf) {
        if (bf != null) {
            bf.getRemovedTUs().forEach(tu -> {
                if(tu.isCommitted()) {
                    addTU(tu);
                }
            });
            bf.getTUsToDisplay().forEach(tu -> {if(tu.isCommitted()) {addTU(tu);}});
        }
        return false;
    }

    public void addFileList(FileList fl) {
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
