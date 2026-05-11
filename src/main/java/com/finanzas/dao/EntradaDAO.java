package com.finanzas.dao;

import com.finanzas.modelo.Entrada;
import com.finanzas.util.ConexionDB;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {

    public boolean registrar(Entrada entrada) {
        String sql = "INSERT INTO entradas (tipo_entrada, monto, fecha, factura_path, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entrada.getTipoEntrada());
            ps.setBigDecimal(2, entrada.getMonto());
            ps.setDate(3, Date.valueOf(entrada.getFecha()));
            ps.setString(4, entrada.getFacturaPath());
            ps.setInt(5, entrada.getUsuarioId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Entrada> listarPorUsuario(int usuarioId) {
        List<Entrada> lista = new ArrayList<>();
        String sql = "SELECT * FROM entradas WHERE usuario_id = ? ORDER BY fecha DESC, created_at DESC";
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
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM entradas WHERE usuario_id = ?";
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

    private Entrada mapear(ResultSet rs) throws SQLException {
        Entrada e = new Entrada();
        e.setId(rs.getInt("id"));
        e.setTipoEntrada(rs.getString("tipo_entrada"));
        e.setMonto(rs.getBigDecimal("monto"));
        Date fecha = rs.getDate("fecha");
        if (fecha != null) e.setFecha(fecha.toLocalDate());
        e.setFacturaPath(rs.getString("factura_path"));
        e.setUsuarioId(rs.getInt("usuario_id"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) e.setCreatedAt(ts.toLocalDateTime());
        return e;
    }
}
