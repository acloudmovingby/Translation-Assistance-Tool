/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndoManager;

import Files.BasicFile;
import Files.Segment;
import State.State;
import java.util.ArrayDeque;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Keeps track of recent undoable events and executes the proper commands to
 * State if undo is called.
 *
 * @author Chris
 */
public class UndoManager {

    /**
     * Previous states of program.
     */
    private ArrayDeque<BasicFile> mainFileHistory;

    public UndoManager() {
        mainFileHistory = new ArrayDeque();
    }

    /**
     * Adds a state to the stack (history of prior states).
     * @param state 
     */
    public void pushState(State state) {
        System.out.println("pushState");
        BasicFile mainFile = state.getMainFile();
        BasicFile copyOfMF = copyBasicFile(mainFile);
        mainFileHistory.add(copyOfMF);
    }

    /**
     * Returns a prior mainFile
     * @param state 
     */
    public void undo(State state) {
        System.out.println("undo in UM");
        if (!mainFileHistory.isEmpty()) {
            System.out.println("history not empty");
            BasicFile pop = mainFileHistory.pop();
            state.setMainFile(pop);
        }
    }
    
    /**
     * Makes a "deep" copy of a BasicFile object.
     * @param file
     * @return 
     */
    protected static BasicFile copyBasicFile(BasicFile file) {
         // copy active segments
        ObservableList<Segment> copyOfActiveSegs = FXCollections.observableArrayList();
        for (Segment s : file.getActiveSegs()) {
            Segment newSeg = new Segment(s.getFileID());
            newSeg.setFileName(s.getFileName());
            newSeg.setID(s.getID());
            newSeg.setThai(s.getThai());
            newSeg.setEnglish(s.getEnglish());
            newSeg.setCommitted(s.isCommitted());
            newSeg.setRemoved(s.isRemoved());
            newSeg.setRank(s.getRank());
            copyOfActiveSegs.add(newSeg);
        }
        
        // makes copy of file and adds copied segments
        BasicFile copy = new BasicFile(file.getFileID(), file.getFileName());
        copy.getActiveSegs().addAll(copyOfActiveSegs);
        
        return copy;
    }
}
