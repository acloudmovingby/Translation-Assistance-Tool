/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Database.DatabaseOperations;
import DataStructures.PostingsList;
import DataStructures.BasicFile;
import DataStructures.MatchList;
import DataStructures.Corpus;
import DataStructures.MainFile;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import comparator.MatchFinder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Chris
 */
public class StateWithDatabase implements State {
    
   

    private static final boolean DATABASE_IS_READABLE = true;
    private static final boolean DATABASE_IS_WRITABLE = true;
    private static final boolean REBOOT_DATABASE = false;
    
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
    private MainFile mainFile;

    /**
     * The corpus where matches are found.
     */
    private Corpus corpus;

    private MatchList compareFile;
    
    private final UIState uiState;
    
    private ObservableList<MatchSegment> matchList;
    
    private IntegerProperty numMatches;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;
   
    private Segment segSelected;
   
    public StateWithDatabase(BasicFile mainFile, Corpus corpus) {
        
        if (REBOOT_DATABASE) {
            DatabaseOperations.rebootDB();
        }
        
        uiState = new UIState();
        
        // Default minimum length for matches
        minMatchLength = 10;
        matchList = FXCollections.observableArrayList();
        numMatches = new SimpleIntegerProperty(0);
        setMainFile(new MainFile(mainFile));
        setCorpus(corpus);
    }

    @Override
    public MainFile getMainFile() {
        return mainFile;
    }
    
    public void setMainFile(MainFile newMainFile) {
        this.mainFile = newMainFile;
        if (!newMainFile.getActiveSegs().isEmpty()) {
            segSelected =  newMainFile.getActiveSegs().get(0);
        }
        uiState.setMainFileSegs(newMainFile.getActiveSegs());
    }
    
    public IntegerProperty getNumMatchesProperty() {
        return numMatches;
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
        System.out.println(selectedSeg.getThai());
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
    
    private void setMatchFile(MatchList newMatches) {
        compareFile = newMatches;
        setMatchList(newMatches.getObservableList());
        numMatches.set(getMatchList().size());
    }
    
    private void setMatchList(ObservableList<MatchSegment> newMatchList) {
        uiState.setMatchList(newMatchList);
        //matchList.setAll(newMatchList);
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
            return MatchFinder.basicMatch(seg, minMatchLength, this);
        }
    }
    // selection calls findMatch
    // find match tells MatchFinder to use minMatchLength
    // MatchFinder makes a postingslist of length minMatchLength
    // could make postings lists for 2->8
    
    private MatchList findExactMatch(String str) {
        return MatchFinder.exactMatch(str, this);
    }

    public void search(String text) {
        setMatchFile(findExactMatch(text));
    }

    @Override
    public UIState getUIState() {
        return uiState;
    }

    @Override
    public void resetMainFile(State priorMainFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void split(Segment seg, int index) {
        getMainFile().splitSeg(seg, index);
    }


  

    
    
    
    
}
