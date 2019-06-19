/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.TranslationFile;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import UserActions.MutateFileAction;

/**
 * Receives all user input from the Controller and delegates these tasks to various components that then work directly with the State data. 
 *
 * @author Chris
 */
public class Dispatcher {

    final DatabaseManager databaseManager;
    final State state;
    final UndoManager undoManager;

    public Dispatcher(State state) {
        this.databaseManager = new DatabaseManager();
        this.state = state;
        this.undoManager = new UndoManager();
        
    }

    public void acceptAction(MutateFileAction a) {
        undoManager.push(state); // current state is stored for undo functionality
        a.execute(state); //action executes, affecting state
        databaseManager.push(state); // new state is pushed to database
    }

    public void undo() {
        undoManager.executeUndo(state); // takes state and replaces the main file with the prior stored version in UndoManager
        databaseManager.push(state); // new state is pushed to database
    }

    public void redo() {
        undoManager.executeRedo(state);
        databaseManager.push(state);
    }

    /**
     * This is pushed up through dispatcher so Controller can access it, but its data depends on State so it is held by State.
     * @return 
     */
    public UIState getUIState() {
        return state.getUIState();
    }

    public State getState() {
        return state;
    }

    /**
     * When a new selection is made in main file viewer, the match table is
     * reset with a new list of matches
     *
     * @param selectedSeg
     */
    public void newSelection(Segment selectedSeg) {
        state.setCurrentSelectedSeg(selectedSeg);
        if (selectedSeg == null) {
            // if the selection is null, this just puts an empty list in the table (no matches are displayed)
            getUIState().setMatchList(FXCollections.observableArrayList());
        } else {
            getUIState().setMatchList(findMatch(selectedSeg));
        }
    }

    public void setMinLength(int newMinLength) {
        state.setMinLength(newMinLength);
        getUIState().setMatchList(
                findMatch(state.getCurrentSelectedSeg()));
    }

    /**
     * Exports to an external .txt file all the English from all committed Segs
     * in the main file.
     */
    public void exportCommittedSegs() {

        PrintWriter out = null;

        try {
            String FILENAME = "TranslationProgramExport.txt";
            out
                    = new PrintWriter(
                            new BufferedWriter(
                                    new FileWriter(FILENAME)));

            for (Segment seg : state.getMainFile().getActiveSegs()) {
                if (seg.isCommitted()) {
                    out.println(seg.getEnglish());
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    /**
     * Finds matching segments according to default match finding algorithm
     *
     * @param seg
     * @return MatchList with matching segments
     */
    private List<MatchSegment> findMatch(Segment seg) {
        if (seg == null) {
            return new ArrayList();
        } else {
            return state.getMatchManager().basicMatch(seg, state.getMinMatchLength());
        }
    }
    
    /**
     * This sets the main file for the program, both changing the main file pointer in state as well as updating the UIState so it correctly shows the main file segments in the translation tab.
     * @param file 
     */
    public void setMainFile(TranslationFile file) {
        System.out.println("dispatcher called");
        // set state's main file
        state.setMainFile(file);
        
        // reset UIState properties
        getUIState().setMainFileSegs(state.getMainFile().getActiveSegs());
        getUIState().setMainFileName(state.getMainFile().getFileName());
    }

}
