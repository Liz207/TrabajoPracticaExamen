/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.strategy;

/**
 *
 * @author ingri
 */
import domain.Orden;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DescuentoClienteFrecuente implements Descuento {
    private final List<Orden> historialOrdenes;

    public DescuentoClienteFrecuente(List<Orden> historialOrdenes) {
        this.historialOrdenes = historialOrdenes;
    }

    @Override
    public BigDecimal aplicar(Orden orden) {
        if (orden.getCliente() == null) return BigDecimal.ZERO;

        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(1);
        BigDecimal totalUltimoMes = historialOrdenes.stream()
                .filter(o -> o.getCliente() != null && o.getCliente().getIdInterno().equals(orden.getCliente().getIdInterno()))
                .filter(o -> o.getFecha().isAfter(unMesAtras))
                .map(Orden::getTotalNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalUltimoMes.compareTo(new BigDecimal("200000")) > 0) {
            return orden.getTotalBruto().multiply(new BigDecimal("0.05")); // 5% de descuento
        }
        return BigDecimal.ZERO;
    }
}
