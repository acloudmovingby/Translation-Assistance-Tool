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
        
        // BOOK 1
        String fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleThaiLaw1.txt";
        String fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";
        
        // BOOK 2
        //fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 2.txt";
        //fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 2-1.txt";

        // BOOK 3 - THAI-SQR, ENG-SQR 
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-SQ.txt";
        fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
        
        
        // BOOK 3 - THAI-AUTO, ENG-SQR
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
        fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
       /*
        
        
        // BOOK 3 - THAI-AUTO, ENG-AUTO
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
        fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-AUTO.txt";
        
        // BOOK 3 - THAI-SQR, ENG-AUTO
        fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-SQ.txt";
        fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-AUTO.txt";
      */
        ThaiLawParser myParser = new ThaiLawParser(fileNameThai, fileNameEng);
        
        
     
    }
}
