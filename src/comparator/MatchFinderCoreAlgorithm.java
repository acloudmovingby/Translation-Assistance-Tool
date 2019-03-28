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
 * Operations for finding matches between a source segment and a corpus (or individual other segments).
 * 
 * Currently uses dynamic programming but could possibly be better implemented with suffix trees.
 * 
 * @author Chris
 */
public class MatchFinderCoreAlgorithm {

    /**
     * For a given source segment this returns a list of matches (MatchSegments) that have substrings in common of a minimum length. 
     * 
     * 
     * @param source The segment which you would like to compare against the corpus and find matches.
     * @param minMatchLength The minimum length a 
     * @param state The state whose corpus you are searching for a match.
     * @return A MatchList that contain MatchSegments indicating where in the Segments are common substrings.
     */
    public static MatchList basicMatch(Segment source, State state) {
        int minMatchLength = state.getMinMatchLength();
        // if a min matchLength is greater than 8, it simply looks at ngrams of length 8
        PostingsList pl = state.getPostingsList(
                (minMatchLength<=8 ? minMatchLength : 8));

        MatchList ml = new MatchList();
        HashSet<Segment> segsAlreadyChecked = new HashSet();
       
        // makes all ngrams from seg
        List<String> nGrams = PostingsList.makeNGrams(source.getThai(), pl.getNGramLength());
        // for each ngram in seg...
        for (String ng : nGrams) {
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl.getMatchingID(ng);
            

            // for each segment that matches that ngram....
            for (Segment target : segList) {
                if (!segsAlreadyChecked.contains(target)) {
                    
                    MatchSegment newMatch = getSingleSegmentMatch(source, target, minMatchLength);

                    if (newMatch != null) {
                        ml.addEntry(newMatch);
                    }
                    
                    segsAlreadyChecked.add(target);
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

                    boolean[] matchingChars = MatchFinderCoreAlgorithm.exactStringMatch(text, s.getThai());
                    boolean hasMatch = false;
                    for (int i = 0; i < matchingChars.length; i++) {
                        if (matchingChars[i]) {
                            hasMatch = true;
                            break;
                        }
                    }

                    if (hasMatch) {
                        MatchSegment newMatch = new MatchSegment(s);
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
     * Compares the source segment and the target segment and if there are common substrings between the Thai strings of both segments of a minimum length, then a MatchSegment is returned. If no such match is found (i.e. no common substrings of a minimum length), then null is returned. 
     *
     * @param source The segment for which you would like to find a match (common substring).
     * @param target The committed segment in which you are searching for a match.
     * @param minMatchLength The minimum desired length of the substring match.
     * @return A MatchSegment if a match exists or null if no match exists.
     */
    public  static MatchSegment getSingleSegmentMatch(Segment source, Segment target, int minMatchLength) {
        boolean[] matchingChars = Substring.getS2Matches(source.getThai(), target.getThai(), minMatchLength);
                    boolean hasMatch = false;
                    // checks to see if there is any match at all
                    for (int i = 0; i < matchingChars.length; i++) {
                        if (matchingChars[i]) {
                            hasMatch = true;
                            break;
                        }
                    }
                    
                    // if there is a match, it creates a MatcSegment and adds it to the MatchList
                    if (hasMatch) {
                        MatchSegment newMatch = new MatchSegment(target);
                        newMatch.setThai(target.getThai());
                        newMatch.setEnglish(target.getEnglish());
                        newMatch.setFileName(target.getFileName());
                        newMatch.setMatches(matchingChars);
                        return newMatch;
                    } else {
                        return null;
                    }
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
