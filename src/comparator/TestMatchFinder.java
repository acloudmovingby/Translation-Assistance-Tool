/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Database.PostingsList;
import Files.BasicFile;
import Files.FileList;
import Files.MatchSegment;
import Files.MatchFile;
import Files.Segment;
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
        BasicFile bfTest = new BasicFile();
        Segment testSeg = bfTest.newSeg();
        testSeg.setThai("test");

        FileList fl = new FileList();
        BasicFile bf1 = new BasicFile();
        Segment seg1 = bf1.newSeg();
        seg1.setThai("test1");
        seg1.setCommitted(true);
        Segment seg2 = bf1.newSeg();
        seg2.setThai("test2");
        seg2.setCommitted(true);
        Segment seg3 = bf1.newSeg();
        seg3.setThai("test3");
        seg3.setCommitted(true);
        seg3.setRemoved(true);
        fl.addFile(bf1);

        System.out.println("filelist: " + fl.getAllCommittedTUs());
        PostingsList pl = new PostingsList(3);
        pl.addFileList(fl);
        for (Entry e : pl.getMap().entrySet()) {
            System.out.println("\t" + e);
        };

        System.out.println("Matches with 'test'" + pl.getMatchingID("test"));

        MatchFile mFile = MatchFinder.basicMatch2(testSeg, 3, fl);
        MatchSegment me = (mFile.getObservableList()).get(0);
        System.out.println("size = " + mFile.getObservableList().size());
        System.out.println("me1" + me.getThai());

        System.out.println("*****");

        System.out.println(MatchFinder.findStringMatches("aaaa", "zzzzz", 1));
        System.out.println(MatchFinder.findStringMatches("aaaaz", "zzzzz", 1));
        System.out.println(MatchFinder.findStringMatches("aazaaz", "zzzzz", 1));
        System.out.println(MatchFinder.findStringMatches("aaaaz", "zzzzz", 2));
        System.out.println(MatchFinder.findStringMatches("zzaaa", "zzzzz", 2));

        System.out.println("*****");

        MatchSegment ms1 = new MatchSegment();
        ms1.setThai("ms1");
        MatchSegment ms2 = new MatchSegment();
        ms2.setThai("filter");
        MatchSegment ms3 = new MatchSegment();
        ms3.setThai("ms3");
        MatchSegment ms4 = new MatchSegment();
        ms4.setThai("ms4");

        MatchFile matchF = new MatchFile();
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
