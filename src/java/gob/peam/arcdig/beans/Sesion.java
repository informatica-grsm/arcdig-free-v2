/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.Date;



/**
 *
 * @author jpgprog84
 */
public class Sesion {

    private String sesiId;
    private Date sesiFechaIngreso;
    private Date sesiFechaSalida;
    
    private String sesiIp;
    private Usuario usuario;
    private Dependencia dependencia;
    private boolean sesiEstado;
    private Boolean check;

    public Sesion() {
    }

    public Sesion(String sesiId, Date sesiFechaIngreso, Date sesiFechaSalida, String sesiIp, Usuario usuario, Dependencia dependencia, boolean sesiEstado, Boolean check) {
        this.sesiId = sesiId;
        this.sesiFechaIngreso = sesiFechaIngreso;
        this.sesiFechaSalida = sesiFechaSalida;
        this.sesiIp = sesiIp;
        this.usuario = usuario;
        this.dependencia = dependencia;
        this.sesiEstado = sesiEstado;
        this.check = check;
    }

    public String getSesiId() {
        return sesiId;
    }

    public void setSesiId(String sesiId) {
        this.sesiId = sesiId;
    }

    public Date getSesiFechaIngreso() {
        return sesiFechaIngreso;
    }

    public void setSesiFechaIngreso(Date sesiFechaIngreso) {
        this.sesiFechaIngreso = sesiFechaIngreso;
    }

    public Date getSesiFechaSalida() {
        return sesiFechaSalida;
    }

    public void setSesiFechaSalida(Date sesiFechaSalida) {
        this.sesiFechaSalida = sesiFechaSalida;
    }

    public String getSesiIp() {
        return sesiIp;
    }

    public void setSesiIp(String sesiIp) {
        this.sesiIp = sesiIp;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public boolean isSesiEstado() {
        return sesiEstado;
    }

    public void setSesiEstado(boolean sesiEstado) {
        this.sesiEstado = sesiEstado;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
    
    

}

