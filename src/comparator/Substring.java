/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class Substring {
    
    
    public static int isZeroCounter;
    public static int isNotZeroCounter;

    /**
     * Finds all common substrings between two strings (s1, s2) of a minimum
     * length with the following conditions: substrings cannot overlap in s2
     * (but may in s1) and a single substring in s1 could map to multiple
     * substrings in s2.
     *
     * @param s1
     * @param s2
     * @param k Minimum length of substrings
     * @return List of common substrings of minimum length k
     */
    public static List<String> commonSubsOfMinLength(String s1, String s2, int minLength) {

        // length of the common substring up to and including these indices
        int[][] subLength = new int[s1.length()][s2.length()];

        // FINDS WHERE ALL SUBSTRINGS COULD BE 
        subLength = findPossibleSubsInS2(s1, s2);
        

        List<String> finalSubs = new ArrayList();
        for (int i = s1.length() - 1; i >= 0; i--) {
            for (int j = s2.length() - 1; j >= 0; j--) {
                if (subLength[i][j]>0 && subLength[i][j] >= minLength) {
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
    
    private static int[][] findPossibleSubsInS2(String s1, String s2) {
        int[][] subLength = new int[s1.length()][s2.length()];
        // FINDS WHERE ALL SUBSTRINGS COULD BE 
        // if a pair of characters are not identical, they are 0
        // if it is >0, it represents the legnth of the substring up to that point
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                       isNotZeroCounter++;
                    if (i == 0 && j == 0) {
                        subLength[0][0] = 1;
                    } else if (i == 0 && j > 0) {
                        subLength[0][j] = 1;
                    } else if (i > 0 && j == 0) {
                        subLength[i][0] = 1;
                    } else if (subLength[i - 1][j - 1]>0) {
                        subLength[i][j] = subLength[i - 1][j - 1] + 1;
                    } else {
                        subLength[i][j] = 1;
                    }
                } else {
                    isZeroCounter++;
                    subLength[i][j] = 0;
                }
            }
        }
        return subLength;
    }
    
    /**
     * Finds which characters in s2 could be part of a common substring between s1 and s2 of a minimum length.
     * Note following conditions: a single substring in s1 could map to multiple
     * substrings in s2. 
     * @param s1
     * @param s2
     * @param minLength
     * @return Boolean array whose size is length of s2.
     */
    public static boolean[] getS2Matches(String s1, String s2, int minLength) {
        int[][] subLengths = findPossibleSubsInS2(s1, s2);
        boolean[] matches = new boolean[s2.length()];
        for (boolean b : matches) {
            b=false;
        }
        
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                int subStringLength = subLengths[i][j];
                if (subStringLength >= minLength) {
                    // backtracks and marks all characters indices for that substring as "true"
                    // sets the lengths of those prior chars to zero, to prevent redundant checking
                    for (int k = 0; k < subStringLength; k++) {
                        subLengths[i - k][j - k] = 0;
                        matches[j-k] = true;
                    }
                }
            }
        }
        return matches;
    }
    
    public static List<Boolean> arrayConverter(boolean[] boolArr) {
         List<Boolean> l = new ArrayList(boolArr.length);
        for (boolean b : boolArr) {
            l.add(b);
        }
        return l;
    }

    
}
