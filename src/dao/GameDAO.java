package dao;

import db.OracleConnection;
import model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public void save(Player p) {
        String sql = "INSERT INTO GAME_PLAYER (PLAYER_NAME, ATTEMPTS, RESULT) VALUES (?, ?, ?)";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps =  con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getAttempts());
            ps.setString(3, p.getResult());
            ps.executeUpdate();
        }  catch (Exception e) {
            System.out.println("Error guardando" + e.getMessage());
        }
    }

    public List<Player> findAll() {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT * FROM GAME_PLAYER";
        try (Connection con = OracleConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Player(
                        rs.getInt("ID"),
                        rs.getString("PLAYER_NAME"),
                        rs.getInt("ATTEMPTS"),
                        rs.getString("RESULT")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error listando: " + e.getMessage());
        }
        return list;
    }

    public void updateName(int id, String newName) {
        String sql = "UPDATE GAME_PLAYER SET PLAYER_NAME=? WHERE ID=?";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error actualizando: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM GAME_PLAYER WHERE ID=?";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error eliminando: " + e.getMessage());
        }
    }
}
