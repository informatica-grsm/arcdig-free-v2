/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jpgprog84
 */
public class Dependencia implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1869029179505543226L;
    private Integer idDependencia;
    private String charId;
    private String nombre;
    private String abreviatura;
    private Integer padre;
    private boolean estado;
    private List<Usuario> usuarios;
    private Integer nivel;

    public Dependencia() {
    }

    public Dependencia(Integer idDependencia, String charId, String nombre, String abreviatura, Integer padre, boolean estado, List<Usuario> usuarios, Integer nivel) {
        this.idDependencia = idDependencia;
        this.charId = charId;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.padre = padre;
        this.estado = estado;
        this.usuarios = usuarios;
        this.nivel = nivel;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getCharId() {
        return charId;
    }

    public void setCharId(String charId) {
        this.charId = charId;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getIdDependencia() {
        return idDependencia;
    }

    public void setIdDependencia(Integer idDependencia) {
        this.idDependencia = idDependencia;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

   
}
