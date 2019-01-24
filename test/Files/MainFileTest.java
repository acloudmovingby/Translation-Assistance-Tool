/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import DataStructures.BasicFile;
import DataStructures.MainFile;
import DataStructures.Segment;
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
public class MainFileTest {

    public MainFileTest() {
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
            
            MainFile mf = new MainFile(new BasicFile());

            // makes 5 segments
            Segment seg1 = mf.newSeg();
            seg1.setID(DatabaseOperations.makeSegID());
            seg1.setThai("th1");
            seg1.setEnglish("en1");

            Segment seg2 = mf.newSeg();
            seg2.setID(DatabaseOperations.makeSegID());
            seg2.setThai("th2");
            seg2.setEnglish("en2");

            Segment seg3 = mf.newSeg();
            seg3.setID(DatabaseOperations.makeSegID());
            seg3.setThai("th3");
            seg3.setEnglish("en3");

            Segment seg4 = mf.newSeg();
            seg4.setID(DatabaseOperations.makeSegID());
            seg4.setThai("th4");
            seg4.setEnglish("en4");

            Segment seg5 = mf.newSeg();
            seg5.setID(DatabaseOperations.makeSegID());
            seg5.setThai("th5");
            seg5.setEnglish("en5");

            DatabaseOperations.addFile(mf);
            Segment selectedTU;
            int splitIndex;
            
            switch (i) {
                case 0: {
                    // if null input (nothing selected)
                    selectedTU = null;
                    splitIndex = 0;
                    mf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, mf.getActiveSegs().size());
                    assertEquals(0, mf.getRemovedSegs().size());
                    assertEquals(mf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mf.getActiveSegs().get(4).equals(seg5), true);
                    
                    // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(mf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> mf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(mf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> mf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(mf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 1: {
                    // if split index is at 0 (nothing happens)
                    selectedTU = seg1;
                    splitIndex = 0;
                    mf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, mf.getActiveSegs().size());
                    assertEquals(0, mf.getRemovedSegs().size());
                    assertEquals(mf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mf.getActiveSegs().get(4).equals(seg5), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(mf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> mf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(mf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> mf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(mf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 2: {
                    // if split index is at end (nothing happens)
                    selectedTU = seg1;
                    splitIndex = seg1.getThai().length();
                    mf.splitTU(selectedTU, splitIndex);
                    assertEquals(5, mf.getActiveSegs().size());
                    assertEquals(0, mf.getRemovedSegs().size());
                    assertEquals(mf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mf.getActiveSegs().get(4).equals(seg5), true);
                    
                     // checks that database is equivalent to what's in memory
                    BasicFile fileFromDB = DatabaseOperations.getFile(mf.getFileID());
                    assertEquals(fileFromDB.getActiveSegs().stream()
                            .allMatch(a -> mf.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(mf.getActiveSegs().stream()
                            .allMatch(a -> fileFromDB.getActiveSegs().contains(a)), 
                            true);
                    assertEquals(fileFromDB.getRemovedSegs().stream()
                            .allMatch(a -> mf.getRemovedSegs().contains(a)), 
                            true);
                    assertEquals(mf.getRemovedSegs().stream()
                            .allMatch(a -> fileFromDB.getRemovedSegs().contains(a)), 
                            true);
                    break;
                }
                case 3: {
                    // if split index is in middle
                    selectedTU = seg1;
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    assertEquals(6, mf.getActiveSegs().size());
                    assertEquals(1, mf.getRemovedSegs().size());
                    assertEquals(mf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(mf.getActiveSegs().get(1).getThai(), "h1");
                    assertEquals(mf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(mf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(mf.getActiveSegs().get(2).equals(seg2), true);
                    assertEquals(mf.getActiveSegs().get(3).equals(seg3), true);
                    assertEquals(mf.getActiveSegs().get(4).equals(seg4), true);
                    assertEquals(mf.getActiveSegs().get(5).equals(seg5), true);
                    assertEquals(mf.getRemovedSegs().get(0).equals(seg1), true);
                    
                    break;
                }
                case 4: {
                    // two TUs are split
                    selectedTU = seg1;
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    selectedTU = seg5;
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    
                    // asserts lists same length
                    assertEquals(7, mf.getActiveSegs().size());
                    assertEquals(2, mf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(mf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(mf.getActiveSegs().get(1).getThai(), "h1");
                    assertEquals(mf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(mf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(mf.getActiveSegs().get(5).getThai(), "t");
                    assertEquals(mf.getActiveSegs().get(6).getThai(), "h5");
                    assertEquals(mf.getActiveSegs().get(5).getEnglish(), "en5");
                    assertEquals(mf.getActiveSegs().get(6).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(mf.getActiveSegs().get(2).equals(seg2), true);
                    assertEquals(mf.getActiveSegs().get(3).equals(seg3), true);
                    assertEquals(mf.getActiveSegs().get(4).equals(seg4), true);
                    assertEquals(mf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(mf.getRemovedSegs().get(1).equals(seg5), true);
                    
                     
                    break;
                }
                case 5: {
                    // if first child of a split TU is then split
                    selectedTU = seg1;
                    splitIndex = 2;
                    mf.splitTU(selectedTU, splitIndex);
                    selectedTU = mf.getActiveSegs().get(0);
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    
                     // asserts lists same length
                    assertEquals(7, mf.getActiveSegs().size());
                    assertEquals(2, mf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(mf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(mf.getActiveSegs().get(1).getThai(), "h");
                    assertEquals(mf.getActiveSegs().get(2).getThai(), "1");
                    assertEquals(mf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(mf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(mf.getActiveSegs().get(2).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(mf.getActiveSegs().get(3), seg2);
                    assertEquals(mf.getActiveSegs().get(4), seg3);
                    assertEquals(mf.getActiveSegs().get(5), seg4);
                    assertEquals(mf.getActiveSegs().get(6), seg5);
                    assertEquals(mf.getRemovedSegs().get(0), seg1);
                    
                    
                    break;
                }
                case 6: {
                    // if second child of a split TU is then split
                    selectedTU = seg1;
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    selectedTU = mf.getActiveSegs().get(1);
                    splitIndex = 1;
                    mf.splitTU(selectedTU, splitIndex);
                    
                     // asserts lists same length
                    assertEquals(7, mf.getActiveSegs().size());
                    assertEquals(2, mf.getRemovedSegs().size());
                    
                    // asserts Thai/English split properly
                    assertEquals(mf.getActiveSegs().get(0).getThai(), "t");
                    assertEquals(mf.getActiveSegs().get(1).getThai(), "h");
                    assertEquals(mf.getActiveSegs().get(2).getThai(), "1");
                    assertEquals(mf.getActiveSegs().get(0).getEnglish(), "en1");
                    assertEquals(mf.getActiveSegs().get(1).getEnglish(), "");
                    assertEquals(mf.getActiveSegs().get(2).getEnglish(), "");
                    
                    // asserts the original TUs are in the proper lists
                    assertEquals(mf.getActiveSegs().get(3), seg2);
                    assertEquals(mf.getActiveSegs().get(4), seg3);
                    assertEquals(mf.getActiveSegs().get(5), seg4);
                    assertEquals(mf.getActiveSegs().get(6), seg5);
                    assertEquals(mf.getRemovedSegs().get(0), seg1);
                    
                 
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

            // makes 5 segments
            Segment seg1 = bf.newSeg();
            seg1.setID(DatabaseOperations.makeSegID());
            seg1.setThai("t1");
            seg1.setEnglish("e1");

            Segment seg2 = bf.newSeg();
            seg2.setID(DatabaseOperations.makeSegID());
            seg2.setThai("t2");
            seg2.setEnglish("e2");

            Segment seg3 = bf.newSeg();
            seg3.setID(DatabaseOperations.makeSegID());
            seg3.setThai("t3");
            seg3.setEnglish("e3");

            Segment seg4 = bf.newSeg();
            seg4.setID(DatabaseOperations.makeSegID());
            seg4.setThai("t4");
            seg4.setEnglish("e4");

            Segment seg5 = bf.newSeg();
            seg5.setID(DatabaseOperations.makeSegID());
            seg5.setThai("t5");
            seg5.setEnglish("e5");

            DatabaseOperations.addFile(bf);
            ArrayList<Segment> selectedSegs = new ArrayList();
            switch (i) {
                case 0: {
                    // if i=0 --> 'merge' first seg (no change)

                    selectedSegs.add(seg1);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(seg5), true);

                    BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    break;
                }
                case 1: {
                    // if i=1 --> merge first two
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(seg3), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg2), true);
                    break;
                }
                case 2: {
                    // if i=2 --> merge first three
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(3, bf.getActiveSegs().size());
                    assertEquals(3, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(seg3), true);

              

                    break;
                }
                case 3: {
                    // if i=3 --> merge tu2-tu3
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg3), true);

                  
                    break;
                }
                // if i=4 --> merge 1-3
                case 4: {
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(4, bf.getActiveSegs().size());
                    assertEquals(2, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg3), true);

                    
                    
                    break;
                }
                // if i=5 --> merge 3-end
                case 5: {
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(3, bf.getActiveSegs().size());
                    assertEquals(3, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(1).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg5), true);
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(seg3), true);
                    
                    
                    
                    break;
                }
                // if i=6 --> merge only end (no difference)
                case 6: {
                    selectedSegs.add(seg5);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(seg5), true);
                    
                    
                    
                    break;
                }
                // if i=7 --> merge all tus
                case 7: {
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    selectedSegs.add(seg5);
                    bf.mergeTUs(selectedSegs);
                    assertEquals(1, bf.getActiveSegs().size());
                    assertEquals(5, bf.getRemovedSegs().size());
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getRemovedSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getRemovedSegs().get(4).equals(seg5), true);
                    
                    
                    
                    break;
                }
                // if i=8 --> selectedItems is empty (but not null)
                case 8: {
                    bf.mergeTUs(selectedSegs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(seg5), true);
                    
                    
                    break;
                }
                // if i=9 --> merge repeatedly
                case 9: {
                    // merges seg1 and seg2
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    bf.mergeTUs(selectedSegs);

                    //merges seg3 and seg4
                    selectedSegs = new ArrayList();
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    bf.mergeTUs(selectedSegs);

                    // at this point the file should have three segments in activeSegs
                    // the first two segs are the result of the prior merges
                    // the last seg is seg5
                    // now we merge the second merged seg with seg5
                    selectedSegs = new ArrayList();
                    selectedSegs.add(bf.getActiveSegs().get(1));
                    selectedSegs.add(seg5);
                    bf.mergeTUs(selectedSegs);

                    // this should result in the file now only having two active segs
                    assertEquals(2, bf.getActiveSegs().size());
                    assertEquals(6, bf.getRemovedSegs().size());
                    assertEquals(bf.getRemovedSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getRemovedSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getRemovedSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getRemovedSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getRemovedSegs().get(5).equals(seg5), true);
                    
                    
                    
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
        tu1.setID(DatabaseOperations.makeSegID());
        tu1.setThai("t1");
        tu1.setEnglish("e1");

        Segment tu2 = bf.newSeg();
        tu2.setID(DatabaseOperations.makeSegID());
        tu2.setThai("t2");
        tu2.setEnglish("e2");

        Segment tu3 = bf.newSeg();
        tu3.setID(DatabaseOperations.makeSegID());
        tu3.setThai("t3");
        tu3.setEnglish("e3");

        Segment tu4 = bf.newSeg();
        tu4.setID(DatabaseOperations.makeSegID());
        tu4.setThai("t4");
        tu4.setEnglish("e4");

        Segment tu5 = bf.newSeg();
        tu5.setID(DatabaseOperations.makeSegID());
        tu5.setThai("t5");
        tu5.setEnglish("e5");

        ArrayList<Segment> selectedTUs = new ArrayList();
    }
    
    /**
     * Tests the split method, but more thoroughly than the test above. 
     */
    @Test
    public void testSplit2() {
        
        // empty file --> nothing should happen regardless of split args
        BasicFile f = new BasicFile();
        
        // non-empty file, but seg is not in file --> nothing happens
        
        // all possibilities where first seg is split
        
        
    }
}