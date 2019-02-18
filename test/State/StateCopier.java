/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import static org.junit.Assert.assertEquals;

/**
 * Records critical properties of a State object which can then be compared later against a given State (the same State object or a different one).
 * Currently checks the size of all postings lists, the total number of segs in the corpus, and the equality of the main file.
 * @author Chris
 */
public class StateCopier {
    
    int postingsListSize;
    int numSegsInCorpus;
    MainFile mainFileCopy;
    
    public StateCopier(State s) {
        postingsListSize = s.getPostingsListManager().size();
        numSegsInCorpus = s.getCorpus().numTotalSegs();
        mainFileCopy = new MainFile(s.getMainFile());
    }
    
    /**
     * Checks to see if the given state has the same properties as the state that was given to StateCopier at construction.
     * @param s
     * @return 
     */
    public boolean compare(State s) {
        
        /*
        the segment was committed and then not removed from plm
        */
        
        assertEquals(numSegsInCorpus, s.getCorpus().numTotalSegs());
        assertEquals(mainFileCopy, s.getMainFile());
        assertEquals(postingsListSize, s.getPostingsListManager().size());
        
        return postingsListSize==s.getPostingsListManager().size() 
                && numSegsInCorpus == s.getCorpus().numTotalSegs()
                && mainFileCopy.equals(s.getMainFile());
    }
}
