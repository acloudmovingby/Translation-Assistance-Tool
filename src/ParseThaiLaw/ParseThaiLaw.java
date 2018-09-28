/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParseThaiLaw;

import java.util.Arrays;

/**
 *
 * @author Chris
 */
public class ParseThaiLaw {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleThaiLaw1.txt";
        String fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
        
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT.txt";
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";

        ThaiLawParser myParser = new ThaiLawParser(fileNameThai, fileNameEng);
        
        
     
    }
}
