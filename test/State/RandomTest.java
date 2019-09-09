/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import comparator.CommonSubstringFinder;

/**
 *
 * @author Chris
 */
public class RandomTest {

    public static void main(String[] args) {
        
        
        
        // test empty intervals
        
        
        /*
        
        String s1 = "abc";
        String s2 = "abababc";
        int minLength = 1;
        CommonSubstringFinder obj = new CommonSubstringFinder(s1, s2, minLength);

        System.out.println("(" + s1 + ", " + s2 + ", " + minLength + ")");
        boolean[] answer = obj.getS2MatchIntervals();
        System.out.println("\nanswer = \n\t");
        for (boolean b : answer) {
            System.out.print(b + ", ");
        }
        
        /*
        
        
        // early termination
        System.out.println("EARLY TERMINATION");
        obj.buildMatrix("", "", 0);
        obj.buildMatrix("", "", 1);
        obj.buildMatrix("", "", 2);
        obj.buildMatrix("a", "", 1);
        obj.buildMatrix("", "a", 1);
        obj.buildMatrix("a", "", 2);
        obj.buildMatrix("a", "a", 0);
        obj.buildMatrix("aaa", "aaa", 0);
        
        // one character length
        System.out.println("\nONE CHARACTER");
        obj.buildMatrix("a", "a", 1);
        obj.buildMatrix("a", "b", 1);
        obj.buildMatrix("a", "a", 2);
        obj.buildMatrix("a", "b", 2);
        
        // two character length
        System.out.println("\nTWO CHARACTERS");
        obj.buildMatrix("aa", "bb", 1);
        obj.buildMatrix("aa", "ab", 1);
        obj.buildMatrix("aa", "ba", 1);
        obj.buildMatrix("ba", "aa", 1);
        obj.buildMatrix("aa", "aa", 1);
        obj.buildMatrix("aa", "ab", 2);
        obj.buildMatrix("aa", "ba", 2);
        obj.buildMatrix("aa", "aa", 2);
        
        // two character length
        System.out.println("\nTHREE+ CHARACTERS");
        obj.buildMatrix("aa", "bab", 1);
        obj.buildMatrix("aa", "abb", 2);
        obj.buildMatrix("aa", "baa", 2);
        obj.buildMatrix("aaa", "aa", 3);
        obj.buildMatrix("abb", "abb", 3);
        obj.buildMatrix("bbb", "babbba", 3);
        
         */
    }

}
