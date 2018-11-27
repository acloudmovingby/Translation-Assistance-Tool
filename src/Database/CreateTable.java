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
public class CreateTable {

    /**
     * Create a new table in the test database
     *
     */
    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database1.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS corpus1 ("
                + "	id       REAL   PRIMARY KEY     NOT NULL,"
                + "	fileID   INT                    NOT NULL,"
                + "	fileName TEXT                   NOT NULL," 
                + "	thai     TEXT,"
                + "	english  TEXT,"
                + "     committed INT                   NOT NULL"
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createNewTable();
    }

}
