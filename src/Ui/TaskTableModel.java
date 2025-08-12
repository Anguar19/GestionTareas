/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ui;
import Dominio.Tarea;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TaskTableModel  extends AbstractTableModel {
    private static final String[] COLUMNAS = {
        "ID", "Título", "Prioridad", "Estado", "Especial", "Fecha Creación"
    };
    
    private List<Tarea> tareas;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public TaskTableModel() {
        this.tareas = new ArrayList<>();
    }
    
    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas != null ? tareas : new ArrayList<>();
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return tareas.size();
    }
    
    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return COLUMNAS[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= tareas.size()) {
            return null;
        }
        
        Tarea tarea = tareas.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return tarea.getId();
            case 1: return tarea.getTitulo();
            case 2: return tarea.getPrioridadTexto();
            case 3: return tarea.getEstadoTexto();
            case 4: return tarea.getMarcaEspecial();
            case 5: return tarea.getFechaCreacion().format(formatter);
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Long.class;
            case 1: case 2: case 3: case 4: case 5: return String.class;
            default: return Object.class;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Tabla de solo lectura
    }
    
    public Tarea getTareaEnFila(int fila) {
        if (fila >= 0 && fila < tareas.size()) {
            return tareas.get(fila);
        }
        return null;
    }
}
