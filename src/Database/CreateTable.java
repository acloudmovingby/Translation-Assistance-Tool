package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates a SQLite table with the name "database1.db". 
 * 
 * @author Chris
 */
public class CreateTable {

    /**
     * Create a new table in the test database for Segment Entries.
     *
     */
    private static void createTUEntryTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // TU Entry table
        String sql = "CREATE TABLE IF NOT EXISTS corpus1 ("
                + "	id       INT   PRIMARY KEY     NOT NULL,"
                + "	fileID   INT                    NOT NULL,"
                + "	fileName TEXT,"
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
