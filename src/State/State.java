/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import DataStructures.PostingsList;
import DataStructures.Segment;


/**
 *
 * @author Chris
 */
public interface State {
    
    void resetMainFile(State state);
    
    public UIState getUIState();
    
    public void split(Segment seg, int index);
    
    PostingsList getPostingsList(int nGramLength);
    
    public MainFile getMainFile();

    public PostingsListManager getPostingsListManager();
    
    /**
     * Takes the "oldSeg" from the MainFile's active segs list and replaces it. If MF's active segs list does not contain the specified Segment, it returns false and nothing changes. If it does, the MainFile and PostingsListManager adjust appropriately. 
     * @param oldSeg
     * @param newSeg 
     * @return  True if seg exists in MF active. False if not.
     */
    public boolean replaceSeg(Segment oldSeg, Segment newSeg);

    public void addSeg(int i, Segment newSeg2);
    
}
