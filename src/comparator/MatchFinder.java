/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.PostingsList;
import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import State.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Chris
 */
public class MatchFinder {
    
    public MatchFinder() {
    }

    public static MatchList basicMatch(Segment seg, int minMatchLength, State state) {
        PostingsList pl = state.getPostingsList(
                (minMatchLength<=8 ? minMatchLength : 8));

        MatchList ml = new MatchList();
        HashSet<Segment> segsAlreadyChecked = new HashSet();
        
        // breaks seg into ngrams
        
        // for each ngram it creates list segs in corpus that have that ngram
            //shd already be tested in pl test
        
        // checks to see if that segment has already been looked at or not
            // shdnt need testing...
            
        // if not, makes boolean array of all matches of mfSeg in the corpus seg
            // this, i believe, has already been tested 
            
        // if that boolean array has at least one true, then it makes a match segment
        // 
        
        

        // makes all ngrams from seg
        List<String> nGrams = PostingsList.makeNGrams(seg.getThai(), pl.getNGramLength());
        // for each ngram in seg...
        for (String ng : nGrams) {
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl.getMatchingID(ng);
            

            // for each segment that matches that ngram....
            for (Segment s : segList) {
                if (!segsAlreadyChecked.contains(s)) {

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
                        ml.addEntry(newMatch);
                    }

                    segsAlreadyChecked.add(s);
                }
            }
        }

        return ml;
    }

    public static MatchList exactMatch(String text, State state) {
        PostingsList pl = state.getPostingsList(
                (text.length()<=8 ? text.length() : 8));
        
        MatchList mf = new MatchList();
        HashSet<Segment> segsAlreadyChecked = new HashSet();
        
         // makes all ngrams from seg
        List<String> nGrams = PostingsList.makeNGrams(text, pl.getNGramLength());
        // for each ngram in the text...
        for (String ng : nGrams) {
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl.getMatchingID(ng);
            

            // for each segment that matches that ngram....
            for (Segment s : segList) {
                if (!segsAlreadyChecked.contains(s)) {

                    boolean[] matchingChars = MatchFinder.exactStringMatch(text, s.getThai());
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
                        mf.addEntry(newMatch);
                    }

                    segsAlreadyChecked.add(s);
                }
            }
        }
        return mf;
    }
    
    /**
     * Finds if s1 is a substring of s2. If so, returns boolean array showing where s1 exists in in s2.
     * @param s1 
     * @param s2 
     * @return A boolean array of length s2 showing where s1 exists in s2.
     */
    private static boolean[] exactStringMatch(String s1, String s2) {
        ArrayList<Integer> indices = new ArrayList();
        
        //finds indices of where s1 exists in s2
        int offset = s2.indexOf(s1, 0);
        while (offset != -1) {
            indices.add(offset);
            offset = s2.indexOf(s2, offset+1);
        }
        
        boolean[] ret = new boolean[s2.length()];
        Arrays.fill(ret, false);
        for (Integer i : indices) {
            Arrays.fill(ret, i, i+s1.length(), true);
        }
        
        return ret;
    }

    public MatchList complexMatch(Segment seg) {
        return new MatchList();
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
