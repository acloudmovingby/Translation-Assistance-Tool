package State;

import DataStructures.TranslationFile;
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
    
    /**
     * Keys are file ids, values are the previously backed up version of that file 
     */
    private final HashMap<Integer,TranslationFile> priorFileVersions;

    public DatabaseManager() {
        priorFileVersions = new HashMap();
    }

    /**
     * Backs up the current program state to the database. 
     * 
     * Currently only backs up the Main File. 
     * 
     * @param state 
     */
    protected final void push(State state) {
        backupFile(state.getMainFile());
    }

    /**
     * Backs up the given file to the database. 
     * 
     * @param file 
     */
    private void backupFile(TranslationFile file) {
        TranslationFile priorFileVersion = getPriorFileVersion(file.getFileID());
        updateFileInDatabase(priorFileVersion, file);
    }
    
    /**
     * If the DatabaseManager has stored a prior version in its hashtable, it returns that, otherwise retrieves whatever is in the database.
     * @param fileID
     * @return The last updated version of the file with the given id.
     */
    private TranslationFile getPriorFileVersion(int fileID) {
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
    private void updateFileInDatabase(TranslationFile priorFileVersion, TranslationFile file) {
        if (DatabaseOperations.addFile(file)) {
                findMissingSegs(priorFileVersion, file).forEach((s) -> {
                    DatabaseOperations.removeSeg(s.getID());
                });
            }
    }
    
    /**
     * Finds what segments previously existed in the file but now no longer do.
     * 
     * @param priorFileVersion Previous version of the file.
     * @param file Current version of the file.
     * @return Segments that use to exist in file but now don't.
     */
    private List<Segment> findMissingSegs(TranslationFile priorFileVersion, TranslationFile file) {
        // if it exists in priorbackup, but doesn't exist in mf, then add to 'missing segs' list
        List<Segment> mainFileSegs = file.getAllSegs();
        List<Segment> missingSegs = new ArrayList();

        priorFileVersion.getAllSegs().stream()
                .filter((s) -> (!mainFileSegs.contains(s)))
                .forEachOrdered((s) -> {
                    missingSegs.add(s);
                });
        return missingSegs;
    }
    
}
