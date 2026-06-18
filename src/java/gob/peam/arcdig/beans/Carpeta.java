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
public class Carpeta {
    private Integer id;
    private Integer tipo;
    private String  ruta;
    private String propietario;

    public Carpeta() {
    }

    public Carpeta(Integer id, Integer tipo, String ruta, String propietario) {
        this.id = id;
        this.tipo = tipo;
        this.ruta = ruta;
        this.propietario = propietario;
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    
}
