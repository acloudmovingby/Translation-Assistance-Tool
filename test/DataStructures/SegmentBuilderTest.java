/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

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
public class SegmentBuilderTest {

    public SegmentBuilderTest() {
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
     * Test of constructor that takes a file.
     */
    @Test
    public void testConstructorFromFile() {
        System.out.println("ConstructorFromFile");
        // see TestObjectBuilder class for details
        TranslationFile bf = TestObjectBuilder.getTestFile();
        int fileID = bf.getFileID();
        SegmentBuilder sb = new SegmentBuilder(bf);
        sb.setThai("hope this works");
        sb.setEnglish("it really should");

        Segment result = sb.createSegment();
        assertEquals(fileID, result.getFileID());
        assertEquals("TestFile", result.getFileName());

        assertEquals("it really should", result.getEnglish());
        assertEquals("hope this works", result.getThai());
    }

    /**
     * Test of constructor that takes a segment.
     */
    @Test
    public void testConstructorFromSegment() {
        System.out.println("ConstructorFromSegment");
        // int id, int fileID, String fileName, String thai, String english, boolean isCommitted, boolean isRemoved, int rank)
        Segment s = new Segment(202, 23002, "TheFileName", "TheThai", "TheEnglish", false);
        SegmentBuilder sb = new SegmentBuilder(s);

        Segment result = sb.createSegment();
        assertEquals(result.getID(), 202);
        assertEquals(result.getFileID(), 23002);
        assertEquals(result.getFileName(), "TheFileName");
        assertEquals(result.getThai(), "TheThai");
        assertEquals(result.getEnglish(), "TheEnglish");
        assertEquals(result.isCommitted(), false);
        assertEquals(s, result);
    }

    /**
     * Test of createSegment method, of class SegmentBuilder. Makes a segment
     * from scratch (using all the setter methods) and makes sure the segment
     * created is correct.
     */
    @Test
    public void testCreateSegment() {
        System.out.println("createSegment");
        SegmentBuilder sb = new SegmentBuilder();
        sb.setID(246);
        sb.setFileID(666);
        sb.setFileName("DevilFile");
        sb.setThai("This is Thai");
        sb.setEnglish("This is English");
        sb.setCommitted(true);

        Segment result = sb.createSegment();
        assertEquals(result.getID(), 246);
        assertEquals(result.getFileID(), 666);
        assertEquals(result.getFileName(), "DevilFile");
        assertEquals(result.getThai(), "This is Thai");
        assertEquals(result.getEnglish(), "This is English");
        assertEquals(result.isCommitted(), true);

    }

    /**
     * Test of createSegmentNewID method, of class SegmentBuilder.
     */
    @Test
    public void testCreateSegmentNewID() {
        SegmentBuilder sb = new SegmentBuilder();
        sb.setID(246);
        sb.setFileID(666);
        sb.setFileName("DevilFile");
        sb.setThai("This is Thai");
        sb.setEnglish("This is English");
        sb.setCommitted(true);

        Segment result = sb.createSegment();
        assertEquals(result.getID(), 246);
        assertEquals(result.getFileID(), 666);
        assertEquals(result.getFileName(), "DevilFile");
        assertEquals(result.getThai(), "This is Thai");
        assertEquals(result.getEnglish(), "This is English");
        assertEquals(result.isCommitted(), true);

        // creates an identical segment except with a different id
        result = sb.createSegmentNewID();
        assertEquals(true, result.getID() != 246); // this is the one thing that should be different
        assertEquals(result.getFileID(), 666);
        assertEquals(result.getFileName(), "DevilFile");
        assertEquals(result.getThai(), "This is Thai");
        assertEquals(result.getEnglish(), "This is English");
        assertEquals(result.isCommitted(), true);
    }

}
