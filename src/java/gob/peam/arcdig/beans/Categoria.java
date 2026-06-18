/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author Cj.Legacy
 */
public class Categoria {
    private Integer cateId;
    private String cateNombre;
    private String cateDescripcion;
    private Boolean cateEstado;

    public Categoria() {
    }

    public Categoria(Integer cateId, String cateNombre, String cateDescripcion, Boolean cateEstado) {
        this.cateId = cateId;
        this.cateNombre = cateNombre;
        this.cateDescripcion = cateDescripcion;
        this.cateEstado = cateEstado;
        
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCateNombre() {
        return cateNombre;
    }

    public void setCateNombre(String cateNombre) {
        this.cateNombre = cateNombre;
    }

    public String getCateDescripcion() {
        return cateDescripcion;
    }

    public void setCateDescripcion(String cateDescripcion) {
        this.cateDescripcion = cateDescripcion;
    }

    public Boolean getCateEstado() {
        return cateEstado;
    }

    public void setCateEstado(Boolean cateEstado) {
        this.cateEstado = cateEstado;
    }

   
    
    
}
