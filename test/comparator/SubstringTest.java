/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * Test of commonSubsOfMinLength method, of class Substring.
     */
    @Test
    public void testCommonSubsOfMinLength() {
        System.out.println("commonSubsOfMinLength");
        List<String> l = new ArrayList();

        String s1 = "";
        String s2 = "";
        int minLength = 0;
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "";
        s2 = "";
        minLength = 1;
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "b";
        minLength = 1;
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "a";
        minLength = 1;
        l.add("a");
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "a";
        s2 = "a";
        minLength = 2;
        l = new ArrayList();
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "bab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abb";
        s2 = "abb";
        minLength = 3;
        l = new ArrayList();
        l.add("abb");
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        l = new ArrayList();
        l.add("ab");
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abb";
        s2 = "abb";
        minLength = 4;
        l = new ArrayList();
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abcabcd";
        s2 = "abcd";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("abcd", "abc"));
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        s1 = "abc";
        s2 = "abababc";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("abc", "ab", "ab"));
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));

        /*
        This test represents possible problems where behavior is currently not defined: complex overlapping substrings
        s1 = "ababaabb";
        s2 = "bbaabbababaabb";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("ababaabb", "baabb"));
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));
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
     * Test of commonSubsOfMinLength method, of class Substring.
     */
    @Test
    public void testgetS2Matches() {
        System.out.println("getS2Matches");
        boolean[] expResult;

        String s1 = "";
        String s2 = "";
        int minLength = 0;
        expResult = new boolean[0];
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "";
        s2 = "";
        minLength = 1;
        expResult = new boolean[0];
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "a";
        s2 = "b";
        minLength = 1;
        expResult = new boolean[1];
        expResult[0] = false;
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "a";
        s2 = "a";
        minLength = 1;
        expResult = new boolean[1];
        expResult[0] = true;
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "a";
        s2 = "a";
        minLength = 2;
        expResult = new boolean[1];
        expResult[0] = false;
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "bab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "abb";
        s2 = "abb";
        minLength = 3;
        expResult = new boolean[]{true, true, true};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "ab";
        s2 = "abb";
        minLength = 2;
        expResult = new boolean[]{true, true, false};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "abb";
        s2 = "abb";
        minLength = 4;
        expResult = new boolean[]{false, false, false};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "abcabcd";
        s2 = "abcd";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "abc";
        s2 = "abababc";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true, true, true, true};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        s1 = "ababaabb";
        s2 = "bbaabbcababab";
        minLength = 2;
        expResult = new boolean[]{true, true, true, true, true, true, false, true, true, true, true, true, true};
        assertEquals(Substring.arrayConverter(expResult),
                Substring.arrayConverter(Substring.getS2Matches(s1, s2, minLength)));

        /*
        This test represents possible problems where behavior is currently not defined: complex overlapping substrings
        s1 = "ababaabb";
        s2 = "bbaabbababaabb";
        minLength = 2;
        l = new ArrayList();
        l.addAll(Arrays.asList("ababaabb", "baabb"));
        assertEquals(l, Substring.commonSubsOfMinLength(s1, s2, minLength));
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

}
