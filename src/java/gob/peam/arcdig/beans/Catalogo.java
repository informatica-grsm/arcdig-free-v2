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
public class Catalogo {
    private Integer cataId;
    private String cataNombre;
    private String cataDescripcion;
    private Boolean cataEstado;
    private String cataCarpeta;
    private String cataEliminado;
    

    public Catalogo() {
    }

    public Catalogo(Integer cataId, String cataNombre, String cataDescripcion, Boolean cataEstado, String cataCarpeta, String cataEliminado) {
        this.cataId = cataId;
        this.cataNombre = cataNombre;
        this.cataDescripcion = cataDescripcion;
        this.cataEstado = cataEstado;
        this.cataCarpeta = cataCarpeta;
        this.cataEliminado = cataEliminado;
    }

    public Integer getCataId() {
        return cataId;
    }

    public void setCataId(Integer cataId) {
        this.cataId = cataId;
    }

    public String getCataNombre() {
        return cataNombre;
    }

    public void setCataNombre(String cataNombre) {
        this.cataNombre = cataNombre;
    }

    public String getCataDescripcion() {
        return cataDescripcion;
    }

    public void setCataDescripcion(String cataDescripcion) {
        this.cataDescripcion = cataDescripcion;
    }

    public Boolean getCataEstado() {
        return cataEstado;
    }

    public void setCataEstado(Boolean cataEstado) {
        this.cataEstado = cataEstado;
    }

    public String getCataCarpeta() {
        return cataCarpeta;
    }

    public void setCataCarpeta(String cataCarpeta) {
        this.cataCarpeta = cataCarpeta;
    }

    public String getCataEliminado() {
        return cataEliminado;
    }

    public void setCataEliminado(String cataEliminado) {
        this.cataEliminado = cataEliminado;
    }
 
    
    
}
