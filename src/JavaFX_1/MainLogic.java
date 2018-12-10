/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Database.DatabaseOperations;
import Files.BasicFile;
import Files.CompareFile;
import Files.FileBuilder;
import Files.FileList;
import Files.TUCompareEntry;
import Files.TUEntryBasic;
import ParseThaiLaw.ThaiLawParser;
import comparator.Comparator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
COMMIT TO DO:
    ------change status color on commit
    ------make so TUs don't match themselves
    - change selection on commit
    - add key press to commit
    - RIGHT NOW: 
        - remove numbering
        - make so "editOnCommit" (i.e. "return") is the commit button
        - make so status color changes on commit
        - check to make sure CF files are working properly

OPTIONAL LATER:
    - make so TUs have post-commit editing status 
    - make it so if that status is true, it changes status color
    - make it so that if you start typing in a cell after committed, it changes the post-commit status

-----HIGHLIGHT MATCHES
    ------make comparetable thai column take TextFlow objects

SPLIT:
    - Make two new TUs: 
        - Thai split
        - whatever English was written is now in first one
        - both uncommitted
    - add new TUs
    - remove original TU from list (this means the commit will be gone)

MERGE: 
    - Make new TU Thai/English combined 
    - If old TU had been committed, make it "hidden", i.e. can't be displayed:
        - could make so observable list
    - remove original TUs
    - merge new TUs

MATCH SCORE
    - (easy) make so that all compare files are run at program opening, then value stored in match score
    - (hard / bad) 
        - store all compare files at beginning, 
        - for redraw table, replace list elemetns in comparefile (so not mess up observable)
        - identify / implement where compare files need to be regenerated:
            - change min length
            - commit / split / merge
        - if space is for some reason an issue, make Comparator method that doesn't store Cfs, but just find max TU match score
GLOSSARY:
    - make top with searchbar
    - listview, each cell has:
           - glossary term
           - english definition, indented over
    - upon selection of TU:
        - glossary module is called
        - glossary module searches if any term exists in Thai, if so, it is returned to Main Logic
        - Main Logic updates listview
    - words are highlighted in 

DATABASE
    - Initial implementation: 
        - one table with each row representing a TU with the following fields:
            - id (primary key)
            - file id
            - file name
            - Thai
            - English
            - commit status
        - ways to access TU and what is wanted/used:
            - parse new file
                - every field
            - comparator
                - commit status and thai (most of all)
                - then, if there is a match, then the english, filename, etc.
            - commit
                - commit status
            - merge / split
                - every aspect of those specific TUs
        - build database by taking files in corpus and entering them.
        - TEMPORARY: every time you start the program, take all files out of memory and store them as file objects again (i.e. recreate your entire corpus in memory)
        - steps:
            - make database
            - make method that enters TUs for 1 file giving unique, ordered id number
        - need to make new table for file id's / file #s in case files exist with no TUs in them.
            - when createFileID() is called, it needs to add the new id to that table
        - need to fix the "sqlite_busy" bug. 
            basically, my tests have to be atomic transactions
            If I write do it with the same connection, does it throw an error?
                - try "addTU" and "getTU" but you pass a connection
                - run one after another adn see if the SQLite error is thrown 
 */
/**
 *
 * @author Chris
 */
public class MainLogic {

    static final boolean DATABASE_IS_READABLE = true;
    static final boolean DATABASE_IS_WRITABLE = true;

    /**
     * The file currently being translated.
     */
    BasicFile mainFile;

    /**
     * The corpus where matches are found.
     */
    FileList corpus;

    private CompareFile currentCompareFile;

    /**
     * The string that was used to set the current compare table.
     */
    private String currentCompareString;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;

    MainLogic() {
        // Default minimum length for matches
        minMatchLength = 5;

        // makes main file
        FileBuilder fileBuilder = new FileBuilder();
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        mainFile = fileBuilder.justThaiFilePath(filePath);
        DatabaseOperations.addFile(mainFile);
        // Commits all TUS in main file (only for testing purposes)
        //mainFile.commitAllTUs();

        // MAKES CORPUS, ADDS SOME FILES
        corpus = DatabaseOperations.getAllCommittedTUs();
       // corpus.addFile(mainFile);
        System.out.println("Corpus size: " + corpus.getFiles().size());
        for (BasicFile bf : corpus.getFiles()) {
            System.out.println(bf.getFileName() + ", " + bf.getFileID());
        }
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

    protected String getCurrentCompareString() {
        return currentCompareString;
    }

    private void setCurrentCompareString(String newCompareString) {
        currentCompareString = newCompareString;
    }

    protected CompareFile getCompareFile(String newCompareString) {
        setCurrentCompareString(newCompareString);
        Comparator c = new Comparator(getCurrentCompareString(), getCorpus(), getMinMatchLength());
        currentCompareFile = c.getCompareFile();
        return currentCompareFile;
    }

    public static boolean databaseIsReadable() {
        return DATABASE_IS_READABLE;
    }

    public static boolean databaseIsWritable() {
        return DATABASE_IS_WRITABLE;
    }

    private void commit(TUEntryBasic selectedTU) {
        // Changes the committed status of this TU
        if (selectedTU != null) {
            selectedTU.setCommitted(true);

            // Finds all matches with this newly committed TU
            Comparator c = new Comparator(getCurrentCompareString(), selectedTU, getMinMatchLength());
            // adds these matches to the current compareFile.
            for (TUCompareEntry tu : c.getCompareFile().getObservableList()) {
                currentCompareFile.addEntry(tu);
            }

            // DATABASE
        }

    }

    void englishEdited(TUEntryBasic selectedTU, String newText) {
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
            
            for (TUEntryBasic tu : getMainFile().getObservableList()) {
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

}
