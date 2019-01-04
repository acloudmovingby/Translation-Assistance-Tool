/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
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
public class BasicFileTest {

    public BasicFileTest() {
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
     * Test of mergeTUs method, of class BasicFile.
     */
    @Test
    public void testSplitTUs() {
        DatabaseOperations.rebootDB();
        for (int i=0; i<8; i++) {
            
            BasicFile bf = new BasicFile();

            // makes two copies of 5 TUs
            Segment tu1 = bf.newSeg();
            tu1.setID(DatabaseOperations.makeTUID());
            tu1.setThai("th1");
            tu1.setEnglish("en1");

            Segment tu2 = bf.newSeg();
            tu2.setID(DatabaseOperations.makeTUID());
            tu2.setThai("th2");
            tu2.setEnglish("en2");

            Segment tu3 = bf.newSeg();
            tu3.setID(DatabaseOperations.makeTUID());
            tu3.setThai("th3");
            tu3.setEnglish("en3");

            Segment tu4 = bf.newSeg();
            tu4.setID(DatabaseOperations.makeTUID());
            tu4.setThai("th4");
            tu4.setEnglish("en4");

            Segment tu5 = bf.newSeg();
            tu5.setID(DatabaseOperations.makeTUID());
            tu5.setThai("th5");
            tu5.setEnglish("en5");

            DatabaseOperations.addFile(bf);
            Segment selectedTU;
            int splitIndex;
            
            switch (i) {
                case 0: {
                    // if null input (nothing selected)
                    selectedTU = null;
                    splitIndex = 0;
                    bf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);
                    
                    // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 1: {
                    // if split index is at 0 (nothing happens)
                    selectedTU = tu1;
                    splitIndex = 0;
                    bf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 2: {
                    // if split index is at end (nothing happens)
                    selectedTU = tu1;
                    splitIndex = tu1.getThai().length();
                    bf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 3: {
                    // if split index is in middle
                    selectedTU = tu1;
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    assertEquals(6, bf.getActiveSegs().size());
                    assertEquals(1, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(bf.getActiveSegs().get(1).getThai(), "h1");
                    assertEquals(bf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(bf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(bf.getActiveSegs().get(2).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(5).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 4: {
                    // two TUs are split
                    selectedTU = tu1;
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    selectedTU = tu5;
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    
                    // asserts lists same length
                    assertEquals(7, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(bf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(bf.getActiveSegs().get(1).getThai(), "h1");
                    assertEquals(bf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(bf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(bf.getActiveSegs().get(5).getThai(), "t");
                    assertEquals(bf.getActiveSegs().get(6).getThai(), "h5");
                    assertEquals(bf.getActiveSegs().get(5).getEnglish(), "en5");
                    assertEquals(bf.getActiveSegs().get(6).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(bf.getActiveSegs().get(2).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu4), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu5), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 5: {
                    // if first child of a split TU is then split
                    selectedTU = tu1;
                    splitIndex = 2;
                    bf.splitTU(selectedTU, splitIndex);
                    selectedTU = bf.getActiveSegs().get(0);
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    
                     // asserts lists same length
                    assertEquals(7, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(bf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(bf.getActiveSegs().get(1).getThai(), "h");
                    assertEquals(bf.getActiveSegs().get(2).getThai(), "1");
                    assertEquals(bf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(bf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(bf.getActiveSegs().get(2).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(bf.getActiveSegs().get(3), tu2);
                    assertEquals(bf.getActiveSegs().get(4), tu3);
                    assertEquals(bf.getActiveSegs().get(5), tu4);
                    assertEquals(bf.getActiveSegs().get(6), tu5);
                    assertEquals(bf.getRemovedSegs().get(0), tu1);
                    
                    // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 6: {
                    // if second child of a split TU is then split
                    selectedTU = tu1;
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    selectedTU = bf.getActiveSegs().get(1);
                    splitIndex = 1;
                    bf.splitTU(selectedTU, splitIndex);
                    
                     // asserts lists same length
                    assertEquals(7, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(bf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(bf.getActiveSegs().get(1).getThai(), "h");
                    assertEquals(bf.getActiveSegs().get(2).getThai(), "1");
                    assertEquals(bf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(bf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(bf.getActiveSegs().get(2).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(bf.getActiveSegs().get(3), tu2);
                    assertEquals(bf.getActiveSegs().get(4), tu3);
                    assertEquals(bf.getActiveSegs().get(5), tu4);
                    assertEquals(bf.getActiveSegs().get(6), tu5);
                    assertEquals(bf.getRemovedSegs().get(0), tu1);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> bf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> bf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 7: {
                    // three split operations (each on second child)
                    break;
                }
                default:
                    break;
            }
                    
            
        }
    }

     
    /**
     * Test of mergeTUs method, of class BasicFile.
     */
    @Test
    public void testMergeTUs() {
        DatabaseOperations.rebootDB();
        for (int i = 0; i < 10; i++) {
            BasicFile bf = new BasicFile();

            // makes two copies of 5 TUs
            Segment tu1 = bf.newSeg();
            tu1.setID(DatabaseOperations.makeTUID());
            tu1.setThai("t1");
            tu1.setEnglish("e1");

            Segment tu2 = bf.newSeg();
            tu2.setID(DatabaseOperations.makeTUID());
            tu2.setThai("t2");
            tu2.setEnglish("e2");

            Segment tu3 = bf.newSeg();
            tu3.setID(DatabaseOperations.makeTUID());
            tu3.setThai("t3");
            tu3.setEnglish("e3");

            Segment tu4 = bf.newSeg();
            tu4.setID(DatabaseOperations.makeTUID());
            tu4.setThai("t4");
            tu4.setEnglish("e4");

            Segment tu5 = bf.newSeg();
            tu5.setID(DatabaseOperations.makeTUID());
            tu5.setThai("t5");
            tu5.setEnglish("e5");

            DatabaseOperations.addFile(bf);
            ArrayList<Segment> selectedTUs = new ArrayList();
            switch (i) {
                case 0: {
                    // if i=0 --> 'merge' first tu (no change)

                    selectedTUs.add(tu1);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);

                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    // is tusToDisplay from database same as in memory?
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(bf.getActiveSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                    // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);

                    break;
                }
                case 1: {
                    // if i=1 --> merge first two
                    selectedTUs.add(tu1);
                    selectedTUs.add(tu2);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu2), true);
                    
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();
                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    break;
                }
                case 2: {
                    // if i=2 --> merge first three
                    selectedTUs.add(tu1);
                    selectedTUs.add(tu2);
                    selectedTUs.add(tu3);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(3, bf.getActiveSegs().size());
                    assertEquals(3, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(tu3), true);

                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);

                    break;
                }
                case 3: {
                    // if i=3 --> merge tu2-tu3
                    selectedTUs.add(tu2);
                    selectedTUs.add(tu3);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu3), true);

                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    break;
                }
                // if i=4 --> merge 1-3
                case 4: {
                    selectedTUs.add(tu2);
                    selectedTUs.add(tu3);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu3), true);

                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                // if i=5 --> merge 3-end
                case 5: {
                    selectedTUs.add(tu1);
                    selectedTUs.add(tu2);
                    selectedTUs.add(tu3);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(3, bf.getActiveSegs().size());
                    assertEquals(3, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(tu3), true);
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                // if i=6 --> merge only end (no difference)
                case 6: {
                    selectedTUs.add(tu5);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                // if i=7 --> merge all tus
                case 7: {
                    selectedTUs.add(tu1);
                    selectedTUs.add(tu2);
                    selectedTUs.add(tu3);
                    selectedTUs.add(tu4);
                    selectedTUs.add(tu5);
                    bf.mergeTUs(selectedTUs);
                    assertEquals(1, bf.getActiveSegs().size());
                    assertEquals(5, bf.getRemovedSegs().size());
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getRemovedSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getRemovedSegs().get(4).equals(tu5), true);
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                // if i=8 --> selectedItems is empty (but not null)
                case 8: {
                    bf.mergeTUs(selectedTUs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(tu5), true);
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                // if i=9 --> merge repeatedly
                case 9: {
                    selectedTUs.add(tu1);
                    selectedTUs.add(tu2);
                    bf.mergeTUs(selectedTUs);

                    selectedTUs = new ArrayList();
                    selectedTUs.add(tu3);
                    selectedTUs.add(tu4);
                    bf.mergeTUs(selectedTUs);

                    selectedTUs = new ArrayList();
                    selectedTUs.add(bf.getActiveSegs().get(1));
                    selectedTUs.add(tu5);
                    bf.mergeTUs(selectedTUs);

                    assertEquals(2, bf.getActiveSegs().size());
                    assertEquals(6, bf.getRemovedSegs().size());
                    assertEquals(bf.getRemovedSegs().get(0).equals(tu1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(tu2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(tu3), true);
                    assertEquals(bf.getRemovedSegs().get(3).equals(tu4), true);
                    assertEquals(bf.getRemovedSegs().get(5).equals(tu5), true);
                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    Stream<Segment> stream1 = fileFromDB.getActiveSegs().stream();
                    Stream<Segment> stream2 = bf.getActiveSegs().stream();

                    // is tusToDisplay from database same as in memory?
                    assertEquals(
                            stream1.allMatch(
                                    a -> bf.getActiveSegs().contains(a)),
                            true);
                    // is tusToDisplay from memory same as in database?
                    assertEquals(
                            stream2.allMatch(
                                    a -> fileFromDB.getActiveSegs().contains(a)),
                            true);
                     // is removed from databse same as in memory?
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(
                                    a -> bf.getRemovedSegs().contains(a)),
                            true);
                    // is removed from memory same as in database?
                    assertEquals(bf.getRemovedSegs().stream()
                            .allMatch(
                                    a -> fileFromDB.getRemovedSegs().contains(a)),
                            true);
                    
                    break;
                }
                default:
                    break;
            }

        }
    }

    /**
     * Test of changeThai method, of class BasicFile.
     */
    @Test
    public void testChangeThai() {
        BasicFile bf = new BasicFile();

        // makes two copies of 5 TUs
        Segment tu1 = bf.newSeg();
        tu1.setID(DatabaseOperations.makeTUID());
        tu1.setThai("t1");
        tu1.setEnglish("e1");

        Segment tu2 = bf.newSeg();
        tu2.setID(DatabaseOperations.makeTUID());
        tu2.setThai("t2");
        tu2.setEnglish("e2");

        Segment tu3 = bf.newSeg();
        tu3.setID(DatabaseOperations.makeTUID());
        tu3.setThai("t3");
        tu3.setEnglish("e3");

        Segment tu4 = bf.newSeg();
        tu4.setID(DatabaseOperations.makeTUID());
        tu4.setThai("t4");
        tu4.setEnglish("e4");

        Segment tu5 = bf.newSeg();
        tu5.setID(DatabaseOperations.makeTUID());
        tu5.setThai("t5");
        tu5.setEnglish("e5");

        ArrayList<Segment> selectedTUs = new ArrayList();
    }

}
