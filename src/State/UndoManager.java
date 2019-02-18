/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import DataStructures.Segment;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 
 * @author Chris
 */
public class UndoManager {
    
    private final Deque<MainFile> priorMainFiles;
    
    public UndoManager() {
        priorMainFiles = new ArrayDeque();
    }

    protected void push(State state) {
        priorMainFiles.offerFirst(new MainFile(state.getMainFile()));
    }

    /**
     * Returns the prior mainFile stored in UndoManager's stack. If no prior main files are in the stack, returns null.
     * @return 
     */
    protected MainFile popPriorMainFile() {
        return priorMainFiles.pollFirst();
    }

    protected void restorePriorMainFile(State state) {
        // if no prior states have been stored, do nothing
        if (priorMainFiles.isEmpty()) {
            return;
        }
        
        // get the current main file and the previous one
        MainFile previousMainFile = priorMainFiles.pollFirst();
        MainFile mainFileInState = state.getMainFile();
        
        // replace all active segs
        mainFileInState.getActiveSegs().clear();
        for (Segment s : previousMainFile.getActiveSegs()) {
            mainFileInState.getActiveSegs().add(s);
        }
        
        // replace all removed segs
        mainFileInState.getHiddenSegs().clear();
        for (Segment s : previousMainFile.getHiddenSegs()) {
            mainFileInState.getHiddenSegs().add(s);
        }
        
        // need to make distinction between hidden and removed
        
        
       
    }
    
}
