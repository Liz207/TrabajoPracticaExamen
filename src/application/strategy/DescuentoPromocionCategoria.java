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
import java.util.Set;

public class DescuentoPromocionCategoria implements Descuento {
    private final Set<String> categoriasEnPromocion;
    private final BigDecimal porcentajeDescuento;

    public DescuentoPromocionCategoria(Set<String> categoriasEnPromocion, BigDecimal porcentajeDescuento) {
        this.categoriasEnPromocion = categoriasEnPromocion;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public BigDecimal aplicar(Orden orden) {
        BigDecimal descuento = BigDecimal.ZERO;
        for (var item : orden.getItems()) {
            if (categoriasEnPromocion.contains(item.getProducto().getCategoria())) {
                descuento = descuento.add(item.getSubtotal().multiply(porcentajeDescuento));
            }
        }
        return descuento;
    }
}