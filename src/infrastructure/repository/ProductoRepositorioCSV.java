/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

/**
 *
 * @author ingri
 */

import domain.Producto;
import java.math.BigDecimal;

public class ProductoRepositorioCSV extends RepositorioCSV<Producto, String> {

    public ProductoRepositorioCSV(String filePath) {
        super(filePath);
    }

    @Override
    protected Producto fromCSVRecord(String[] record) {
        return new Producto(
                record[0], // codigo
                record[1], // nombre
                record[2], // categoria
                new BigDecimal(record[3]), // precio
                Integer.parseInt(record[4]), // stockMin
                Integer.parseInt(record[5])  // stockActual
        );
    }

    @Override
    protected String[] toCSVRecord(Producto producto) {
        return new String[]{
                producto.getCodigo(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio().toString(),
                String.valueOf(producto.getStockMin()),
                String.valueOf(producto.getStockActual())
        };
    }

    @Override
    protected String getId(Producto producto) {
        return producto.getCodigo();
    }

    @Override
    protected boolean matchesId(String[] record, String id) {
        return record[0].equals(id);
    }
}