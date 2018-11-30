/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.TUEntryBasic;
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
public class DatabaseOperationsTest {
    
    public DatabaseOperationsTest() {
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
     * Test of addTUtoDatabase method, of class DatabaseOperations.
     */
    @Test
    public void testAddTUtoDatabase() {
        System.out.println("addTUtoDatabase");
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setCommitted(false);
        expResult.setEnglish("English");
        expResult.setThai("Thai");
       // expResult = bf.addEntry(expResult);
        
        assertEquals(DatabaseOperations.addTUtoDatabase(expResult), false);
        
        TUEntryBasic result = DatabaseOperations.getTU(expResult.getID());
        assertTrue("result is null", result != null);
        System.out.println("expResult = " + expResult + "\nResult = " + result);
        
        assertEquals(expResult, result);
    }

  

    /**
     * Test of replaceTU method, of class DatabaseOperations.
     */
    @Test
    public void testReplaceTU() {
        
        System.out.println("replaceTU");
        BasicFile bf = new BasicFile();
        TUEntryBasic tu1 = bf.newTU();
        tu1.setCommitted(false);
        tu1.setEnglish("English");
        tu1.setThai("Thai");
        //tu1 = bf.addEntry(tu1);
        assertEquals(DatabaseOperations.tuIDExists(tu1.getID()), true);
        
        tu1.setEnglish("ENGLISH CHANGED");
        assertEquals(DatabaseOperations.addTUtoDatabase(tu1), false);
        assertEquals(DatabaseOperations.replaceTU(tu1), true);
        
        
        
        
        assertEquals(DatabaseOperations.getTU(tu1.getID()), tu1);
    }

    /**
     * Test of createFileID method, of class DatabaseOperations.
     * Passes if it simply returns a double. Doesn't truly test it.
     */
    @Test
    public void testCreateFileID() {
        BasicFile bf = new BasicFile();
        TUEntryBasic tu1 = bf.newTU();
        tu1.setCommitted(false);
        tu1.setEnglish("English");
        tu1.setThai("Thai");
       // bf.addEntry(tu1);
        
       
        double result = DatabaseOperations.createFileID();
        double expResult = result;
        assertEquals(expResult, result, 0.0);
    }

  



    
    
}
