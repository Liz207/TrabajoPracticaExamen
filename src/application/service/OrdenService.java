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
import infrastructure.repository.OrdenRepositorioCSV;
import application.exception.StockInsuficienteException;
import application.exception.PagoRechazadoException;
import application.strategy.*;
import infrastructure.security.HMACUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OrdenService {
    private final OrdenRepositorioCSV repositorio;
    private final ProductoService productoService;

    public OrdenService(OrdenRepositorioCSV repositorio, ProductoService productoService) {
        this.repositorio = repositorio;
        this.productoService = productoService;
    }

    public void crearOrden(Orden orden, List<Descuento> descuentos, Pago metodoPago) throws StockInsuficienteException, PagoRechazadoException {
        // Validar stock
        for (ItemOrden item : orden.getItems()) {
            if (item.getProducto().getStockActual() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + item.getProducto().getNombre());
            }
        }

        // Calcular totales
        orden.calcularTotales();
        BigDecimal totalDescuentos = BigDecimal.ZERO;
        for (Descuento descuento : descuentos) {
            totalDescuentos = totalDescuentos.add(descuento.aplicar(orden));
        }
        orden.setDescuentos(totalDescuentos);

        BigDecimal impuestos = orden.getTotalBruto().subtract(orden.getDescuentos()).multiply(new BigDecimal("0.13")); // 13% IVA
        orden.setImpuestos(impuestos);

        BigDecimal totalNeto = orden.getTotalBruto().subtract(orden.getDescuentos()).add(orden.getImpuestos());
        orden.setTotalNeto(totalNeto);

        // Procesar pago
        metodoPago.procesar(orden.getTotalNeto());

        // Reducir stock
        for (ItemOrden item : orden.getItems()) {
            Producto prod = item.getProducto();
            prod.setStockActual(prod.getStockActual() - item.getCantidad());
            try {
                productoService.actualizarProducto(prod);
            } catch (Exception e) {
                // Manejar excepciones de validación si es necesario
                System.err.println("Error al actualizar stock: " + e.getMessage());
            }
        }

        // Guardar orden
        repositorio.guardar(orden);
    }

    public Optional<Orden> buscarPorId(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Orden> listarTodos() {
        return repositorio.listarTodos();
    }

    public void generarComprobante(Orden orden, String filePath) throws Exception {
        StringBuilder contenido = new StringBuilder();
        contenido.append("=== Comprobante de Orden ===\n");
        contenido.append("ID Orden: ").append(orden.getId()).append("\n");
        contenido.append("Fecha: ").append(orden.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        contenido.append("Cliente: ").append(orden.getCliente().getNombre()).append("\n");
        contenido.append("--- Items ---\n");
        for (ItemOrden item : orden.getItems()) {
            contenido.append(item.getProducto().getNombre())
                     .append(" x ").append(item.getCantidad())
                     .append(" @ ").append(item.getPrecioUnitario())
                     .append(" = ").append(item.getSubtotal()).append("\n");
        }
        contenido.append("------------------------\n");
        contenido.append("Total Bruto: ").append(orden.getTotalBruto()).append("\n");
        contenido.append("Descuentos: ").append(orden.getDescuentos()).append("\n");
        contenido.append("Impuestos (13%): ").append(orden.getImpuestos()).append("\n");
        contenido.append("TOTAL NETO: ").append(orden.getTotalNeto()).append("\n");
        contenido.append("========================\n");

        String hmac = HMACUtil.generateHMAC(contenido.toString());
        contenido.append("HMAC-SHA256: ").append(hmac).append("\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(contenido.toString());
        }
    }
}
