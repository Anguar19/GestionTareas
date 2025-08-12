/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Dominio.Tarea;
import Dominio.TaskManagerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class ConexionBD {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=taskmanager_db;encrypt=false";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "1234";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    private static ConexionBD instancia;
    private Connection conexion;
    
    private ConexionBD() {
        try {
            Class.forName(DRIVER);
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            inicializarEsquema();
        } catch (ClassNotFoundException | SQLException e) {
            throw new TaskManagerException("Error al conectar con la base de datos", e);
        }
    }
    
    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
    
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            throw new TaskManagerException("Error al obtener conexión", e);
        }
        return conexion;
    }
    
    private void inicializarEsquema() throws SQLException {
        String sql = """
            IF NOT EXISTS (
                SELECT * FROM sysobjects WHERE name='tareas' AND xtype='U'
            )
            CREATE TABLE tareas (
                id INT IDENTITY(1,1) PRIMARY KEY,
                titulo NVARCHAR(255) NOT NULL,
                prioridad INT NOT NULL CHECK (prioridad IN (1, 2, 3)),
                estado BIT DEFAULT 0,
                especial BIT DEFAULT 0,
                fecha_creacion DATE,
                activo BIT DEFAULT 1,
                fecha_modificacion DATETIME DEFAULT GETDATE(),
                usuario_modificacion NVARCHAR(100) DEFAULT 'SYSTEM'
            );

            IF NOT EXISTS (
                SELECT * FROM sys.indexes WHERE name='idx_tareas_activo'
            )
            CREATE INDEX idx_tareas_activo ON tareas(activo);

            IF NOT EXISTS (
                SELECT * FROM sys.indexes WHERE name='idx_tareas_prioridad'
            )
            CREATE INDEX idx_tareas_prioridad ON tareas(prioridad);

            IF NOT EXISTS (
                SELECT * FROM sys.indexes WHERE name='idx_tareas_fecha'
            )
            CREATE INDEX idx_tareas_fecha ON tareas(fecha_creacion);
            """;
            
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
    
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
