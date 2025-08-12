/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;
import Dominio.Tarea;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public interface  TareaService {
    Tarea crearTarea(String titulo, int prioridad, boolean especial);
    List<Tarea> obtenerTareasActivas();
    void alternarEstado(Long id);
    void eliminarTarea(Long id);
    boolean deshacerEliminacion();
    Map<String, List<Tarea>> obtenerTareasPorEstado();
    List<Tarea> obtenerTareasPorPrioridad(int prioridad);
    int contarTareasPendientes();
}
