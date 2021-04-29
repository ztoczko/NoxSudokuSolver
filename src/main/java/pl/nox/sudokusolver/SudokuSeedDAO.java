package pl.nox.sudokusolver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class SudokuSeedDAO {

    private static DBUtil dBUtil = new DBUtil("mysql:/localhost:3306", "root", "password", "NoxSudoku");
    private static final String CREATE = "INSERT INTO sudokus VALUES (null, ?, ?);";
    private static final String COUNT_BY_DIFFICULTY = "SELECT COUNT(*) FROM sudokus WHERE difficulty = ?;";
    private static final String READ_BY_ID = "SELECT * FROM sudokus WHERE id = ?";
    private static final String READ_RANDOM_BY_DIFFICULTY = "SELECT * FROM sudokus WHERE difficulty = ? LIMIT ?, 1;";


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
                String result = rs.getString(1) + difficulty + rs.getString(3);
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

