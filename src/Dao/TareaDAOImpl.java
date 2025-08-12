/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Dominio.Tarea;
import Dominio.TaskManagerException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TareaDAOImpl implements TareaDAO {
     private final ConexionBD conexionBD;
    
    public TareaDAOImpl() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    public Tarea guardar(Tarea tarea) {
        String sql = """
            INSERT INTO tareas (titulo, prioridad, estado, especial, fecha_creacion, activo) 
            VALUES (?, ?, ?, ?, ?, ?) RETURNING id
            """;
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, tarea.getTitulo());
            ps.setInt(2, tarea.getPrioridad());
            ps.setBoolean(3, tarea.isEstado());
            ps.setBoolean(4, tarea.isEspecial());
   
            ps.setBoolean(6, true);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tarea.setId(rs.getLong("id"));
            }
            return tarea;
        } catch (SQLException e) {
            throw new TaskManagerException("Error al guardar tarea", e);
        }
    }
    
    public List<Tarea> listarActivas() {
        String sql = """
            SELECT * FROM tareas 
            WHERE activo = true 
            ORDER BY prioridad ASC, fecha_creacion DESC
            """;
        
        List<Tarea> tareas = new ArrayList<>();
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                tareas.add(mapearTarea(rs));
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al listar tareas", e);
        }
        
        return tareas;
    }
    
    public void actualizarEstado(Long id, boolean estado) {
        String sql = """
            UPDATE tareas 
            SET estado = ?, fecha_modificacion = CURRENT_TIMESTAMP 
            WHERE id = ? AND activo = true
            """;
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setLong(2, id);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new TaskManagerException("Tarea no encontrada o ya eliminada");
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al actualizar estado", e);
        }
    }
    
    public void eliminar(Long id) {
        String sql = """
            UPDATE tareas 
            SET activo = false, fecha_modificacion = CURRENT_TIMESTAMP 
            WHERE id = ? AND activo = true
            """;
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql)) {
            ps.setLong(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new TaskManagerException("Tarea no encontrada o ya eliminada");
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al eliminar tarea", e);
        }
    }
    
    public void restaurar(Long id) {
        String sql = """
            UPDATE tareas 
            SET activo = true, fecha_modificacion = CURRENT_TIMESTAMP 
            WHERE id = ? AND activo = false
            """;
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql)) {
            ps.setLong(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new TaskManagerException("Tarea no encontrada o ya activa");
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al restaurar tarea", e);
        }
    }
    
    public Optional<Tarea> buscarPorId(Long id) {
        String sql = "SELECT * FROM tareas WHERE id = ?";
        
        try (PreparedStatement ps = conexionBD.getConexion().prepareStatement(sql)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearTarea(rs));
                }
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al buscar tarea", e);
        }
        
        return Optional.empty();
    }
    
    private Tarea mapearTarea(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        tarea.setId(rs.getLong("id"));
        tarea.setTitulo(rs.getString("titulo"));
        tarea.setPrioridad(rs.getInt("prioridad"));
        tarea.setEstado(rs.getBoolean("estado"));
        tarea.setEspecial(rs.getBoolean("especial"));
        tarea.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
        tarea.setActivo(rs.getBoolean("activo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_modificacion");
        if (timestamp != null) {
            tarea.setFechaModificacion(timestamp.toLocalDateTime());
        }
        
        return tarea;
    }
}
