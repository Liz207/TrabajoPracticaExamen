/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ingri
 */
public class Orden {
      private String id;
    private Cliente cliente;
    private List<ItemOrden> items;
    private BigDecimal totalBruto;
    private BigDecimal impuestos;
    private BigDecimal descuentos;
    private BigDecimal totalNeto;
    private LocalDateTime fecha;

    public Orden() {
        this.items = new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.totalBruto = BigDecimal.ZERO;
        this.impuestos = BigDecimal.ZERO;
        this.descuentos = BigDecimal.ZERO;
        this.totalNeto = BigDecimal.ZERO;
    }
     public Orden(String id, Cliente cliente) {
        this();
        this.id = id;
        this.cliente = cliente;
    }

    public void calcularTotales() {
        this.totalBruto = BigDecimal.ZERO;
        for (ItemOrden item : items) {
            this.totalBruto = this.totalBruto.add(item.getSubtotal());
        }
        // Impuestos y descuentos se aplicarán desde el servicio
    }
 public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemOrden> getItems() { return items; }
    public void setItems(List<ItemOrden> items) { this.items = items; }

    public BigDecimal getTotalBruto() { return totalBruto; }
    public void setTotalBruto(BigDecimal totalBruto) { this.totalBruto = totalBruto; }

    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
     public BigDecimal getDescuentos() { return descuentos; }
    public void setDescuentos(BigDecimal descuentos) { this.descuentos = descuentos; }

    public BigDecimal getTotalNeto() { return totalNeto; }
    public void setTotalNeto(BigDecimal totalNeto) { this.totalNeto = totalNeto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orden orden = (Orden) o;
        return Objects.equals(id, orden.id);
    }
      @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Orden{" +
                "id='" + id + '\'' +
                ", cliente=" + (cliente != null ? cliente.getIdInterno() : "null") +
                ", items=" + items.size() +
                ", totalBruto=" + totalBruto +
                ", impuestos=" + impuestos +
                ", descuentos=" + descuentos +
                ", totalNeto=" + totalNeto +
                ", fecha=" + fecha +
                '}';
    }
}

