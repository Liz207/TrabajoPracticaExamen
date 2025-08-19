/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.service;

/**
 *
 * @author ingri
 */

import domain.ItemOrden;
import domain.Orden;
import domain.Producto;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class AnalisisService {
    private final List<Orden> ordenes;
    private final List<Producto> productos;

    public AnalisisService(List<Orden> ordenes, List<Producto> productos) {
        this.ordenes = ordenes;
        this.productos = productos;
    }

    public List<Map.Entry<String, BigDecimal>> getTop5ProductosPorIngresos() {
        Map<String, BigDecimal> ingresosPorProducto = new HashMap<>();

        for (Orden orden : ordenes) {
            for (ItemOrden item : orden.getItems()) {
                String nombreProducto = item.getProducto().getNombre();
                BigDecimal ingresoItem = item.getSubtotal();
                ingresosPorProducto.merge(nombreProducto, ingresoItem, BigDecimal::add);
            }
        }

        return ingresosPorProducto.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Map.Entry<String, BigDecimal>> getTop5ClientesPorMonto() {
        Map<String, BigDecimal> gastoPorCliente = new HashMap<>();

        for (Orden orden : ordenes) {
            if (orden.getCliente() != null) {
                String nombreCliente = orden.getCliente().getNombre();
                gastoPorCliente.merge(nombreCliente, orden.getTotalNeto(), BigDecimal::add);
            }
        }

        return gastoPorCliente.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Producto> getExistenciasCriticas() {
        return productos.stream()
                .filter(p -> p.getStockActual() < p.getStockMin())
                .sorted(Comparator.comparingInt(Producto::getStockActual))
                .collect(Collectors.toList());
    }
}
