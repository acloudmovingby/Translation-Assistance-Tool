/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.FileList;
import Files.TUEntryBasic;
import comparator.NGramWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parses TUEntryBasic into ngrams to be stored/retrieved from database.
 *
 * @author Chris
 */
public class PostingsList {

    private final int nGramLength;
    private HashMap<String, List<Integer>> map;

    /**
     * Parses TUEntryBasic into ngrams to be stored/retrieved from database.
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
    public void addTU(TUEntryBasic tu) {
        if (tu != null) {
            List<String> l = makeNGrams(tu.getThai(), nGramLength);
            l.forEach(s -> {
                List<Integer> oldIDs = map.get(s);
                if (oldIDs == null) {
                    oldIDs = new ArrayList();
                    oldIDs.add(tu.getID());
                    map.put(s, oldIDs);
                } else {
                    if (!oldIDs.contains(tu.getID())) {
                        oldIDs.add(tu.getID());
                        map.put(s, oldIDs);
                    }
                }
            });
        }
    }

    public void addFile(BasicFile bf) {
        if (bf != null) {
            System.out.println("removed: " + bf.getRemovedTUs().size());
            System.out.println("disp:" + bf.getTUsToDisplay().size());
            bf.getRemovedTUs().forEach(tu -> {if(tu.isCommitted()) {addTU(tu);}});
            bf.getTUsToDisplay().forEach(tu -> {if(tu.isCommitted()) {addTU(tu);}});
        }
    }

    public void addFileList(FileList fl) {
        System.out.println("hash before: " + map.size());
        if (fl != null) {
            System.out.println("here1");
            fl.getFiles().forEach(f -> addFile(f));
        }
        System.out.println("hash after: " + map.size());
    }

    /**
     * Returns a list of ids of TUs that contain the specified ngram. If no such
     * TU exists, an empty list is returned.
     *
     * @param ngram
     * @return A (possibly empty) list of TU ids.
     */
    public List<Integer> getMatchingID(String ngram) {
        List<Integer> ret = map.get(ngram);
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

}
