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
public class RutaRapida {
    private String ruta;
    private Integer cataId;
    private String dni;

    public RutaRapida() {
    }

    public RutaRapida(String ruta, Integer cataId, String dni) {
        this.ruta = ruta;
        this.cataId = cataId;
        this.dni = dni;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Integer getCataId() {
        return cataId;
    }

    public void setCataId(Integer cataId) {
        this.cataId = cataId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    
    
            
}
