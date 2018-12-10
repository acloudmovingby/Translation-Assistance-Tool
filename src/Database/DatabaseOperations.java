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
import java.util.Arrays;
import javafx.collections.ObservableList;

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

            String sql = "INSERT OR REPLACE INTO corpus1(id, fileID, thai, english, committed, removed) VALUES(?,?,?,?,?,?)";

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

                if (tu.isRemoved()) {
                    pstmt.setInt(6, 1);
                } else {
                    pstmt.setInt(6, 0);
                }

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
            for (TUEntryBasic tu : bf.getObservableList()) {
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
    public static double createFileID(String fileName) {

        double fileID;

        if (!MainLogic.databaseIsWritable()) {
            fileID = (int) (Math.random() * 100000);
        } else {
            fileID = 0;

            // checks to see if the curent fileID is in database. If yes, then it generates a random new one until a new one is found. 
            while (fileIDExists(fileID)) {
                fileID = (Math.random() * 100000);
            }
            // adds this id/name pairing to the database
            addOrUpdateFileName(fileID, fileName);
            // returns the id so the BasicFile object can store it.
        }
        return fileID;
    }

    /**
     * Removes the TU with the specified id from the database
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
                System.out.println(e.getMessage());
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
            String sql = "SELECT id, fileID, thai, english, committed FROM corpus1 WHERE id =" + idAsString + ";";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    TUEntryBasic ret = new TUEntryBasic(rs.getDouble("id"), rs.getDouble("fileID"));
                    ret.setThai(rs.getString("thai"));
                    ret.setEnglish(rs.getString("english"));
                    int committedStatus = rs.getInt("committed");
                    if (committedStatus == 0) {
                        ret.setCommitted(false);
                    } else if (committedStatus == 1) {
                        ret.setCommitted(true);
                    }
                    int removedStatus = rs.getInt("removed");
                    if (removedStatus == 0) {
                        ret.setRemoved(false);
                    } else if (removedStatus == 1) {
                        ret.setRemoved(true);
                    }
                    return ret;
                }
                return null;
            } catch (SQLException e) {
                System.out.println("getTU: " + e.getMessage());
            }
            return null;
        }
    }

    public static FileList getAllCommittedTUs() {

        FileList fList = new FileList();

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return fList;
        } else {

            // gets all fileIDs
            // rebuilds a file for each one and adds to the fileList
            ArrayList<Double> allFileIDs = getAllFileIDs();
            allFileIDs.forEach((fileID) -> {
                fList.addFile(getFile(fileID));
            });
            return fList;
        }
    }

    /**
     * Retrieves a file from the database. If that fileID
     *
     * @param fileID
     * @return
     */
    private static BasicFile getFile(double fileID) {

        // recreates the BasicFile object with the specified id and name.
        BasicFile file = new BasicFile(fileID, getFileName(fileID));

        // If database isn't active, this returns the empty file;
        if (!MainLogic.databaseIsReadable()) {
            return file;
        } else {

            String idAsString = String.valueOf(fileID);
            String sql = "SELECT id, fileID, thai, english, committed FROM corpus1 WHERE (fileID =" + idAsString + ") AND (removed = 0);";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    TUEntryBasic tu = new TUEntryBasic(rs.getDouble("id"), rs.getDouble("fileID"));
                    tu.setThai(rs.getString("thai"));
                    tu.setEnglish(rs.getString("english"));
                    int committedStatus = rs.getInt("committed");
                    if (committedStatus == 0) {
                        tu.setCommitted(false);
                    } else if (committedStatus == 1) {
                        tu.setCommitted(true);
                    }
                    file.addTU(tu);
                }
            } catch (SQLException e) {
                System.out.println("getTU: " + e.getMessage());
            }
            return file;
        }
    }

    /**
     * Retrieves all fileID from database
     *
     * @return
     */
    private static ArrayList<Double> getAllFileIDs() {
        ArrayList<Double> fileIDs = new ArrayList();

        // If database isn't active, this returns default of null;
        if (!MainLogic.databaseIsReadable()) {
            return fileIDs;
        } else {

            String sql = "SELECT DISTINCT fileID FROM files;";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    fileIDs.add(rs.getDouble("fileID"));
                }
                return fileIDs;
            } catch (SQLException e) {
                System.out.println("getTU: " + e.getMessage());
            }
            return null;
        }
    }

    protected static boolean tuIDExists(double tuID) {
        String sql = "SELECT id FROM corpus1";

        // If database isn't active, this returns false;
        if (!MainLogic.databaseIsReadable()) {
            return false;
        } else {

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    if (tuID == rs.getDouble("id")) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.out.println("TU id exists: " + e.getMessage());
            }
            return false;
        }
    }

    protected static boolean fileIDExists(double fileID) {

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

}
