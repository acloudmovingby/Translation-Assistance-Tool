/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import DataStructures.Segment;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javafx.collections.ObservableList;

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
        
        
        
        ////////
        List<Segment> allCurrentSegs = mainFileInState.getAllSegs();
        ObservableList<Segment> previousActiveSegs = previousMainFile.getActiveSegs();
        ArrayList<Segment> previousHiddenSegs = previousMainFile.getHiddenSegs();
        
        // removes all segs from main file
        allCurrentSegs.forEach(s -> {state.removeSeg2(s);});
        
        // rebuild active segs
        for (int i=0; i<previousActiveSegs.size(); i++) {
            state.addSeg(i, previousActiveSegs.get(i));
        }
        
        // rebuild hidden segs
        for (int i=0; i<previousHiddenSegs.size(); i++) {
            state.addSeg(i, previousHiddenSegs.get(i));
        }
        
        ///////////
        
        /*
        // replace all active segs
        mainFileInState.getActiveSegs().clear();
        for (Segment s : previousMainFile.getActiveSegs()) {
        mainFileInState.getActiveSegs().add(s);
        }
        // replace all hidden segs
        mainFileInState.getHiddenSegs().clear();
        for (Segment s : previousMainFile.getHiddenSegs()) {
        mainFileInState.getHiddenSegs().add(s);
        }
         */
       
    }
    
}
