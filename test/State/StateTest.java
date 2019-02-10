/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.PostingsList;
import DataStructures.Segment;
import DataStructures.TestObjectBuilder;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
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
public class StateTest {

    State state1;
    State state2;
    int numFiles;

    public StateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // state where the "mainFile" is already in corpus
        Corpus c1 = TestObjectBuilder.getCommittedTestCorpus();
        System.out.println(c1);
        numFiles = c1.getFiles().size();
        state1 = new State(c1.getFiles().get(0), c1);
      

        // state where the mainfile was created outside of corpus
        Corpus c2 = TestObjectBuilder.getCommittedTestCorpus();
        BasicFile bf = TestObjectBuilder.getTestFile();
        bf.commitAllSegs();
        state2 = new State(bf, c2);
    }

    @After
    public void tearDown() {
    }


    /**
     * This test makes sure that regardless of whether the mainFile originated in the corpus or came from elsewhere, the mainFile ends up in the corpus and no extra copies are added.
     */
    @Test
    public void testGetCorpus() {
        System.out.println("getCorpus");
        
        System.out.println(state1.getCorpus());
        
        assertEquals(numFiles, state1.getCorpus().getFiles().size());
        
        assertEquals(numFiles+1, state2.getCorpus().getFiles().size());
    }

    
}
