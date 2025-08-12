/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;
import Dominio.Tarea;
import Dominio.TaskManagerException;
import Dao.TareaDAO;
import Dao.TareaDAOImpl;
import java.util.*;
import java.util.stream.Collectors;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TareaServiceImpl {
    private final TareaDAO tareaDAO;
    private final Deque<Long> pilaEliminadas; // Estructura tipo pila para deshacer
    private final Map<Integer, List<Tarea>> tareasPorPrioridad; // Mapa para agrupar por prioridad
    private static final int LIMITE_DESHACER = 10;
    
    public TareaServiceImpl() {
        this.tareaDAO = new TareaDAOImpl();
        this.pilaEliminadas = new ArrayDeque<>();
        this.tareasPorPrioridad = new HashMap<>();
    }
    
    public Tarea crearTarea(String titulo, int prioridad, boolean especial) {
        try {
            Tarea tarea = new Tarea(titulo, prioridad);
            tarea.setEspecial(especial);
            
            Tarea tareaGuardada = tareaDAO.guardar(tarea);
            actualizarCachePrioridades();
            
            System.out.println("Tarea creada exitosamente: " + tareaGuardada);
            return tareaGuardada;
            
        } catch (Exception e) {
            System.err.println("Error al crear tarea: " + e.getMessage());
            throw new TaskManagerException("No se pudo crear la tarea: " + e.getMessage(), e);
        }
    }
    
    public List<Tarea> obtenerTareasActivas() {
        try {
            List<Tarea> tareas = tareaDAO.listarActivas();
            actualizarCachePrioridades(tareas);
            return tareas;
        } catch (Exception e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
            throw new TaskManagerException("No se pudieron obtener las tareas", e);
        }
    }
    
    public void alternarEstado(Long id) {
        try {
            Optional<Tarea> tareaOpt = tareaDAO.buscarPorId(id);
            if (tareaOpt.isPresent()) {
                Tarea tarea = tareaOpt.get();
                boolean nuevoEstado = !tarea.isEstado();
                tareaDAO.actualizarEstado(id, nuevoEstado);
                
                System.out.println("Estado alternado para tarea " + id + ": " + 
                                 (nuevoEstado ? "Hecho" : "Pendiente"));
            } else {
                throw new TaskManagerException("Tarea no encontrada");
            }
        } catch (Exception e) {
            System.err.println("Error al alternar estado: " + e.getMessage());
            throw new TaskManagerException("No se pudo alternar el estado", e);
        }
    }
    
    public void eliminarTarea(Long id) {
        try {
            tareaDAO.eliminar(id);
            
            // Agregar a pila para deshacer
            pilaEliminadas.push(id);
            if (pilaEliminadas.size() > LIMITE_DESHACER) {
                pilaEliminadas.removeLast();
            }
            
            actualizarCachePrioridades();
            System.out.println("Tarea " + id + " eliminada (se puede deshacer)");
            
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            throw new TaskManagerException("No se pudo eliminar la tarea", e);
        }
    }
    
    public boolean deshacerEliminacion() {
        if (pilaEliminadas.isEmpty()) {
            System.out.println("No hay eliminaciones para deshacer");
            return false;
        }
        
        try {
            Long ultimaEliminada = pilaEliminadas.pop();
            tareaDAO.restaurar(ultimaEliminada);
            
            actualizarCachePrioridades();
            System.out.println("Tarea " + ultimaEliminada + " restaurada");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al deshacer eliminación: " + e.getMessage());
            throw new TaskManagerException("No se pudo deshacer la eliminación", e);
        }
    }
    
    public Map<String, List<Tarea>> obtenerTareasPorEstado() {
        return obtenerTareasActivas().stream()
                .collect(Collectors.groupingBy(
                    tarea -> tarea.isEstado() ? "Completadas" : "Pendientes"
                ));
    }
    
    public List<Tarea> obtenerTareasPorPrioridad(int prioridad) {
        actualizarCachePrioridades();
        return tareasPorPrioridad.getOrDefault(prioridad, new ArrayList<>());
    }
    
    public int contarTareasPendientes() {
        return (int) obtenerTareasActivas().stream()
                .filter(tarea -> !tarea.isEstado())
                .count();
    }
    
    private void actualizarCachePrioridades() {
        actualizarCachePrioridades(obtenerTareasActivas());
    }
    
    private void actualizarCachePrioridades(List<Tarea> tareas) {
        tareasPorPrioridad.clear();
        tareas.stream()
              .collect(Collectors.groupingBy(Tarea::getPrioridad))
              .forEach(tareasPorPrioridad::put);
    }
}
