package State;

import comparator.PostingsListManager;
import comparator.PostingsList;
import DataStructures.BasicFile;
import DataStructures.Segment;
import comparator.MatchManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
     * The file currently being translated. Is in the corpus.
     */
    private BasicFile mainFile;

    /**
     * The corpus where matches are found. The main file must be in this.
     */
    private final List<BasicFile> corpus;

    private final UIState uiState;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;

    private Segment segSelected;

    
    public State(List<BasicFile> fileList) {

        uiState = new UIState();

        // Default minimum length for matches
        minMatchLength = 5;

        this.corpus = fileList;
        
/*
        // the following code ensures that the file selected as main file is in fact in the corpus
        corpus.remove(mainFile);
        BasicFile mf = new BasicFile(mainFile);
        corpus.add(mf);

        setMainFile(mf);*/

        //matchManager = new MatchManager(this);
        HashSet<Segment> allCommittedSegsInCorpus = BasicFile.getAllCommittedSegsInFileList(corpus);
        matchManager = new MatchManager(allCommittedSegsInCorpus);

        uiState.setAllFilesInCorpus(corpus);
    }

    public BasicFile getMainFile() {
        return mainFile;
    }

    /**
     * Sets the specified file as the current main file for translating. If the file previously did not exist in the corpus, it adds it.
     * @param newMainFile 
     */
    public final void setMainFile(BasicFile newMainFile) {
        if (! corpus.contains(newMainFile)) {
            this.addFileToCorpus(newMainFile);
        }
            
        this.mainFile = newMainFile;
        if (!newMainFile.getActiveSegs().isEmpty()) {
            segSelected = newMainFile.getActiveSegs().get(0);
        }
        uiState.setMainFileSegs(newMainFile.getActiveSegs());
        uiState.setMainFileName(newMainFile.getFileName());
    }
    
    public void setCurrentSelectedSeg(Segment selectedSeg) {
        this.segSelected = selectedSeg;
    }
    
    public Segment getCurrentSelectedSeg() {
        return segSelected;
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
    }
    
    public List<BasicFile> getCorpusFiles() {
        return corpus;
    }

    public PostingsList getPostingsList(int nGramLength) {
        return matchManager.getPostingsListManager().getPostingsList(nGramLength);
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
     * @param file
     * @return True if the seg is an active Segment in the file.
     */
    public boolean replaceSegInFile(Segment oldSeg, Segment newSeg, BasicFile file) {

        ObservableList<Segment> activeSegsInFile = file.getActiveSegs();
        // checks to make sure oldSeg exists in MainFile active segs
        if (!activeSegsInFile.contains(oldSeg)) {
            return false;
        } else {
            int index = activeSegsInFile.indexOf(oldSeg);
            // replaces the oldSeg with the newSeg at that index
            activeSegsInFile.set(index, newSeg);
            // adds the oldSeg to the "hidden" list, in case it was committed, so it can be later found in searches but is not displayed on screen
            file.getHiddenSegs().add(oldSeg);

            //adjusts Postings Lists (so the newSeg can be found in searches if it is committed)
            boolean bool = matchManager.includeSegmentInMatches(newSeg);
            // TEMP DELETE
            System.out.println("file is equal to main file? " + file.equals(mainFile));
            System.out.println("result of matchManager include seg: " + bool);

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
     * @param file
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
     * @param file
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
     * @param file
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

    protected MatchManager getMatchManager() {
        return matchManager;
    }


    /**
     * Because some data (like the postings lists) depend on the corpus, the list of files in the corpus should not be modified directly, as this may not update the other data. 
     * 
     * @param file
     * @return true if file was added, false if file already existed in corpus
     */
    protected boolean addFileToCorpus(BasicFile file) {
        if (corpus.contains(file)) {
            return false;
        } else {
            corpus.add(file); // add to corpus
            file.getAllSegs().forEach(seg -> 
                    matchManager.includeSegmentInMatches(seg)); // add to MatchManager
            return true;
        }
    }
}
