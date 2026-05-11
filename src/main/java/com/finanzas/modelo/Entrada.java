package com.finanzas.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Entrada implements Serializable {
    private int id;
    private String tipoEntrada;
    private BigDecimal monto;
    private LocalDate fecha;
    private String facturaPath;
    private int usuarioId;
    private LocalDateTime createdAt;

    public Entrada() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipoEntrada() { return tipoEntrada; }
    public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getFacturaPath() { return facturaPath; }
    public void setFacturaPath(String facturaPath) { this.facturaPath = facturaPath; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getFacturaNombre() {
        if (facturaPath == null || facturaPath.isEmpty()) return null;
        return facturaPath.substring(facturaPath.lastIndexOf("/") + 1);
    }
}
