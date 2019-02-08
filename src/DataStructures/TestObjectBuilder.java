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
     * Makes a corpus composed of 3 files (like getTestCorpus), but each file is an "identical" file where all 5 segments are identical. See gettIdenticalFile(). NO SEGMENTS ARE COMMITTED.
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
     * all exactly equal to the file that comes out of getTestFile(); These
     * files, if you run .equals comparing them, will return true. NO SEGMENTS
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
     * Same as getTestCorpus(), except all segs in corpus are committed.
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
     * State composed of the corpus from getTestCorpus() and a main file from
     * getTestFile().
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

}
