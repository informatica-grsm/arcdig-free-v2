/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author jpgprog84
 */
public class Rol {

    private Integer idRol;
    private String nombre;
    private boolean estado;
    private Boolean check;
    public Modulo selectModulo;

    public Rol() {
    }

    public Rol(Integer idRol, String nombre, Boolean estado, Boolean check) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.estado = estado;
        this.check = check;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
