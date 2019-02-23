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
     * Changes the state, replacing the segments in the main file with the last stored version. Does not change the mainFile object.
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
        for (Segment s : mainFileInState.getAllSegs()) {
            allCurrentSegs.add(Segment.getDeepCopy(s));
        }
        
        List<Segment> previousActiveSegs = new ArrayList();
        for (Segment s : previousMainFile.getActiveSegs()) {
            previousActiveSegs.add(Segment.getDeepCopy(s));
        }
        
        ArrayList<Segment> previousHiddenSegs = new ArrayList();
        for (Segment s : previousMainFile.getHiddenSegs()) {
            previousHiddenSegs.add(Segment.getDeepCopy(s));
        }
        
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
