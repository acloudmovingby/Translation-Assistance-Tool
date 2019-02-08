/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.BasicFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import Database.DatabaseOperations;
import State.State;
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
public class MergeTest {
    
    public MergeTest() {
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
     * Test of execute method, of class Merge.
     */
    @Test
    public void testExecute() {
        for (int i = 0; i < 10; i++) {
            BasicFile bf = new BasicFile();

            // makes 5 segments
            SegmentBuilder sb = new SegmentBuilder(bf);
            sb.setThai("t1");
            sb.setEnglish("e1");
            Segment seg1 = sb.createSegment();
            bf.addSeg(seg1);

            sb.setThai("t2");
            sb.setEnglish("e2");
            Segment seg2 = sb.createSegmentNewID();
            bf.addSeg(seg2);

            sb.setThai("t3");
            sb.setEnglish("e3");
            Segment seg3 = sb.createSegmentNewID();
            bf.addSeg(seg3);

            sb.setThai("t4");
            sb.setEnglish("e4");
            Segment seg4 = sb.createSegmentNewID();
            bf.addSeg(seg4);

            sb.setThai("t5");
            sb.setEnglish("e5");
            Segment seg5 = sb.createSegmentNewID();
            bf.addSeg(seg5);

            ArrayList<Segment> selectedSegs = new ArrayList();
            switch (i) {
                case 0: {
                    // if i=0 --> 'merge' first seg (no change)

                    selectedSegs.add(seg1);
                    bf.mergeSegs(selectedSegs);
                    assertEquals(5, bf.getActiveSegs().size());
                    assertEquals(0, bf.getRemovedSegs().size());
                    assertEquals(bf.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(bf.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(bf.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(bf.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(bf.getActiveSegs().get(4).equals(seg5), true);

                    //BasicFile fileFromDB = DatabaseOperations.getFile(bf.getFileID());
                    break;
                }
                case 1: {
                    // if i=1 --> merge first two
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);
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
                    bf.mergeSegs(selectedSegs);

                    //merges seg3 and seg4
                    selectedSegs = new ArrayList();
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    bf.mergeSegs(selectedSegs);

                    // at this point the file should have three segments in activeSegs
                    // the first two segs are the result of the prior merges
                    // the last seg is seg5
                    // now we merge the second merged seg with seg5
                    selectedSegs = new ArrayList();
                    selectedSegs.add(bf.getActiveSegs().get(1));
                    selectedSegs.add(seg5);
                    bf.mergeSegs(selectedSegs);

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
    
}
