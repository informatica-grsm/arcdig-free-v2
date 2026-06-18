/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.Date;

/**
 *
 * @author alabajos
 */
public class Notificacion {
    private Integer id;
    private Date fechaVencimiento;
    private Date fechaActual;
    private Integer veces;
    private Integer proceso;
    private String persDni;
    private Integer docuId;

    public Notificacion() {
    }


    
    public Notificacion(Integer id, Date fechaVencimiento, Date fechaActual, Integer veces, Integer proceso) {
        this.id = id;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaActual = fechaActual;
        this.veces = veces;
        this.proceso = proceso;
    }

    public String getPersDni() {
        return persDni;
    }

    public void setPersDni(String persDni) {
        this.persDni = persDni;
    }

    public Integer getDocuId() {
        return docuId;
    }

    public void setDocuId(Integer docuId) {
        this.docuId = docuId;
    }

    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Integer getVeces() {
        return veces;
    }

    public void setVeces(Integer veces) {
        this.veces = veces;
    }

    public Integer getProceso() {
        return proceso;
    }

    public void setProceso(Integer proceso) {
        this.proceso = proceso;
    }
    
    
    
}
