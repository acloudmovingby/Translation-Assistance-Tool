package State;

import DataStructures.BasicFile;
import DataStructures.MainFile;
import DataStructures.Segment;
import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Is responsible for backing up the state to the database. SHOULD ONLY BE ONE OF THESE PER PROGRAM (SINGLETON).
 * 
 * This class stores "prior" versions of files so it doesn't waste time on SQL lookups. Because of that, it's important that multiple DatabaseManagers are not used, otherwise the file may backup incorrectly and become corrupted.
 *
 * This is arguably bad code and can be fixed later (possibly by figuring out better ways to manage the SQL and/or using multiple threads where it only backs up changes once every 30 seconds).
 * 
 * @author Chris
 */
public class DatabaseManager {
    
    HashMap<Integer,BasicFile> priorFileVersions;

    public DatabaseManager(State state) {
        priorFileVersions = new HashMap();
        push(state);
    }

    /**
     * Backs up the current program state to the database. 
     * 
     * Currently only backs up the Main File. 
     * 
     * @param state 
     */
    final protected void push(State state) {
        backupFile(state.getMainFile());
    }
    
    /*
     Finds what segments existed in priorMainFile that now don't exist in currentMainFile.
    Returns this as a list.
     */
    private List<Segment> findMissingSegs(BasicFile priorMainFile, BasicFile currentMainFile) {
        // if it exists in priorbackup, but doesn't exist in mf, then add to 'missing segs' list
        List<Segment> mainFileSegs = currentMainFile.getAllSegs();
        List<Segment> missingSegs = new ArrayList();

        priorMainFile.getAllSegs().stream()
                .filter((s) -> (!mainFileSegs.contains(s)))
                .forEachOrdered((s) -> {
                    missingSegs.add(s);
                });
        return missingSegs;
    }

    /**
     * Backs up the given file to the database. 
     * 
     * @param file 
     */
    public void backupFile(BasicFile file) {
        BasicFile priorFileVersion = getPriorFileVersion(file.getFileID());
        updateFileInDatabase(priorFileVersion, file);
    }
    
    /**
     * If the DatabaseManager has stored a prior version in its hashtable, it returns that, otherwise retrieves whatever is in the database.
     * @param fileID
     * @return The last updated version of the file with the given id.
     */
    private BasicFile getPriorFileVersion(int fileID) {
        if (priorFileVersions.containsKey(fileID)) {
            return priorFileVersions.get(fileID);
        } else {
            return DatabaseOperations.getFile(fileID);
        }
    }
   
    /**
     * Adds the file to the database and then removes from the database any Segments that were in the priorFileVersion but not in the file.
     * 
     * When you call "addFile" in DatabaseOperations, it just adds the segments. The database has no way of knowing that some of the segments in the file no longer exist. This method ensures that the database is then updated correctly with no-longer-existent Segments removed. 
     * 
     * @param priorFileVersion
     * @param file 
     */
    private void updateFileInDatabase(BasicFile priorFileVersion, BasicFile file) {
        if (DatabaseOperations.addFile(file)) {
                findMissingSegs(priorFileVersion, file).forEach((s) -> {
                    DatabaseOperations.removeSeg(s.getID());
                });
            }
    }
    
}
