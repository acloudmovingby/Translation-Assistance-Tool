package Database;

import DataStructures.TranslationFile;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The core class for writing/reading to the SQLite database.
 *
 * @author Chris
 */
public class DatabaseOperations {

    /**
     * ***********************************************************************
     *
     * Methods that can WRITE to the database
     *
     *************************************************************************
     */
    /**
     * Adds all Segment entries contained in the file to the database or updates
     * them if they already exist. If there are Segment entries matching this
     * fileID already in the database but are not in the current file, they are
     * NOT deleted. (In other words, Segment entries can be added or updated via
     * this method, but never removed from the database).
     *
     * @param bf
     * @return True if all Segments are added successfully. If file is null or
     * there is an SQL error, returns false.
     */
    public static boolean addFile(TranslationFile bf) {
        System.out.println("File added to database.");
        DatabaseOperations.addOrUpdateFileName(bf.getFileID(), bf.getFileName());
        String sql = "INSERT OR REPLACE INTO corpus1(id, fileID, fileName, thai, english, committed, removed, rank) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            // this makes sure that the segments in the file, when retrieved from the db, can be ordered in the proper order.
            // simply increments by 1 on each segment. 
            int rank = 0;
            // keeps count of the number of segs added so that the SQL can run a batch transaction (which is much more efficient than individual transactions).
            // when i=1000, or at the last segment, the SQL is then run as one batch transaction.
            int i = 0;

            for (Segment seg : bf.getActiveSegs()) {

                pstmt.setDouble(1, seg.getID());
                pstmt.setDouble(2, seg.getFileID());
                pstmt.setString(3, seg.getFileName());
                pstmt.setString(4, seg.getThai());
                pstmt.setString(5, seg.getEnglish());
                // committed/removed booleans are stored as binary (0 = false, 1 = true)
                pstmt.setInt(6, seg.isCommitted() ? 1 : 0);
                // removed is set to "false"
                pstmt.setInt(7, 0);
                pstmt.setInt(8, rank);
                pstmt.addBatch();
                rank++;
                i++;

                if (i % 1000 == 0 || i == bf.getActiveSegs().size()) {
                    pstmt.executeBatch(); //Execute every 1000 segments.
                }
            }

            // resetting counters
            i = 0;
            rank = 0;
            for (Segment seg : bf.getHiddenSegs()) {

                pstmt.setDouble(1, seg.getID());
                pstmt.setDouble(2, seg.getFileID());
                pstmt.setString(3, seg.getFileName());
                pstmt.setString(4, seg.getThai());
                pstmt.setString(5, seg.getEnglish());
                // committed/removed booleans are stored as binary (0 = false, 1 = true)
                pstmt.setInt(6, seg.isCommitted() ? 1 : 0);
                // removed is set  to "true"
                pstmt.setInt(7, 1);
                pstmt.setInt(8, rank);
                pstmt.addBatch();
                rank++;
                i++;

                if (i % 1000 == 0 || i == bf.getHiddenSegs().size()) {
                    pstmt.executeBatch(); //Execute every 1000 segments.
                }
            }
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("AddFileAsBatch: " + e.getMessage());
            return false;
        }
    }

    /**
     * If said file does not exist in the database, it adds the id and name. If
     * the id already exists, it updates the name.
     *
     * @param fileID
     * @param fileName
     * @return True if added successfully, false if an SQLException is thrown.
     */
    public static boolean addOrUpdateFileName(double fileID, String fileName) {
        String sql = "INSERT OR REPLACE INTO files(fileID, fileName) VALUES(?,?)";

        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, fileID);
            pstmt.setString(2, fileName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("addOrUpdateFileName: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a fileID for the given fileName and adds to the database.
     *
     * @param fileName The name of the file.
     * @return A new file ID that doesn't exist in the database
     */
    public static int createFileID(String fileName) {

        int fileID;

        fileID = 0;

        // checks to see if the curent fileID is in database. If yes, then it generates a random new one until a new one is found. 
        while (containsFileID(fileID)) {
            fileID = (int) (Math.random() * 100000000);
        }
        // adds this id/name pairing to the database
        addOrUpdateFileName(fileID, fileName);
        System.out.println("CreateFileID: " + fileID + ", " + fileName);
        // returns the id so the TranslationFile object can store it.

        return fileID;
    }

    /**
     * Removes the Segment with the specified id from the database
     *
     * @param id
     * @return True if removed successfully with no SQL errors.
     */
    public static boolean removeSeg(double id) {

        String sql = "DELETE FROM corpus1 WHERE id = ?";

        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("removeSeg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Removes the file with the specified id from the database
     *
     * @param fileId
     * @return True if removed successfully with no SQL errors.
     */
    public static boolean removeFile(double fileId) {

        String sql = "DELETE FROM corpus1 WHERE fileId = ?";

        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, fileId);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("removeSeg: " + e.getMessage());
            return false;
        }
        
        sql = "DELETE FROM files WHERE fileID = ?";
        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, fileId);
            // execute the delete statement
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("removeSeg: " + e.getMessage());
            return false;
        }
    }

    /**
     * ***********************************************************************
     *
     * Methods that READ from database
     *
     *************************************************************************
     */
    public static int numberOfSegs() {
        String sql = "SELECT COUNT(*) FROM corpus1;";

        int notDistinctCount = 0;
        int distinctCount = 0;

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            notDistinctCount = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("count: " + e.getMessage());
        }

        sql = "SELECT COUNT(DISTINCT id) FROM corpus1;";
        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            distinctCount = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("count: " + e.getMessage());
        }

        if (distinctCount != notDistinctCount) {
            System.out.println(distinctCount + ", " + notDistinctCount);
        }

        return distinctCount;
    }

    public static String getFileName(double fileID) {

        String idAsString = String.valueOf(fileID);
        String sql = "SELECT fileName FROM files WHERE fileID =" + idAsString + ";";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                return rs.getString("fileName");
            }
            return null;
        } catch (SQLException e) {
            System.out.println("getFileName: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the Segment object to which the specified key is mapped, or null
     * if the database contains no mapping for the key.
     *
     * @param id
     * @return The Segment object associated with that id
     */
    public static Segment getSegment(double id) {

        String idAsString = String.valueOf(id);
        String sql = "SELECT id, fileID, fileName, thai, english, committed, removed, rank FROM corpus1 WHERE id =" + idAsString + ";";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                // Segment ret = new Segment(rs.getInt("id"), rs.getInt("fileID"), rs.getString("fileName"));
                return rebuildSegment(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("getSegment: " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<TranslationFile> getAllFiles() {
        ArrayList<TranslationFile> fileList = new ArrayList();

        ArrayList<Integer> allFileIDs = getAllFileIDs();
        allFileIDs.forEach((fileID) -> {
            fileList.add(getFile(fileID));
        });
        
        return fileList;
    }

    /**
     * Retrieves a file from the database.
     *
     * @param fileID
     * @return
     */
    public static TranslationFile getFile(int fileID) {

        // recreates the TranslationFile object with the specified id and name.
        TranslationFile file = new TranslationFile(fileID, getFileName(fileID));

        String idAsString = String.valueOf(fileID);
        String sql = "SELECT id, fileID, fileName, thai, english, committed, removed, rank FROM corpus1 WHERE (fileID =" + idAsString + ") ORDER BY rank ASC;";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {

                // Segment seg = new Segment(file.getFileID());
                Segment seg = rebuildSegment(rs);
                // if "removed" == 1 (which means "true"), then it is put in removed segs.
                if (rs.getInt("removed") == 1) {
                    file.getHiddenSegs().add(seg);
                } else {
                    file.getActiveSegs().add(seg);
                }
            }
        } catch (SQLException e) {
            System.out.println("getFile: " + e.getMessage());
        }
        return file;

    }

    /**
     * Sets all fields on the Segment except the id and fileID.
     *
     * @param rs The result set returned from the database.
     * @param seg The Segment to be rebuilt in memory.
     * @return
     * @throws SQLException
     */
    private static Segment rebuildSegment(ResultSet rs) throws SQLException {

        SegmentBuilder sb = new SegmentBuilder();
        sb.setFileID(rs.getInt("fileID"));
        sb.setID(rs.getInt("id"));
        sb.setFileName(rs.getString("fileName"));
        sb.setThai(rs.getString("thai"));
        sb.setEnglish(rs.getString("english"));
        int committedStatus = rs.getInt("committed");
        sb.setCommitted(rs.getInt("committed") == 1);

        return sb.createSegment();
    }

    /**
     * Returns list of the names of all files in the database. If an SQL
     * exception occurs, returns null.
     *
     * @return
     */
    public static List<String> getAllFileNames() {
        List<String> fileNames = new ArrayList();

        String sql = "SELECT fileName FROM files;";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fileNames.add(rs.getString("fileName"));
            }
            return fileNames;
        } catch (SQLException e) {
            System.out.println("getAllFileIDs: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all fileIDs from the database. If an SQL exception occurs,
     * returns null.
     *
     * @return
     */
    public static ArrayList<Integer> getAllFileIDs() {
        ArrayList<Integer> fileIDs = new ArrayList();

        String sql = "SELECT DISTINCT fileID FROM files;";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fileIDs.add(rs.getInt("fileID"));
            }
            return fileIDs;
        } catch (SQLException e) {
            System.out.println("getAllFileIDs: " + e.getMessage());
        }
        return null;

    }

    /**
     * Sees if the specified Segment id exists in the database.
     *
     * @param segID
     * @return
     */
    public static boolean containsID(int segID) {
        String sql = "SELECT id FROM corpus1 where id= ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseOperations.connect();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, segID);
            rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                if (segID == rs.getInt("id")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("containsID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("containsID: " + e.getMessage());
            }
        }
        return false;

    }

    /**
     * Sees if the specified file ID exists in the database.
     *
     * @param fileID
     * @return
     */
    protected static boolean containsFileID(double fileID) {

        String sql = "SELECT fileID FROM files";

        try (Connection conn = DatabaseOperations.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if (fileID == rs.getDouble("fileID")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("fileIDExists: " + e.getMessage());
        }
        return false;

    }

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    protected static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Connect: " + e.getMessage());
        }
        return conn;
    }

    public static void rebootDB() {
        DeleteTable.deleteAllTables();
        CreateTable.createTables();
        RebootDatabaseAndBuildCorpus.build();
    }

    /**
     * Generates a random Segment id. Not guaranteed to be unique, but likely
     * so.
     *
     * @return
     */
    public static int makeSegID() {
        int newID = (int) (Math.random() * 100000000);
        return newID;
    }

}
