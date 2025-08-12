/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Dominio.Tarea;
import Dominio.TaskManagerException;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public interface TareaDAO {
    Tarea guardar(Tarea tarea);
    List<Tarea> listarActivas();
    public void actualizarEstado(Long id, boolean estado);
    void eliminar(Long id);
    void restaurar(Long id);
    Optional<Tarea> buscarPorId(Long id);
}
