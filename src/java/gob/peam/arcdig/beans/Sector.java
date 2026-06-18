/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author ecr
 */
public class Sector {

    private Integer idIns;
    private Integer idSec;
    private String codSec;
    private String desSec;

    public Sector() {
    }

    public Integer getIdIns() {
        return idIns;
    }

    public void setIdIns(Integer idIns) {
        this.idIns = idIns;
    }

    public Integer getIdSec() {
        return idSec;
    }

    public void setIdSec(Integer idSec) {
        this.idSec = idSec;
    }

    public String getCodSec() {
        return codSec;
    }

    public void setCodSec(String codSec) {
        this.codSec = codSec;
    }

    public String getDesSec() {
        return desSec;
    }

    public void setDesSec(String desSec) {
        this.desSec = desSec;
    }
    
    

}
