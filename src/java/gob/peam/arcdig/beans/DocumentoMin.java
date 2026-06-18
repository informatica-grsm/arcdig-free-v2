/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author alabajos
 */
public class DocumentoMin {
    private Integer id;
    private String titulo;
    private String resumen;
    private String fecha;
    private String nuevo;
    

    public DocumentoMin() {
    }

    public DocumentoMin(Integer id, String titulo, String resumen, String fecha, String nuevo) {
        this.id = id;
        this.titulo = titulo;
        this.resumen = resumen;
        this.fecha = fecha;
        this.nuevo = nuevo;
    }

    
    public String getNuevo() {
        return nuevo;
    }

    public void setNuevo(String nuevo) {
        this.nuevo = nuevo;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    
}
