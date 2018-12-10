/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.TUEntryBasic;
import java.sql.Connection;
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
     * Test of addOrUpdateTU method, of class DatabaseOperations.
     
    @Test
    public void testAddOrUpdateTU() {
        System.out.println("addOrUpdateTU");
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setEnglish("English");
        expResult.setThai("Thai");
        
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), true);
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), false);
        
        TUEntryBasic result = DatabaseOperations.getTU(expResult.getID());
        assertTrue("result is null", result != null);
        System.out.println("expResult = " + expResult + "\nResult = " + result);
        
        assertEquals(expResult, result);
        
        expResult.setThai("WHatever");
        result = DatabaseOperations.getTU(expResult.getID());
        assertFalse("results same??", expResult.equals(result));
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), true);
        result = DatabaseOperations.getTU(expResult.getID());
        assertEquals(expResult, result);
        
    }
*/
  

    /**
     * Test of replaceTU method, of class DatabaseOperations.
     
    @Test
    public void testReplaceTU() {
        
        System.out.println("replaceTU");
        BasicFile bf = new BasicFile();
        TUEntryBasic tu1 = bf.newTU();
        tu1.setCommitted(false);
        tu1.setEnglish("English");
        tu1.setThai("Thai");
        assertEquals(DatabaseOperations.addTUtoDatabase(tu1), true);
        assertEquals(DatabaseOperations.tuIDExists(tu1.getID()), true);
        
        tu1.setEnglish("ENGLISH CHANGED");
        assertEquals(DatabaseOperations.addTUtoDatabase(tu1), false);
        assertFalse(DatabaseOperations.getTU(tu1.getID()).equals(tu1));
        assertEquals(DatabaseOperations.replaceTU(tu1), true);
        assertEquals(DatabaseOperations.getTU(tu1.getID()),tu1);
        
        
        
        assertEquals(DatabaseOperations.getTU(tu1.getID()), tu1);
    }
*/
   

    
/**
     * Test of addTU2 method, of class DatabaseOperations.
     * By using a single connection, maybe you can avoid certain SQL errors?
     */
    @Test
    public void testAddOrUpdateTU() {
        System.out.println("addOrUpdateTU");
        
        // initialize tu entry
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setCommitted(false);
        expResult.setEnglish("English");
        expResult.setThai("Thai");
     
        int numOfTUsInDB = DatabaseOperations.numberOfTUs();
        // Add TU (should return true)
        System.out.println("1st Push");
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), true);
        assertEquals(DatabaseOperations.getTU(expResult.getID()), expResult);
       
        // Add again (should return true)
        System.out.println("2nd Push");
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), true);
        assertEquals(DatabaseOperations.getTU(expResult.getID()), expResult);
        
        // Add a third time, with a change. When you get it the version from the database, shouldn't match, but will return true if added. 
        System.out.println("3rd Push (with change)");
        expResult.setEnglish("English changed");
        assertEquals(false, DatabaseOperations.getTU(expResult.getID()).equals(expResult));
        assertEquals(DatabaseOperations.addOrUpdateTU(expResult), true);
        
        // When you get the TU from the db, it should have the updated value. 
        assertEquals(DatabaseOperations.getTU(expResult.getID()), expResult);
        
        // After all these operations, there should still only be one more TU in db
        assertEquals(numOfTUsInDB + 1, DatabaseOperations.numberOfTUs());
    }
    
    
  



    
    
}
