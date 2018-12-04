/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Files.TUEntryBasic;
import JavaFX_1.MainLogic;
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
    
    /**
     * Puts the TU into the database. If a TU with that id already exists, then it is replaced by the new TU. If successful, returns true. If there is a SQL error, then this returns false. 
     * @param tu
     * @return 
     */
    public static boolean pushTU(TUEntryBasic tu) {
         if (!MainLogic.isDatabaseActive()) {
            return true;
        } else {
             String sql = "INSERT INTO corpus1(id, fileID, fileName, thai, english, committed) VALUES(?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET id=?, fileID=?, fileName=?, thai=?, english=?, committed=?";

            try (Connection conn = DatabaseOperations.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, tu.getID());
                pstmt.setDouble(7, tu.getID());
                
                pstmt.setDouble(2, tu.getFileID());
                pstmt.setDouble(8, tu.getFileID());
                
                pstmt.setString(3, tu.getFileName());
                pstmt.setString(9, tu.getFileName());
                
                pstmt.setString(4, tu.getThai());
                pstmt.setString(10, tu.getThai());
                
                pstmt.setString(5, tu.getEnglish());
                pstmt.setString(11, tu.getEnglish());
                
                if (tu.isCommitted()) {
                    pstmt.setInt(6, 1);
                    pstmt.setInt(12, 1);
                } else {
                    pstmt.setInt(6, 0);
                    pstmt.setInt(12, 0);
                }
                
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("PushTU: " + e.getMessage());
                return false;
            }
        
        }
    }

    /**
     * Insert a new TU into the database if it doesn't exist. If the TU already
     * exists in the database, false is returned and the database is not
     * changed.
     *
     * @param tu The TU to be added/replaced
     * @return True if it successfully added the new TU.
     */
    public static boolean addTUtoDatabase(TUEntryBasic tu) {

        // If database isn't active, this returns default of true;
        if (!MainLogic.isDatabaseActive()) {
            return true;
        } else {
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
    }
    
   

    /**
     * Replaces the TU if a TU with the same id exists in the database. If the
     * TU doesn't exist, returns false and no TU is added.
     *
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
     * Called when you create a new file. Creates a new unique file ID for that
     * file.
     *
     * @return A new file ID that doesn't exist in the database
     */
    public static double createFileID() {
        double fileID = 0;

        // checks to see if the curent fileID is in database. If yes, then it generates a random new one until a new one is found. 
        while (fileIDExists(fileID)) {
            fileID = (Math.random() * 100000);
        }

        return fileID;
    }

    protected static boolean tuIDExists(double tuID) {
        String sql = "SELECT id FROM corpus1";

        // If database isn't active, this returns false;
        if (!MainLogic.isDatabaseActive()) {
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
        if (!MainLogic.isDatabaseActive()) {
            return false;
        } else {

            String sql = "SELECT DISTINCT fileID FROM corpus1";

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
                System.out.println("File id exists: " + e.getMessage());
            }
            return false;
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
        if (!MainLogic.isDatabaseActive()) {
            return null;
        } else {

            String idAsString = String.valueOf(id);
            String sql = "SELECT id, fileID, fileName, thai, english, committed FROM corpus1 WHERE id =" + idAsString + ";";

            try (Connection conn = DatabaseOperations.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    TUEntryBasic ret = new TUEntryBasic(rs.getDouble("id"), rs.getDouble("fileID"), rs.getString("fileName"));
                   
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
    }

    private static boolean removeTU(double id) {

        // If database isn't active, this returns default of true;
        if (!MainLogic.isDatabaseActive()) {
            return true;
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
     * For testing. Tries taking a connection to avoid SQL errors.
     * @param conn
     * @param tu
     * @return 
     */
     public static boolean addTU2(Connection conn, TUEntryBasic tu) {
         // If database isn't active, this returns default of true;
        if (!MainLogic.isDatabaseActive()) {
            return true;
        } else {
            String sql = "INSERT INTO corpus1(id, fileID, fileName, thai, english, committed) VALUES(?,?,?,?,?,?)";


            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                System.out.println("AddTU2: " + e.getMessage());
                return false;
            }
    }
    }
    
     /**
      * For testing. Tries taking a connection to avoid SQL errors.
      * @param conn
      * @param id
      * @return 
      */
    public static TUEntryBasic getTU2(Connection conn, double id) {

        // If database isn't active, this returns default of null;
        if (!MainLogic.isDatabaseActive()) {
            return null;
        } else {

            String idAsString = String.valueOf(id);
            String sql = "SELECT id, fileID, fileName, thai, english, committed FROM corpus1 WHERE id =" + idAsString + ";";

            try (
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                // loop through the result set
                while (rs.next()) {
                    TUEntryBasic ret = new TUEntryBasic(rs.getDouble("id"), rs.getDouble("fileID"), rs.getString("fileName"));
                   
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
                System.out.println("4");
                System.out.println("GetTU2: " + e.getMessage());
            }
            System.out.println("5");
            
            return null;
            
        }
    }

}
