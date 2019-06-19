/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import DataStructures.TranslationFile;
import DataStructures.FileBuilder;
import ParseThaiLaw.ThaiLawParser;

/**
 * Deletes and recreates the database named "database1.db". Then it builds a
 * corpus from several Thai legal documents and adds to this database.
 *
 * @author Chris
 */
public class RebootDatabaseAndBuildCorpus {

    public static void build() {

            String thaiFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 3-AUTO.txt";
            String engFile1 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 3-SQ.txt";
            TranslationFile file1 = (new ThaiLawParser(thaiFile1, engFile1)).makeFile();

            file1.setFileName("Civil and Commercial Code: Book 3");
            DatabaseOperations.addFile(file1);
            

            String thaiFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 2.txt";
            String engFile2 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Eng Book 2-1.txt";
            TranslationFile file2 = (new ThaiLawParser(thaiFile2, engFile2)).makeFile();
            file2.commitAllSegs();
            DatabaseOperations.addFile(file2);

            String thaiFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/Thai Book 1TXT2.txt";
            String engFile3 = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";
            TranslationFile file3 = (new ThaiLawParser(thaiFile3, engFile3)).makeFile();
            file3.commitAllSegs();
            DatabaseOperations.addFile(file3);
             
 
            //  BUILD FROM TMX FILES
             
            String tmxFilePath1 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk5.csv-650387.tmx";
            TranslationFile tmxParse1 = FileBuilder.parseTMX(tmxFilePath1);
            DatabaseOperations.addFile(tmxParse1);

            String tmxFilePath2 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk6.csv-650403.tmx";
            TranslationFile tmxParse2 = FileBuilder.parseTMX(tmxFilePath2);
            DatabaseOperations.addFile(tmxParse2);

            String tmxFilePath3 = "/Users/Chris/Desktop/Docs/Documents/Work:Financial/Translation Work/2017 Jobs/2:13:17 VENGA #52/Translation : TERM BASE April 20/th_en_batch_1_fixed_chunk8.csv-650422.tmx";
            TranslationFile tmxParse3 = FileBuilder.parseTMX(tmxFilePath3);
            DatabaseOperations.addFile(tmxParse3);
             
        
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
