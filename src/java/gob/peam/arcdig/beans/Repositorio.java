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
public class Repositorio {
    private Integer id;
    private String nombre;
    private Boolean estado;
    private Integer padre;
    private Integer nivel;

    public Repositorio(Integer id, String nombre, Boolean estado, Integer padre, Integer nivel) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.padre = padre;
        this.nivel = nivel;
    }

    public Repositorio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
    
    
}
