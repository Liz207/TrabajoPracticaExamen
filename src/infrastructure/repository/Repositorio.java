/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

/**
 *
 * @author ingri
 */
import java.util.List;
import java.util.Optional;

public interface Repositorio<T, ID> {
    void guardar(T entidad);
    Optional<T> buscarPorId(ID id);
    List<T> listarTodos();
    void eliminar(ID id);
    void actualizar(T entidad);
}
