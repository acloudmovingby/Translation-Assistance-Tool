/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
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
public class FileListTest {
    
    public FileListTest() {
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
     * Test of getAllCommittedTUs method, of class Corpus.
     */
    @Test
    public void testGetAllCommittedTUs() {
        BasicFile bf1 = new BasicFile();
        Segment tu11 = bf1.newSeg();
        tu11.setThai("bf1.1.th");
        tu11.setCommitted(true);
        Segment tu12 = bf1.newSeg();
        tu12.setThai("bf1.2.th");
        tu12.setCommitted(false);
        
        BasicFile bf2 = new BasicFile();
        Segment tu21 = bf2.newSeg();
        tu21.setThai("bf2.1.th");
        tu21.setCommitted(false);
        Segment tu22 = bf2.newSeg();
        tu22.setThai("bf2.2.th");
        tu22.setCommitted(true);
        
        Corpus fl = new Corpus();
        fl.addFile(bf1);
        fl.addFile(bf2);
        
        ArrayList<Segment> result = fl.getAllCommittedTUs();
        System.out.println(result);
        assertEquals(2, result.size());
        ArrayList<Segment> expResult = new ArrayList();
        expResult.add(tu11);
        expResult.add(tu22);
        assertEquals(expResult, result);
    }
    
}
