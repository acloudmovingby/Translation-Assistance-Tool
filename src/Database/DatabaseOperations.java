/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.FileList;
import Files.TUEntryBasic;
import JavaFX_1.MainLogic;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class DatabaseOperations {

    /**
     * ***********************************************************************
     *
     * Methods that can WRITE to database
     *
     *************************************************************************
     */
    /**
     * Puts the TU into the database. If a TU with that id already exists, then
     * it is replaced by the new TU. If successful, returns true. If there is a
     * SQL error, then returns false.
     *
     * @param tu
     * @return
     */
    public static boolean addOrUpdateTU(TUEntryBasic tu) {
        if (!MainLogic.databaseIsWritable()) {
            return false;
        } else {
            
            if (tu.getID() == 0) {
                tu.setID(makeTUID());
            }

            String sql = "INSERT OR REPLACE INTO corpus1(id, fileID, thai, english, committed, removed, rank) VALUES(?,?,?,?,?,?,?)";

            try (Connection conn = DatabaseOperations.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, tu.getID());

                pstmt.setDouble(2, tu.getFileID());

                pstmt.setString(3, tu.getThai());

                pstmt.setString(4, tu.getEnglish());

                // committed/removed booleans are stored as binary (0 = false, 1 = true)
                if (tu.isCommitted()) {
                    pstmt.setInt(5, 1);
                } else {
                    pstmt.setInt(5, 0);
                }
                pstmt.setInt(6, tu.isRemoved() ? 1 : 0);
                
                pstmt.setInt(7, tu.getRank());

                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("AddOrUpdateTU: " + e.getMessage());
                return false;
            }

        }
    }

    /**
     * Adds all TU entries contained in the file to the database or updates them
     * if they already exist. If there are TU entries matching this fileID
     * already in the database but are not in the current file, they are NOT
     * deleted. (In other words, TU entries can be added or updated via this
     * method, but never removed from the database).
     *
     * @param bf
     * @return True if all TUs are added successfully. If file is null or there
     * is an SQL error, returns false.
     */
    public static boolean addFile(BasicFile bf) {
        if (!MainLogic.databaseIsWritable()) {
            return false;
        } else {

            boolean ret = true;

            if (bf == null) {
                return false;
            }
            // updates fileName
            addOrUpdateFileName(bf.getFileID(), bf.getFileName());

            // adds all TUs in file to database
            for (TUEntryBasic tu : bf.getTUsToDisplay()) {
                ret = DatabaseOperations.addOrUpdateTU(tu);
            }
            return ret;
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
        if (!MainLogic.databaseIsWritable()) {
            return false;
        } else {
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
    }

    /**
     * Creates a fileID for the given fileName and adds to the database.
     *
     * @param fileName The name of the file.
     * @return A new file ID that doesn't exist in the database
     */
    public static int createFileID(String fileName) {

        int fileID;

        if (!MainLogic.databaseIsWritable()) {
            fileID = (int) (Math.random() * 100000000);
        } else {
            fileID = 0;

            // checks to see if the curent fileID is in database. If yes, then it generates a random new one until a new one is found. 
            while (containsFileID(fileID)) {
                fileID = (int) (Math.random() * 100000000);
            }
            // adds this id/name pairing to the database
            addOrUpdateFileName(fileID, fileName);
            System.out.println("CreateFileID: " + fileID + ", " + fileName);
            // returns the id so the BasicFile object can store it.
        }
        return fileID;
    }

    /**
     * Removes the TU with the specified id from the database
     *
     * @param id
     * @return True if removed successfully with no SQL errors.
     */
    private static boolean removeTU(double id) {

        // If database isn't active, this returns default of false;
        if (!MainLogic.databaseIsWritable()) {
            return false;
        } else {

            String sql = "DELETE FROM corpus1 WHERE id = ?";

            try (Connection conn = DatabaseOperations.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // set the corresponding param
                pstmt.setDouble(1, id);
                // execute the delete statement
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("removeTU: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * ***********************************************************************
     *
     * Methods that READ from database
     *
     *************************************************************************
     */
    public static int numberOfTUs() {
        if (!MainLogic.databaseIsReadable()) {
            return 0;
        } else {
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
    }

    public static String getFileName(double fileID) {

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return null;
        } else {

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
    }

    /**
     * Returns the TUEntryBasic object to which the specified key is mapped, or
     * null if the database contains no mapping for the key.
     *
     * @param id
     * @return The TUEntryBasic object associated with that id
     */
    public static TUEntryBasic getTU(double id) {

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return null;
        } else {

            String idAsString = String.valueOf(id);
            String sql = "SELECT id, fileID, thai, english, committed, removed FROM corpus1 WHERE id =" + idAsString + ";";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    TUEntryBasic ret = new TUEntryBasic(rs.getInt("id"), rs.getInt("fileID"));
                    return rebuildTU(rs, ret);
                }
                return null;
            } catch (SQLException e) {
                System.out.println("getTU: " + e.getMessage());
            }
            return null;
        }
    }

    public static FileList getAllTUs() {

        FileList fList = new FileList();

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return fList;
        } else {

            // gets all fileIDs
            // rebuilds a file for each one and adds to the fileList
            ArrayList<Integer> allFileIDs = getAllFileIDs();
            allFileIDs.forEach((fileID) -> {
                fList.addFile(getFile(fileID));
            });
            return fList;
        }
    }

    /**
     * Retrieves a file from the database.
     *
     * @param fileID
     * @return
     */
    public static BasicFile getFile(int fileID) {

        // recreates the BasicFile object with the specified id and name.
        BasicFile file = new BasicFile(fileID, getFileName(fileID));

        // If database isn't active, this returns the empty file;
        if (!MainLogic.databaseIsReadable()) {
            return file;
        } else {

            String idAsString = String.valueOf(fileID);
            String sql = "SELECT id, fileID, thai, english, committed, removed, rank FROM corpus1 WHERE (fileID =" + idAsString + ") ORDER BY rank ASC;";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {

                    TUEntryBasic tu = new TUEntryBasic(file.getFileID());
                    tu = rebuildTU(rs, tu);
                    if (tu.isRemoved()) {
                       file.getRemovedTUs().add(tu);
                    } else {
                       file.getTUsToDisplay().add(tu);
                    }

                    /*tu2.setThai(rs.getString("thai"));
                    tu2.setEnglish(rs.getString("english"));
                    int committedStatus = rs.getInt("committed");
                    if (committedStatus == 0) {
                        tu2.setCommitted(false);
                    } else if (committedStatus == 1) {
                        tu2.setCommitted(true);
                    }
                    int removedStatus = rs.getInt("removed");
                    if (removedStatus == 0) {
                        tu2.setRemoved(false);
                    } else if (removedStatus == 1) {
                        tu2.setRemoved(true);
                    }
                    tu2.setRank(rs.getInt("rank"));
                    
                    
                    /*
                    TUEntryBasic tu = new TUEntryBasic(rs.getInt("id"), rs.getInt("fileID"));
                    tu.setThai(rs.getString("thai"));
                    tu.setEnglish(rs.getString("english"));
                    int committedStatus = rs.getInt("committed");
                    if (committedStatus == 0) {
                        tu.setCommitted(false);
                    } else if (committedStatus == 1) {
                        tu.setCommitted(true);
                    }
                    file.addTUAtEnd(tu);
                     */
                }
            } catch (SQLException e) {
                System.out.println("getFile: " + e.getMessage());
            }
            return file;
        }
    }

    /**
     * Sets all fields on the TU except the id and fileID.
     *
     * @param rs The result set returned from the database.
     * @param tu The TUEntryBasic to be rebuilt in memory.
     * @return
     * @throws SQLException
     */
    private static TUEntryBasic rebuildTU(ResultSet rs, TUEntryBasic tu) throws SQLException {

        tu.setID(rs.getInt("id"));
        tu.setThai(rs.getString("thai"));
        tu.setEnglish(rs.getString("english"));
        int committedStatus = rs.getInt("committed");
        tu.setCommitted(rs.getInt("committed") == 1);
        tu.setRemoved(rs.getInt("removed") == 1);
        tu.setRank(rs.getInt("rank"));

        return tu;
    }

    /**
     * Retrieves all fileID from database
     *
     * @return
     */
    public static ArrayList<Integer> getAllFileIDs() {
        ArrayList<Integer> fileIDs = new ArrayList();

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return fileIDs;
        } else {

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
    }

    /**
     * Sees if the specified TU id exists in the database.
     *
     * @param tuID
     * @return
     */
    public static boolean containsID(int tuID) {
        String sql = "SELECT id FROM corpus1 where id= ?";

        // If database isn't active, this returns false;
        if (!MainLogic.databaseIsReadable()) {
            return false;
        } else {

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseOperations.connect();
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, tuID);
                rs = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                    if (tuID == rs.getInt("id")) {
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
    }

    /**
     * Sees if the specified file ID exists in the database.
     *
     * @param fileID
     * @return
     */
    protected static boolean containsFileID(double fileID) {

        // If database isn't active, this returns default of false;
        if (!MainLogic.databaseIsReadable()) {
            return false;
        } else {

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
        BuildCorpus.build();
    }

    /**
     * Generates a unique TU id. However, unlike createFileID, this does not ad
     * it to the database.
     *
     * @return
     */
    public static int makeTUID() {
        int newID = 0;

        if (MainLogic.databaseIsReadable()) {

            // If id already exists or is equal to zero, it regenerates.
            while (containsFileID(newID) || newID == 0) {
                newID = (int) (Math.random() * 100000000);
            }
            // returns the id so the BasicFile object can store it.
        }
        return newID;
    }

}
