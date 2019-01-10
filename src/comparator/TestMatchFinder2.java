/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import State.State;
import State.StateWithDatabase;

/**
 *
 * @author Chris
 */
public class TestMatchFinder2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // makes a corpus with 1 file
        // makes that 1 file have 1 segment called corpusSeg1
        // the Thai text in corpusSeg1 is "test"
        Corpus corpus = new Corpus();
        BasicFile bf1 = new BasicFile();
        Segment corpusSeg1 = bf1.newSeg();
        corpusSeg1.setThai("test");
        corpusSeg1.setCommitted(true);
        corpus.addFile(bf1);
        State state = new StateWithDatabase(bf1, corpus);
        
        // makes a second file with 1 segment: mainFileSeg
        // mainFileSeg has Thai text of "test"
        // this represents a segment that is selected in main file for which the user wants to see matches
        BasicFile bf2 = new BasicFile();
        Segment mainFileSeg = bf2.newSeg();
        mainFileSeg.setThai("test");
        
        // We run mainFileSeg through MatchFinder, 
        // minMatchLength is 3
        // it should return 1 matching segment (i.e. corpusSeg1)
        MatchList mList = MatchFinder.basicMatch(mainFileSeg, 3, state);
        MatchSegment ms = (mList.getObservableList()).get(0);
        System.out.println("test : " + ms.getThai());
      
        // give our corpus file another segment: corpusSeg2
        // corpusSeg2 has Thai text of "aaeestcc"
        // running MatchFinder again should now return both corpusSegs
        Segment corpusSeg2 = bf1.newSeg();
        corpusSeg2.setThai("aaestcc");
        corpusSeg2.setCommitted(true);
        mList = MatchFinder.basicMatch(mainFileSeg, 3, state);
        System.out.println("2 : " +  mList.getObservableList().size());
        System.out.println("test : " + mList.getObservableList().get(0).getThai());
        System.out.println("aaestcc : " + mList.getObservableList().get(1).getThai());
        
    }
    
}
