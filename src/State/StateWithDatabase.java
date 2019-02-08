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
    

    /**
     * The object that handles the making of ngrams for faster search.
     */
    private final PostingsListManager plm;
    
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
        minMatchLength = 5;
        matchList = FXCollections.observableArrayList();
        numMatches = new SimpleIntegerProperty(0);
        
        
        // makes sure mainfile is actually a part of corpus
        // if not, it adds mainFile to corpus
        if (!corpus.contains(mainFile)) {
            corpus.addFile(mainFile);
        }
        
        setMainFile(new MainFile(mainFile));
        
        plm = new PostingsListManager(corpus);
        
        
        //setCorpus(corpus);
        compareFile = findMatch(segSelected);
    }

    @Override
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
    
    @Override
    public PostingsList getPostingsList(int nGramLength) {
        return plm.getPostingsList(nGramLength);
        
        /*PostingsList pl;
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
                pl.addCorpus(getCorpus());
                break;
        }
        return pl;
        */
    }
    
     /**
     * Prints the English from all committed TUs in the main file to a file. 
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

   

    @Override
    public PostingsListManager getPostingsListManager() {
        return plm;
    }


  

    
    
    
    
}
