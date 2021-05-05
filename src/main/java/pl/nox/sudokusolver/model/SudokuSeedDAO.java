package pl.nox.sudokusolver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class SudokuSeedDAO {

    private static DBUtil dBUtil = new DBUtil("mysql://remotemysql.com:3306", "TraDU2ybRx", "q9FIzRp9bp", "TraDU2ybRx");
    private static final String CREATE = "INSERT INTO sudokus VALUES (null, ?, ?);";
    private static final String CREATE_SOLUTION = "INSERT INTO sudokus_solutions VALUES (null, ?, ?);";
    private static final String COUNT_BY_DIFFICULTY = "SELECT COUNT(*) FROM sudokus WHERE difficulty = ?;";
    private static final String READ_BY_ID = "SELECT * FROM sudokus WHERE id = ?";
    private static final String READ_SOLUTION_BY_ID = "SELECT * FROM sudokus_solutions WHERE sudoku_id = ?";
    private static final String READ_RANDOM_BY_DIFFICULTY = "SELECT * FROM sudokus WHERE difficulty = ? LIMIT ?, 1;";

    //creating new sudoku seed record
    public static boolean create(int difficulty, String shortSeed) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(CREATE)) {

            ps.setInt(1, difficulty);
            ps.setString(2, shortSeed);
            if (ps.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //creating new sudoku solution seed record
    public static boolean createSolution(int sudokuId, String shortSeed) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(CREATE_SOLUTION)) {

            ps.setInt(1, sudokuId);
            ps.setString(2, shortSeed);
            if (ps.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //counting number of seeds of predetermined difficulty
    public static int countByDifficulty(int difficulty) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(COUNT_BY_DIFFICULTY)) {

            ps.setInt(1, difficulty);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int result = rs.getInt(1);
                rs.close();
                return result;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //loading seed of given id
    public static String readById(int id) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(READ_BY_ID)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String result = rs.getString(3);
                rs.close();
                return result;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //loading solution seed of given id
    public static String readSolutionById(int sudoku_id) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(READ_SOLUTION_BY_ID)) {

            ps.setInt(1, sudoku_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String result = rs.getString(3);
                rs.close();
                return result;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //loading random sudoku seed of given difficulty
    public static String randomByDifficulty(int difficulty) {

        try (Connection conn = dBUtil.connect(); PreparedStatement ps = conn.prepareStatement(READ_RANDOM_BY_DIFFICULTY)) {

            Random rand = new Random();
            int size = countByDifficulty(difficulty);
            if (size < 1) {
                return null;
            }
            ps.setInt(1, difficulty);
            ps.setInt(2, rand.nextInt(size));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String id = rs.getString(1);
                while (id.length() < 3) {
                    id = "0" + id;
                }
                String result = id + difficulty + rs.getString(3);
                rs.close();
                return result;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

