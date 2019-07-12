/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.List;

/**
 * REFACTORING: 
 * - This class uses dynamic programming to find common substrings between two strings of a minimum length. Because Java doesn't have good tuple support, this class then stores the answers to various questions: 
 *            - are there any common substrings
 *            - where are common substrings in each string 
 *            - what's the longest common substring
 * 
 * OLD VERSION:
 * This class serves one purpose: Uses dynamic programming to find substrings of
 * a minimum length in s2 that are also substrings in s1. This is returned as a
 * boolean array of length s2 where true indicates it is a common substring.
 * Note that a single substring in s1 could map to multiple substrings in s2.
 *
 * @author Chris
 */
public final class CommonSubstringAlgorithmHelper {
    
    // Didn't use getters/setters for simplicity (this whole class is just a tuple returned from a function) 
    public boolean hasCommonSubstrings;
    public final int minLength;
    public final boolean[] s2MatchIntervals; // need to change to list of int interval offsets
    public final List<int[]> s1MatchIntervals;

    public CommonSubstringAlgorithmHelper(String s1, String s2, int minLength) {
        this.minLength = minLength;
        this.s2MatchIntervals = getS2Matches(s1,s2,minLength);
        this.s1MatchIntervals = new ArrayList(); // TEMP, not correct
    }
    /**
     * Uses dynamic programming to find substrings of a minimum length in s2
     * that are also substrings in s1. This is returned as a boolean array of
     * length s2 where true indicates it is a common substring. Note that a
     * single substring in s1 could map to multiple substrings in s2.
     *
     * @param s1
     * @param s2
     * @param minLength
     * @return Boolean array whose size is length of s2.
     */
    protected static boolean[] getS2Matches(String s1, String s2, int minLength) {
        int[][] subLengths = findPossibleSubsInS2(s1, s2);
        
        
        boolean[] matches = new boolean[s2.length()];
        for (boolean b : matches) {
            b = false;
        }

        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                int subStringLength = subLengths[i][j];
                if (subStringLength >= minLength) {
                    // backtracks and marks all characters indices for that substring as "true"
                    
                    for (int k = 0; k < subStringLength; k++) {
                        subLengths[i - k][j - k] = 0;
                        matches[j - k] = true;
                    }
                }
            }
        }
        return matches;
    }

    /**
     * REFACTOR: 
     * - will replace this with a version that builds tuple as it operates
     * 
     * Builds a two dimensional array showing all common substrings between s1
     * and s2. A value of 0 in a given cell indicates the two characters are not
     * identical. A value greater than 0 indicates those characters are
     * identical and the length of the common substring up to that point
     * (common substrings will make numbers increment diagonally from top-left to bottom-right).
     * 
     * For example, if s1 = "ab" and s2 = "xab", then this would return [[0,1,0], [0,0,2]]
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
     * redo of findPossibleSubsInS2. Will write intervals as you go, will take the min length, won't build the whole matrix first and then delete as before. May not be much faster (not faster in big O for sure), but then you do everything you need in one step?
     * @param s1
     * @param s2 
     */
    private void buildMatrix(String s1, String s2, int minLength) {
        
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
    
    
}
