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
public class CatalogoUsuario {
    private Integer persId;
    private Integer cataId;

    public CatalogoUsuario() {
    }

    public CatalogoUsuario(Integer persId, Integer cataId) {
        this.persId = persId;
        this.cataId = cataId;
    }

    public Integer getPersId() {
        return persId;
    }

    public void setPersId(Integer persId) {
        this.persId = persId;
    }

    public Integer getCataId() {
        return cataId;
    }

    public void setCataId(Integer cataId) {
        this.cataId = cataId;
    }
            
    
}
