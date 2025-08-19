/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.service;

/**
 *
 * @author ingri
 */


import domain.Cliente;
import infrastructure.repository.ClienteRepositorioCSV;
import infrastructure.security.CipherService;
import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepositorioCSV repositorio;
    private final CipherService cipherService;

    public ClienteService(ClienteRepositorioCSV repositorio, CipherService cipherService) {
        this.repositorio = repositorio;
        this.cipherService = cipherService;
    }

    public void crearCliente(Cliente cliente, String cedulaPlana) throws Exception {
        byte[] cedulaCifrada = cipherService.encrypt(cedulaPlana);
        cliente.setCedulaCifrada(cedulaCifrada);
        repositorio.guardar(cliente);
    }

    public void actualizarCliente(Cliente cliente, String cedulaPlana) throws Exception {
        byte[] cedulaCifrada = cipherService.encrypt(cedulaPlana);
        cliente.setCedulaCifrada(cedulaCifrada);
        repositorio.actualizar(cliente);
    }

    public Optional<Cliente> buscarPorId(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Cliente> listarTodos() {
        return repositorio.listarTodos();
    }

    public void eliminarCliente(String id) {
        repositorio.eliminar(id);
    }

    public String descifrarCedula(Cliente cliente) throws Exception {
        if (cliente.getCedulaCifrada() == null) return "";
        return cipherService.decrypt(cliente.getCedulaCifrada());
    }
}
