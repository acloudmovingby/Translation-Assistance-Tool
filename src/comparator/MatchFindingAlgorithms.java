/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;

/**
 * Provides methods to find matches between a source Segment and some target Segment.
 *
 * Currently uses dynamic programming but could probably be better implemented
 * with suffix trees or other means.
 *
 * @author Chris
 */
public class MatchFindingAlgorithms {

    /**
     * Returns a list representing all the segments from a corpus that form
     * a "basic match" with the source Segment. A "basic match" between two
     * segments means these two Segments have common substring(s) of at least
     * the minimum length.
     *
     * @param source The segment which you would like to compare against the
     * corpus and find matches.
     * @param minMatchLength The minimum length of a "match" (a common
     * substring).
     * @param pl This is the PostingsList (ngrams-Segment pairs) representing
     * the corpus.
     * @return A MatchList showing all matches found for the source Segment.
     */
    public static List<MatchSegment> basicMatch(Segment source, int minMatchLength, PostingsList pl) {

        List<MatchSegment> matchList = new ArrayList();
        HashSet<Segment> segsAlreadyChecked = new HashSet();

        // for testing
        List<Pair<String, Integer>> ngramFreq = new ArrayList();
        
        // makes ngrams from seg
        List<String> nGrams = PostingsList.makeNGrams(source.getThai(), pl.getNGramLength());
        // for each ngram in the source text...
        for (String ng : nGrams) {
            // finds segments in the corpus that match that ngram
            List<Segment> segList = pl.getMatchingID(ng);
            ngramFreq.add(new Pair(ng + ": " + segList.size(), segList.size()));//testing
            // for each segment that matches that ngram....
            for (Segment target : segList) {
                if (!segsAlreadyChecked.contains(target) && target.getID()!=source.getID()) {
                    // uses Optionals to check if there is a new match. If so, adds to the list of matches
                    singleSegBasicMatch(source, target, minMatchLength)
                            .ifPresent(newMatchSegment -> matchList.add(newMatchSegment));
                    segsAlreadyChecked.add(target);
                }
            }
        }
        /* useful for testing:
        ngramFreq.sort((a,b) -> b.getValue()-a.getValue());//testing
        ngramFreq.stream().map(a -> a.getKey()).forEach(System.out::println);//testing
        */
        
        
        // sort in decreasing order according to the longest matching substring
            ///listOfMatches.sort((a,b) -> b.longestMatchLengthProperty().get()-a.longestMatchLengthProperty().get());
        return matchList;
    }
    
    
            
            

   
    /**
     * Compares the source segment and the target segment and if there are
     * common substrings between the Thai strings of both segments of a minimum
     * length, then a MatchSegment is returned. If no such match is found (i.e.
     * no common substrings of a minimum length), then null is returned.
     *
     * @param source The segment for which you would like to find a match
     * (common substring).
     * @param target The committed segment in which you are searching for a
     * match.
     * @param minMatchLength The minimum desired length of the substring match.
     * @return A MatchSegment if a match exists or null if no match exists.
     */
    public static Optional<MatchSegment> singleSegBasicMatch(Segment source, Segment target, int minMatchLength) {
        CommonSubstringFinder commonSubstrings = new CommonSubstringFinder(source.getThai(), target.getThai(), minMatchLength);
        //boolean[] matchingChars = commonSubstrings.getS2MatchIntervals();
        boolean hasMatch = commonSubstrings.hasCommonSubstrings();

        // if there is a match, it creates a MatchSegment and adds it to the MatchList
        if (hasMatch) {
            MatchSegment newMatch = new MatchSegment(target);
            /*newMatch.setThai(target.getThai());
            newMatch.setEnglish(target.getEnglish());
            newMatch.setFileName(target.getFileName());
            newMatch.setMatches(matchingChars);*/
            newMatch.setSourceMatchIntervals(commonSubstrings.getS1Intervals());
            newMatch.setTargetMatchIntervals(commonSubstrings.getS2Intervals());
            return Optional.of(newMatch);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Finds if s1 is a substring of s2. If so, returns boolean array showing
     * where s1 exists in in s2.
     *
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
            offset = s2.indexOf(s2, offset + 1);
        }

        boolean[] ret = new boolean[s2.length()];
        Arrays.fill(ret, false);
        for (Integer i : indices) {
            Arrays.fill(ret, i, i + s1.length(), true);
        }

        return ret;
    }

}
