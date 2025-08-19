/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

/**
 *
 * @author ingri
 */
import infrastructure.persistence.CSVUTIL;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class RepositorioCSV<T, ID> implements Repositorio<T, ID> {
    private static final Logger logger = Logger.getLogger(RepositorioCSV.class.getName());
    protected String filePath;

    public RepositorioCSV(String filePath) {
        this.filePath = filePath;
    }

    protected abstract T fromCSVRecord(String[] record);
    protected abstract String[] toCSVRecord(T entidad);
    protected abstract ID getId(T entidad);
    protected abstract boolean matchesId(String[] record, ID id);

    @Override
    public void guardar(T entidad) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            ID id = getId(entidad);
            boolean found = false;
            for (int i = 0; i < records.size(); i++) {
                if (matchesId(records.get(i), id)) {
                    records.set(i, toCSVRecord(entidad));
                    found = true;
                    break;
                }
            }
            if (!found) {
                records.add(toCSVRecord(entidad));
            }
            CSVUTIL.writeCSV(filePath, records);
        } catch (IOException e) {
            logger.severe("Error al guardar entidad: " + e.getMessage());
        }
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            for (int i = 1; i < records.size(); i++) {
                if (matchesId(records.get(i), id)) {
                    return Optional.of(fromCSVRecord(records.get(i)));
                }
            }
        } catch (IOException e) {
            logger.severe("Error al buscar entidad por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<T> listarTodos() {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            return records.stream().skip(1).map(this::fromCSVRecord).toList();
        } catch (IOException e) {
            logger.severe("Error al listar entidades: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public void eliminar(ID id) {
        try {
            List<String[]> records = CSVUTIL.readCSV(filePath);
            records.removeIf(record -> matchesId(record, id));
            CSVUTIL.writeCSV(filePath, records);
        } catch (IOException e) {
            logger.severe("Error al eliminar entidad: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(T entidad) {
        guardar(entidad);
    }
}