/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProgramAnalysisNotImportant;

import DataStructures.TranslationFile;
import Database.DatabaseOperations;
import comparator.PostingsList;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class PostingsListSize {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
        ArrayList<TranslationFile> allFiles = DatabaseOperations.getAllFiles();
        int numSegs = TranslationFile.getAllCommittedSegsInFileList(allFiles).size();
        int totalSourceLength = TranslationFile.getAllCommittedSegsInFileList(allFiles).stream()
                .mapToInt(a -> a.getThai().length())
                .sum();
        System.out.println("numSegs = " + numSegs);
        System.out.println("totalSourceLength = " + totalSourceLength);
        
        PostingsList pl1 = new PostingsList(3);
        pl1.addMultipleSegments(TranslationFile.getAllCommittedSegsInFileList(allFiles));
        System.out.println("pl2 total ngrams = " + pl1.getMap().keySet().size());
        System.out.println("pl2 total segment pointers = " + pl1.getMap().values().stream().mapToInt(a -> a.size()).sum());  
        
        PostingsList pl2 = new PostingsList(2);
        pl2.addMultipleSegments(TranslationFile.getAllCommittedSegsInFileList(allFiles));
        System.out.println("pl2 total ngrams = " + pl2.getMap().keySet().size());
        System.out.println("pl2 total segment pointers = " + pl2.getMap().values().stream().mapToInt(a -> a.size()).sum());  
        
        PostingsList pl3 = new PostingsList(1);
        pl3.addMultipleSegments(TranslationFile.getAllCommittedSegsInFileList(allFiles));
        System.out.println("pl2 total ngrams = " + pl3.getMap().keySet().size());
        System.out.println("pl2 total segment pointers = " + pl3.getMap().values().stream().mapToInt(a -> a.size()).sum());  
  
        
}
    
}
