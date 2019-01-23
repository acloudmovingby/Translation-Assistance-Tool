/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import State.State;
import State.StateWithDatabase;

/**
 *
 * @author Chris
 */
public class TestObjectBuilder {
    public static BasicFile getTestFile() {
        BasicFile bf = new BasicFile();
      
            Segment seg1 = bf.newSeg();
            seg1.setThai("th1");
            seg1.setEnglish("en1");

            Segment seg2 = bf.newSeg();
            seg2.setThai("th2");
            seg2.setEnglish("en2");

            Segment seg3 = bf.newSeg();
            seg3.setThai("th3");
            seg3.setEnglish("en3");

            Segment seg4 = bf.newSeg();
            seg4.setThai("th4");
            seg4.setEnglish("en4");

            Segment seg5 = bf.newSeg();
            seg5.setThai("th5");
            seg5.setEnglish("en5");
            
            bf.setFileName("testFile");
            
            return bf;
    }
    
    public static Corpus getTestCorpus() {
        Corpus c = new Corpus();
        c.addFile(getTestFile());
        c.addFile(getTestFile());
        c.addFile(getTestFile());
        return c;
    }
    
    public static State getTestState() {
        return new StateWithDatabase(getTestFile(), getTestCorpus());
    }
    
    /**
     * Returns a state where the main file has no segments and the corpus only contains that main file. 
     * @return 
     */
    public static State getEmptyState() {
        BasicFile bf = new BasicFile();
        Corpus c = new Corpus();
        c.addFile(bf);
        return new StateWithDatabase(bf, c);
    }

    public static Segment getTestSeg() {
         return (new SegmentBuilder()).createSegment();
    }
}
