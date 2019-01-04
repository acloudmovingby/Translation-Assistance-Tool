/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Database.DatabaseOperations;
import State.State;

/**
 * Handles events that require backing up to the database. Multithreaded to improve UI speed. 
 * @author Chris
 */
public class BackupManager {
    
    public void backupState(State state) {
        DatabaseOperations.addFile(state.getMainFile());
    }
    
}
