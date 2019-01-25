/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import State.State;
import State.StateWithDatabase;

/**
 * Creates various objects to make testing easier. 
 * @author Chris
 */
public class TestObjectBuilder {
    public static BasicFile getTestFile() {
        BasicFile bf = new BasicFile();
        
        bf.setFileName("testFile");
      
            SegmentBuilder sb = new SegmentBuilder(bf);
            sb.setThai("th1");
            sb.setEnglish("en1");
            bf.addSeg(sb.createSegment());

            sb.setThai("th2");
            sb.setEnglish("en2");
            bf.addSeg(sb.createSegmentNewID());

            sb.setThai("th3");
            sb.setEnglish("en3");
            bf.addSeg(sb.createSegmentNewID());

            sb.setThai("th4");
            sb.setEnglish("en4");
            bf.addSeg(sb.createSegmentNewID());

            sb.setThai("th5");
            sb.setEnglish("en5");
            bf.addSeg(sb.createSegmentNewID());
            
            
            
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
