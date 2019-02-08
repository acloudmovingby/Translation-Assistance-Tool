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
    
}
