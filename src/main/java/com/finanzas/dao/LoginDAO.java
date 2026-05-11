package com.finanzas.dao;

import com.finanzas.modelo.Usuario;
import com.finanzas.util.ConexionDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginDAO {

    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, username, nombre FROM usuarios WHERE username = ? AND password = SHA2(?, 256)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nombre")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
