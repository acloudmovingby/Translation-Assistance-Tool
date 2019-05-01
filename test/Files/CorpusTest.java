/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
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
public class CorpusTest {

    public CorpusTest() {
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
     * Test of getAllCommittedSegs method, of class Corpus.
     */
    @Test
    public void testGetAllCommittedSegs() {
        System.out.println("getAllCommittedSegs");

        // basic file and segment builder
        BasicFile bf1 = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(bf1);

        // first segment
        sb.setThai("bf1.1.th");
        sb.setCommitted(true);
        Segment tu11 = sb.createSegment();
        bf1.addSeg(tu11);

        // second segment
        sb.setThai("bf1.2.th");
        sb.setCommitted(false);
        Segment tu12 = sb.createSegmentNewID();
        bf1.addSeg(tu12);

        // second file
        BasicFile bf2 = new BasicFile();

        // first segment
        sb = new SegmentBuilder(bf2);
        sb.setThai("bf2.1.th");
        sb.setCommitted(false);
        Segment tu21 = sb.createSegmentNewID();
        bf2.addSeg(tu21);

        // second segment
        sb.setThai("bf2.2.th");
        sb.setCommitted(true);
        Segment tu22 = sb.createSegmentNewID();
        bf2.addSeg(tu22);

        Corpus c = new Corpus();
        c.addFile(bf1);
        c.addFile(bf2);

        System.out.println("c = " + c);
        ArrayList<Segment> result = c.getAllCommittedSegs();
        System.out.println("result = " + result);
        assertEquals(2, result.size());
        ArrayList<Segment> expResult = new ArrayList();
        expResult.add(tu11);
        expResult.add(tu22);
        assertEquals(expResult, result);
    }

}
