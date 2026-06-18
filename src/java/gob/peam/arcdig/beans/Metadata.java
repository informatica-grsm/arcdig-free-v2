package gob.peam.arcdig.beans;

public class Metadata {

    private Integer id;
    private String campo;
    private String detalle;
    private Boolean manual;
    private Boolean estado;
    private String tipoDato;
    private String defecto;
    private Boolean obligado;
    private Integer secuencia;

    public Metadata() {
    }

    public Metadata(Integer id, String campo, String detalle, Integer secuencia) {
        this.id = id;
        this.campo = campo;
        this.detalle = detalle;
        this.secuencia = secuencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getDefecto() {
        return defecto;
    }

    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    public Boolean getObligado() {
        return obligado;
    }

    public void setObligado(Boolean obligado) {
        this.obligado = obligado;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

}
