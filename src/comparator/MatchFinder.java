/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Database.DatabaseOperations;
import Database.PostingsList;
import Files.MatchFile;
import Files.FileList;
import Files.MatchSegment;
import Files.Segment;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Chris
 */
public class MatchFinder {

    public MatchFinder() {
    }

    /**
     * Takes a segment (seg) and looks through the FileList (corpus) to find all
     * other segments that have matching substrings of a minimum length.
     *
     * @param seg
     * @param minMatchLength
     * @param corpus
     * @return
     *
    public static MatchFile basicMatch(Segment seg, int minMatchLength, FileList corpus) {

        PostingsList pl3 = new PostingsList(minMatchLength);
        // processes the corpus to quickly be able to search for ngrams
        pl3.addFileList(corpus);

        MatchFile cf = new MatchFile();
        HashSet<Segment> segsAlreadyStored = new HashSet();

        int totalSegs = 0;
        int numNonRepeats = 0;
        int numMatching = 0;

        // makes all ngrams from seg1
        List<String> nGrams = PostingsList.makeNGrams(seg.getThai(), minMatchLength);

        // for each ngram in seg1...
        for (String ng : nGrams) {
            //System.out.println("\tng = " + ng);
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl3.getMatchingID(ng);
            totalSegs = totalSegs + segList.size();

            // for each segment id that matches that ngram....
            for (Segment s : segList) {
                //System.out.println("\t\ts = " + s.getID() + ", ");
                if (!segsAlreadyStored.contains(s)) {
                    numNonRepeats = numNonRepeats + 1;
                    // creates a Matches object
                    Matches m = findStringMatches(seg.getThai(), s.getThai(), minMatchLength);
                    // if the Matches object is not empty, then adds that match to the compare fie
                    if (!m.isEmpty()) {
                        numMatching = numMatching + 1;
                        MatchSegment ce = new MatchSegment();
                        ce.setThai(s.getThai());
                        ce.setEnglish(s.getEnglish());
                        ce.setFileName(s.getFileName());

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
                        segsAlreadyStored.add(s);
                    }
                }
            }
        }
        System.out.println("finished: \n\ttotalSegs = " + totalSegs);
        System.out.println("\tnumNonRepeats = " + numNonRepeats);
        System.out.println("\tnumMatching = " + numMatching);

        return cf;
    }
*/
    public static MatchFile basicMatch2(Segment seg, int minMatchLength, FileList corpus) {
        PostingsList pl3 = new PostingsList(minMatchLength);
        // processes the corpus to quickly be able to search for ngrams
        pl3.addFileList(corpus);

        MatchFile cf = new MatchFile();
        HashSet<Segment> segsAlreadyChecked = new HashSet();

        // makes all ngrams from seg1
        List<String> nGrams = PostingsList.makeNGrams(seg.getThai(), minMatchLength);

        // for each ngram in seg1...
        for (String ng : nGrams) {
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl3.getMatchingID(ng);

            // for each segment id that matches that ngram....
            for (Segment s : segList) {
                //System.out.println("\t\ts = " + s.getID() + ", ");
                if (!segsAlreadyChecked.contains(s)) {
                    // creates a Matches object
                    Matches m = findStringMatches(seg.getThai(), s.getThai(), minMatchLength);

                    boolean[] matchingChars = Substring.getS2Matches(seg.getThai(), s.getThai(), minMatchLength);
                    boolean hasMatch = false;
                    for (int i = 0; i < matchingChars.length; i++) {
                        if (matchingChars[i]) {
                            hasMatch = true;
                            break;
                        }
                    }

                    if (hasMatch) {
                        MatchSegment newMatch = new MatchSegment();
                        newMatch.setThai(s.getThai());
                        newMatch.setEnglish(s.getEnglish());
                        newMatch.setFileName(s.getFileName());
                        newMatch.setMatches(matchingChars);
                        cf.addEntry(newMatch);
                    }

                    segsAlreadyChecked.add(s);
                }
            }
        }

        return cf;
    }

    public MatchFile exactMatch(String text) {
        return new MatchFile();
    }

    public MatchFile complexMatch(Segment seg) {
        return new MatchFile();
    }

    /*
   LEGACY CODE FROM ORIGINAL COMPARATOR
     */

    /**
     * Takes two strings (s1, s2) and returns a Matches object, which represents
     * all the common substrings between s1 and s2 that are of at least length
     * k.
     *
     * @param s1 First string
     * @param s2 Second string
     * @param k Minimum length of match
     * @return Matches object
     */
    public static Matches findStringMatches(String s1, String s2, int k) {

        NGramWrapper n1 = new NGramWrapper(s1, k);
        NGramWrapper n2 = new NGramWrapper(s2, k);

        Matches ret = new Matches();

        for (int i = 0; i < n1.getList().size(); i++) {
            String s = n1.getList().get(i);

            List<Integer> indices = n2.contains(s);

            if (indices != null) {
                for (int j : indices) {

                    if (!checkPrior(i, j, s1, s2)) {

                        String segment = s;

                        String foo1 = remaining(i, n1, k);
                        String foo2 = remaining(j, n2, k);

                        segment = segment.concat(afters(foo1, foo2));

                        ret.addMatch(segment, i, j);

                    }
                }
            }
        }
        return ret;
    }

    private static String afters(String t1, String t2) {

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
    private static boolean checkPrior(int i, int j, String fileText, String corpusText) {

        if (i > 0 && j > 0) {
            return fileText.charAt(i - 1) == corpusText.charAt(j - 1);
        } else {
            return false;
        }

    }

    private static String remaining(int i, NGramWrapper text, int length) {

        String str = "";

        if (text.length() - i > length) {
            str = text.getText().substring(i + length);
        }
        return str;
    }

    // split getThai into ngrams ( O(m) m=length of thai Text, m is small)
    // look up each ngram matching segments, hash table, O(1), 
    // for each segment match, see if it 
    // make copy of ngram/segment hashtable, each time a segment is accessed, remove from memory?
    // problem: we re-search adjacent ngrams and keep
    //      even if we see smth once, we may find a match later in the ngram
    //      key: remove ngrams from prior parts
    //      what if we just searched the whole corpus as a string and just see if it works? 
}
