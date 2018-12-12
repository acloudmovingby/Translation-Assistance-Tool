/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

TU ids go up by 1000.
When I insert 
    put in at 500 (750, etc..)
    check to make sure it doesn't exist
    
If it exists (which is unlikely because user probably won't merge/split more than a time or two).
    have method to reassign neighboring TU ids. This requires at most log2 1000 upateTU calls (?).


 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Chris
 */
public class CreateTable {

    /**
     * Create a new table in the test database for TU Entries.
     *
     */
    private static void createTUEntryTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // TU Entry table
        String sql = "CREATE TABLE IF NOT EXISTS corpus1 ("
                + "	id       INT   PRIMARY KEY     NOT NULL,"
                + "	fileID   INT                    NOT NULL,"
                + "	thai     TEXT,"
                + "	english  TEXT,"
                + "     committed INT                   NOT NULL,"
                + "     removed  INT                    NOT NULL,"
                + "     rank     INT                    NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Create a new table in the test database for files.
     *
     */
    private static void createFileTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // TU Entry table
        String sql = "CREATE TABLE IF NOT EXISTS files ("
                + "	fileID       ID     PRIMARY KEY     NOT NULL,"
                + "	fileName     TEXT                   NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        createTUEntryTable();
        createFileTable();
    }
}
