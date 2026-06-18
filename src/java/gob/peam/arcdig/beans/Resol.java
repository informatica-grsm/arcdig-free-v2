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
public class Resol {
    private String anho;
    private Integer numero;
    private Date fecha;
    private String titulo;
    private String detalle1;
    private Date feccha;
    private String dependencia;
    private String path;

    public Resol() {
    }
    
    

    public Resol(String anho, Integer numero, Date fecha, String titulo, String detalle1, Date feccha) {
        this.anho = anho;
        this.numero = numero;
        this.fecha = fecha;
        this.titulo = titulo;
        this.detalle1 = detalle1;
        this.feccha = feccha;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }
    
    

    public Resol(String anho) {
        this.anho = anho;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle1() {
        return detalle1;
    }

    public void setDetalle1(String detalle1) {
        this.detalle1 = detalle1;
    }

    public Date getFeccha() {
        return feccha;
    }

    public void setFeccha(Date feccha) {
        this.feccha = feccha;
    }
    
    
}
