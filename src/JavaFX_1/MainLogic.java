/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Files.BasicFile;
import Files.CompareFile;
import Files.FileBuilder;
import Files.FileList;
import Files.TUCompareEntry;
import Files.TUEntry;
import ParseThaiLaw.ThaiLawParser;
import comparator.Comparator;
import java.util.ArrayList;

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
        - LONG TERM PLAN:
            - I dont generate parsed law files each time, I instead pull from DB:
                - make optional part of file parser be it adds to the DB. At some point just run it and add.
                - take out ThaiLawParser from main
            - when game begins, instead of builing corpus, it just makes calls to Comparator
                - Comparator retrieves stuff from DB on its own
                - main though can add stuff to DB of course:
                    - commit, merge, split
            - main file? 
                - Just have it pulled from db
                - just put in main the file id
                - have a method that calls id and makes that file the MF
                - requires a DBOperations method GetFile (creates BasicFile)
          
*/

/**
 *
 * @author Chris
 */
public class MainLogic {
    
    private static final boolean IS_DATABASE_DISABLED = false;
    
    /**
     * The file currently being translated.
     */
    BasicFile mainFile;
    
     /**
     * The corpus where matches are found.
     */
    FileList corpus;
    
    /**
     * The cached compare files.
     */
    private CompareFile[] compareFileCache;
    
    /**
     * THe current compare file being displayed in the compare table view.
     */
    private CompareFile currentCompareFile;
    
    /**
     * The string that was used to set the current compare table.
     */
    private int currentIndex;

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
        // Commits all TUS in main file (only for testing purposes)
        //mainFile.commitAllTUs();
        
        //creates comparefile cache
        compareFileCache = new CompareFile[mainFile.getObservableList().size()];
        
        
        
        // MAKES CORPUS, ADDS SOME FILES
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
        for(CompareFile c : compareFileCache) {
            c=null;
        }
    }

    protected int getCurrentIndex() {
        return currentIndex;
    }
    
    private void setCurrentTU(int index) {
        currentIndex = index;
    }

    protected CompareFile getCompareFile(int index) {
        if (compareFileCache[index] == null) {
             setCurrentTU(index);
            Comparator c = new Comparator(mainFile.getTU(getCurrentIndex()).getThai(), getCorpus(), getMinMatchLength());
            currentCompareFile = c.getCompareFile();
            compareFileCache[index] = currentCompareFile;
            return currentCompareFile;
        } else {
            return compareFileCache[index];
        }
    }

    void commit(TUEntry selectedTU) {
        // Changes the committed status of this TU
        if (selectedTU != null) {
             selectedTU.setCommitted(true);
        }
        
        for(CompareFile c : compareFileCache) {
            c=null;
        }
        
        // Finds all matches with this newly committed TU (not with whole corpus)
        Comparator c = new Comparator(mainFile.getTU(getCurrentIndex()).getThai(), selectedTU, getMinMatchLength());
        // adds these matches to the current compareFile.
        for (TUCompareEntry tu : c.getCompareFile().getObservableList()) {
            currentCompareFile.addEntry(tu);
        }
    }
    
    void englishEdited(TUEntry selectedTU, String newText) {
       selectedTU.setEnglish(newText);
       commit(selectedTU);
    }
    
    public static boolean isDatabaseDisabled() {
        return IS_DATABASE_DISABLED;
    }
}
