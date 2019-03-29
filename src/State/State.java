/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import comparator.PostingsListManager;
import Database.DatabaseOperations;
import DataStructures.PostingsList;
import DataStructures.BasicFile;
import DataStructures.MatchList;
import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import comparator.MatchFinderCoreAlgorithm;
import comparator.MatchManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.collections.ObservableList;


/**
 *
 * @author Chris
 */
public class State {
    
   

    private static final boolean DATABASE_IS_READABLE = true;
    private static final boolean DATABASE_IS_WRITABLE = true;
    private static final boolean REBOOT_DATABASE = false;
    
    
    /**
     * Handles the searching and caching of matches. 
     */
    private final MatchManager matchManager;
    
    /**
     * The file currently being translated.
     */
    private MainFile mainFile;

    /**
     * The corpus where matches are found.
     */
    private final Corpus corpus;

    private MatchList compareFile;
    
    private final UIState uiState;
    

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;
   
    private Segment segSelected;
   
    public State(BasicFile mainFile, Corpus corpus) {
        
        if (REBOOT_DATABASE) {
            DatabaseOperations.rebootDB();
        }
        
        uiState = new UIState();
        
        // Default minimum length for matches
        minMatchLength = 5;
        
        this.corpus = corpus;
        
        // the following code ensures that the file selected as main file is in fact in the corpus
        corpus.removeFile(mainFile);
        MainFile mf = new MainFile(mainFile);
        corpus.addFile(mf);
        
        setMainFile(mf);
        
        matchManager = new MatchManager(this);
        
        
        mf.equals(mainFile);
        compareFile = findMatch(segSelected);
        
        
    }

    public MainFile getMainFile() {
        return mainFile;
    }
    
    public final void setMainFile(MainFile newMainFile) {
        this.mainFile = newMainFile;
        if (!newMainFile.getActiveSegs().isEmpty()) {
            segSelected =  newMainFile.getActiveSegs().get(0);
        }
        uiState.setMainFileSegs(newMainFile.getActiveSegs());
    }
    
    public MatchList getMatchFile() {
         return compareFile;
    }
    
    public ObservableList<MatchSegment> getMatchList() {
        return uiState.getMatchList();
    }
    
     /**
     * When a new selection is made in main file viewer.
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
        matchManager.minMatchLengthChanged(minMatchLength);
        setMatchFile(findMatch(segSelected));
    }
    
    public Corpus getCorpus() {
        return corpus;
    }
    
    public PostingsList getPostingsList(int nGramLength) {
        return matchManager.getPLM().getPostingsList(nGramLength);
    }
    
     /**
     * Exports to an external .txt file all the English from all committed Segs in the main file. 
     */
    public void exportCommittedSegs() {

        PrintWriter out = null;
        
        try {
            String FILENAME = "TranslationProgramExport.txt";
            out = 
                    new PrintWriter(
                            new BufferedWriter(
                                    new FileWriter(FILENAME)));
            
            for (Segment tu : getMainFile().getActiveSegs()) {
                if (tu.isCommitted()) {
                    out.println(tu.getEnglish());
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
        compareFile = newMatches;
        uiState.setMatchList(newMatches.getMatchSegments());
    }

    public static boolean databaseIsReadable() {
        return DATABASE_IS_READABLE;
    }

    public static boolean databaseIsWritable() {
        return DATABASE_IS_WRITABLE;
    }
    
    public void commitSeg(Segment seg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Finds matching segments according to default match finding algorithm
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
    
    private MatchList findExactMatch(String str) {
        return MatchFinderCoreAlgorithm.exactMatch(str, this);
    }

    public void search(String text) {
        setMatchFile(findExactMatch(text));
    }

    public UIState getUIState() {
        return uiState;
    }

    public void restorePriorMainFile(MainFile priorMainFile) {
         
    }

    public void split(Segment seg, int index) {
        getMainFile().splitSeg(seg, index);
    }

    public PostingsListManager getPostingsListManager() {
        return matchManager.getPLM();
    }

    /**
     * Takes the "oldSeg" from the MainFile's active segs list and replaces it. If MF's active segs list does not contain the specified Segment, it returns false and nothing changes. If it does, the MainFile and PostingsListManager adjust appropriately. The old segment is NOT removed from PostingsList if it had been committed. If the new Segment is committed, it will be added to the postings lists.This mehtod does NOT check to see if ids are different or same, or any other relationship between the old and new seg, it merely replaces it if it exists in activeSegs of the main file.
     * @param oldSeg
     * @param newSeg 
     * @return  True if seg exists in MF active. False if not.
     */
    public boolean replaceSeg(Segment oldSeg, Segment newSeg) {
        
        ObservableList<Segment> mfActiveSegs = getMainFile().getActiveSegs();
        // checks to make sure oldSeg exists in MainFile active segs
        if (!mfActiveSegs.contains(oldSeg)) {
            return false;
        } else {
            int index = mfActiveSegs.indexOf(oldSeg);
            // replaces the oldSeg with the newSeg at that index
            mfActiveSegs.set(index, newSeg);
            // adds the oldSeg to the "removed" list, in case it was committed, so it can be later found in searches but is not displayed on screen
            getMainFile().getHiddenSegs().add(oldSeg);
            
            //adjusts Postings Lists
            PostingsListManager plManager = getPostingsListManager();
            //plManager.removeSegmentFromMatches(oldSeg); // not necessary actually, because if a seg was committed, even if it is now no longer visible in the file being edited, it should still be stored as a possible match
            plManager.addSegment(newSeg);
            
            return true;
        }
    }
    
     public boolean removeSeg(Segment seg) {
        ObservableList<Segment> mfActiveSegs = getMainFile().getActiveSegs();
        // checks to make sure oldSeg exists in MainFile active segs
        if (!mfActiveSegs.contains(seg)) {
            return false;
        } else {
            // removes from active segs
            mfActiveSegs.remove(seg);
            // adds the oldSeg to the "removed" list, in case it was committed, so it can be later found in searches but is not displayed on screen
            getMainFile().getHiddenSegs().add(seg);
            
            // postings lists not changed:
            // if segment had been committed, we still want it stored
            // if segment had not been committed, it won't exist in postings lists anyways. 
            
            return true;
        }
    }

    /**
     * Adds the segment to the MainFile at the specified index and then adds it to the postings lists. If the file already contains this seg as an active seg (i.e. it's the same object instance), it still adds it. However, if the seg exists as a removed seg, it does not add it and instead throws an exception. 
     * @param insertIndex
     * @param seg 
     */
    public void addSeg(int insertIndex, Segment seg) {
        getMainFile().getActiveSegs().add(insertIndex, seg);
        getPostingsListManager().addSegment(seg);
    }

   
    
    /**
     * If seg is either an active or hidden seg in the main file, it removes it. If the seg does not exist in the file, then it returns false.
     * @param seg
     * @return 
     */
    public boolean removeSeg2(Segment seg) {
        ObservableList<Segment> activeSegs = getMainFile().getActiveSegs();
        ArrayList<Segment> hiddenSegs = getMainFile().getHiddenSegs();
        
        if (activeSegs.contains(seg)) {
            // removes from active and from plm
            activeSegs.remove(seg);
            getPostingsListManager().removeSegment(seg);
            return true;
        } else if (hiddenSegs.contains(seg)) {
            // removes from hidden and from plm
            hiddenSegs.remove(seg);
            //matchManager.removeSegmentFromMatches(seg);
            //getPostingsListManager().removeSegmentFromMatches(seg);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Adds the segment to the main file's list of hidden segs. If the seg already exists in the file as a hidden seg, it will still add it again. If this file already contains the seg as an active seg, this throws an IllegalArgumentException.
     * @param seg
     * @return 
     */
    public void addToHidden(Segment seg) {
         ObservableList<Segment> activeSegs = getMainFile().getActiveSegs();
        ArrayList<Segment> hiddenSegs = getMainFile().getHiddenSegs();
        
        if (activeSegs.contains(seg)) {
            throw new IllegalArgumentException("A seg cannot be 'hidden' if it's already active. Remove seg from file first.");
        } else {
            if (! hiddenSegs.contains(seg)) {
                hiddenSegs.add(seg);
                getPostingsListManager().addSegment(seg);
            }
        }
    }
    
    
    
    
  

    
    
    
    
}
