/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.TUEntryBasic;
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
     * Test of addTU method, of class PostingsList.
     */
    @Test
    public void testParseTU() {
        System.out.println("parseTU");
         // 3-gram postings list
        PostingsList pl1 = new PostingsList(3);
        
        // no TUs have been parsed yet, so returns empty list.
        assertEquals(true, pl1.getMatchingID("null").isEmpty());
        
         // create TU
        BasicFile bf1 = new BasicFile();
        TUEntryBasic tu1 = bf1.newTU();
        tu1.setThai("Abcabcdddd");
        pl1.addTU(tu1);
        
        // set up expResult and result lists
        List<Integer> expResult = new ArrayList();
        expResult.add(tu1.getID());
        List<Integer> result;
        
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
        
        
        // test if no TU has been added
        // test if an ngram of improper length is queried
        // test if one TU has been added, should return that id
        // test if two TUs have been added, should return:
            // both ids for ngrams that match both
            // one id for ngram that matches one
        //   
       
        // 3-gram postings list
        PostingsList pl1 = new PostingsList(3);
        
        // no TUs have been parsed yet, so returns empty list.
        assertEquals(true, pl1.getMatchingID("null").isEmpty());
        
        // test 1 TU
        BasicFile bf1 = new BasicFile();
        TUEntryBasic tu1 = bf1.newTU();
        tu1.setThai("Abcabcdddd");
        pl1.addTU(tu1);
        assertEquals(Integer.valueOf(tu1.getID()), pl1.getMatchingID("abc").get(0));
        
        // test reparsing TU (shouldn't change results)
        pl1.addTU(tu1);
        assertEquals(1, pl1.getMatchingID("abc").size());
        
        // test adding 2nd TU
        TUEntryBasic tu2 = bf1.newTU();
        tu2.setThai("Abcabcdeee");
        pl1.addTU(tu2);
        List<Integer> expResult = new ArrayList();
        expResult.add(tu1.getID(), tu2.getID());
        List<Integer> result = pl1.getMatchingID("abc");
        assertEquals(expResult, result);
        
        // test uniqueness of 2nd TU
        expResult = new ArrayList();
        expResult.add(tu2.getID());
        result = pl1.getMatchingID("eee");
        assertEquals(expResult, result);
        
        // test uniquness of 1st TU
        expResult = new ArrayList();
        expResult.add(tu1.getID());
        result = pl1.getMatchingID("ddd");
        assertEquals(expResult, result);
    }

    
    
}
