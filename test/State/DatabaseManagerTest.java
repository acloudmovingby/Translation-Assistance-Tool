/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
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
        BasicFile emptyMF = emptyState.getMainFile();
        DatabaseManager dmEmpty = new DatabaseManager(emptyState);
        // checks to see that the file is in the db already
        assertEquals(emptyMF, DatabaseOperations.getFile(emptyMF.getFileID()));
        // checks to see that the file is the same even after backing up the unchanged file
        dmEmpty.push(emptyState);
        assertEquals(emptyMF, DatabaseOperations.getFile(emptyMF.getFileID()));

        State state = TestObjectBuilder.getTestState();
        DatabaseManager dm = new DatabaseManager(state);
        BasicFile mf = state.getMainFile();
        int fileID = mf.getFileID();

        // (1) 
        // check to see that the new file is in the database 
        // this should happen because DatabaseManager should auto-add the file to the db on construction
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));

        // (2)
        // check to see that the file is the same after just adding the file without any changes made
        int numFiles = DatabaseOperations.getAllFileIDs().size();
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // checks that number of files in DB are still the same
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());

        // (3) 
        // make a subtraction to the file (beg / middle / end?)
        // check to make sure changes are reflected in db
        Segment removed = mf.getActiveSegs().get(0);
        mf.hideSeg(removed);
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        // checks that number of files in DB are still the same
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());

        // (4)
        // make several additions to the file (in the beg/middle/end)
        // checks to make sure each change is reflected in database
        SegmentBuilder sb = new SegmentBuilder(mf);
        mf.getActiveSegs().add(0, sb.createSegment());
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        mf.getActiveSegs().add(2, sb.createSegmentNewID());
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        mf.getActiveSegs().add(mf.getActiveSegs().size(), sb.createSegmentNewID());
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
            mf.hideSeg(s);
        }
        dm.push(state);
        assertEquals(mf, DatabaseOperations.getFile(mf.getFileID()));
        assertEquals(numFiles, DatabaseOperations.getAllFileIDs().size());

    }

    /**
     * If the segs in the main file are removed from both active/removed lists,
     * the db should then remove those rows from the db.
     */
    @Test
    public void testDeleteSegsBeforeBackup() {
        // have a file, make sure it's backed up
        State state = TestObjectBuilder.getTestState();
        BasicFile mainFile = state.getMainFile();
        DatabaseManager dm = new DatabaseManager(state);

        SegmentBuilder sb = new SegmentBuilder(mainFile);
        mainFile.getHiddenSegs().add(sb.createSegment());
        dm.push(state);
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        // delete all active segs in mf
        mainFile.getActiveSegs().clear();
        dm.push(state);
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));

        // delete all hidden segs in mf
        mainFile.getHiddenSegs().clear();
        dm.push(state);
        assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
    }

}
