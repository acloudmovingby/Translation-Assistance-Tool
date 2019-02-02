/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import DataStructures.BasicFile;
import DataStructures.FileBuilder;
import State.StateWithDatabase;
import ParseThaiLaw.ThaiLawParser;

/**
 *
 * @author Chris
 */
public class BuildCorpus {

    public static void build() {
        if (!StateWithDatabase.databaseIsReadable()) {
            System.out.println("Database not active. Corpus not built.");
        } else {

            String thaiFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
            String engFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
            BasicFile file1 = (new ThaiLawParser(thaiFile1, engFile1)).makeFile();
            file1.commitAllSegs();
            //DatabaseOperations.addFile(file1);
            DatabaseOperations.addFile(file1);
            //file1.setFileName("Civil and Commercial Code: Book 3");
/*
            String thaiFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 2.txt";
            String engFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 2-1.txt";
            BasicFile file2 = (new ThaiLawParser(thaiFile2, engFile2)).makeFile();
            file2.commitAllSegs();
            //DatabaseOperations.addFile(file2);
            DatabaseOperations.addFile(file2);

            String thaiFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";
            String engFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
            BasicFile file3 = (new ThaiLawParser(thaiFile3, engFile3)).makeFile();
            file3.commitAllSegs();
            //DatabaseOperations.addFile(file3);
            DatabaseOperations.addFile(file3);
*/
            /*
        BUILD FROM TMX FILES
             
            String tmxFilePath1 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk5.csv-650387.tmx";
            BasicFile tmxParse1 = FileBuilder.parseTMX(tmxFilePath1);
            //DatabaseOperations.addFile(tmxParse1);
            DatabaseOperations.addFile(tmxParse1);

            String tmxFilePath2 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk6.csv-650403.tmx";
            BasicFile tmxParse2 = FileBuilder.parseTMX(tmxFilePath2);
            //DatabaseOperations.addFile(tmxParse2);
            DatabaseOperations.addFile(tmxParse2);

            String tmxFilePath3 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk8.csv-650422.tmx";
            BasicFile tmxParse3 = FileBuilder.parseTMX(tmxFilePath3);
            //DatabaseOperations.addFile(tmxParse3);
            DatabaseOperations.addFile(tmxParse3);
*/
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeleteTable.deleteAllTables();
        CreateTable.createTables();
        build();
    }

}
