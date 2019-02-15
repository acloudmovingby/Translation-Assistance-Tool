/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Chris
 */
public class UndoManager {
    
    private final Deque stateStack;
    
    public UndoManager() {
        stateStack = new ArrayDeque();
    }

    protected void push(State state) {
        BasicFile save = state.getMainFile();
        // make copies of active segs and remove segs
    }

    protected State pop() {
        // take everything in state's active segs an put it in removed segs
        // add the stored removed segs to that list
        // add the stored actives to the active list
        
        return null;
    }
    
}
