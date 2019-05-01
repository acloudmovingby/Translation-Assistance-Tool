/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import Database.DatabaseOperations;
import State.Dispatcher;
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
            // create test objects
            Corpus c = TestObjectBuilder.getCommittedTestCorpus();
            BasicFile mainFile = c.getFiles().get(0);
            Dispatcher d = TestObjectBuilder.getDispatcher(c, mainFile);
            mainFile = d.getState().getMainFile();

            // makes 5 segments
            Segment seg1 = mainFile.getActiveSegs().get(0);
            Segment seg2 = mainFile.getActiveSegs().get(1);
            Segment seg3 = mainFile.getActiveSegs().get(2);
            Segment seg4 = mainFile.getActiveSegs().get(3);
            Segment seg5 = mainFile.getActiveSegs().get(4);

            ArrayList<Segment> selectedSegs = new ArrayList();
            switch (i) {
                case 0: {
                    // if i=0 --> 'merge' first seg (no change)

                    selectedSegs.add(seg1);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(5, mainFile.getActiveSegs().size());
                    assertEquals(0, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(4).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                case 1: {
                    // if i=1 --> merge first two
                    StringBuilder sb = new StringBuilder(seg1.getThai());
                    sb.append(seg2.getThai()); // combine the Thai from both segs

                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);

                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(4, mainFile.getActiveSegs().size());
                    assertEquals(2, mainFile.getHiddenSegs().size());
                    assertEquals(sb.toString(), d.getUIState().getMainFileSegs().get(0).getThai());
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg3), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg2), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                case 2: {
                    // if i=2 --> merge first three
                    StringBuilder sb = new StringBuilder(seg1.getThai());
                    sb.append(seg2.getThai());
                    sb.append(seg3.getThai());// combine the Thai from the three segs

                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(3, mainFile.getActiveSegs().size());
                    assertEquals(3, mainFile.getHiddenSegs().size());
                    assertEquals(sb.toString(), d.getUIState().getMainFileSegs().get(0).getThai());
                    assertEquals(seg4.getThai(), d.getUIState().getMainFileSegs().get(1).getThai());
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg5), true);
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(2).equals(seg3), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                case 3: {
                    // if i=3 --> merge tu2-tu3
                    StringBuilder sb = new StringBuilder(seg2.getThai());
                    sb.append(seg3.getThai());

                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(4, mainFile.getActiveSegs().size());
                    assertEquals(2, mainFile.getHiddenSegs().size());
                    assertEquals(sb.toString(), d.getUIState().getMainFileSegs().get(1).getThai());
                    assertEquals(seg1.getThai(), d.getUIState().getMainFileSegs().get(0).getThai());
                    assertEquals(seg4.getThai(), d.getUIState().getMainFileSegs().get(2).getThai());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg3), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=4 --> merge 1-3
                case 4: {

                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(4, mainFile.getActiveSegs().size());
                    assertEquals(2, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg5), true);
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg3), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=5 --> merge 3-end
                case 5: {
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(3, d.getState().getMainFile().getActiveSegs().size());
                    assertEquals(3, d.getState().getMainFile().getHiddenSegs().size());
                    assertEquals("th1th2th3", mainFile.getActiveSegs().get(0).getThai());
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg5), true);
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(2).equals(seg3), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=6 --> merge only end (no difference)
                case 6: {
                    selectedSegs.add(seg5);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(5, mainFile.getActiveSegs().size());
                    assertEquals(0, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(4).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=7 --> merge all segs
                case 7: {
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    selectedSegs.add(seg5);
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(1, mainFile.getActiveSegs().size());
                    assertEquals(5, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getHiddenSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getHiddenSegs().get(4).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=8 --> selectedItems is empty (but not null)
                case 8: {
                    d.acceptAction(new Merge(selectedSegs));
                    assertEquals(5, mainFile.getActiveSegs().size());
                    assertEquals(0, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(4).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=9 --> merge repeatedly
                case 9: {
                    // merges seg1 and seg2
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    d.acceptAction(new Merge(selectedSegs));

                    //merges seg3 and seg4
                    selectedSegs = new ArrayList();
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    d.acceptAction(new Merge(selectedSegs));

                    // at this point the file should have three segments in activeSegs
                    // the first two segs are the result of the prior merges
                    // the last seg is seg5
                    // now we merge the second merged seg with seg5
                    selectedSegs = new ArrayList();
                    selectedSegs.add(mainFile.getActiveSegs().get(1));
                    selectedSegs.add(seg5);
                    d.acceptAction(new Merge(selectedSegs));

                    // this should result in the file now only having two active segs
                    assertEquals(2, mainFile.getActiveSegs().size());
                    assertEquals(6, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getHiddenSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getHiddenSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getHiddenSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getHiddenSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getHiddenSegs().get(5).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                // if i=10 --> merge invalid argument (segs not contiguous)
                case 10: {
                    selectedSegs.add(seg1);
                    selectedSegs.add(seg2);
                    selectedSegs.add(seg3);
                    selectedSegs.add(seg4);
                    selectedSegs.add(seg2); // this seg is repeated and out of order
                    d.acceptAction(new Merge(selectedSegs));

                    assertEquals(5, mainFile.getActiveSegs().size());
                    assertEquals(0, mainFile.getHiddenSegs().size());
                    assertEquals(mainFile.getActiveSegs().get(0).equals(seg1), true);
                    assertEquals(mainFile.getActiveSegs().get(1).equals(seg2), true);
                    assertEquals(mainFile.getActiveSegs().get(2).equals(seg3), true);
                    assertEquals(mainFile.getActiveSegs().get(3).equals(seg4), true);
                    assertEquals(mainFile.getActiveSegs().get(4).equals(seg5), true);

                    assertEquals(mainFile, DatabaseOperations.getFile(mainFile.getFileID()));
                    break;
                }
                default:
                    break;
            }

        }
    }

}
