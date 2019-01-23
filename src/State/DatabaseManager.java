/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import Database.DatabaseOperations;

/**
 * Is responsible for backing up the state to the database. 
 * @author Chris
 */
public class DatabaseManager {
    
    MainFile mainFile;
    
    public DatabaseManager(State state) {
        mainFile = state.getMainFile();
        DatabaseOperations.addFile(mainFile);
    }
     
    protected void push(State state) {
        backupMainFile(state.getMainFile());
    }
    
    protected void backupMainFile(MainFile mf) {
        
        if (!mf.equals(mainFile)) {
            throw new IllegalArgumentException("MainFile does not match the one registered with DatabaseManager.");
        }
        
        DatabaseOperations.addFile(mf);
    }
     
    
}
