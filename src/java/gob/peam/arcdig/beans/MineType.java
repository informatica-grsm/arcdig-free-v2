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
public class MineType {

    private Integer mineId;
    private String mineNombre;
    private String mineExt;
    private Boolean mineEstado;

    public MineType() {
    }

    public MineType(Integer mineId, String mineNombre, String mineExt, Boolean mineEstado) {
        this.mineId = mineId;
        this.mineNombre = mineNombre;
        this.mineExt = mineExt;
        this.mineEstado = mineEstado;
    }

    public Integer getMineId() {
        return mineId;
    }

    public void setMineId(Integer mineId) {
        this.mineId = mineId;
    }

    public String getMineNombre() {
        return mineNombre;
    }

    public void setMineNombre(String mineNombre) {
        this.mineNombre = mineNombre;
    }

    public String getMineExt() {
        return mineExt;
    }

    public void setMineExt(String mineExt) {
        this.mineExt = mineExt;
    }

    public Boolean getMineEstado() {
        return mineEstado;
    }

    public void setMineEstado(Boolean mineEstado) {
        this.mineEstado = mineEstado;
    }

    

}
