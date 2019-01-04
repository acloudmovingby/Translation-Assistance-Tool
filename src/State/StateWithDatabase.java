/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Database.DatabaseOperations;
import Database.PostingsList;
import Files.BasicFile;
import Files.MatchFile;
import Files.FileBuilder;
import Files.Corpus;
import Files.MatchSegment;
import Files.Segment;
import UndoManager.UndoManager;
import comparator.MatchFinder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;


/**
 *
 * @author Chris
 */
public class StateWithDatabase implements State {
    
   

    private static final boolean DATABASE_IS_READABLE = true;
    private static final boolean DATABASE_IS_WRITABLE = true;
    private static final boolean REBOOT_DATABASE = false;
    private static final Font DEFAULT_THAI_FONT = Font.font("Arial");
    private static final Font DEFAULT_ENGLISH_FONT = Font.font("Arial");
    
    private PostingsList pl2;
    private PostingsList pl3;
    private PostingsList pl4;
    private PostingsList pl5;
    private PostingsList pl6;
    private PostingsList pl7;
    private PostingsList pl8;
    


    /**
     * The file currently being translated.
     */
    BasicFile mainFile;
    
    /**
     * 
     */
    ObservableList<Segment> mainFileSegs;

    /**
     * The corpus where matches are found.
     */
    private Corpus corpus;

    private MatchFile compareFile;
    
    private ObservableList<MatchSegment> matchList;
    
    private IntegerProperty numMatches;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;
   
    private Segment segSelected;
    
    private UndoManager um;
   
    public StateWithDatabase(BasicFile mainFile, Corpus corpus) {
        // constructor takes: main file, corpus (both already built)
        // state contains: 
                // corpus
                // main file
                // match file
                // postings lists
                // access to database stuff
        // state needs to be started:
                // corpus
        
        if (REBOOT_DATABASE) {
            DatabaseOperations.rebootDB();
        }
        // Default minimum length for matches
        minMatchLength = 10;
        mainFileSegs = FXCollections.observableArrayList();
        matchList = FXCollections.observableArrayList();
        numMatches = new SimpleIntegerProperty(0);
        setMainFile(mainFile);
        setCorpus(corpus);
        um = new UndoManager();
    }

    @Override
    public BasicFile getMainFile() {
        return mainFile;
    }
    
    public IntegerProperty getNumMatchesProperty() {
        return numMatches;
    }
    
    public UndoManager getUndoManager() {
        return um;
    }
    
    @Override
    public void setMainFile(BasicFile newMainFile) {
        this.mainFile = newMainFile;
        segSelected =  newMainFile.getActiveSegs().get(0);
        mainFileSegs = newMainFile.getActiveSegs();
    }
    
    public ObservableList<Segment> getMainFileSegs() {
        return mainFileSegs;
    }
    
    
    
     @Override
    public MatchFile getMatchFile() {
         return compareFile;
    }
    
    @Override
    public ObservableList<MatchSegment> getMatchList() {
        return matchList;
    }
    
     /**
     * When a new selection is made in main file viewer.
     * @param selectedSeg 
     */
    @Override
    public void newSelection(Segment selectedSeg) {
        System.out.println(selectedSeg.getThai());
        this.segSelected = selectedSeg;
        MatchFile newMatches = findMatch(selectedSeg);
        setMatchFile(newMatches);
    }
    
    @Override
    public int getMinMatchLength() {
        return minMatchLength;
    }
    
     /**
     * 
     * @param minMatchLength 
     */
    @Override
    public void setMinLength(int minMatchLength) {
        this.minMatchLength = minMatchLength;
        setMatchFile(findMatch(segSelected));
    }
    
    @Override
    public Corpus getCorpus() {
        return corpus;
    }
    
    private void setCorpus(Corpus corpus) {
        this.corpus = corpus;
        
        long initial = System.nanoTime();
        pl2 = new PostingsList(2);
        pl2.addFileList(corpus);
        long after = System.nanoTime();
        System.out.println("after 2: " + (after-initial));
        initial = after;
        
        pl3 = new PostingsList(3);
        pl3.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 3: " + (after-initial));
        initial = after;
        
        pl4 = new PostingsList(4);
        pl4.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 4: " + (after-initial));
        initial = after;
        
        pl5 = new PostingsList(5);
        pl5.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 5: " + (after-initial));
        initial = after;
        
        pl6 = new PostingsList(6);
        pl6.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 6: " + (after-initial));
        initial = after;
        
        pl7 = new PostingsList(7);
        pl7.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 7: " + (after-initial));
        initial = after;
        
        pl8 = new PostingsList(8);
        pl8.addFileList(corpus);
        after = System.nanoTime();
        System.out.println("after 8: " + (after-initial));
        initial = after;
        
        compareFile = findMatch(segSelected);
    }
    
    @Override
    public PostingsList getPostingsList(int nGramLength) {
        PostingsList pl;
        switch (nGramLength) {
            case 2:
                pl = pl2;
                break;
            case 3:
                pl = pl3;
                break;
            case 4:
                pl = pl4;
                break;
            case 5:
                pl = pl5;
                break;
            case 6:
                pl = pl6;
                break;
            case 7:
                pl = pl7;
                break;
            case 8:
                pl = pl8;
                break;
            default:
                pl = new PostingsList(nGramLength);
                pl.addFileList(getCorpus());
                break;
        }
        return pl;
    }
    
     /**
     * Prints the English from all committed TUs in the main file to a file. 
     */
    @Override
    public void exportCommittedTUs() {

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

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
                if (out != null) {
                    out.close();
                }
        }

    }
    
    private void setMatchFile(MatchFile newMatches) {
        compareFile = newMatches;
        setMatchList(newMatches.getObservableList());
        numMatches.set(getMatchList().size());
    }
    
    private void setMatchList(ObservableList<MatchSegment> newMatchList) {
        matchList.setAll(newMatchList);
    }

    public static boolean databaseIsReadable() {
        return DATABASE_IS_READABLE;
    }

    public static boolean databaseIsWritable() {
        return DATABASE_IS_WRITABLE;
    }

    // NOT WORKING AS ORIGINALLY DID (DOESN'T RESET MATCH LIST)
    private void commit(Segment selectedTU) {
        // Changes the committed status of this TU
        if (selectedTU != null) {
            selectedTU.setCommitted(true);

            // Finds all matches with this newly committed TU
            //OrigComparator c = new OrigComparator(getCurrentCompareString(), selectedTU, getMinMatchLength());
            /* adds these matches to the current compareFile.
            for (MatchSegment tu : c.getMatchFile().getObservableList()) {
                compareFile.addEntry(tu);
            }
*/
            // DATABASE
        }

    }

    void englishEdited(Segment selectedTU, String newText) {
        selectedTU.setEnglish(newText);
        commit(selectedTU);
    }
    
    public void commitSeg(Segment seg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
   
    
    /**
     * Finds matching segments according to default match finding algorithm
     * @param seg
     * @return MatchFile with matching segments
     */
    private MatchFile findMatch(Segment seg) {
        System.out.println("corpus size: " + corpus.getAllCommittedTUs().size());
        return MatchFinder.basicMatch(seg, minMatchLength, this);
    }
    // selection calls findMatch
    // find match tells MatchFinder to use minMatchLength
    // MatchFinder makes a postingslist of length minMatchLength
    // could make postings lists for 2->8
    
    private MatchFile findExactMatch(String str) {
        return MatchFinder.exactMatch(str, this);
    }
    
   
    
    
    public static Font getThaiFont() {
        return DEFAULT_THAI_FONT;
    }
    
    public static Font getEnglishFont() {
        return DEFAULT_ENGLISH_FONT;
    }

    @Override
    public void search(String text) {
        setMatchFile(findExactMatch(text));
    }

    @Override
    public void acceptAction(UserAction a) {
        getUndoManager().pushState(this);
        a.executeStateChange(this);
    }

    public void undo() {
        System.out.println("here");
         getUndoManager().undo(this);
    }

  

    
    
    
    
}
