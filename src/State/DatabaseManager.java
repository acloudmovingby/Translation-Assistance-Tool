/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.MainFile;
import DataStructures.Segment;
import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Is responsible for backing up the state to the database. 
 * @author Chris
 */
public class DatabaseManager {
    
    MainFile priorBackup;
    
    public DatabaseManager(State state) {
        priorBackup = new MainFile(state.getMainFile());
        DatabaseOperations.addFile(priorBackup);
    }
     
    protected void push(State state) {
        backupMainFile(state.getMainFile());
    }
    
    protected void backupMainFile(MainFile mf) {
        
        // if the priorBackup matches the one that DatabaseManager was originally asssigned, then back it up
        if (mf.getFileID() == priorBackup.getFileID()) {
            // after updating/adding segs, then any segs that are now missing need to be removed.
            if (DatabaseOperations.addFile(mf)) {
                findMissingSegs(mf, priorBackup).forEach((s) -> {
                    DatabaseOperations.removeSeg(s.getID());
                });
            }
        } else {
            throw new IllegalArgumentException("MainFile does not match the one registered with DatabaseManager at construction.");
        }
    }

    /*
     Finds what segments existed in priorMainFile that now don't exist in newMainFile.
    Returns this as a list.
    */
    private List<Segment> findMissingSegs(MainFile priorMainFile, MainFile newMainFile) {
        // if it exists in priorbackup, but doesn't exist in mf, then add to list
        List<Segment> mainFileSegs = newMainFile.getAllSegs();
        List<Segment> missingSegs = new ArrayList();
        
        priorMainFile.getAllSegs().stream()
                .filter((s) -> (!mainFileSegs.contains(s)))
                .forEachOrdered((s) -> {missingSegs.add(s);
        });
        return missingSegs;
    }
     
    
}
