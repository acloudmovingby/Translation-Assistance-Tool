/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class DeleteTable {

    /**
     * Deletes the TU entry table.
     *
     */
    private static void deleteSegsTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // SQL statement for creating a new table
        String sql = "DROP TABLE IF EXISTS corpus1;";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes the files table.
     *
     */
    private static void deleteFileTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // SQL statement for creating a new table
        String sql = "DROP TABLE IF EXISTS files;";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAllTables() {
        deleteSegsTable();
        deleteFileTable();
    }
}
