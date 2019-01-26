/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import DataStructures.BasicFile;
import DataStructures.FileBuilder;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
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
public class FileFactoryTest {
    
    public FileFactoryTest() {
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
     * Test of buildBasicParse method, of class FileBuilder.
     */
    @Test
    public void testBuildBasicParse1() {
        System.out.println("buildBasicParse1");
        String thai = "";
        String english = "";
        FileBuilder instance = new FileBuilder();
        BasicFile result = instance.buildBasicParse(thai, english);
        
        BasicFile expResult = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(expResult);
        sb.setThai(thai);
        sb.setEnglish(english);
        expResult.addSeg(sb.createSegment());
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of buildBasicParse method using two entries.
     */
    @Test
    public void testBuildBasicParse2() {
        System.out.println("buildBasicParse2");
        String thai = "การเมือง";
        String english = "politics";
        FileBuilder instance = new FileBuilder();
        BasicFile result = instance.buildBasicParse(thai, english);
        
        BasicFile expResult = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(expResult);
        sb.setThai(thai);
        sb.setEnglish(english);
        expResult.addSeg(sb.createSegment());
        
        assertEquals(expResult, result);
    }
    
     /**
     * Test of buildBasicParse method using two entries.
     */
    @Test
    public void testBuildBasicParse3() {
        System.out.println("buildBasicParse3");
        String thai = "การเมือง\nของประเทศไทย";
        String english = "politics\nof Thailand";
        FileBuilder instance = new FileBuilder();
        BasicFile result = instance.buildBasicParse(thai, english);
        
        BasicFile expResult = new BasicFile();
        
        SegmentBuilder sb = new SegmentBuilder(expResult);
        sb.setThai("การเมือง");
        sb.setEnglish("politics");
        expResult.addSeg(sb.createSegment());
        
        sb.setThai("ของประเทศไทย");
        sb.setEnglish("of Thailand");
        expResult.addSeg(sb.createSegmentNewID());
            
           
        
        System.out.println("result is: \n" + result);
        System.out.println("expResult is: \n" + expResult);
        
        assertEquals(expResult, result);
    }
    
}
