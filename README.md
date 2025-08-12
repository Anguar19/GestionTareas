🎯 Gestor de Tareas - Práctica de Programación 1
📋 Descripción
Aplicación de escritorio desarrollada en Java utilizando NetBeans, Swing para la interfaz gráfica y JDBC para la persistencia en base de datos PostgreSQL. Implementa un sistema completo de gestión de tareas con funcionalidades CRUD, validaciones, manejo de excepciones y arquitectura en capas.
🛠️ Tecnologías Utilizadas

Lenguaje: Java 8+
IDE: NetBeans con Maven
Interfaz Gráfica: Swing (JFrame, JPanel, JTable)
Base de Datos: PostgreSQL 13+
Driver JDBC: org.postgresql.Driver (versión 42.7.0)
Arquitectura: Capas separadas (MVC + DAO)
Control de Versiones: Git

🗄️ Configuración de Base de Datos
Base de Datos Elegida: PostgreSQL
Justificación: PostgreSQL fue seleccionado por su robustez, capacidad de manejar transacciones ACID, excelente soporte para JDBC y su naturaleza open-source que facilita la instalación y configuración.
Configuración de Conexión
properties# Configuración de Base de Datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=taskmanager_db
DB_USER=taskmanager_user
DB_PASSWORD=taskmanager_pass
DB_DRIVER=org.postgresql.Driver
Formato URL JDBC
jdbc:postgresql://[host]:[puerto]/[nombre_bd]?[parámetros]
Ejemplo específico:
jdbc:postgresql://localhost:5432/taskmanager_db
Plantilla de Clase de Conexión
javapublic class ConexionBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/taskmanager_db";
    private static final String USUARIO = "taskmanager_user";
    private static final String PASSWORD = "taskmanager_pass";
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static ConexionBD instancia;
    private Connection conexion;
    
    private ConexionBD() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado", e);
        }
    }
    
    public static synchronized ConexionBD getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
    
    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        }
        return conexion;
    }
}
🏗️ Proceso de Inicialización
1. Instalación de PostgreSQL
bash# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# Windows: Descargar desde https://www.postgresql.org/download/windows/
# macOS: brew install postgresql
2. Configuración de Base de Datos
sql-- Conectarse como superusuario (postgres)
CREATE DATABASE taskmanager_db;
CREATE USER taskmanager_user WITH PASSWORD 'taskmanager_pass';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON DATABASE taskmanager_db TO taskmanager_user;

-- Conectarse a la BD creada
\c taskmanager_db

-- Otorgar permisos en el esquema
GRANT ALL PRIVILEGES ON SCHEMA public TO taskmanager_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO taskmanager_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO taskmanager_user;
3. Esquema de Base de Datos
La aplicación crea automáticamente las tablas necesarias, pero el esquema es:
sqlCREATE TABLE IF NOT EXISTS tareas (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    prioridad INTEGER NOT NULL CHECK (prioridad IN (1, 2, 3)),
    estado BOOLEAN DEFAULT FALSE,
    especial BOOLEAN DEFAULT FALSE,
    fecha_creacion DATE DEFAULT CURRENT_DATE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_modificacion VARCHAR(100) DEFAULT 'SYSTEM'
);

-- Índices para optimización
CREATE INDEX IF NOT EXISTS idx_tareas_activo ON tareas(activo);
CREATE INDEX IF NOT EXISTS idx_tareas_prioridad ON tareas(prioridad);
CREATE INDEX IF NOT EXISTS idx_tareas_fecha ON tareas(fecha_creacion);
🏛️ Arquitectura del Sistema
Estructura de Paquetes
src/main/java/
├── com.taskmanager.dominio/     # Entidades y lógica de dominio
│   ├── Tarea.java
│   ├── TareaEspecial.java
│   ├── TareaFactory.java
│   └── TaskManagerException.java
├── com.taskmanager.dao/         # Acceso a datos
│   ├── ConexionBD.java
│   ├── TareaDAO.java
│   └── TareaDAOImpl.java
├── com.taskmanager.servicio/    # Lógica de negocio
│   ├── TareaService.java
│   ├── TareaServiceImpl.java
│   └── ValidacionService.java
├── com.taskmanager.ui/          # Interfaz gráfica
│   ├── SimpleMainFrame.java
│   ├── TaskTableModel.java
│   └── ComponentUtils.java
├── com.taskmanager.utils/       # Utilidades
│   ├── TareaUtils.java
│   └── TaskLogger.java
├── com.taskmanager.config/      # Configuración
│   └── AppConfig.java
└── com.taskmanager.app/         # Aplicación principal
    └── TaskManagerApp.java
Capas de la Arquitectura

Capa de Presentación (UI): Interfaz gráfica Swing
Capa de Servicios: Lógica de negocio y validaciones
Capa de Acceso a Datos (DAO): Comunicación con BD
Capa de Dominio: Entidades y reglas de negocio

🎨 Paradigmas de Programación Implementados
1. Paradigma Estructurado (Imperativo)
java// Ejemplo: Procesamiento secuencial con estructuras de control
public static void calcularEstadisticas(int[] prioridades) {
    int contadorAlta = 0;
    
    // Bucle for tradicional
    for (int i = 0; i < prioridades.length; i++) {
        if (prioridades[i] == 1) {  // Estructura condicional
            contadorAlta++;
        }
    }
    
    // Lógica estructurada paso a paso
    double promedio = calcularPromedio(prioridades);
    mostrarResultados(contadorAlta, promedio);
}
Características implementadas:

Variables y tipos de datos primitivos
Estructuras de control (if/else, for, while)
Procedimientos y funciones
Secuencia lógica de instrucciones

2. Paradigma Orientado a Objetos (POO)
java// Ejemplo: Encapsulamiento y abstracción
public class Tarea {
    private String titulo;    // Encapsulamiento
    private int prioridad;
    
    // Constructor con validación
    public Tarea(String titulo, int prioridad) {
        setTitulo(titulo);    // Validación encapsulada
        setPrioridad(prioridad);
    }
    
    // Método con validación - Encapsulamiento
    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) {
            throw new TaskManagerException("Prioridad inválida");
        }
        this.prioridad = prioridad;
    }
    
    // Polimorfismo - Sobrescritura
    @Override
    public String toString() {
        return String.format("Tarea[%s]: %s", id, titulo);
    }
}

// Herencia - TareaEspecial extiende Tarea
public class TareaEspecial extends Tarea {
    private String categoria;
    
    // Polimorfismo - Sobrescritura
    @Override
    public String getPrioridadTexto() {
        return super.getPrioridadTexto() + " (" + categoria + ")";
    }
}
Características implementadas:

Encapsulamiento (campos privados, métodos públicos)
Herencia (TareaEspecial extends Tarea)
Polimorfismo (sobrecarga de métodos, sobrescritura)
Abstracción (interfaces DAO, Service)

3. Paradigma Declarativo/Funcional (Streams)
java// Ejemplo: Programación funcional con Streams
public List<Tarea> filtrarYOrdenar(List<Tarea> tareas) {
    return tareas.stream()
        .filter(t -> t.getPrioridad() == 1)           // Filtrado declarativo
        .filter(t -> !t.isEstado())                   // Múltiples filtros
        .sorted(Comparator.comparing(Tarea::getFechaCreacion)) // Ordenamiento
        .collect(Collectors.toList());                // Recolección
}

// Operaciones de agregación
public Map<Integer, Long> contarPorPrioridad(List<Tarea> tareas) {
    return tareas.stream()
        .collect(Collectors.groupingBy(        // Agrupación
            Tarea::getPrioridad,               // Función clasificadora
            Collectors.counting()              // Operación de reducción
        ));
}

// Transformación de datos
public List<String> obtenerResumenes(List<Tarea> tareas) {
    return tareas.stream()
        .map(t -> t.getTitulo() + " - " + t.getPrioridadTexto()) // Transformación
        .sorted()                                                 // Ordenamiento
        .collect(Collectors.toList());                           // Materialización
}
Características implementadas:

Streams para procesamiento de colecciones
Expresiones lambda y referencias a métodos
Operadores filter, map, reduce, collect
Programación declarativa (qué hacer, no cómo)

🏗️ Decisiones de Diseño
Estructuras de Datos Utilizadas
1. ArrayList<Tarea> - Lista Dinámica
Ubicación: TareaServiceImpl.obtenerTareasActivas()
Justificación: Ideal para almacenar la lista de tareas activas porque:

Redimensionamiento automático
Acceso eficiente por índice O(1)
Iteración secuencial rápida
Compatibilidad nativa con Streams

2. ArrayDeque<Long> - Pila para Deshacer
Ubicación: TareaServiceImpl.pilaEliminadas
Justificación: Perfecta para implementar "deshacer eliminaciones":

Operaciones LIFO (Last In, First Out) eficientes
Push/Pop en O(1)
Límite configurable de elementos
Mejor rendimiento que Stack (thread-safe no necesario)

3. HashMap<Integer, List<Tarea>> - Caché por Prioridad
Ubicación: TareaServiceImpl.tareasPorPrioridad
Justificación: Optimiza consultas por prioridad:

Búsqueda por clave en O(1) promedio
Agrupación eficiente de datos relacionados
Ideal para reportes y estadísticas
Reduce consultas repetitivas a BD

Patrones de Diseño Implementados
1. DAO (Data Access Object)
javapublic interface TareaDAO {
    Tarea guardar(Tarea tarea);
    List<Tarea> listarActivas();
    // ... más métodos
}
Ventaja: Separa lógica de acceso a datos del resto de la aplicación.
2. Singleton para Conexión BD
javapublic static synchronized ConexionBD getInstancia() {
    if (instancia == null) {
        instancia = new ConexionBD();
    }
    return instancia;
}
Ventaja: Una sola instancia de conexión, control centralizado.
3. Factory para Creación de Tareas
javapublic static Tarea crearTarea(TipoTarea tipo, String titulo, int prioridad) {
    switch (tipo) {
        case ESPECIAL: return new TareaEspecial(titulo, prioridad, "Especial");
        // ... más casos
    }
}
Ventaja: Centraliza la lógica de creación de objetos.
📊 Transacciones y Manejo de Excepciones
Estrategia de Transacciones
java// Ejemplo de transacción manual cuando sea necesario
Connection conn = conexionBD.getConexion();
try {
    conn.setAutoCommit(false);
    
    // Múltiples operaciones relacionadas
    tareaDAO.guardar(tarea);
    auditDAO.registrarCreacion(tarea.getId());
    
    conn.commit();  // Confirmar todas las operaciones
} catch (SQLException e) {
    conn.rollback(); // Revertir en caso de error
    throw new TaskManagerException("Error en transacción", e);
} finally {
    conn.setAutoCommit(true);
}
Jerarquía de Excepciones
java// Excepción personalizada
public class TaskManagerException extends RuntimeException {
    private String codigoError;
    
    public TaskManagerException(String mensaje) {
        super(mensaje);
    }
    
    public TaskManagerException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
Estrategias de Recuperación

Validación Preventiva: Verificar datos antes de procesarlos
Try-Catch por Capas: Cada capa maneja sus errores específicos
Mensajes Usuario-Friendly: Traducir errores técnicos
Logging Detallado: Registrar errores para debugging
Graceful Degradation: La aplicación continúa funcionando

🚀 Pasos para Ejecutar
Prerrequisitos

Java 8 o superior
bashjava -version  # Verificar instalación

PostgreSQL 13+ instalado y ejecutándose
bashsudo systemctl status postgresql  # Linux
# o verificar en Services en Windows

NetBeans IDE con Maven
Git para clonar el repositorio

Instalación

Clonar el repositorio:
bashgit clone https://github.com/usuario/taskmanager-poo.git
cd taskmanager-poo

Configurar base de datos:
bash# Conectarse a PostgreSQL
sudo -u postgres psql

# Ejecutar comandos SQL de configuración (ver sección anterior)

Configurar conexión (opcional):

Editar src/main/java/com/taskmanager/dao/ConexionBD.java
Modificar URL, usuario y contraseña si es diferente


Compilar proyecto:
bashmvn clean compile

Ejecutar aplicación:
bashmvn exec:java -Dexec.mainClass="com.taskmanager.app.TaskManagerApp"
O desde NetBeans:

Abrir proyecto
Ejecutar TaskManagerApp.java



Verificación de Instalación

La aplicación debe abrir sin errores
Debe mostrar ventana principal con formulario y tabla
Debe poder crear al menos una tarea de prueba
Al reiniciar, la tarea debe persistir
