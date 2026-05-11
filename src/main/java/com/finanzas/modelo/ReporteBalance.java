package com.finanzas.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ReporteBalance implements Serializable {
    private List<Entrada> entradas;
    private List<Salida> salidas;
    private BigDecimal totalEntradas;
    private BigDecimal totalSalidas;
    private BigDecimal balance;

    public ReporteBalance() {
        this.totalEntradas = BigDecimal.ZERO;
        this.totalSalidas = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }

    public ReporteBalance(List<Entrada> entradas, List<Salida> salidas) {
        this.entradas = entradas;
        this.salidas = salidas;
        calcularTotales();
    }

    private void calcularTotales() {
        this.totalEntradas = entradas.stream()
            .map(Entrada::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalSalidas = salidas.stream()
            .map(Salida::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.balance = this.totalEntradas.subtract(this.totalSalidas);
    }

    public List<Entrada> getEntradas() { return entradas; }
    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
        calcularTotales();
    }

    public List<Salida> getSalidas() { return salidas; }
    public void setSalidas(List<Salida> salidas) {
        this.salidas = salidas;
        calcularTotales();
    }

    public BigDecimal getTotalEntradas() { return totalEntradas; }
    public BigDecimal getTotalSalidas() { return totalSalidas; }
    public BigDecimal getBalance() { return balance; }

    public double getPorcentajeEntradas() {
        BigDecimal total = totalEntradas.add(totalSalidas);
        if (total.compareTo(BigDecimal.ZERO) == 0) return 0;
        return totalEntradas.divide(total, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100")).doubleValue();
    }

    public double getPorcentajeSalidas() {
        return 100.0 - getPorcentajeEntradas();
    }
}
