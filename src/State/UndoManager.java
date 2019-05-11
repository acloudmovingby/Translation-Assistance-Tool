/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.Segment;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stores prior versions of the MainFile to allow for undo/redo capability.
 *
 * @author Chris
 */
public class UndoManager {

    /**
     * A stack representing the prior states of the program. When undo is
     * performed, the top of the stack is popped and added to the redoStack.
     */
    private final Deque<BasicFile> undoStack;

    /**
     * A stack representing "future" MainFile states that have been undone but
     * could be redone (redo-ed ?).
     */
    private final Deque<BasicFile> redoStack;

    public UndoManager() {
        undoStack = new ArrayDeque();
        redoStack = new ArrayDeque();
    }

    /**
     * Stores the current state (currently just the MainFile), so that if undo
     * is called the state can be restored to this version.
     *
     * Note: THIS CLEARS REDOSTACK. UndoManager does not support branching file
     * history, so if undo is performed and then a new state is pushed onto the
     * undo stack, the redo is cleared (so no redo can then be performed).
     *
     * @param file
     */
    protected void push(State state) {
        undoStack.offerFirst(new BasicFile(state.getMainFile()));
        redoStack.clear();
    }

    /**
     * Restores the prior stored state, replacing the segments in the main file
     * with the last stored version. Does not change the mainFile object itself,
     * but rather operates through the State object's access methods
     * (addSegment, addToHidden, removeSegment). This ensures UndoManager
     * doesn't have to worry about what data in State depends on the MF (such as
     * the postings lists) and the state updates those automatically.
     *
     * @param state
     */
    protected void executeUndo(State state) {
        // if no prior states have been stored, do nothing
        if (undoStack.isEmpty()) {
            return;
        }

        // add the current state to the redo stack
        redoStack.offerFirst(new BasicFile(state.getMainFile()));

        // get the previous one
        BasicFile previousMainFile = undoStack.pollFirst();

        // replace the main file
        replaceMainFileInState(previousMainFile, state);

    }

    protected void executeRedo(State state) {
        // if no prior states have been stored, do nothing
        if (redoStack.isEmpty()) {
            return;
        }

        // add the current state to the undo stack
        undoStack.offerFirst(new BasicFile(state.getMainFile()));

        // get the "next" state (i.e what we are re-doing into)
        BasicFile nextRedoFile = redoStack.pollFirst();

        // replace the main file
        replaceMainFileInState(nextRedoFile, state);
    }

    /**
     * MUTATES STATE. This replaces the MainFile object stored in State with the
     * specified MainFile.
     *
     * @param newFile
     * @param state
     */
    private void replaceMainFileInState(BasicFile newFile, State state) {
        BasicFile mainFileInState = state.getMainFile();

        List<Segment> allCurrentSegs = new ArrayList();
        mainFileInState.getAllSegs().forEach((s) -> {
            allCurrentSegs.add(Segment.getDeepCopy(s));
        });

        List<Segment> previousActiveSegs = new ArrayList();
        newFile.getActiveSegs().forEach((s) -> {
            previousActiveSegs.add(Segment.getDeepCopy(s));
        });

        ArrayList<Segment> previousHiddenSegs = new ArrayList();
        newFile.getHiddenSegs().forEach((s) -> {
            previousHiddenSegs.add(Segment.getDeepCopy(s));
        });

        // removes all segs from main file
        allCurrentSegs.forEach(s -> {
            state.removeSegFromFile(s, state.getMainFile());
        });

        // add active segs to MainFile
        for (int i = 0; i < previousActiveSegs.size(); i++) {
            state.addSegToFileActiveList(i, previousActiveSegs.get(i), state.getMainFile());
        }

        // add hidden segs to MainFile
        for (int i = 0; i < previousHiddenSegs.size(); i++) {
            state.addToMainFileHidden(previousHiddenSegs.get(i), state.getMainFile());
        }
    }
}
