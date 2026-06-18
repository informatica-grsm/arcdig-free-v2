/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.List;

/**
 *
 * @author Usuario
 */
public class Siado {

    private Integer siadId;
    private Serie serie;/**/
    private SubSerie subSerie;/**/
    private String titular;
    private String tituloHabilitante;
    private String NumeroSITD;
    private String MesInicio;
    private String AnhoFin;
    private String AreaUbicacion;
    private String AnhoInicio;
    private String descripcion;
    private String password;
    private String mesFin;
    private String observacion;
    private String fechaEnvio;
    private List<SiadoDetalle> siadoDetalle;
    private Boolean estado;
    private Boolean regente;
    private Integer estadoTramiteId;
    private Sector sector;
    private String numeroTramite;
    private String numeroExpediente;


    public Siado() {
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    
    
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    
    
    public Integer getEstadoTramiteId() {
        return estadoTramiteId;
    }

    public void setEstadoTramiteId(Integer estadoTramiteId) {
        this.estadoTramiteId = estadoTramiteId;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getSiadId() {
        return siadId;
    }

    public void setSiadId(Integer siadId) {
        this.siadId = siadId;
    }

    public Boolean getRegente() {
        return regente;
    }

    public void setRegente(Boolean regente) {
        this.regente = regente;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public SubSerie getSubSerie() {
        return subSerie;
    }

    public void setSubSerie(SubSerie subSerie) {
        this.subSerie = subSerie;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getTituloHabilitante() {
        return tituloHabilitante;
    }

    public void setTituloHabilitante(String tituloHabilitante) {
        this.tituloHabilitante = tituloHabilitante;
    }

    public String getNumeroSITD() {
        return NumeroSITD;
    }

    public void setNumeroSITD(String NumeroSITD) {
        this.NumeroSITD = NumeroSITD;
    }

    public String getMesInicio() {
        return MesInicio;
    }

    public void setMesInicio(String MesInicio) {
        this.MesInicio = MesInicio;
    }

    public String getAnhoFin() {
        return AnhoFin;
    }

    public void setAnhoFin(String AnhoFin) {
        this.AnhoFin = AnhoFin;
    }

    public String getAreaUbicacion() {
        return AreaUbicacion;
    }

    public void setAreaUbicacion(String AreaUbicacion) {
        this.AreaUbicacion = AreaUbicacion;
    }

    public String getAnhoInicio() {
        return AnhoInicio;
    }

    public void setAnhoInicio(String AnhoInicio) {
        this.AnhoInicio = AnhoInicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMesFin() {
        return mesFin;
    }

    public void setMesFin(String mesFin) {
        this.mesFin = mesFin;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<SiadoDetalle> getSiadoDetalle() {
        return siadoDetalle;
    }

    public void setSiadoDetalle(List<SiadoDetalle> siadoDetalle) {
        this.siadoDetalle = siadoDetalle;
    }

}
