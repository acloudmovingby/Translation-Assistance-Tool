/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class SubstringTest {

    public SubstringTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of commonSubsOfMinLength method, of class CommonSubstringFinder.
     */
    @Test
    public void testCommonSubsOfMinLength() {
        System.out.println("commonSubsOfMinLength");
        List<String> l = new ArrayList();

        String s1 = "";
        String s2 = "";
        int minLength = 0;
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "";
        s2 = "";
        minLength = 1;
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "b";
        minLength = 1;
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "a";
        minLength = 1;
        l.add("a");
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "a";
        minLength = 2;
        l = new ArrayList();
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "bab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abb";
        s2 = "abb";
        minLength = 3;
        l = new ArrayList();
        l.add("abb");
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abb";
        s2 = "abb";
        minLength = 4;
        l = new ArrayList();
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abcabcd";
        s2 = "abcd";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("abcd", "abc"));
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abc";
        s2 = "abababc";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("abc", "ab", "ab"));
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));

        /*
        This test represents possible problems where behavior is currently not defined: complex overlapping substrings
        s1 = "ababaabb";
        s2 = "bbaabbababaabb";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("ababaabb", "baabb"));
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));
         */
        // SHOULD DO:
        // no overlapping substrings
        // should s1 be able to map to multiple places in s2? (yes?)
        // prioritize longest substring OR maximize number of characters
        // example: 
        //       abc || abTaTbTc || k=1
        //       could return: "ab" OR "a", "b", "c" OR all of them
        //       
        // if something matches multiple places in second str, should both be recorded?
        // aaTabbcTcc|| aaabbcc --> two options, equal totals
        //  
        // case where multpile shorter strings ad up to more than 1 big string but are exclusive
    }

    /**
     * Test of commonSubsOfMinLength method, of class CommonSubstringFinder.
     */
    @Test
    public void testgetS2Matches() {
        System.out.println("getS2Matches");
        boolean[] expResult;

        String s1 = "";
        String s2 = "";
        int minLength = 0;
        expResult = new boolean[0];
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));
        // CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "";
        s2 = "";
        minLength = 1;
        expResult = new boolean[0];
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "a";
        s2 = "b";
        minLength = 1;
        expResult = new boolean[1];
        expResult[0] = false;
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "a";
        s2 = "a";
        minLength = 1;
        expResult = new boolean[1];
        expResult[0] = true;
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "a";
        s2 = "a";
        minLength = 2;
        expResult = new boolean[1];
        expResult[0] = false;
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "bab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "abb";
        s2 = "abb";
        minLength = 3;
        expResult = new boolean[]{true, true, true};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "abb";
        s2 = "abb";
        minLength = 4;
        expResult = new boolean[]{false, false, false};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "abcabcd";
        s2 = "abcd";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "abc";
        s2 = "abababc";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true, true, true, true};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        s1 = "ababaabb";
        s2 = "bbaabbcababab";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true, true, true, false, true, true, true, true, true, true};
        assertEquals(CommonSubstringFinder.arrayConverter(expResult),
                CommonSubstringFinder.arrayConverter((new CommonSubstringFinder(s1, s2, minLength)).getS2MatchIntervals()));

        /*
        This test represents possible problems where behavior is currently not defined: complex overlapping substrings
        s1 = "ababaabb";
        s2 = "bbaabbababaabb";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("ababaabb", "baabb"));
        assertEquals(l, CommonSubstringFinder.commonSubsOfMinLength(s1, s2, minLength));
         */
        // SHOULD DO:
        // no overlapping substrings
        // should s1 be able to map to multiple places in s2? (yes?)
        // prioritize longest substring OR maximize number of characters
        // example: 
        //       abc || abTaTbTc || k=1
        //       could return: "ab" OR "a", "b", "c" OR all of them
        //       
        // if something matches multiple places in second str, should both be recorded?
        // aaTabbcTcc|| aaabbcc --> two options, equal totals
        //  
        // case where multpile shorter strings ad up to more than 1 big string but are exclusive
    }
    
    
    /**
     * Test of commonSubsOfMinLength method, of class CommonSubstringFinder.
     */
    @Test
    public void testMergeOverlappingIntervals() {
        System.out.println("mergeOverlappingIntervals");
        
        Pair<Integer,Integer> zeroZero = new Pair(0,0);
        Pair<Integer,Integer> oneOne = new Pair(1,1);
        Pair<Integer,Integer> zeroTwo = new Pair(0,2);
        Pair<Integer,Integer> oneThree = new Pair(1,3);
        Pair<Integer,Integer> twoFour = new Pair(2,4);
        Pair<Integer,Integer> threeFive = new Pair(3,5);
        Pair<Integer,Integer> fiveSeven = new Pair(5,7);
        Pair<Integer,Integer> sixEight = new Pair(6,8);
        Pair<Integer,Integer> fourFive = new Pair(4,5);
        Pair<Integer,Integer> zeroThree = new Pair(0,3);
        Pair<Integer,Integer> zeroFour = new Pair(0,4);
        Pair<Integer,Integer> zeroFive = new Pair(0,5);
        Pair<Integer,Integer> oneFive = new Pair(1,5);
        Pair<Integer,Integer> threeSeven = new Pair(3,7);
        Pair<Integer,Integer> zeroSix = new Pair(0,6);
        
        // no intervals
        List<Pair<Integer,Integer>> intervals = Arrays.asList();
        String result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        String expResult = stringConverter(Arrays.asList());
        assertEquals(expResult, result);     
        
        // single intervals
        intervals = Arrays.asList(zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroTwo));
        assertEquals(expResult, result);  
        
        // sorts if non-overlapping
        intervals = Arrays.asList(threeFive, zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroTwo, threeFive));
        assertEquals(expResult, result);  
        
        // one of them empty, touching the other
        intervals = Arrays.asList(zeroZero, zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroTwo));
        assertEquals(expResult, result);  
        
        // both empty (empty intervals should be discounted)
        intervals = Arrays.asList(zeroZero, oneOne);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList());
        assertEquals(expResult, result); 
        
        // if two overlapping, it merges
        intervals = Arrays.asList(zeroTwo, oneThree);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroThree));
        assertEquals(expResult, result);  
        
        // two touching, it merges
        intervals = Arrays.asList(zeroTwo, twoFour);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result);  
        
        // two overlapping, but reverse --> still merge
        intervals = Arrays.asList(oneThree, zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroThree));
        assertEquals(expResult, result); 
        
        // two touching, but reversed --> still merge
        intervals = Arrays.asList(twoFour, zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result); 
        
        
        // two of them, one entirely encompassing the other 
        intervals = Arrays.asList(oneThree, zeroFour);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result); 
        
        // two of them, one encompassing the other, but edges same 1
        intervals = Arrays.asList(zeroTwo, zeroFour);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result); 
        
        // two of them, one encompassing the other, but edges same 2
        intervals = Arrays.asList(zeroFour, twoFour);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result); 
        
        // three of them, all distinct
        intervals = Arrays.asList(threeFive, zeroTwo, sixEight);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroTwo, threeFive, sixEight));
        assertEquals(expResult, result); 
        
        // three of them, first two touching
        intervals = Arrays.asList(oneThree, threeFive, sixEight);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(oneFive, sixEight));
        assertEquals(expResult, result); 
        
        // three of them, last two touching
        intervals = Arrays.asList(zeroTwo, fiveSeven, threeFive);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroTwo, threeSeven));
        assertEquals(expResult, result);  
        
        // three of them, all touching
        intervals = Arrays.asList(zeroTwo, twoFour, fourFive);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFive));
        assertEquals(expResult, result); 
        
        // three of them, all overlapping
        intervals = Arrays.asList(twoFour, oneThree, zeroTwo);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFour));
        assertEquals(expResult, result); 
        
        // three of them, nested like Russian dolls
        intervals = Arrays.asList(zeroSix, oneFive, twoFour);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroSix));
        assertEquals(expResult, result); 
        
        // three of them, various overlapping edges, nesting
        intervals = Arrays.asList(zeroFive, oneOne, oneThree);
        result = stringConverter(CommonSubstringFinder.mergeOverlappingIntervals(intervals));
        expResult = stringConverter(Arrays.asList(zeroFive));
        assertEquals(expResult, result); 
        
        
    }
    
    public static String stringConverter(List<Pair<Integer,Integer>> intervals) {
        StringBuilder sb = new StringBuilder();
        for (Pair<Integer,Integer> interval : intervals) {
            sb.append("(");
            sb.append(interval.getKey());
            sb.append(",");
            sb.append(interval.getValue());
            sb.append("), ");
        }
        return sb.toString();
    }

}
