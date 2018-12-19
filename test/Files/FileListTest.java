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
     * Test of getAllCommittedTUs method, of class FileList.
     */
    @Test
    public void testGetAllCommittedTUs() {
        BasicFile bf1 = new BasicFile();
        TUEntryBasic tu11 = bf1.newTU();
        tu11.setThai("bf1.1.th");
        tu11.setCommitted(true);
        TUEntryBasic tu12 = bf1.newTU();
        tu12.setThai("bf1.2.th");
        tu12.setCommitted(false);
        
        BasicFile bf2 = new BasicFile();
        TUEntryBasic tu21 = bf2.newTU();
        tu21.setThai("bf2.1.th");
        tu21.setCommitted(false);
        TUEntryBasic tu22 = bf2.newTU();
        tu22.setThai("bf2.2.th");
        tu22.setCommitted(true);
        
        FileList fl = new FileList();
        fl.addFile(bf1);
        fl.addFile(bf2);
        
        ArrayList<TUEntryBasic> result = fl.getAllCommittedTUs();
        System.out.println(result);
        assertEquals(2, result.size());
        ArrayList<TUEntryBasic> expResult = new ArrayList();
        expResult.add(tu11);
        expResult.add(tu22);
        assertEquals(expResult, result);
    }
    
}
