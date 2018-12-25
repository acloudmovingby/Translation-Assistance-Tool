/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

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
        Segment e = expResult.newSeg();
        e.setThai(thai);
        e.setEnglish(english);
        //expResult.addEntry(e);
        
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
        Segment e = expResult.newSeg();
        e.setThai(thai);
        e.setEnglish(english);
        //expResult.addEntry(e);
        
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
        Segment e = expResult.newSeg();
        e.setThai("การเมือง");
        e.setEnglish("politics");
        //expResult.addEntry(e);
            
        e = expResult.newSeg();
        e.setThai("ของประเทศไทย");
        e.setEnglish("of Thailand");
       // expResult.addEntry(e);
           
        
        System.out.println("result is: \n" + result);
        System.out.println("expResult is: \n" + expResult);
        
        assertEquals(expResult, result);
    }
    
}
