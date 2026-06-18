/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author Usuario
 */
public class SiadoDetalle {
    
    private Integer sideId;
    private TipoDoc tipoDoc;
    private SubTipoDoc subTipoDoc;
    private DetSubTipoDoc detSubTipoDoc;
    private Boolean bRegente;
    private String NumZafra;
    private String Numero;
    private String descripcion;
    private String observacion;
    private String sRegente;
    private String anhoAprobacion;
    private String anhoFin;
    private String usuarioEnvio;
    private String fechaEnvio;
    private String opcion;
    private Documento documento;
    private Siado siado;
    
    public SiadoDetalle() {
    }

    public Documento getDocumento() {
        return documento;
    }

    public Integer getSideId() {
        return sideId;
    }

    public void setSideId(Integer sideId) {
        this.sideId = sideId;
    }

    
    
    public Siado getSiado() {
        return siado;
    }

    public void setSiado(Siado siado) {
        this.siado = siado;
    }

    
    
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public SubTipoDoc getSubTipoDoc() {
        return subTipoDoc;
    }

    public void setSubTipoDoc(SubTipoDoc subTipoDoc) {
        this.subTipoDoc = subTipoDoc;
    }

    public DetSubTipoDoc getDetSubTipoDoc() {
        return detSubTipoDoc;
    }

    public void setDetSubTipoDoc(DetSubTipoDoc detSubTipoDoc) {
        this.detSubTipoDoc = detSubTipoDoc;
    }

    public Boolean getbRegente() {
        return bRegente;
    }

    public void setbRegente(Boolean bRegente) {
        this.bRegente = bRegente;
    }

    public String getNumZafra() {
        return NumZafra;
    }

    public void setNumZafra(String NumZafra) {
        this.NumZafra = NumZafra;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getsRegente() {
        return sRegente;
    }

    public void setsRegente(String sRegente) {
        this.sRegente = sRegente;
    }

    public String getAnhoAprobacion() {
        return anhoAprobacion;
    }

    public void setAnhoAprobacion(String anhoAprobacion) {
        this.anhoAprobacion = anhoAprobacion;
    }

    public String getAnhoFin() {
        return anhoFin;
    }

    public void setAnhoFin(String anhoFin) {
        this.anhoFin = anhoFin;
    }

    public String getUsuarioEnvio() {
        return usuarioEnvio;
    }

    public void setUsuarioEnvio(String usuarioEnvio) {
        this.usuarioEnvio = usuarioEnvio;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
    
    
    
}
