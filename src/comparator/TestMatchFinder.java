/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.PostingsList;
import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.MatchSegment;
import DataStructures.MatchList;
import DataStructures.Segment;
import State.StateWithDatabase;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author Chris
 */
public class TestMatchFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicFile mainFile = new BasicFile();
        Segment testSeg = mainFile.newSeg();
        testSeg.setThai("test1");

        Corpus corpus = new Corpus();
        BasicFile corpusFile = new BasicFile();
        Segment seg1 = corpusFile.newSeg();
        seg1.setThai("test1");
        seg1.setCommitted(true);
        Segment seg2 = corpusFile.newSeg();
        seg2.setThai("test2");
        seg2.setCommitted(true);
        Segment seg3 = corpusFile.newSeg();
        seg3.setThai("test3");
        seg3.setCommitted(true);
        seg3.setRemoved(true);
        corpus.addFile(corpusFile);
        
        StateWithDatabase state = new StateWithDatabase(mainFile, corpus);

        System.out.println("filelist: " + corpus.getAllCommittedTUs());
        PostingsList pl = new PostingsList(3);
        pl.addFileList(corpus);
        pl.getMap().entrySet().forEach((e) -> {
            System.out.println("\t" + e);
        });


        MatchList mFile = MatchFinder.basicMatch(testSeg, 4, state);
        MatchSegment me = (mFile.getObservableList()).get(0);
        System.out.println("mFile obsList: " + mFile.getObservableList());

        System.out.println("*****");

        System.out.println("*****");

        MatchSegment ms1 = new MatchSegment();
        ms1.setThai("ms1");
        MatchSegment ms2 = new MatchSegment();
        ms2.setThai("filter");
        MatchSegment ms3 = new MatchSegment();
        ms3.setThai("ms3");
        MatchSegment ms4 = new MatchSegment();
        ms4.setThai("ms4");

        MatchList matchF = new MatchList();
        matchF.addEntry(ms1);
        matchF.addEntry(ms2);
        matchF.addEntry(ms3);
        matchF.addEntry(ms4);

        System.out.println(
                matchF.getObservableList()
        );
        
        System.out.println(
                matchF.getObservableList().stream()
                .filter(ms -> (!ms.getThai().equals("filter")))
                .collect(Collectors.toList())
        );

    }

}
