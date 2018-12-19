/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Database.DatabaseOperations;
import Database.PostingsList;
import Files.CompareFile;
import Files.FileList;
import Files.TUCompareEntry;
import Files.TUEntryBasic;
import java.util.List;

/**
 *
 * @author Chris
 */
public class MatchFinder {

    PostingsList pl1;
    PostingsList pl2;
    PostingsList pl3;
    PostingsList pl4;
    PostingsList pl5;
    PostingsList pl6;

    public MatchFinder() {
    }

    public CompareFile basicMatch(TUEntryBasic seg, int minMatchLength, FileList corpus) {
        
        pl3 = new PostingsList(3);
        pl3.addFileList(corpus);
        
        
        CompareFile cf = new CompareFile();

        List<String> nGrams = PostingsList.makeNGrams(seg.getThai(), 3);

        // for each ngram in the segment...
        for (String ng : nGrams) {
            List<Integer> idList = pl3.getMatchingID(ng);
            // for each segment id that matches that ngram....
            for (Integer id : idList) {
                // get that segment
                TUEntryBasic possibleMatchSeg = corpus.getTU(id);
                // find actual matches between it and the input segment
                Matches m = findStringMatches(possibleMatchSeg.getThai(), seg.getThai(), minMatchLength);
                // if matches is not empty, then add them to the compare fie
                if (!m.isEmpty()) {
                    TUCompareEntry ce = new TUCompareEntry();
                    ce.setThai(seg.getThai());
                    ce.setEnglish(seg.getEnglish());
                    ce.setFileName(seg.getFileName());

                    for (MatchEntry3 me : m.getMatchList()) {
                        int startIndex = -1;
                        int endIndex = -1;
                        for (int i = 0; i < me.indices.size(); i++) {
                            startIndex = me.indices.get(i);
                            endIndex = startIndex + me.match.length() - 1;
                        }
                        ce.addMatchInterval(startIndex, endIndex);
                    }
                    cf.addEntry(ce);
                }
            }
        }

        return cf;
    }

    public CompareFile exactMatch(String text) {
        return new CompareFile();
    }

    public CompareFile complexMatch(TUEntryBasic seg) {
        return new CompareFile();
    }

    /*
   LEGACY CODE FROM ORIGINAL COMPARATOR
     */
    private CompareFile findMatches(String text, TUEntryBasic tu, int length) {
        CompareFile cFile = new CompareFile();
        if (tu.isCommitted()) {
            Matches m = findStringMatches(text, tu.getThai(), length);
            if (!m.isEmpty()) {
                TUCompareEntry ce = new TUCompareEntry();
                ce.setThai(tu.getThai());
                ce.setEnglish(tu.getEnglish());
                ce.setFileName(tu.getFileName());

                for (MatchEntry3 me : m.getMatchList()) {
                    int startIndex = -1;
                    int endIndex = -1;
                    for (int i = 0; i < me.indices.size(); i++) {
                        startIndex = me.indices.get(i);
                        endIndex = startIndex + me.match.length() - 1;
                    }
                    ce.addMatchInterval(startIndex, endIndex);
                }
                cFile.addEntry(ce);
            }
        }
        return cFile;
    }

    private Matches findStringMatches(String t1, String t2, int length) {

        NGramWrapper n1 = new NGramWrapper(t1, length);
        NGramWrapper n2 = new NGramWrapper(t2, length);

        Matches ret = new Matches();

        for (int i = 0; i < n1.getList().size(); i++) {
            String s = n1.getList().get(i);

            // finds indices of all matching ngrams in corpus
            List<Integer> indices = n2.contains(s);

            if (indices != null) {
                for (int j : indices) {

                    if (!checkPrior(i, j, t1, t2)) {

                        String segment = s;

                        String foo1 = remaining(i, n1, length);
                        String foo2 = remaining(j, n2, length);

                        segment = segment.concat(afters(foo1, foo2));

                        ret.addMatch(segment, i, j);

                    }
                }
            }
        }
        return ret;
    }

    private String afters(String t1, String t2) {

        StringBuilder afterMatch = new StringBuilder(14);
        boolean finished = false;

        for (int i = 0; i < t1.length() && i < t2.length() && finished == false; i++) {

            if (t1.charAt(i) == t2.charAt(i)) {
                afterMatch.append(t1.charAt(i));

            } else {
                finished = true;
            }
        }
        return afterMatch.toString();
    }

    /**
     *
     * @param i text index of ngram match
     * @param j corpus index of ngram match
     * @return true if the prior character at i-1, j-1 is identical. Helps
     * prevent redundant match checking
     */
    private boolean checkPrior(int i, int j, String fileText, String corpusText) {

        if (i > 0 && j > 0) {
            return fileText.charAt(i - 1) == corpusText.charAt(j - 1);
        } else {
            return false;
        }

    }

    private String remaining(int i, NGramWrapper text, int length) {

        String str = "";

        if (text.length() - i > length) {
            str = text.getText().substring(i + length);
        }
        return str;
    }
}
