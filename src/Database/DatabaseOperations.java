/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.BasicFile;
import Files.TUEntry;
import Files.TUEntryBasic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

/**
 *
 * @author Chris
 */
public class DatabaseOperations {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private static Connection connect() {
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
     /**
     * Insert a new TU into the database if it doesn't exist. If the TU already exists in the database, false is returned and the database is not changed. 
     *
     * @param tu The TU to be added/replaced
     * @return True if it successfully added the new TU.
     */
    public static boolean addTUtoDatabase(TUEntryBasic tu) {
        String sql = "INSERT INTO corpus1(id, fileID, fileName, thai, english, committed) VALUES(?,?,?,?,?,?)";
 
        if (DatabaseOperations.tuIDExists(tu.getID())) {
            return false;
        }
        
        try (Connection conn = DatabaseOperations.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, tu.getID());
            pstmt.setDouble(2, tu.getFileID());
            pstmt.setString(3, tu.getFileName());
            pstmt.setString(4, tu.getThai());
            pstmt.setString(5, tu.getEnglish());
            if (tu.isCommitted()) {
                pstmt.setInt(6, 1);
            } else {
                pstmt.setInt(6, 0);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Add TU to database: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Replaces the TU if a TU with the same id exists in the database. If the TU doesn't exist, returns false and no TU is added.
     * @param tu
     * @return True if a TU was replaced, false if not.
     */
    public static boolean replaceTU(TUEntryBasic tu) {
        if (DatabaseOperations.tuIDExists(tu.getID())) {
            removeTU(tu.getID());
            return addTUtoDatabase(tu);
        } else {
            return false;
        }
    }
    

    /**
     * Called when you create a new file. Creates a new unique file ID for that file.
     * @return A new file ID that doesn't exist in the database
     */
    public static double createFileID() {
        double fileID = 0;
        
        // checks to see if the curent fileID is in database. If yes, then it generates a random new one until a new one is found. 
        while (fileIDExists(fileID)) {
            fileID = (Math.random()*100000);
        }
        
        return fileID;
    }
    
   
    protected static boolean tuIDExists(double tuID) {
        String sql = "SELECT id FROM corpus1";
        
        try (Connection conn = DatabaseOperations.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
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
    
    protected static boolean fileIDExists(double fileID) {
        String sql = "SELECT DISTINCT fileID FROM corpus1";
        
        try (Connection conn = DatabaseOperations.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                if (fileID == rs.getDouble("fileID")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("File id exists: " + e.getMessage());
        }
        return false;
    }
    
    
    /**
     * Returns the TUEntryBasic object to which the specified key is mapped, or null if the database contains no mapping for the key.
     * @param id
     * @return The TUEntryBasic object associated with that id
     */
    public static TUEntryBasic getTU(double id) {
        String idAsString = String.valueOf(id);
        String sql = "SELECT id, fileID, fileName, thai, english, committed FROM corpus1 WHERE id =" + idAsString + ";";
        
        
        try (Connection conn = DatabaseOperations.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                TUEntryBasic ret = new TUEntryBasic(rs.getDouble("id"));
                ret.setFileID(rs.getDouble("fileID"));
                ret.setFileName(rs.getString("fileName"));
                ret.setThai(rs.getString("thai"));
                ret.setEnglish(rs.getString("english"));
                int committedStatus = rs.getInt("committed");
                if (committedStatus == 0) {
                    ret.setCommitted(false);
                } else if (committedStatus == 1) {
                    ret.setCommitted(true);
                }
                return ret;
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void removeTU(double id) {
         String sql = "DELETE FROM corpus1 WHERE id = ?";
 
        try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setDouble(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
