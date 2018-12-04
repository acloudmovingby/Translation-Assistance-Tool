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
     * Test of addTU2 method, of class DatabaseOperations.
     * By using a single connection, maybe you can avoid certain SQL errors?
     */
    @Test
    public void testAddTU2() {
        System.out.println("addTU2");
        
        // initialize tu entry
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setCommitted(false);
        expResult.setEnglish("English");
        expResult.setThai("Thai");
        
        // set up connectoin
        System.out.println("1 Connect");
        Connection conn = DatabaseOperations.connect();
        
        // Add TU
        System.out.println("2 Add");
        assertEquals(DatabaseOperations.addTU2(conn, expResult), true);
       /* try {conn.close();} catch (Exception e) {};
        conn = DatabaseOperations.connect();*/
        // Get TU and check
        System.out.println("3 Get");
        TUEntryBasic result = DatabaseOperations.getTU2(conn, expResult.getID());
        
       
        assertEquals(expResult, result);
    }
    
    /**
     * Test of addTUtoDatabase method, of class DatabaseOperations.
     */
    @Test
    public void testAddTUtoDatabase() {
        System.out.println("addTUtoDatabase");
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setEnglish("English");
        expResult.setThai("Thai");
        
        assertEquals(DatabaseOperations.addTUtoDatabase(expResult), true);
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
        assertEquals(DatabaseOperations.addTUtoDatabase(tu1), true);
        assertEquals(DatabaseOperations.tuIDExists(tu1.getID()), true);
        
        tu1.setEnglish("ENGLISH CHANGED");
        assertEquals(DatabaseOperations.addTUtoDatabase(tu1), false);
        assertFalse(DatabaseOperations.getTU(tu1.getID()).equals(tu1));
        assertEquals(DatabaseOperations.replaceTU(tu1), true);
        assertEquals(DatabaseOperations.getTU(tu1.getID()),tu1);
        
        
        
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
        
       
        double result = DatabaseOperations.createFileID();
        double expResult = result;
        assertEquals(expResult, result, 0.0);
    }

    
/**
     * Test of addTU2 method, of class DatabaseOperations.
     * By using a single connection, maybe you can avoid certain SQL errors?
     */
    @Test
    public void testPushTU() {
        System.out.println("pushTU");
        
        // initialize tu entry
        BasicFile bf = new BasicFile();
        TUEntryBasic expResult = bf.newTU();
        expResult.setCommitted(false);
        expResult.setEnglish("English");
        expResult.setThai("Thai");
     
        
        // Add TU (should return true)
        System.out.println("1st Push");
        assertEquals(DatabaseOperations.pushTU(expResult), true);
       
        // Add again (should return true)
        System.out.println("2nd Push");
        assertEquals(DatabaseOperations.pushTU(expResult), true);
        
        // Add a third time, with a change (should still return true)
        System.out.println("3rd Push (with change)");
        expResult.setEnglish("English changed");
        assertEquals(DatabaseOperations.pushTU(expResult), true);
        
        // When you get the TU from the db, it should have the updated value. 
        assertEquals(DatabaseOperations.getTU(expResult.getID()), expResult);
    }
    
    
  



    
    
}
