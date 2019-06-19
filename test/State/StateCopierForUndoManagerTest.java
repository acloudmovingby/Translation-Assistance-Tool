/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.TranslationFile;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Records critical properties of a State object which can then be compared
 * later against a given State (the same State object or a different one).
 *
 * @author Chris
 */
public class StateCopierForUndoManagerTest {

    int postingsListSize;
    int numSegsInCorpus;
    TranslationFile mainFileCopy;

    public StateCopierForUndoManagerTest(State s) {
        postingsListSize = s.getPostingsListManager().size();
        numSegsInCorpus = getNumsSegsInFileList(s.getCorpusFiles());
        mainFileCopy = new TranslationFile(s.getMainFile());
    }

    /**
     * Checks to see if the given state has the same properties as the state
 that was given to StateCopierForUndoManagerTest at construction.
     *
     * @param s
     * @return
     */
    public boolean compare(State s) {

        assertEquals(numSegsInCorpus, getNumsSegsInFileList(s.getCorpusFiles()));
        assertEquals(mainFileCopy, s.getMainFile());
        assertEquals(postingsListSize, s.getPostingsListManager().size());

        return postingsListSize == s.getPostingsListManager().size()
                && numSegsInCorpus == getNumsSegsInFileList(s.getCorpusFiles())
                && mainFileCopy.equals(s.getMainFile());
    }
    
    private int getNumsSegsInFileList(List<TranslationFile> fileList) {
        int numSegs = 0;
        for (TranslationFile bf : fileList) {
            numSegs = numSegs + bf.getActiveSegs().size();
            numSegs = numSegs + bf.getHiddenSegs().size();
        }
        return numSegs;
    }
}
