/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import DataStructures.BasicFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import DataStructures.TestObjectBuilder;
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
     * Test of addSeg method, of class DatabaseOperations.
     * By using a single connection, maybe you can avoid certain SQL errors?
     */
    @Test
    public void testAddOrUpdateSeg() {
        System.out.println("addOrUpdateSeg");
        DatabaseOperations.rebootDB();
        
        // make the file
        BasicFile bf = new BasicFile();
        
        // add a segment
        SegmentBuilder sb = new SegmentBuilder(bf);
        sb.setThai("Thai");
        sb.setEnglish("English");
        Segment expResult = sb.createSegment();
        bf.addSeg(expResult);
     
        int numOfTUsInDB = DatabaseOperations.numberOfSegs();
        // Add seg (should return true)
        System.out.println("1st Push");
        assertEquals(DatabaseOperations.addOrUpdateSegment(expResult), true);
        assertEquals(expResult, DatabaseOperations.getSegment(expResult.getID()));
       
        // Add again (should return true)
        System.out.println("2nd Push");
        assertEquals(DatabaseOperations.addOrUpdateSegment(expResult), true);
        assertEquals(DatabaseOperations.getSegment(expResult.getID()), expResult);
        
        // Add a third time, with a change. When you get it the version from the database, shouldn't match, but will return true if added. 
        System.out.println("3rd Push (with change)");
        /*sb = new SegmentBuilder(expResult);
        sb.setEnglish("English changed");
        expResult = sb.createSegment();*/
        expResult.setEnglish("English changed");
        assertEquals(false, DatabaseOperations.getSegment(expResult.getID()).equals(expResult));
        assertEquals(DatabaseOperations.addOrUpdateSegment(expResult), true);
        
        // When you get the seg from the db, it should have the updated value. 
        assertEquals(DatabaseOperations.getSegment(expResult.getID()), expResult);
        
        // After all these operations, there should still only be one more seg in db
        assertEquals(numOfTUsInDB + 1, DatabaseOperations.numberOfSegs());
    }
    
    /**
     *
     */
    @Test
    public void testAddFile() {
        System.out.println("addFile");
        DatabaseOperations.rebootDB();
        
        
        // adding an empty file, checking it's the same when retrieved
        BasicFile emptyFile = new BasicFile();
        DatabaseOperations.addFile(emptyFile);
        assertEquals(emptyFile, DatabaseOperations.getFile(emptyFile.getFileID()));
        
        // adding the standard test file, checking it's the same.
        BasicFile file = TestObjectBuilder.getTestFile();
        DatabaseOperations.addFile(file);
        assertEquals(file, DatabaseOperations.getFile(file.getFileID()));
        
        // committing all the segs in the standard test file, checking it's the same.
        file.commitAllSegs();
        DatabaseOperations.addFile(file);
        assertEquals(file, DatabaseOperations.getFile(file.getFileID()));
        
        // inserting a segment, then checking the file's the same when retrieved.
        SegmentBuilder sb = new SegmentBuilder();
        sb.setFileID(file.getFileID());
        sb.setFileName(file.getFileName());
        file.getActiveSegs().add(sb.createSegment());
        DatabaseOperations.addFile(file);
        assertEquals(file, DatabaseOperations.getFile(file.getFileID()));
        
        // removing a segment, then checking the file's the same when retrieved.
        Segment removedSeg = file.getActiveSegs().remove(file.getActiveSegs().size()-1);
        //removedSeg.setRemoved(true);
        file.getHiddenSegs().add(removedSeg);
        DatabaseOperations.addFile(file);
        assertEquals(file, DatabaseOperations.getFile(file.getFileID()));
    }
    
    
    
    
  



    
    
}
