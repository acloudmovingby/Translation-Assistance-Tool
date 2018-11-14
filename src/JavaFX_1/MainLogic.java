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

/*
COMMIT TO DO:
    -change status color on commit
    ------make so TUs don't match themselves
    - change selection on commit
    - add key press to commit
    - make English cells editable again

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
*/

/**
 *
 * @author Chris
 */
public class MainLogic {
    
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
        mainFile.commitAllTUs();
        
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

    void commit(TUEntry selectedTU) {
        // Changes the committed status of this TU
        if (selectedTU != null) {
             selectedTU.setCommitted(true);
        }
        
        // Finds all matches with this newly committed TU
        Comparator c = new Comparator(getCurrentCompareString(), selectedTU, getMinMatchLength());
        // adds these matches to the current compareFile.
        for (TUCompareEntry tu : c.getCompareFile().getObservableList()) {
            currentCompareFile.addEntry(tu);
        }
    }
}
