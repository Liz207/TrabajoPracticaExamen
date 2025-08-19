/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.service;

/**
 *
 * @author ingri
 */


import domain.Producto;
import infrastructure.repository.ProductoRepositorioCSV;
import application.exception.PrecioInvalidoException;
import application.exception.StockNegativoException;
import application.exception.ProductoDuplicadoException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductoService {
    private final ProductoRepositorioCSV repositorio;

    public ProductoService(ProductoRepositorioCSV repositorio) {
        this.repositorio = repositorio;
    }

    public void crearProducto(Producto producto) throws PrecioInvalidoException, StockNegativoException, ProductoDuplicadoException {
        validarProducto(producto);
        if (repositorio.buscarPorId(producto.getCodigo()).isPresent()) {
            throw new ProductoDuplicadoException("Producto con código " + producto.getCodigo() + " ya existe.");
        }
        repositorio.guardar(producto);
    }

    public void actualizarProducto(Producto producto) throws PrecioInvalidoException, StockNegativoException {
        validarProducto(producto);
        repositorio.actualizar(producto);
    }

    public Optional<Producto> buscarPorCodigo(String codigo) {
        return repositorio.buscarPorId(codigo);
    }

    public List<Producto> listarTodos() {
        return repositorio.listarTodos();
    }

    public void eliminarProducto(String codigo) {
        repositorio.eliminar(codigo);
    }

    public List<Producto> buscarPorTexto(String texto) {
        return repositorio.listarTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                             p.getCategoria().toLowerCase().contains(texto.toLowerCase()) ||
                             p.getCodigo().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void validarProducto(Producto producto) throws PrecioInvalidoException, StockNegativoException {
        if (producto.getPrecio() != null && producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new PrecioInvalidoException("El precio no puede ser negativo.");
        }
        if (producto.getStockActual() < 0) {
            throw new StockNegativoException("El stock actual no puede ser negativo.");
        }
        if (producto.getStockMin() < 0) {
            throw new StockNegativoException("El stock mínimo no puede ser negativo.");
        }
    }
}
