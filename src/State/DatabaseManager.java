/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import Database.DatabaseOperations;

/**
 *
 * @author Chris
 */
public class DatabaseManager {
    
    BasicFile mainFile;
    
    public DatabaseManager(BasicFile mainFile) {
        this.mainFile = mainFile;
    }
     
    protected void push(State state) {
        // not yet implemented
    }
    
     
    
}
