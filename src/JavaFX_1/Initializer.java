/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.FileBuilder;
import Database.DatabaseOperations;
import State.DatabaseManager;
import State.Dispatcher;
import State.State;
import State.StateWithDatabase;
import State.UIState;
import State.UndoManager;

/**
 *
 * @author Chris
 */
public class Initializer {
    
    private final Dispatcher d;
    private final StateWithDatabase state;
    private final DatabaseManager dm;
    
    protected Initializer() {
        // retrieves all files previously stored in database
        Corpus corpus = DatabaseOperations.getAllSegments();
        
        // builds a main file from some random Thai document
        FileBuilder fileBuilder = new FileBuilder();
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        BasicFile mainFile = fileBuilder.justThaiFilePath(filePath);
        
        // adds main file to corpus
        corpus.addFile(mainFile);
        
        // creates State object
        state = new StateWithDatabase(mainFile, corpus);
        
        dm = new DatabaseManager(state.getMainFile());
        d = new Dispatcher(dm, state, new UndoManager());
    }
    
    protected UIState getUIState() {
        return state.getUIState();
    }
    
    protected Dispatcher getDispatcher() {
        return d;
    }
        
    protected StateWithDatabase getState() {
        return state;
    }
    
}
