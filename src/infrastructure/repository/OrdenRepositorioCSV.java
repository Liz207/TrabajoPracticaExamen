/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

/**
 *
 * @author ingri
 */

import domain.ItemOrden;
import domain.Orden;
import domain.Producto;
import domain.Cliente;
import infrastructure.persistence.CSVUTIL;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class OrdenRepositorioCSV implements Repositorio<Orden, String> {
    private static final Logger logger = Logger.getLogger(OrdenRepositorioCSV.class.getName());
    private final String filePath;
    private final Repositorio<Producto, String> productoRepo;
    private final Repositorio<Cliente, String> clienteRepo;

    public OrdenRepositorioCSV(String filePath, Repositorio<Producto, String> productoRepo, Repositorio<Cliente, String> clienteRepo) {
        this.filePath = filePath;
        this.productoRepo = productoRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public void guardar(Orden orden) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            boolean found = false;
            String[] ordenRecord = toCSVRecord(orden);
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i)[0].equals(orden.getId())) {
                    records.set(i, ordenRecord);
                    found = true;
                    break;
                }
            }
            if (!found) {
                records.add(ordenRecord);
            }
            CSVUTIL.writeCSV(filePath, records);
        } catch (IOException e) {
            logger.severe("Error al guardar orden: " + e.getMessage());
        }
    }

    @Override
    public Optional<Orden> buscarPorId(String id) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            for (int i = 1; i < records.size(); i++) {
                if (records.get(i)[0].equals(id)) {
                    return Optional.of(fromCSVRecord(records.get(i)));
                }
            }
        } catch (IOException e) {
            logger.severe("Error al buscar orden por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Orden> listarTodos() {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            return records.stream().skip(1).map(this::fromCSVRecord).toList();
        } catch (IOException e) {
            logger.severe("Error al listar órdenes: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public void eliminar(String id) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            records.removeIf(record -> record[0].equals(id));
            CSVUTIL.writeCSV(filePath, records);
        } catch (IOException e) {
            logger.severe("Error al eliminar orden: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Orden orden) {
        guardar(orden);
    }

    private Orden fromCSVRecord(String[] record) {
        Orden orden = new Orden();
        orden.setId(record[0]);
        Optional<Cliente> clienteOpt = clienteRepo.buscarPorId(record[1]);
        orden.setCliente(clienteOpt.orElse(null));
        orden.setTotalBruto(new BigDecimal(record[2]));
        orden.setImpuestos(new BigDecimal(record[3]));
        orden.setDescuentos(new BigDecimal(record[4]));
        orden.setTotalNeto(new BigDecimal(record[5]));
        orden.setFecha(LocalDateTime.parse(record[6], DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Items (asumimos que están en columnas 7 en adelante, formato: codigoProducto,cantidad,precioUnitario|...)
        String itemsStr = record.length > 7 ? record[7] : "";
        List<ItemOrden> items = new ArrayList<>();
        if (!itemsStr.isEmpty()) {
            String[] itemParts = itemsStr.split("\\|");
            for (String part : itemParts) {
                String[] itemData = part.split(",");
                if (itemData.length == 3) {
                    Optional<Producto> prodOpt = productoRepo.buscarPorId(itemData[0]);
                    if (prodOpt.isPresent()) {
                        ItemOrden item = new ItemOrden();
                        item.setProducto(prodOpt.get());
                        item.setCantidad(Integer.parseInt(itemData[1]));
                        item.setPrecioUnitario(new BigDecimal(itemData[2]));
                        items.add(item);
                    }
                }
            }
        }
        orden.setItems(items);
        return orden;
    }

    private String[] toCSVRecord(Orden orden) {
        StringBuilder itemsBuilder = new StringBuilder();
        for (int i = 0; i < orden.getItems().size(); i++) {
            ItemOrden item = orden.getItems().get(i);
            if (i > 0) itemsBuilder.append("|");
            itemsBuilder.append(item.getProducto().getCodigo()).append(",")
                       .append(item.getCantidad()).append(",")
                       .append(item.getPrecioUnitario());
        }

        return new String[]{
                orden.getId(),
                orden.getCliente() != null ? orden.getCliente().getIdInterno() : "",
                orden.getTotalBruto().toString(),
                orden.getImpuestos().toString(),
                orden.getDescuentos().toString(),
                orden.getTotalNeto().toString(),
                orden.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                itemsBuilder.toString()
        };
    }
}
