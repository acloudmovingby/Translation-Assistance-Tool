/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Database.DatabaseOperations;
import Files.BasicFile;
import Files.MatchFile;
import Files.FileBuilder;
import Files.FileList;
import Files.MatchSegment;
import Files.Segment;
import comparator.MatchFinder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javafx.scene.text.Font;


/**
 *
 * @author Chris
 */
public class MainLogic {

    static final boolean DATABASE_IS_READABLE = true;
    static final boolean DATABASE_IS_WRITABLE = true;
    static final boolean REBOOT_DATABASE = false;
    private static final Font DEFAULT_THAI_FONT = Font.font("Arial");
    private static final Font DEFAULT_ENGLISH_FONT = Font.font("Arial");


    /**
     * The file currently being translated.
     */
    BasicFile mainFile;

    /**
     * The corpus where matches are found.
     */
    FileList corpus;

    private final MatchFile compareFile;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;
   
   private Segment segSelected;
   
   

    MainLogic() {
        if (REBOOT_DATABASE) {
            DatabaseOperations.rebootDB();
        }
        // Default minimum length for matches
        minMatchLength = 10;

        // makes main file
        
        FileBuilder fileBuilder = new FileBuilder();
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        mainFile = fileBuilder.justThaiFilePath(filePath);
        DatabaseOperations.addFile(mainFile);
        
       // mainFile = DatabaseOperations.getFile(0);
        // Commits all TUS in main file (only for testing purposes)
        //mainFile.commitAllTUs();

        // MAKES CORPUS, ADDS SOME FILES
        corpus = DatabaseOperations.getAllTUs();
        
       // corpus.addFile(mainFile);
        System.out.println("Corpus size: " + corpus.getFiles().size());
        for (BasicFile bf : corpus.getFiles()) {
            System.out.println(bf.getFileName() + ", " + bf.getFileID());
        }
        
        segSelected =  mainFile.getTUsToDisplay().get(0);
        compareFile = defaultMatchFind(segSelected);
        
        
        /*
        corpus = new FileList();
        corpus.addFile(mainFile);
        
        String thaiFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
        String engFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
        BasicFile file1 = (new ThaiLawParser(thaiFile1, engFile1)).makeFile();
        file1.commitAllTUs();
        corpus.addFile(file1);
        
        String thaiFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 2.txt";
        String engFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 2-1.txt";
        BasicFile file2 = (new ThaiLawParser(thaiFile2, engFile2)).makeFile();
        file2.commitAllTUs();
        corpus.addFile(file2);
        
        String thaiFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";
        String engFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
        BasicFile file3 = (new ThaiLawParser(thaiFile3, engFile3)).makeFile();
        file3.commitAllTUs();
        corpus.addFile(file3);
         */
    }

    protected BasicFile getMainFile() {
        return mainFile;
    }

    protected FileList getCorpus() {
        return corpus;
    }

    protected int getMinMatchLength() {
        return minMatchLength;
    }

    protected void setMinMatchLength(int k) {
        minMatchLength = k;
    }
    
    protected MatchFile getCompareFile(String newCompareString) {
        //setCurrentCompareString(newCompareString);
        //OrigComparator c = new OrigComparator(getCurrentCompareString(), getCorpus(), getMinMatchLength());
        //compareFile = c.getCompareFile();
        return compareFile;
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
            for (MatchSegment tu : c.getCompareFile().getObservableList()) {
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

    /**
     * Prints the English from all committed TUs in the main file to a file. 
     */
    protected void exportCommittedTUs() {

        PrintWriter out = null;
        
        try {
            String FILENAME = "TranslationProgramExport.txt";
            out = 
                    new PrintWriter(
                            new BufferedWriter(
                                    new FileWriter(FILENAME)));
            
            for (Segment tu : getMainFile().getTUsToDisplay()) {
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
    
    
    /**********
     * REDO OF COMPARE TABLE IMPL
     */
    
    

    /**
     * When a new selection is made in main file viewer.
     * @param segSelected 
     */
    public void newSelection(Segment segSelected) {
        this.segSelected = segSelected;
        MatchFile newMatches = defaultMatchFind(segSelected);
        resetMatchList(newMatches);
    }
    
    /**
     * 
     * @param minMatchLength 
     */
    public void setMinLength(int minMatchLength) {
        this.setMinMatchLength(minMatchLength);
        resetMatchList(defaultMatchFind(segSelected));
    }
    
    public void commitSeg(Segment seg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    private void resetMatchList(MatchFile newMatches) {
        List<MatchSegment> l = newMatches.getObservableList();
        compareFile.getObservableList().clear();
        compareFile.getObservableList().addAll(l);
    }
    
   
    
    /**
     * Finds matching segments according to default match fining algorithm
     * @param seg
     * @return MatchFile with matching segments
     */
    private MatchFile defaultMatchFind(Segment seg) {
        return MatchFinder.basicMatch2(seg, minMatchLength, corpus);
    }
    
    private MatchFile findExactMatch(String str) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    MatchFile getCompareFile() {
         return compareFile;
    }
    
    
    public static Font getThaiFont() {
        return DEFAULT_THAI_FONT;
    }
    
    public static Font getEnglishFont() {
        
        return DEFAULT_ENGLISH_FONT;
    }
}
