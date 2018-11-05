/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Files.BasicFile;
import Files.FileFactory;
import Files.FileList;
import ParseThaiLaw.ThaiLawParser;

/**
 *
 * @author Chris
 */
public class MainLogic {
    
    /**
     * The file currently being translated.
     */
    BasicFile file1;
    
     /**
     * The corpus where matches are found.
     */
    FileList corpus;
    
    MainLogic() {
        // makes main file
        FileFactory ff = new FileFactory();
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        file1 = ff.justThaiFilePath(filePath);
        file1.setFileName("Main File");
        
         // makes corpus (list of files to find matches in)
        corpus = new FileList();
        corpus.addFile(file1);
        String thaiFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
        String engFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
        corpus.addFile((new ThaiLawParser(thaiFile1, engFile1)).makeFile());
        String thaiFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 2.txt";
        String engFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 2-1.txt";
        corpus.addFile((new ThaiLawParser(thaiFile2, engFile2)).makeFile());
        String thaiFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";
        String engFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
        corpus.addFile((new ThaiLawParser(thaiFile3, engFile3)).makeFile());
    }
}
