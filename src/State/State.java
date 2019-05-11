package State;

import comparator.PostingsListManager;
import comparator.PostingsList;
import DataStructures.BasicFile;
import DataStructures.MatchList;
import DataStructures.Corpus;
import DataStructures.Segment;
import comparator.MatchManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import javafx.collections.ObservableList;

/**
 *
 * @author Chris
 */
public class State {


    /**
     * Handles the searching and caching of matches.
     */
    private final MatchManager matchManager;

    /**
     * The file currently being translated.
     */
    private BasicFile mainFile;

    /**
     * The corpus where matches are found.
     */
    private final Corpus corpus;

    private final UIState uiState;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;

    private Segment segSelected;

    public State(BasicFile mainFile, Corpus corpus) {

        uiState = new UIState();

        // Default minimum length for matches
        minMatchLength = 5;

        this.corpus = corpus;

        // the following code ensures that the file selected as main file is in fact in the corpus
        corpus.removeFile(mainFile);
        BasicFile mf = new BasicFile(mainFile);
        corpus.addFile(mf);

        setMainFile(mf);

        //matchManager = new MatchManager(this);
        matchManager = new MatchManager(new HashSet(corpus.getAllCommittedSegs()));

        mf.equals(mainFile);

        uiState.setAllFilesInCorpus(corpus.getFiles());
    }

    public BasicFile getMainFile() {
        return mainFile;
    }

    public final void setMainFile(BasicFile newMainFile) {
        this.mainFile = newMainFile;
        if (!newMainFile.getActiveSegs().isEmpty()) {
            segSelected = newMainFile.getActiveSegs().get(0);
        }
        uiState.setMainFileSegs(newMainFile.getActiveSegs());
    }

    /**
     * When a new selection is made in main file viewer.
     *
     * @param selectedSeg
     */
    public void newSelection(Segment selectedSeg) {
        this.segSelected = selectedSeg;
        MatchList newMatches = findMatch(selectedSeg);
        setMatchFile(newMatches);
    }

    public int getMinMatchLength() {
        return minMatchLength;
    }

    /**
     *
     * @param minMatchLength
     */
    public void setMinLength(int minMatchLength) {
        this.minMatchLength = minMatchLength;
        setMatchFile(findMatch(segSelected));
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public PostingsList getPostingsList(int nGramLength) {
        return matchManager.getPostingsListManager().getPostingsList(nGramLength);
    }

    /**
     * Exports to an external .txt file all the English from all committed Segs
     * in the main file.
     */
    public void exportCommittedSegs() {

        PrintWriter out = null;

        try {
            String FILENAME = "TranslationProgramExport.txt";
            out
                    = new PrintWriter(
                            new BufferedWriter(
                                    new FileWriter(FILENAME)));

            for (Segment seg : getMainFile().getActiveSegs()) {
                if (seg.isCommitted()) {
                    out.println(seg.getEnglish());
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    private void setMatchFile(MatchList newMatches) {
        uiState.setMatchList(newMatches.getMatchSegments());
    }

    /**
     * Finds matching segments according to default match finding algorithm
     *
     * @param seg
     * @return MatchList with matching segments
     */
    private MatchList findMatch(Segment seg) {
        if (seg == null) {
            return new MatchList();
        } else {
            return matchManager.basicMatch(seg, getMinMatchLength());
        }
    }

    public UIState getUIState() {
        return uiState;
    }
    
    /**
     * Required by StateCopier class for testing purpose only.
     *
     * @return
     */
    protected PostingsListManager getPostingsListManager() {
        return matchManager.getPostingsListManager();
    }

    /**
     * Takes oldSeg from the file's active segs list and replaces it with newSeg.
     * 
     * If the file's active segs list does not contain the specified Segment, it
     * returns false and nothing changes. If it does, the file and
     * PostingsListManager adjust appropriately. The old segment is NOT removed
     * from PostingsList if it had been committed. If the new Segment is
     * committed, it will be added to the postings lists.This method does NOT
     * check to see if ids are different or same, or any other relationship
     * between the old and new seg, it merely replaces it if it exists in the
     * activeSegs of the file.
     *
     * @param oldSeg
     * @param newSeg
     * @return True if the seg is an active Segment in the file.
     */
    public boolean replaceSegInFile(Segment oldSeg, Segment newSeg, BasicFile file) {

        ObservableList<Segment> mfActiveSegs = file.getActiveSegs();
        // checks to make sure oldSeg exists in MainFile active segs
        if (!mfActiveSegs.contains(oldSeg)) {
            return false;
        } else {
            int index = mfActiveSegs.indexOf(oldSeg);
            // replaces the oldSeg with the newSeg at that index
            mfActiveSegs.set(index, newSeg);
            // adds the oldSeg to the "hidden" list, in case it was committed, so it can be later found in searches but is not displayed on screen
            file.getHiddenSegs().add(oldSeg);

            //adjusts Postings Lists (so the newSeg can be found in searches if it is committed)
            matchManager.includeSegmentInMatches(newSeg);

            return true;
        }
    }

    /**
     * Adds the segment to the file at the specified index and then adds it
     * to the postings lists. If the file already contains this seg as an active
     * seg (i.e. it's the same object instance), it still adds it. However, if
     * the seg exists as a removed seg, it does not add it and instead throws an
     * exception.
     *
     * @param insertIndex
     * @param seg
     */
    public void addSegToFileActiveList(int insertIndex, Segment seg, BasicFile file) {
        file.getActiveSegs().add(insertIndex, seg);
        matchManager.includeSegmentInMatches(seg);
    }

    /**
     * If seg is either an active or hidden seg in the main file, it removes it.
     * If the seg does not exist in the file, then it returns false.
     *
     * @param seg
     * @return
     */
    public boolean removeSegFromFile(Segment seg, BasicFile file) {
        ObservableList<Segment> activeSegs = file.getActiveSegs();
        ArrayList<Segment> hiddenSegs = file.getHiddenSegs();

        if (activeSegs.contains(seg)) {
            // removes from active and from plm
            activeSegs.remove(seg);
            matchManager.removeSegmentFromMatches(seg);
            return true;
        } else if (hiddenSegs.contains(seg)) {
            // removes from hidden and from plm
            hiddenSegs.remove(seg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the segment to the main file's list of hidden segs. If the seg
     * already exists in the file as a hidden seg, it will still add it again.
     * If this file already contains the seg as an active seg, this throws an
     * IllegalArgumentException.
     *
     * @param seg
     * @return
     */
    public void addToMainFileHidden(Segment seg, BasicFile file) {
        ObservableList<Segment> activeSegs = file.getActiveSegs();
        ArrayList<Segment> hiddenSegs = file.getHiddenSegs();

        if (activeSegs.contains(seg)) {
            throw new IllegalArgumentException("A seg cannot be 'hidden' if it's already active. Remove seg from file first.");
        } else {
            if (!hiddenSegs.contains(seg)) {
                hiddenSegs.add(seg);
                matchManager.includeSegmentInMatches(seg);
            }
        }
    }

}
