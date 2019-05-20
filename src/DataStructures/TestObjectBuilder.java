package DataStructures;

import Database.DatabaseOperations;
import State.DatabaseManager;
import State.Dispatcher;
import State.State;
import State.UndoManager;
import java.util.ArrayList;
import java.util.List;

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
     * Returns a file with one segment in it. The segment is not committed and
     * not hidden.
     *
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
     * Makes a corpus composed of 3 files (like getTestCorpus), but each file is
     * an "identical" file where all 5 segments are identical. See
     * gettIdenticalFile(). No segments are committed.
     *
     * @return
     */
    public static List<BasicFile> getIdenticalCorpus() {
        List<BasicFile> c = new ArrayList();
        c.add(getIdenticalFile());
        c.add(getIdenticalFile());
        c.add(getIdenticalFile());
        return c;
    }

    /**
     * Makes a corpus composed of 3 files, which while different objects, are
     * all exactly equal to the file that comes out of getTestFile() (though
     * they will have distinct file ids). NO SEGMENTS ARE COMMITTED.
     *
     * @return
     */
    public static List<BasicFile> getTestCorpus() {
        List<BasicFile> c = new ArrayList();
        c.add(getTestFile());
        c.add(getTestFile());
        c.add(getTestFile());
        return c;
    }

    /**
     * Same as getTestCorpus(), except all segments in corpus are COMMITTED.
     *
     * @return
     */
    public static List<BasicFile> getCommittedTestCorpus() {
        List<BasicFile> fileList = getTestCorpus();
        fileList.forEach((f) -> {
            f.commitAllSegs();
        });
        return fileList;
    }

    /**
     * Same as getTestState(), except ALL SEGS (in corpus and main file) are
     * COMMITTED.
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
        List<BasicFile> fileList = new ArrayList();
        fileList.add(bf);
        return new State(bf, fileList);
    }

    /**
     * Returns a state where the main file has 1 segment and the corpus only
     * contains that main file. That segment is not committed. (note, this file
     * is identical to the file produced by method getOneSegFile())
     *
     * @return a state with a single file with a single segment
     */
    public static State getOneSegState() {
        return new State(getOneSegFile(), new ArrayList());
    }

    public static Segment getTestSeg() {
        return (new SegmentBuilder()).createSegment();
    }
    
    public static Dispatcher getDispatcher(BasicFile f, List<BasicFile> fileList) {
        State state = new State(f, fileList);
        return new Dispatcher(new DatabaseManager(state), state, new UndoManager());
    }
}
