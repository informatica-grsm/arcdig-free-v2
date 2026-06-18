/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.List;

/**
 *
 * @author jpgprog84
 */
public class Etiqueta {

    private Integer idEtiqueta;
    private String descripcion;
    private String icono;
    private boolean estado;
    private List<SubModulo> subModulos;

    public Integer getIdEtiqueta() {
        return idEtiqueta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    
    public void setIdEtiqueta(Integer idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<SubModulo> getSubModulos() {
        return subModulos;
    }

    public void setSubModulos(List<SubModulo> subModulos) {
        this.subModulos = subModulos;
    }
    
}
