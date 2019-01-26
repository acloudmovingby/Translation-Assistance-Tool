/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import DataStructures.PostingsList;
import DataStructures.BasicFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import java.util.ArrayList;
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
public class PostingsListTest {
    
    public PostingsListTest() {
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
     * Test of tokenizeSegment method, of class PostingsList.
     */
    @Test
    public void testParseseg() {
        System.out.println("parseseg");
         // 3-gram postings list
        PostingsList pl1 = new PostingsList(3);
        
        // no segs have been parsed yet, so returns empty list.
        assertEquals(true, pl1.getMatchingID("null").isEmpty());
        
         // create seg
        BasicFile bf1 = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(bf1);
        sb.setThai("Abcabcdddd");
        Segment seg1 = sb.createSegment();
        pl1.tokenizeSegment(seg1);
        
        // set up expResult and result lists
        List<Segment> expResult = new ArrayList();
        expResult.add(seg1);
        List<Segment> result;
        
        // all these ngrams should now be stored in PostingsList
        result = pl1.getMatchingID("abc");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("Abc");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("bca");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("cab");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("bcd");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("cdd");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("ddd");
        assertEquals(expResult, result);
        
        // these won't be
        expResult = new ArrayList();
        result = pl1.getMatchingID("");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("d");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("dd");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("dddd");
        assertEquals(expResult, result);
        result = pl1.getMatchingID("acb");
        assertEquals(expResult, result);
    }

    /**
     * Test of getMatchingID method, of class PostingsList.
     */
    @Test
    public void testGetMatchingID() {
        System.out.println("getMatchingID");
        
        
        // test if no seg has been added
        // test if an ngram of improper length is queried
        // test if one seg has been added, should return that id
        // test if two segs have been added, should return:
            // both ids for ngrams that match both
            // one id for ngram that matches one
        //   
       
        // 3-gram postings list
        PostingsList pl1 = new PostingsList(3);
        
        // no segs have been parsed yet, so returns empty list.
        assertEquals(true, pl1.getMatchingID("null").isEmpty());
        
        // test 1 seg
        BasicFile bf1 = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(bf1);
        sb.setThai("Abcabcdddd");
        
        
        Segment seg1 = sb.createSegment();
        pl1.tokenizeSegment(seg1);
        assertEquals(seg1, pl1.getMatchingID("abc").get(0));
        
        // test reparsing seg (shouldn't change results)
        pl1.tokenizeSegment(seg1);
        assertEquals(1, pl1.getMatchingID("abc").size());
        
        // test adding 2nd Seg'
        sb = new SegmentBuilder(bf1);
        sb.setThai("Abcabcdeee");
        Segment seg2 = sb.createSegment();
        pl1.tokenizeSegment(seg2);
        List<Segment> expResult = new ArrayList();
        expResult.add(seg1);
        expResult.add(seg2);
        List<Segment> result = pl1.getMatchingID("abc");
        assertEquals(expResult, result);
        
        // test uniqueness of 2nd Seg
        expResult = new ArrayList();
        expResult.add(seg2);
        result = pl1.getMatchingID("eee");
        assertEquals(expResult, result);
        
        // test uniquness of 1st Seg
        expResult = new ArrayList();
        expResult.add(seg1);
        result = pl1.getMatchingID("ddd");
        assertEquals(expResult, result);
    }

    
    
}
