/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.MainFile;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import Database.DatabaseOperations;
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
public class DatabaseManagerTest {
    
    public DatabaseManagerTest() {
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
     * Test of backupMainFile method, of class DatabaseManager.
     */
    @Test
    public void testBackupMainFile() {
        DatabaseOperations.rebootDB();
        
        System.out.println("backupMainFile");
        
        // (0)
        // test with an empty main file
        State emptyState = TestObjectBuilder.getEmptyState();
        MainFile emptyMF = emptyState.getMainFile();
        DatabaseManager dmEmpty = new DatabaseManager(emptyState);
        // checks to see that the file is in the db already
        assertEquals(emptyMF, DatabaseOperations.getFile(emptyMF.getFileID()));
        // checks to see that the file is the sam even after backing up the unchanged file
        dmEmpty.push(emptyState);
        assertEquals(emptyMF, DatabaseOperations.getFile(emptyMF.getFileID()));
        
        
        State state = TestObjectBuilder.getTestState();
        DatabaseManager dm = new DatabaseManager(state);
        MainFile mf = state.getMainFile();
        int fileID = mf.getFileID();
        
        // (1) 
        // check to see that the new file is in the database 
        // this should happen because DatabaseManager should auto-add the file to the db on construction
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        
        // (2)
        // check to see that the file is the same after I we just add the file without any changes made
        int numFiles = DatabaseOperations.getAllFileIDs().size();
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // checks that number of files in DB are still the same
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());
         
        // (3) 
        // make a subtraction to the file (beg / middle / end?)
        // check to make sure changes are reflected in db
        Segment removed = mf.getActiveSegs().get(0);
        mf.removeTU(removed);
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // checks that number of files in DB are still the same
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());
        
        // (4)
        // make several additions to the file (in the beg/middle/end)
        // checks to make sure each change is reflected in database
        mf.getActiveSegs().add(0, removed);
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        mf.getActiveSegs().add(2, removed);
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        mf.getActiveSegs().add(mf.getActiveSegs().size(), removed);
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // checks that number of files in DB are still the same
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());
        
        // (5) 
        // subtract down to an empty file, backup, check
        ArrayList<Segment> segsToRemove = new ArrayList();
        for (Segment s : mf.getActiveSegs()) {
            segsToRemove.add(s);
        }
        for (Segment s : segsToRemove) {
            mf.removeTU(s);
        }
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());
       
        
    }
    
}
