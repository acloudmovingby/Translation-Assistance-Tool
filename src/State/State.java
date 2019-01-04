/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Database.PostingsList;
import Files.BasicFile;
import Files.Corpus;
import Files.MatchFile;
import Files.MatchSegment;
import Files.Segment;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * Represents state of program. Must contain a corpus and main file. Also caches
 * @author Chris
 */
public interface State {
    
    
    BasicFile getMainFile();
    
    void setMainFile(BasicFile bf);
    
    ObservableList<Segment> getMainFileSegs();
    
    Corpus getCorpus();
    
    //void setCorpus(Corpus corpus);
    
    MatchFile getMatchFile();
    
    ObservableList<MatchSegment> getMatchList();
    
    void newSelection(Segment s);
    
    int getMinMatchLength();
    
    void setMinLength(int minLength);
    
    void exportCommittedTUs();
    

    /**
     * Returns a postings list for ngrams of length k;
     * @param k
     * @return 
     */
    PostingsList getPostingsList(int k);

    public void search(String text);
    
    public void acceptAction(UserAction a);
    
    

    
}
