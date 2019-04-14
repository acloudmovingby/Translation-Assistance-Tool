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
     * Changes the state, replacing the segments in the main file with the last stored version. Does not change the mainFile object itself, but rather operates through the State object's access methods (addSegment, addToHidden, removeSegment). This ensures UndoManager doesn't have to worry about what data in State depends on the MF (such as the postings lists) and the state updates those automatically.  
     * @param state 
     */
    protected void restorePriorMainFile(State state) {
        // if no prior states have been stored, do nothing
        if (priorMainFiles.isEmpty()) {
            return;
        }
        
        // get the current main file and the previous one
        MainFile previousMainFile = priorMainFiles.pollFirst();
        MainFile mainFileInState = state.getMainFile();
        
        
        
        ////////
        
        List<Segment> allCurrentSegs = new ArrayList();
        mainFileInState.getAllSegs().forEach((s) -> {
            allCurrentSegs.add(Segment.getDeepCopy(s));
        });
        
        List<Segment> previousActiveSegs = new ArrayList();
        previousMainFile.getActiveSegs().forEach((s) -> {
            previousActiveSegs.add(Segment.getDeepCopy(s));
        });
        
        ArrayList<Segment> previousHiddenSegs = new ArrayList();
        previousMainFile.getHiddenSegs().forEach((s) -> {
            previousHiddenSegs.add(Segment.getDeepCopy(s));
        });
        
        // removes all segs from main file
        
        allCurrentSegs.forEach(s -> {state.removeSeg2(s);});
        
        // add active segs to MainFile
        for (int i=0; i<previousActiveSegs.size(); i++) {
            state.addSeg(i, previousActiveSegs.get(i));
        }
        
        // add hidden segs to MainFile
        for (int i=0; i<previousHiddenSegs.size(); i++) {
            state.addToHidden(previousHiddenSegs.get(i));
        }
       
    }
    
}
