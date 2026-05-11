package com.finanzas.dao;

import com.finanzas.modelo.Salida;
import com.finanzas.util.ConexionDB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalidaDAO {

    public boolean registrar(Salida salida) {
        String sql = "INSERT INTO salidas (tipo_salida, monto, fecha, factura_path, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, salida.getTipoSalida());
            ps.setBigDecimal(2, salida.getMonto());
            ps.setDate(3, Date.valueOf(salida.getFecha()));
            ps.setString(4, salida.getFacturaPath());
            ps.setInt(5, salida.getUsuarioId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Salida> listarPorUsuario(int usuarioId) {
        List<Salida> lista = new ArrayList<>();
        String sql = "SELECT * FROM salidas WHERE usuario_id = ? ORDER BY fecha DESC, created_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public BigDecimal totalPorUsuario(int usuarioId) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM salidas WHERE usuario_id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private Salida mapear(ResultSet rs) throws SQLException {
        Salida s = new Salida();
        s.setId(rs.getInt("id"));
        s.setTipoSalida(rs.getString("tipo_salida"));
        s.setMonto(rs.getBigDecimal("monto"));
        Date fecha = rs.getDate("fecha");
        if (fecha != null) s.setFecha(fecha.toLocalDate());
        s.setFacturaPath(rs.getString("factura_path"));
        s.setUsuarioId(rs.getInt("usuario_id"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setCreatedAt(ts.toLocalDateTime());
        return s;
    }
}
