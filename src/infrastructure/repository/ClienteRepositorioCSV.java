/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;



/**
 *
 * @author ingri
 */
import domain.Cliente;
import java.util.Base64;

public class ClienteRepositorioCSV extends RepositorioCSV<Cliente, String> {

    public ClienteRepositorioCSV(String filePath) {
        super(filePath);
    }

    @Override
    protected Cliente fromCSVRecord(String[] record) {
        byte[] cedulaCifrada = record[4].isEmpty() ? null : Base64.getDecoder().decode(record[4]);
        return new Cliente(
                record[0], // idInterno
                record[1], // nombre
                record[2], // correo
                record[3], // telefono
                cedulaCifrada
        );
    }

    @Override
    protected String[] toCSVRecord(Cliente cliente) {
        String cedulaCifradaStr = cliente.getCedulaCifrada() == null ? "" : Base64.getEncoder().encodeToString(cliente.getCedulaCifrada());
        return new String[]{
                cliente.getIdInterno(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cedulaCifradaStr
        };
    }

    @Override
    protected String getId(Cliente cliente) {
        return cliente.getIdInterno();
    }

    @Override
    protected boolean matchesId(String[] record, String id) {
        return record[0].equals(id);
    }
}
