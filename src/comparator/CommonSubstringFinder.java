/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.util.Pair;

/**
 * This class uses dynamic programming to find common substrings
 * between two strings of a minimum length. Because Java doesn't have good tuple
 * support, this class then not only runs the algorithm but then stores the various information that that algorithm produces (were there substrings (matches), where in the source string are they, where in the target string are they, etc.)
 *
 * @author Chris
 */
public final class CommonSubstringFinder {

    private final int s2LenTemp;

    public final int minLengthGlobal;
    private List<Pair<Integer,Integer>> s1commonSubIntervals;
    private List<Pair<Integer,Integer>> s2commonSubIntervals;

    /**
     * Old method that didn't use pairs of integers but instead created a bit matrix showing where matches were. 
     * @return 
     */
    public boolean[] getS2MatchIntervals() {
        boolean[] ret = new boolean[s2LenTemp];
        Arrays.fill(ret, false);
        s2commonSubIntervals.forEach((interval) -> {
            Arrays.fill(ret, interval.getKey(), interval.getValue(), true);
        });
        return ret;
    }

    public List<Pair<Integer,Integer>> getS1Intervals() {
        return this.s1commonSubIntervals;
    }
    
    public List<Pair<Integer,Integer>> getS2Intervals() {
        return this.s2commonSubIntervals;
    }

    public boolean hasCommonSubstrings() {
        // if no intervals were recorded, then we can say there are no common substrings (that reach the minimum length)
        return !s2commonSubIntervals.isEmpty();
    }

    public CommonSubstringFinder(String s1, String s2, int minLength) {
        this.minLengthGlobal = minLength;
        s1commonSubIntervals = new ArrayList();
        s2commonSubIntervals = new ArrayList();

        this.s2LenTemp = s2.length();

        if (s1.length() == 0 || s2.length() == 0 || minLength < 1) {

        }

        int[][] commonSubLengths = new int[s1.length()][s2.length()];

        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                boolean charsAreEqual = s1.charAt(i) == s2.charAt(j);

                // in theory, the next two parts could be combined, but it would be less clear and would only save a few boolean checks worth of runtime
                // (1) First part is just responsible for writing the numbers in the array to record the length of the common substrings up to that point
                if (charsAreEqual) {
                    if (i == 0 || j == 0) {
                        // if it's the begining of either string, then the length up to that point must be 1
                        commonSubLengths[i][j] = 1;
                    } else {
                        // if it's not at the beginning of the string, then the length of the common substring is the prior length up that point plus 1
                        commonSubLengths[i][j] = commonSubLengths[i - 1][j - 1] + 1;
                    }
                } else {
                    // if two characters are not the same, then set common substring length at that point to 0
                    commonSubLengths[i][j] = 0;
                }

                // (2) Second part actually checks to see if a substring of at least the minimum length has been reached yet, and if so, to record those intervals
                // note it only does this when you come to the end of a common substring (i.e. when the end of one string has been reached or if the current characters don't match)
                if (charsAreEqual) {
                    // check to see if it's at the end of either string
                    if (i + 1 == s1.length() || j + 1 == s2.length()) {
                        // add s1 interval
                        // add s2 interval
                        int currentComSubLength = commonSubLengths[i][j];
                        if (currentComSubLength >= minLength) {
                            s1commonSubIntervals.add(new Pair(i - currentComSubLength + 1, i + 1));
                            s2commonSubIntervals.add(new Pair(j - currentComSubLength + 1, j + 1));
                        }
                    }
                } else {
                    // if the chars don't match, this could indicate that a common substring just ended previous to these characters
                    // check to see if the prior characters in each string were the same; if so, then that was the end of a common substring 
                    if (i > 0 && j > 0) {
                        int currentComSubLength = commonSubLengths[i - 1][j - 1];
                        if (currentComSubLength > 0 && currentComSubLength >= minLength) {
                            s1commonSubIntervals.add(new Pair(i - currentComSubLength, i));
                            s2commonSubIntervals.add(new Pair(j - currentComSubLength, j));
                        }
                    }
                }

            }
        }
        // it's a little complicated to explain what the intervals "mean" before this, but this next step merges any overlapping intervals, losing information but making it much easier to reason about for other parts of the program
        s1commonSubIntervals = mergeOverlappingIntervals(s1commonSubIntervals);
        s2commonSubIntervals = mergeOverlappingIntervals(s2commonSubIntervals);
        

    }
    
    
    /**
     * Takes lists of intervals, sorts and then merges any that overlap. Assumes these intervals are VALID (i.e. that for each interval (i,j) that i <= j)
     * @param intervals
     * @return 
     */
    public static List<Pair<Integer,Integer>> mergeOverlappingIntervals(List<Pair<Integer,Integer>> intervals) {
        
        // sort by beginning index
        List<Pair<Integer,Integer>> sorted = intervals.stream()
                .sorted((a,b) -> a.getKey()-b.getKey())
                .collect(Collectors.toList());
        
        // remove zero length intervals
        sorted.removeIf(a -> a.getKey()==a.getValue());
        
        if (sorted.size() < 2) {
            return sorted;
        }
        
        List<Pair<Integer,Integer>> ret = new ArrayList();
        
        int begin = sorted.get(0).getKey();
        int end = sorted.get(0).getValue();
        
        for (Pair<Integer,Integer> interval : sorted) {
            if (end >= interval.getKey() && end <= interval.getValue()) {
                end = interval.getValue();
            } else if (end <= interval.getValue()) {
                ret.add(new Pair(begin, end));
                begin = interval.getKey();
                end = interval.getValue();
            }
        }
        ret.add(new Pair(begin, end));
        return ret;
    }

    /**
     * // NO LONGER USING
     *
     * Builds a two dimensional array showing all common substrings between s1
     * and s2. A value of 0 in a given cell indicates the two characters are not
     * identical. A value greater than 0 indicates those characters are
     * identical and the length of the common substring up to that point (common
     * substrings will make numbers increment diagonally from top-left to
     * bottom-right).
     *
     * For example, if s1 = "ab" and s2 = "xab", then this would return
     * [[0,1,0], [0,0,2]]
     *
     * @param s1
     * @param s2
     * @return
     */
    private static int[][] findPossibleSubsInS2(String s1, String s2) {
        int[][] subLength = new int[s1.length()][s2.length()];
        // FINDS WHERE ALL COMMON SUBSTRINGS COULD BE 
        // if a pair of characters are not identical, they are 0
        // if they are identical, then it stores the length of the substring up to that point
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (i == 0 || j == 0) {
                        subLength[i][j] = 1;
                    } else {
                        subLength[i][j] = subLength[i - 1][j - 1] + 1;
                    }
                } else {
                    subLength[i][j] = 0;
                }
            }
        }
        return subLength;
    }

    /**
     * Helper method for tests for this class.
     *
     * @param boolArr
     * @return
     */
    public static List<Boolean> arrayConverter(boolean[] boolArr) {
        List<Boolean> l = new ArrayList(boolArr.length);
        for (boolean b : boolArr) {
            l.add(b);
        }
        return l;
    }

    /**
     * NO LONGER USING Finds all common substrings between two strings (s1, s2)
     * of a minimum length with the following conditions: substrings cannot
     * overlap in s2 (but may in s1) and a single substring in s1 could map to
     * multiple substrings in s2.
     *
     * @param s1
     * @param s2
     * @param k Minimum length of substrings
     * @return List of common substrings of minimum length k
     */
    protected static List<String> commonSubsOfMinLength(String s1, String s2, int minLength) {

        // length of the common substring up to and including these indices
        int[][] subLength;

        // FINDS WHERE ALL SUBSTRINGS COULD BE 
        subLength = findPossibleSubsInS2(s1, s2);

        List<String> finalSubs = new ArrayList();
        for (int i = s1.length() - 1; i >= 0; i--) {
            for (int j = s2.length() - 1; j >= 0; j--) {
                if (subLength[i][j] > 0 && subLength[i][j] >= minLength) {
                    int subLen = subLength[i][j];
                    StringBuilder sb = new StringBuilder();
                    for (int k = 0; k < subLen; k++) {
                        sb = sb.append(s1.charAt(i - k));
                        subLength[i - k][j - k] = 0;
                    }
                    sb.reverse();
                    finalSubs.add(sb.toString());
                }
            }
        }
        return finalSubs;
    }

    public static String stringConverter(List<int[]> intervals) {
        StringBuilder sb = new StringBuilder();
        for (int[] interval : intervals) {
            sb.append("(");
            sb.append(interval[0]);
            sb.append(",");
            sb.append(interval[1]);
            sb.append("), ");
        }
        return sb.toString();
    }
}
