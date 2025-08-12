/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TaskManagerException  extends RuntimeException{
    private String codigoError;
    
    public TaskManagerException(String mensaje) {
        super(mensaje);
    }
    
    public TaskManagerException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public TaskManagerException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }
    
    public String getCodigoError() {
        return codigoError;
    }
}
