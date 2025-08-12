/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;
import Dominio.Tarea;
import Dominio.TaskManagerException;
import Ui.Frame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TaskManagerApp {
    private static final Logger LOGGER = Logger.getLogger(TaskManagerApp.class.getName());
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // Demostración de paradigmas
        demonstrarParadigmas();
        
        // Configurar Look and Feel
        configurarLookAndFeel();
        
        // Iniciar aplicación GUI
        SwingUtilities.invokeLater(() -> {
            try {
                new Frame().setVisible(true);
                LOGGER.info("Aplicación iniciada exitosamente");
            } catch (TaskManagerException e) {
                LOGGER.log(Level.SEVERE, "Error al iniciar aplicación", e);
                System.err.println("Error fatal: " + e.getMessage());
                System.exit(1);
            }
        });
    }
    
    /**
     * Demostración de los tres paradigmas requeridos
     */
    private static void demonstrarParadigmas() {
        System.out.println("=== DEMOSTRACIÓN DE PARADIGMAS ===");
        
        // 1. Paradigma Estructurado (Imperativo)
        demonstrarParadigmaEstructurado();
        
        // 2. Paradigma Orientado a Objetos
        demonstrarParadigmaOOP();
        
        // 3. Paradigma Declarativo/Funcional (Streams)
        demonstrarParadigmaDeclarativo();
    }
    
    /**
     * Paradigma Estructurado - Programación imperativa con estructuras de control
     */
    private static void demonstrarParadigmaEstructurado() {
        System.out.println("\n1. PARADIGMA ESTRUCTURADO:");
        
        // Variables y estructuras de control básicas
        int[] prioridades = {1, 2, 3, 1, 2};
        int contadorAlta = 0;
        
        // Bucle for tradicional
        for (int i = 0; i < prioridades.length; i++) {
            if (prioridades[i] == 1) {
                contadorAlta++;
            }
        }
        
        // Estructura condicional
        if (contadorAlta > 0) {
            System.out.println("   - Encontradas " + contadorAlta + " tareas de alta prioridad");
        } else {
            System.out.println("   - No hay tareas de alta prioridad");
        }
        
        // Procedimiento estructurado
        calcularEstadisticas(prioridades);
    }
    
    private static void calcularEstadisticas(int[] datos) {
        int suma = 0;
        for (int valor : datos) {
            suma += valor;
        }
        double promedio = (double) suma / datos.length;
        System.out.println("   - Promedio de prioridades: " + String.format("%.2f", promedio));
    }
    
    /**
     * Paradigma Orientado a Objetos - Encapsulamiento, herencia, polimorfismo
     */
    private static void demonstrarParadigmaOOP() {
        System.out.println("\n2. PARADIGMA ORIENTADO A OBJETOS:");
        
        // Crear objetos y demostrar encapsulamiento
        try {
            Tarea tarea1 = new Tarea("Estudiar POO", 1);
            tarea1.setEspecial(true);
            
            // Demostrar método toString sobrescrito
            System.out.println("   - Objeto creado: " + tarea1);
            
            // Demostrar validación encapsulada
            System.out.println("   - Prioridad como texto: " + tarea1.getPrioridadTexto());
            System.out.println("   - Estado: " + tarea1.getEstadoTexto());
            
        } catch (TaskManagerException e) {
            System.out.println("   - Excepción capturada: " + e.getMessage());
        }
        
        // Demostrar polimorfismo con interfaces
        System.out.println("   - Polimorfismo demostrado en arquitectura DAO/Service");
    }
    
    /**
     * Paradigma Declarativo/Funcional - Streams y programación funcional
     */
    private static void demonstrarParadigmaDeclarativo() {
        System.out.println("\n3. PARADIGMA DECLARATIVO/FUNCIONAL (Streams):");
        
        // Crear lista de datos para streams
        java.util.List<Integer> numeros = java.util.Arrays.asList(1, 2, 3, 1, 2, 3, 1);
        
        // Stream con filter, map y collect
        long tareasAlta = numeros.stream()
                .filter(p -> p == 1)  // Filtrar alta prioridad
                .count();
        
        System.out.println("   - Tareas alta prioridad (Stream): " + tareasAlta);
        
        // Stream con reduce
        double promedio = numeros.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        
        System.out.println("   - Promedio (Stream): " + String.format("%.2f", promedio));
        
        // Stream con grouping
        java.util.Map<Integer, Long> conteosPorPrioridad = numeros.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    java.util.function.Function.identity(),
                    java.util.stream.Collectors.counting()
                ));
        
        System.out.println("   - Conteo por prioridad: " + conteosPorPrioridad);
        
        // Stream funcional con método reference
        String resultado = numeros.stream()
                .distinct()
                .sorted()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining(", "));
        
        System.out.println("   - Prioridades únicas ordenadas: " + resultado);
    }
    
    private static void configurarLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.warning("No se pudo configurar Look and Feel del sistema");
        }
    }
}
