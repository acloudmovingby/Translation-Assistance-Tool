/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import Database.DatabaseOperations;
import State.DatabaseManager;
import State.Dispatcher;
import State.State;
import State.UndoManager;

/**
 * Creates various objects to make testing easier.
 *
 * @author Chris
 */
public class TestObjectBuilder {

    /**
     * Creates a file with a random id, and a fileName of "TestFile". It
     * contains 5 segments, with the Thai/English text fields as follows: "th1"
     * / "en1", "th2"/"en2" etc... Other than that, the segments have default
     * values (random id, rank=0, not committed). Note, none of the segments are
     * "committed"
     *
     * @return
     */
    public static BasicFile getTestFile() {
        BasicFile bf = new BasicFile(DatabaseOperations.createFileID("TestFile"), "TestFile");

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

    /**
     * This file has 5 "identical" segments, where each one has Thai saying
     * "This is Thai" and english saying "This is English". The segment ids are
     * still all unique. All segments are NOT committed.
     *
     * @return BasicFile with 5 identical segments
     */
    public static BasicFile getIdenticalFile() {
        BasicFile bf = new BasicFile(DatabaseOperations.createFileID("TestFile"), "TestFile");

        SegmentBuilder sb = new SegmentBuilder(bf);
        sb.setThai("This is Thai");
        sb.setEnglish("This is English");
        bf.addSeg(sb.createSegment());
        bf.addSeg(sb.createSegmentNewID());
        bf.addSeg(sb.createSegmentNewID());
        bf.addSeg(sb.createSegmentNewID());
        bf.addSeg(sb.createSegmentNewID());

        return bf;
    }
    
    /**
     * Returns a file with one segment in it. The segment is not committed and not hidden. 
     * @return 
     */
    public static BasicFile getOneSegFile() {
        BasicFile bf = new BasicFile();
        SegmentBuilder sb = new SegmentBuilder(bf);
        sb.setThai("thai");
        sb.setEnglish("english");
        bf.addSeg(sb.createSegment());
        return bf;
    }
    
    /**
     * Makes a corpus composed of 3 files (like getTestCorpus), but each file is an "identical" file where all 5 segments are identical. See gettIdenticalFile(). No segments are committed.
     * @return 
     */
    public static Corpus getIdenticalCorpus() {
        Corpus c = new Corpus();
        c.addFile(getIdenticalFile());
        c.addFile(getIdenticalFile());
        c.addFile(getIdenticalFile());
        return c;
    }

    /**
     * Makes a corpus composed of 3 files, which while different objects, are
     * all exactly equal to the file that comes out of getTestFile() (though they will have distinct file ids). NO SEGMENTS
     * ARE COMMITTED.
     *
     * @return
     */
    public static Corpus getTestCorpus() {
        Corpus c = new Corpus();
        c.addFile(getTestFile());
        c.addFile(getTestFile());
        c.addFile(getTestFile());
        return c;
    }

    /**
     * Same as getTestCorpus(), except all segments in corpus are COMMITTED.
     *
     * @return
     */
    public static Corpus getCommittedTestCorpus() {
        Corpus c = getTestCorpus();
        for (BasicFile f : c.getFiles()) {
            f.commitAllSegs();
        }
        return c;
    }
    
    /**
     * Same as getTestState(), except ALL SEGS (in corpus and main file) are COMMITTED.
     *
     * @return
     */
    public static State getCommittedTestState() {
        BasicFile mainFile = getTestFile();
        mainFile.commitAllSegs();
        
        return new State(mainFile, getCommittedTestCorpus());
    }

    /**
     * State composed of the corpus from getTestCorpus() and a main file from
     * getTestFile(). No segments are committed.
     *
     * @return
     */
    public static State getTestState() {
        return new State(getTestFile(), getTestCorpus());
    }

    /**
     * Returns a state where the main file has no segments and the corpus only
     * contains that main file.
     *
     * @return
     */
    public static State getEmptyState() {
        BasicFile bf = new BasicFile();
        Corpus c = new Corpus();
        c.addFile(bf);
        return new State(bf, c);
    }
    
    /**
     * Returns a state where the main file has 1 segment and the corpus only
     * contains that main file. That segment is not committed. (note, this file is identical to the file produced by method getOneSegFile())
     *
     * @return a state with a single file with a single segment
     */
    public static State getOneSegState() {
        Corpus c = new Corpus();
        return new State(getOneSegFile(), c);
    }
    

    public static Segment getTestSeg() {
        return (new SegmentBuilder()).createSegment();
    }

    /**
     * Returns a dispatcher wrapped around the state from getTestState();
     *
     * @return
     */
    public static Dispatcher getTestDispatcher() {
        State state = getTestState();
        return new Dispatcher(new DatabaseManager(state), state, new UndoManager());
    }

    public static Dispatcher getDispatcher(Corpus c, BasicFile f) {
        State state = new State(f, c);
        return new Dispatcher(new DatabaseManager(state), state, new UndoManager());
    }

    /**
     * State composed of the corpus from getTestCorpus() and a main file from getTestFile(). The mainFile also has 3 segs added to the hidden segs list. All segs (both in main file and in corpus) are committed.
     * @return 
     */
    public static State getComplexState() {
        BasicFile mainFile = getTestFile();
        SegmentBuilder sb = new SegmentBuilder(mainFile);
        sb.setThai("thHidden1");
        sb.setEnglish("enHidden1");
        mainFile.getHiddenSegs().add(sb.createSegment());
        sb.setThai("thHidden2");
        sb.setEnglish("enHidden2");
        mainFile.getHiddenSegs().add(sb.createSegmentNewID());
        sb.setThai("thHidden3");
        sb.setEnglish("enHidden3");
        mainFile.getHiddenSegs().add(sb.createSegmentNewID());
        
        return new State(mainFile, getTestCorpus());
    }
    
    /**
     * Takes a state and returns it exactly as it is, except with all segs in the corpus committed. THE MAIN FILE IS NOT CHANGED. Because the corpus can contain the main file, this method assures that the committed status of any segs in the main file are not affected. It does this by copying the MainFile before re-adding it using the MainFile constructor MainFile(BasicFile). Note, the segs committed will have the same id, but will not be the same objects.  
     * @param s
     * @return 
     */
    public static State commitAllCorpus(State s) {
        Corpus c = s.getCorpus();
        MainFile mf = s.getMainFile();
        MainFile mfCopy = new MainFile(mf);
        
        c.commitAllFiles();
        
        return new State(mfCopy, c);
        
    }
    
    /**
     * Takes a state and returns it exactly as it is, except with all segs in the mainFile committed. Segs in other files are not affected. The method used is commitAllSegs in mainFile, which, at the time of writing this comment, mutates the seg instead of replacing it.  
     * @param s
     * @return 
     */
    public static State commitAllMainFile(State s) {
        Corpus c = s.getCorpus();
        MainFile mf = s.getMainFile();
        mf.commitAllSegs();
        
        return new State(mf, c);
    }
    /**
    * State composed of the corpus from getTestCorpus() and a main file from getTestFile(). The mainFile also has 3 segs added to the hidden segs list. All segs (both in main file and in corpus) are committed.
     * @return 
    */
    public static State getCommittedComplexState() {
        BasicFile mainFile = getTestFile();
        SegmentBuilder sb = new SegmentBuilder(mainFile);
        sb.setThai("thHidden1");
        sb.setEnglish("enHidden1");
        mainFile.getHiddenSegs().add(sb.createSegment());
        sb.setThai("thHidden2");
        sb.setEnglish("enHidden2");
        mainFile.getHiddenSegs().add(sb.createSegmentNewID());
        sb.setThai("thHidden3");
        sb.setEnglish("enHidden3");
        mainFile.getHiddenSegs().add(sb.createSegmentNewID());
        
        mainFile.commitAllSegs();
        
        return new State(mainFile, getCommittedTestCorpus());
    }
    
}
